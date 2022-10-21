package br.com.outlier.rascunhospringdata.entity.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ParentType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer parentTypeId;
	private String parentTypeName;
}