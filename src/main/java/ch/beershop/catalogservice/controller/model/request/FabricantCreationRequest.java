package ch.beershop.catalogservice.controller.model.request;



import javax.validation.constraints.NotEmpty;



public class FabricantCreationRequest {

	
	@NotEmpty(message = "Le nom est requis")
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
