package ch.beershop.catalogservice.controller.model.response;

public class BeerInUseResponse {

	private String messageString = "The beer with the id: {} is still in use. You muss delete the cart itemn first";

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public BeerInUseResponse(Integer fabricantId) {
		this.messageString = messageString.replace("{}", String.valueOf(fabricantId));
	}
	
}
