package br.com.outlier.rascunhospringdata.service;

import java.io.Serializable;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.outlier.rascunhospringdata.entity.EntidadeBase;
import br.com.outlier.rascunhospringdata.entity.UnidadeTrabalho;

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
		System.out.println(String.format("[%s] %s", entityName, mensagem));
	}

	public void menu(Scanner scanner) {

		String opcao = "-1";
		while (!opcao.equals("0")) {
			// imprime 5 linhas
			IntStream.rangeClosed(1, 5).boxed().forEach(x -> System.out.println());
			print("Escolha a opção desejada: ");
			print("0 - Retornar ");
			print("1 - Listar registros");
			print("2 - Novo registro");
			print("3 - Atualizar registro");
			print("4 - Excluir registro");
			print("5 - Pesquisar registro");

			opcao = scanner.nextLine();

			switch (opcao) {
			case "0": {
				break;
			}
			case "1": {
				listar(scanner);
				break;
			}
			case "2": {
				salvar(scanner);
				break;
			}
			case "3": {
				atualizar(scanner);
				break;
			}
			case "4": {
				deletar(scanner);
				break;
			}
			case "5": {
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

	public static void main(String[] args) {
		Class<UnidadeTrabalho> c = UnidadeTrabalho.class;
		System.out.println(c.getSimpleName());
	}
}