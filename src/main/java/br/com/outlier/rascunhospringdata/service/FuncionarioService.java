package br.com.outlier.rascunhospringdata.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.outlier.rascunhospringdata.entity.Cargo;
import br.com.outlier.rascunhospringdata.entity.Funcionario;
import br.com.outlier.rascunhospringdata.entity.UnidadeTrabalho;
import br.com.outlier.rascunhospringdata.repository.FuncionarioRepository;

@Service
@Transactional
public class FuncionarioService extends GenericCrudService<Long, Funcionario, FuncionarioRepository> {

	private CargoService cargoService;
	private UnidadeTrabalhoService unidadeTrabalhoService;
	private static DateTimeFormatter dtft = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public FuncionarioService(FuncionarioRepository funcionarioRepository, CargoService cargoService,
			UnidadeTrabalhoService unidadeTrabalhoService) {
		super(funcionarioRepository, Funcionario.class, Long.class);
		this.cargoService = cargoService;
		this.unidadeTrabalhoService = unidadeTrabalhoService;
	}

	public Funcionario buildSalvar(Scanner scanner) {

		print("Informe o nome do novo Funcionario: ");
		String nome = scanner.nextLine().toUpperCase();
		print("Informe o cpf do novo Funcionario: ");
		String cpf = scanner.nextLine().toUpperCase();
		print("Informe o salário do novo Funcionario: ");
		String salario = scanner.nextLine();
		print("Informe a data de contratação do novo Funcionario: ");
		String dataContratacao = scanner.nextLine();
		print("Informe o cargo (id) do novo Funcionario: ");
		cargoService.listar(scanner);
		String idCargo = scanner.nextLine();
		Cargo cargo = cargoService.getCargoById(Integer.valueOf(idCargo));
		print("Informe a(s) unidade(s) de trabalho (id) do novo Funcionario (separe por vírgula): ");
		unidadeTrabalhoService.listar(scanner);
		String listaIdUnidadeTrabalho = scanner.nextLine();

		Iterable<UnidadeTrabalho> unidadesTrabalho = unidadeTrabalhoService.findUnidadesTrabalhoPorListaId(
				Stream.of(listaIdUnidadeTrabalho.split(",")).map(Integer::valueOf).collect(Collectors.toList()));

		List<UnidadeTrabalho> listaUt = new ArrayList<>();
		unidadesTrabalho.forEach(listaUt::add);

		Funcionario funcionario = Funcionario.builder().nome(nome).cpf(cpf).salario(new BigDecimal(salario))
				.dataContratacao(LocalDate.parse(dataContratacao, dtft)).cargo(cargo).unidadesTrabalho(listaUt).build();

		return funcionario;
	}

	public Funcionario buildAtualizar(Scanner scanner) {

		print("Informe o ID do registro que deseja atualizar: ");
		Long id = Long.valueOf(scanner.nextLine());
		Optional<Funcionario> funcionario = crudRepository.findById(id);

		funcionario.ifPresentOrElse(f -> {

			print("Registro localizado: ");
			System.out.println(f);

			// lê os novos dados
			print("Informe o nome do novo Funcionario: ");
			String nome = scanner.nextLine().toUpperCase();
			print("Informe o cpf do novo Funcionario: ");
			String cpf = scanner.nextLine().toUpperCase();
			print("Informe o salário do novo Funcionario: ");
			String salario = scanner.nextLine();
			print("Informe a data de contratação do novo Funcionario: ");
			String dataContratacao = scanner.nextLine();
			print("Informe o cargo (id) do novo Funcionario: ");
			cargoService.listar(scanner);
			String idCargo = scanner.nextLine();
			Cargo cargo = cargoService.getCargoById(Integer.valueOf(idCargo));
			print("Informe a(s) unidade(s) de trabalho (id) do novo Funcionario (separe por vírgula): ");
			String listaIdUnidadeTrabalho = scanner.nextLine();

			Iterable<UnidadeTrabalho> unidadesTrabalho = unidadeTrabalhoService.findUnidadesTrabalhoPorListaId(
					Stream.of(listaIdUnidadeTrabalho.split(",")).map(Integer::valueOf).collect(Collectors.toList()));

			List<UnidadeTrabalho> listaUt = new ArrayList<>();
			unidadesTrabalho.forEach(listaUt::add);
			f.setNome(nome);
			f.setCpf(cpf);
			f.setSalario(new BigDecimal(salario));
			f.setDataContratacao(LocalDate.parse(dataContratacao, dtft));
			f.setCargo(cargo);
			f.getUnidadesTrabalho().clear();
			f.getUnidadesTrabalho().addAll(listaUt);

		}, () -> print(String.format("Registro de ID<%s> não localizado.", id)));

		return funcionario.orElse(null);
	}

	@Override
	protected void menuPesquisar(Scanner scanner) {
		print("Escolha a opção desejada: ");
		print("0 - Retornar ");
		print("1 - Pesquisar por nome");
		print("2 - Pesquisar por cargo (id)");

		String opcao = scanner.nextLine();

		switch (opcao) {
		case "0": {
			break;
		}
		case "1": {
			pesquisarPorNome(scanner);
			break;
		}
		case "2": {
			pesquisarPorCargo(scanner);
			break;
		}
		default:
			print("Opção inválida!");
		}
	}

	public void pesquisarPorNome(Scanner scanner) {
		print("Informe o nome ou parte dele: ");
		String nome = scanner.nextLine();
		List<Funcionario> funcionarios = crudRepository.findByNomeLike(("%" + nome + "%").toUpperCase());
		print("Listando registros encontrados: ");
		funcionarios.forEach(f -> print(f + "\n"));
	}

	public void pesquisarPorCargo(Scanner scanner) {
		print("Informe o cargo (id) desejado: ");
		cargoService.listar(scanner);
		String idCargo = scanner.nextLine();
		List<Funcionario> funcionarios = crudRepository.findByCargoId(Integer.valueOf(idCargo));
		print("Listando registros encontrados: ");
		funcionarios.forEach(f -> print(f + "\n"));
	}

}