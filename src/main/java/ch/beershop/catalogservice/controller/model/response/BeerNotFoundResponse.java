package ch.beershop.catalogservice.controller.model.response;

public class BeerNotFoundResponse {

	private String messageString = "The beer with the provided id don't exist";
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public BeerNotFoundResponse(Long beerId) {
		this.id = beerId;
	}
	
}
