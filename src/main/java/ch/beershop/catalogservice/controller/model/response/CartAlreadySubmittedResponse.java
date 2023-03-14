package ch.beershop.catalogservice.controller.model.response;

public class CartAlreadySubmittedResponse {

	private String messageString = "The cart id: {} was alreday submitted";

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public CartAlreadySubmittedResponse(Long cartId) {
		this.messageString = messageString.replace("{}", String.valueOf(cartId));
	}
	
}
