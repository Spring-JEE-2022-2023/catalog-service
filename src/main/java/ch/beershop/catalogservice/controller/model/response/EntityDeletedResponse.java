package ch.beershop.catalogservice.controller.model.response;

public class EntityDeletedResponse {

	private String messageString = "The entity with the id: {1} and type {2} was successfully deleted";

	public String getMessageString() {
		return messageString;
	}
	

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public EntityDeletedResponse(Long fabricantId, Class type) {
		this.messageString = messageString.replace("{1}", String.valueOf(fabricantId));
		this.messageString = messageString.replace("{2}", type.getName());
	}
}
