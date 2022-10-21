package br.com.outlier.rascunhospringdata.repository.specification.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SearchCriteriaList {

	private List<SearchCriteria> searchCriterias;

	public SearchCriteriaList() {
		searchCriterias = new ArrayList<>();
	}

	public SearchCriteriaList add(String key, Object value, SearchOperation operation) {
		if (value != null && !value.toString().isBlank()) {
			searchCriterias.add(new SearchCriteria(key, value, operation));
		}
		return this;
	}

}