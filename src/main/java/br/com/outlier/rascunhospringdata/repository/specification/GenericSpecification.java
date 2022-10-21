package br.com.outlier.rascunhospringdata.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.outlier.rascunhospringdata.repository.specification.util.SearchCriteria;
import br.com.outlier.rascunhospringdata.repository.specification.util.SearchCriteriaList;
import br.com.outlier.rascunhospringdata.repository.specification.util.SearchOperation;

public class GenericSpecification<ENTITY> implements Specification<ENTITY> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -592421584155019199L;
	private List<SearchCriteria> searchCriterias;

	public GenericSpecification() {
		searchCriterias = new ArrayList<>();
	}

	public void add(SearchCriteriaList searchCriteriaList) {
		searchCriterias.addAll(searchCriteriaList.getSearchCriterias());
	}

	@Override
	public Predicate toPredicate(Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		// create a new predicate list
		List<Predicate> predicates = new ArrayList<>();

		// add criteria to predicate
		for (SearchCriteria criteria : searchCriterias) {

			if (criteria.getKey().contains(".")) {
				String[] keyVector = criteria.getKey().split("\\.");
				String joinPath = keyVector[0];
				String joinedEntityAttribute = keyVector[1];
				Join<ENTITY, ?> join = root.join(joinPath);

				if (criteria.getOperation().equals(SearchOperation.EQUAL)) {

					predicates.add(builder.equal(join.get(joinedEntityAttribute), criteria.getValue()));

				} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {

					predicates.add(builder.greaterThan(join.<String>get(joinedEntityAttribute), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {

					predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {

					predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {

					predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));

				} else if (criteria.getOperation().equals(SearchOperation.MATCH)) {

					predicates.add(builder.like(join.<String>get(joinedEntityAttribute), "%" + criteria.getValue().toString().toUpperCase() + "%"));

				} else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {

					predicates.add(builder.like(root.get(criteria.getKey()), criteria.getValue().toString().toLowerCase() + "%"));

				} else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {

					predicates.add(builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString().toLowerCase()));

				} else if (criteria.getOperation().equals(SearchOperation.IN)) {

					predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));

				} else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {

					predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));

				}

			} else {

				if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
					predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
					predicates.add(builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
					predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
					predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));

				} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
					predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));

				} else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
					predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));

				} else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
					predicates.add(builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString().toUpperCase() + "%"));

				} else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
					predicates.add(builder.like(root.get(criteria.getKey()), criteria.getValue().toString().toUpperCase() + "%"));

				} else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
					predicates.add(builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString().toUpperCase()));

				} else if (criteria.getOperation().equals(SearchOperation.IN)) {
					predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));

				} else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
					predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));

				}
			}
		}

		return builder.and(predicates.toArray(new Predicate[0]));
	}
}
