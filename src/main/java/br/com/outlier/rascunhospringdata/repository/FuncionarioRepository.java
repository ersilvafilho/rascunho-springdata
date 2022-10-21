package br.com.outlier.rascunhospringdata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.outlier.rascunhospringdata.entity.Funcionario;
import br.com.outlier.rascunhospringdata.repository.projection.ResumoFuncionario;

@Repository
public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Long>, JpaSpecificationExecutor<Funcionario> {

	public List<Funcionario> findByNomeLike(String nome);

	public List<Funcionario> findByCargoId(Integer idCargo);

	@Query(value = "SELECT f.nome, f.cpf, f.data_contratacao AS dataContratacao, c.descricao AS cargo, u.nome AS unidade FROM funcionario f JOIN cargo c ON f.id_cargo = c.id "
			+ "JOIN funcionario_unid fu ON f.id = fu.id_funcionario JOIN unidade_trabalho u ON fu.id_unidade_trabalho = u.id", nativeQuery = true)
	public List<ResumoFuncionario> findAllResumo();

}
