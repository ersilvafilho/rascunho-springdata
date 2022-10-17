package br.com.outlier.rascunhospringdata.service;

import java.io.Serializable;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import br.com.outlier.rascunhospringdata.entity.EntidadeBase;

@Service
@Transactional
public abstract class GenericCrudService<ID extends Serializable, T extends EntidadeBase<ID>, REPO extends CrudRepository<T, ID>> {

	protected REPO crudRepository;
	private String entityName;
	private Class<ID> idClass;

	public GenericCrudService(REPO crudRepository, Class<T> entidadeBase, Class<ID> idClass) {
		this.crudRepository = crudRepository;
		this.entityName = entidadeBase.getSimpleName();
		this.idClass = idClass;
	}

	protected void print(Object mensagem) {
		print(mensagem, false);
	}

	protected void print(Object mensagem, boolean clearScreen) {
		System.out.println(String.format("[%s] %s", entityName, mensagem));
		if (clearScreen) {
			for (int i = 0; i < 50; i++) {
				System.out.println();
			}
		}
	}

	public void menu(Scanner scanner) {

		String opcao = "-1";
		while (!opcao.equals("0")) {
			// imprime 5 linhas
			IntStream.rangeClosed(1, 5).boxed().forEach(x -> System.out.println());
			print("Escolha a opção desejada: ");
			print("0 - Retornar ");
			print("1 - Listar registros");
			print("2 - Listar registros (paginado)");
			print("3 - Novo registro");
			print("4 - Atualizar registro");
			print("5 - Excluir registro");
			print("6 - Pesquisar registro");
			
			opcao = scanner.nextLine();
			print("", true);

			switch (opcao) {
			case "0": {
				break;
			}
			case "1": {
				listar(scanner);
				break;
			}
			case "2": {
				listarPaginado(scanner);
				break;
			}
			case "3": {
				salvar(scanner);
				break;
			}
			case "4": {
				atualizar(scanner);
				break;
			}
			case "5": {
				deletar(scanner);
				break;
			}
			case "6": {
				menuPesquisar(scanner);
				break;
			}
			default:
				print("Opção inválida!");
			}

		}
	}

	protected abstract void menuPesquisar(Scanner scanner);

	public void listar(Scanner scanner) {
		print("Listando registros cadastrados: ");
		Iterable<T> registros = crudRepository.findAll();
		registros.forEach(r -> print(r.toString() + "\n"));
	}

	public void listarPaginado(Scanner scanner) {
		doListarPaginado(scanner, 0);
	}

	@SuppressWarnings("unchecked")
	public void doListarPaginado(Scanner scanner, int pagina) {
		print("Listando registros cadastrados: ");
		PagingAndSortingRepository<T, ID> pasr = (PagingAndSortingRepository<T, ID>) crudRepository;
		PageRequest pr = PageRequest.of(pagina, 5);
		Page<T> page = pasr.findAll(pr);
		print("");

		page.getContent().forEach(r -> print(r + "\n"));
		print(String.format("Mostrando %s registros", page.getNumberOfElements()));
		print(String.format("Página atual: %s", page.getNumber() + 1));
		print(String.format("Total de páginas: %s", page.getTotalPages()));
		print(String.format("Total de registros: %s", page.getTotalElements()));
		print("Informe qual página deseja visualizar, ou 0 para retornar: ");
		String paginaInformada = scanner.nextLine();

		Integer novaPagina = Integer.valueOf(paginaInformada);
		if (novaPagina.equals(0)) {
			return;
		} else if (novaPagina < 1 || novaPagina.compareTo(page.getTotalPages()) > 0) {
			print("Página inválida.");
			return;
		} else {
			print("", true);
			doListarPaginado(scanner, novaPagina - 1);
		}
	}

	public abstract T buildSalvar(Scanner scanner);

	public abstract T buildAtualizar(Scanner scanner);

	public T salvar(Scanner scanner) {
		T registro = crudRepository.save(buildSalvar(scanner));
		print("Operação realizada com sucesso!");
		return registro;
	}

	public T atualizar(Scanner scanner) {
		T registro = crudRepository.save(buildAtualizar(scanner));
		print("Operação realizada com sucesso!");
		return registro;
	}

	@SuppressWarnings("unchecked")
	public void deletar(Scanner scanner) {
		print("Informe o ID do registro que deseja excluir, ou T para excluir todos: ");
		String opcao = scanner.nextLine();

		if (opcao.trim().toUpperCase().equals("T")) {
			crudRepository.deleteAll();
			print("Operação realizada com sucesso!");

		} else {

			Object newInstance;
			try {
				newInstance = idClass.getDeclaredConstructors()[0].newInstance(opcao);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			Optional<T> registro = crudRepository.findById((ID) newInstance);

			registro.ifPresentOrElse(r -> {

				print("Registro localizado: ");
				print(r);
				print("Confirmar exclusão? (S/N) ");
				String confirmacao = scanner.nextLine();
				if (confirmacao.trim().toUpperCase().equals("S")) {
					crudRepository.delete(r);
					print("Operação realizada com sucesso!");
				} else {
					print("Operação não realizada!");
				}

			}, () -> print(String.format("Registro de ID<%s> não localizado.", newInstance)));
		}
	}

}