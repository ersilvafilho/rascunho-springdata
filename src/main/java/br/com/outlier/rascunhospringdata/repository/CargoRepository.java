package br.com.outlier.rascunhospringdata.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.outlier.rascunhospringdata.entity.Cargo;

@Repository
public interface CargoRepository extends CrudRepository<Cargo, Integer>{

}
