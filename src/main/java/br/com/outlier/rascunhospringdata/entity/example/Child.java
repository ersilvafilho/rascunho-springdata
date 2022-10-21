package br.com.outlier.rascunhospringdata.entity.example;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
public class Child {

	@Id
	private Integer childId;
	private String childName;
	@ManyToOne
	private Parent parent;

}
