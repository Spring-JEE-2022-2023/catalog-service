package ch.beershop.catalogservice.controller.model.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EvaluationCreationRequest {

	public Integer getNote() {
		return note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}

	@NotNull(message = "La note est requise")
	private Integer note;
	
}
