package br.com.outlier.rascunhospringdata.entity.example;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer parentId;
	private String parentName;
	@ManyToOne
	private ParentType parentType;
	@OneToMany(mappedBy = "parent")
	private List<Child> children;

}
