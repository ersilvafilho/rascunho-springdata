package br.com.outlier.rascunhospringdata.repository.example;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.outlier.rascunhospringdata.entity.example.Child;
import br.com.outlier.rascunhospringdata.entity.example.Parent;

@Repository
public class ParentDAO {

	private EntityManager em;

	public ParentDAO(EntityManager em) {
		this.em = em;
	}

	public List<Parent> findByChildId(Integer childId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Parent> cq = cb.createQuery(Parent.class);
		Root<Parent> root = cq.from(Parent.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(root.join("children").get("childId"), childId));
		cq.where(predicates.toArray(new Predicate[0]));

		return em.createQuery(cq).getResultList();
	}

	public List<Parent> findByChild(Child child) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Parent> cq = cb.createQuery(Parent.class);
		Root<Parent> root = cq.from(Parent.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(root.join("children"), child));
		cq.where(predicates.toArray(new Predicate[0]));

		return em.createQuery(cq).getResultList();
	}

}
