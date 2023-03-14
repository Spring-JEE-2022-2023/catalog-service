package ch.beershop.catalogservice.controller.model.response;

public class ManufacturerNotFoundResponse {

	private String messageString = "The fabricant with the id: {} was not found";

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public ManufacturerNotFoundResponse(Integer fabricantId) {
		this.messageString = messageString.replace("{}", String.valueOf(fabricantId));
	}
	
}
