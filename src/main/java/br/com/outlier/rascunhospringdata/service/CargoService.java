package br.com.outlier.rascunhospringdata.service;

import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.outlier.rascunhospringdata.entity.Cargo;
import br.com.outlier.rascunhospringdata.repository.CargoRepository;

@Service
@Transactional
public class CargoService {

	private CargoRepository cargoRepository;

	public CargoService(CargoRepository cargoRepository) {
		this.cargoRepository = cargoRepository;
	}

	public void menu(Scanner scanner) {

		String opcao = "-1";
		while (!opcao.equals("0")) {
			// imprime 5 linhas
			IntStream.rangeClosed(1, 5).boxed().forEach(x -> System.out.println());
			System.out.println("[CARGOS] Escolha a opção desejada: ");
			System.out.println("[CARGOS] 0 - Retornar ");
			System.out.println("[CARGOS] 1 - Listar registros");
			System.out.println("[CARGOS] 2 - Novo registro");
			System.out.println("[CARGOS] 3 - Atualizar registro");
			System.out.println("[CARGOS] 4 - Excluir registro");

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
			default:
				throw new IllegalArgumentException("[CARGO] Opção inválida!");
			}

		}
	}

	public void listar(Scanner scanner) {
		System.out.println("[CARGO] Listando cargos cadastrados.");
		Iterable<Cargo> cargos = cargoRepository.findAll();
		cargos.forEach(c -> System.out.println("[CARGO] " + c));
	}

	public Cargo salvar(Scanner scanner) {
		System.out.println("[CARGO] Informe a descrição do novo cargo: ");
		Cargo cargo = Cargo.builder().descricao(scanner.nextLine().toUpperCase()).build();
		cargoRepository.save(cargo);
		System.out.println("[CARGO] Operação realizada com sucesso!");
		return cargo;
	}

	public Cargo atualizar(Scanner scanner) {
		System.out.println("[CARGO] Informe o ID do registro que deseja atualizar: ");
		Integer id = Integer.valueOf(scanner.nextLine());
		Optional<Cargo> cargo = cargoRepository.findById(id);

		cargo.ifPresentOrElse(c -> {

			System.out.println("[CARGO] Cargo localizado: ");
			System.out.println(c);
			System.out.println("[CARGO] Informe a nova descrição do cargo: ");
			c.setDescricao(scanner.nextLine().toUpperCase());
			cargoRepository.save(c);
			System.out.println("[CARGO] Operação realizada com sucesso!");

		}, () -> System.out.println(String.format("[CARGO] Cargo de ID<%s> não localizado.", id)));

		return cargo.orElse(null);
	}

	public void deletar(Scanner scanner) {
		System.out.println("[CARGO] Informe o ID do registro que deseja excluir, ou T para excluir todos: ");
		String opcao = scanner.nextLine();

		if (opcao.trim().toUpperCase().equals("T")) {
			cargoRepository.deleteAll();
			System.out.println("[CARGO] Operação realizada com sucesso!");

		} else {

			Integer id = Integer.valueOf(scanner.nextLine());
			Optional<Cargo> cargo = cargoRepository.findById(id);

			cargo.ifPresentOrElse(c -> {

				System.out.println("[CARGO] Cargo localizado: ");
				System.out.println(c);
				System.out.println("[CARGO] Confirmar exclusão? (S/N) ");
				String confirmacao = scanner.nextLine();
				if (confirmacao.trim().toUpperCase().equals("S")) {
					cargoRepository.delete(c);
					System.out.println("[CARGO] Operação realizada com sucesso!");
				} else {
					System.out.println("[CARGO] Operação não realizada!");
				}

			}, () -> System.out.println(String.format("[CARGO] Cargo de ID<%s> não localizado.", id)));
		}
	}

	public Cargo getCargoById(Integer id) {
		return cargoRepository.findById(id).orElse(null);
	}
}