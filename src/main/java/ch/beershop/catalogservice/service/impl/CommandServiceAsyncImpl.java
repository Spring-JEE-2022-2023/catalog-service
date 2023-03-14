package ch.beershop.catalogservice.service.impl;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.beershop.catalogservice.controller.model.order.OrderInProgressEvent;
import ch.beershop.catalogservice.controller.model.order.OrderSubmitedEvent;
import ch.beershop.catalogservice.repository.CartRepository;
import ch.beershop.catalogservice.service.CommandService;
import ch.beershop.catalogservice.service.model.Cart;

@Service
@Profile("async")
public class CommandServiceAsyncImpl  implements CommandService{

	Logger logger = Logger.getLogger(CommandServiceAsyncImpl.class.getName());
	
	@Autowired
	CartRepository cartRepository;
	@Autowired
	JmsTemplate jmsTemplate;
	@Value("${spring.activemq.order.queue}")
    String queue;
	
	@Autowired
	ObjectMapper mapper;
	
	@Override
	public Cart submitCommand(Cart cart) throws JsonProcessingException{
		
       try {
           
    	   	OrderSubmitedEvent event = new OrderSubmitedEvent();
    	   	event.setCartId(cart.getId());
    	   	event.setTotalCartAmount(cart.computeTotalCartAmount());
    	   	event.setSubmitedDate(new Date());
    	   
            String jsonObj = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(event);
            
            jmsTemplate.send(queue, messageCreator -> {
                TextMessage message = messageCreator.createTextMessage();
                message.setText(jsonObj);
                return message;
            });
            
            cart.setOrderSubmited(Boolean.TRUE);
            cart.setOrdredDate(new Date());
            cartRepository.save(cart);
            
        }
        catch (Exception ex) {
            System.out.println("ERROR in sending message to queue");
        }
       
       return cart;
    }
	
	/**
	 * Listener sur commande in progress
	 * @param jsonMessage
	 * @throws JMSException
	 */
	@JmsListener(destination = "order-inprogress-q")
    public void readInprogressMessage(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received inprogress-q message " + jsonMessage);
        
        if(jsonMessage instanceof TextMessage) {
        	OrderInProgressEvent event = null;
        	Cart cart = null;
        	
        	TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
           
            try {
                event = mapper.readValue(messageData, OrderInProgressEvent.class);
                
                //TOTO optionnal
                cart = cartRepository.findById(event.getCartSubmitedId()).get();
                
                cart.setOrderId(event.getOrderId());
                cartRepository.save(cart);
                
            } catch (Exception e) {
                logger.log(Level.SEVERE,"error converting to cart", e);
            }
            System.out.println("messageData:"+messageData);
        }
       
    }
	
	@JmsListener(destination = "order-completed-q")
    public void receiveAndForwardMessageFromQueue(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if(jsonMessage instanceof TextMessage) {
            Cart cart = null;
        	
        	TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
           
            try {
                cart = mapper.readValue(messageData, Cart.class);
            } catch (Exception e) {
                logger.log(Level.SEVERE,"error converting to cart", e);
            }
            System.out.println("messageData:"+messageData);
        }
       
    }


	
}
