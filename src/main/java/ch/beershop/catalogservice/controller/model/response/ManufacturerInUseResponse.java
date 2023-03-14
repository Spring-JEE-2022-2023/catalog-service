package ch.beershop.catalogservice.controller.model.response;

public class ManufacturerInUseResponse {

	private String messageString = "The fabricant with the id: {} is still in use. You muss delete the beer first";

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public ManufacturerInUseResponse(Integer fabricantId) {
		this.messageString = messageString.replace("{}", String.valueOf(fabricantId));
	}
	
}
