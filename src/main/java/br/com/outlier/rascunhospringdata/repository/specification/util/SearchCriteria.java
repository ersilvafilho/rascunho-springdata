package br.com.outlier.rascunhospringdata.repository.specification.util;

import lombok.Data;

@Data
public class SearchCriteria {

	private String key;
	private Object value;
	private SearchOperation operation;

	public SearchCriteria() {
	}

	public SearchCriteria(String key, Object value, SearchOperation operation) {
		this.key = key;
		this.value = value;
		this.operation = operation;
	}

}