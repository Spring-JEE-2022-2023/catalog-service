package ch.beershop.catalogservice.controller.model.response;

public class CartItemNotFoundResponse {

	private String messageString = "The cart with the id: {} was not found";

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public CartItemNotFoundResponse(Long fabricantId) {
		this.messageString = messageString.replace("{}", String.valueOf(fabricantId));
	}
	
}
