package br.com.outlier.rascunhospringdata.service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.outlier.rascunhospringdata.entity.UnidadeTrabalho;
import br.com.outlier.rascunhospringdata.repository.UnidadeTrabalhoRepository;

@Service
@Transactional
public class UnidadeTrabalhoService extends GenericCrudService<Integer, UnidadeTrabalho, UnidadeTrabalhoRepository> {

	public UnidadeTrabalhoService(UnidadeTrabalhoRepository unidadeTrabalhoRepository) {
		super(unidadeTrabalhoRepository, UnidadeTrabalho.class, Integer.class);
	}

	public UnidadeTrabalho buildSalvar(Scanner scanner) {
		print("Informe o nome da nova UnidadeTrabalho: ");
		String nome = scanner.nextLine().toUpperCase();
		print("Informe o endereço da nova UnidadeTrabalho: ");
		String endereco = scanner.nextLine().toUpperCase();
		UnidadeTrabalho unidadeTrabalho = UnidadeTrabalho.builder().nome(nome).endereco(endereco).build();
		return unidadeTrabalho;
	}

	public UnidadeTrabalho buildAtualizar(Scanner scanner) {
		print("Informe o ID do registro que deseja atualizar: ");
		Integer id = Integer.valueOf(scanner.nextLine());
		Optional<UnidadeTrabalho> unidadeTrabalho = crudRepository.findById(id);

		unidadeTrabalho.ifPresentOrElse(u -> {

			print("Registro localizado: ");
			System.out.println(u);
			print("Informe o novo nome: ");
			u.setNome(scanner.nextLine().toUpperCase());
			print("Informe o novo endereço: ");
			u.setEndereco(scanner.nextLine().toUpperCase());
		}, () -> print(String.format("Registro de ID<%s> não localizado.", id)));

		return unidadeTrabalho.orElse(null);
	}

	public Iterable<UnidadeTrabalho> findUnidadesTrabalhoPorListaId(List<Integer> listaIdUnidadeTrabalho) {
		return crudRepository.findAllById(listaIdUnidadeTrabalho);
	}

	@Override
	protected void menuPesquisar(Scanner scanner) {
		print("Opção não disponível.");
	}

}