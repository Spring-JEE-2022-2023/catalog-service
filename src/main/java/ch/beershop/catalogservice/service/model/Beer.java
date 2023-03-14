package ch.beershop.catalogservice.service.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;





/**
 * Model of a beer
 * @author Seb
 *
 */
@Entity
public class Beer {

	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Integer stock;
	private String name;
	private BigDecimal price;
	
	
	
	@OneToMany(mappedBy="beer")
	private List<Evaluation> evaluations;
	
	
	@ManyToOne
	private Manufacturer fabricant;
	
	

	public Manufacturer getFabricant() {
		return fabricant;
	}


	public void setFabricant(Manufacturer fabricant) {
		this.fabricant = fabricant;
	}


	public Long getId() {
		return id;
	}

	
	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}
	

	public void setPrice(BigDecimal price) {

		this.price = price;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Beer other = (Beer) obj;
		return Objects.equals(id, other.id);
	}


	public List<Evaluation> getEvaluations() {
		return evaluations;
	}


	
	
}
