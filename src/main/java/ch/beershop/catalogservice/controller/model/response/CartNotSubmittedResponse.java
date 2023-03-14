package ch.beershop.catalogservice.controller.model.response;

public class CartNotSubmittedResponse {

	private String messageString = "The cart id: {} was not submitted due to an error";

	public String getMessageString() {
		return messageString;
	}
	
	

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public CartNotSubmittedResponse(Long cartId) {
		this.messageString = messageString.replace("{}", String.valueOf(cartId));
	}
	
}
