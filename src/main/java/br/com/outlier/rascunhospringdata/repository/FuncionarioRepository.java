package br.com.outlier.rascunhospringdata.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.outlier.rascunhospringdata.entity.Funcionario;

@Repository
public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Long> {

	public List<Funcionario> findByNomeLike(String nome);

	public List<Funcionario> findByCargoId(Integer idCargo);

}
