
package br.com.outlier.rascunhospringdata.repository.specification;

import java.time.LocalDate;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import br.com.outlier.rascunhospringdata.entity.Cargo;
import br.com.outlier.rascunhospringdata.entity.Funcionario;
import br.com.outlier.rascunhospringdata.entity.UnidadeTrabalho;

public class FuncionarioSpecification {

	public static Specification<Funcionario> nomeLike(String nome) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
	}

	public static Specification<Funcionario> cpf(String cpf) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("cpf"), cpf);
	}

	public static Specification<Funcionario> cargo(Cargo cargo) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.join("cargo"), cargo);
		};
	}

	public static Specification<Funcionario> unidadeTrabalho(UnidadeTrabalho unidadeTrabalho) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			Join<Funcionario, UnidadeTrabalho> joinUt = root.join("unidadesTrabalho");
			// Essencial para evitar registros de funcionário duplicados na Query, o mesmo
			// que o antigo Criteria.DISTINCT_ROOT_ENTITY
			criteriaQuery.distinct(true);
			return criteriaBuilder.equal(joinUt, unidadeTrabalho);
		};
	}

	public static Specification<Funcionario> dataContratacaoGe(LocalDate dataContratacao) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("dataContratacao"), dataContratacao);
	}

	public static Specification<Funcionario> dataContratacaoLe(LocalDate dataContratacao) {
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("dataContratacao"), dataContratacao);
	}
}
