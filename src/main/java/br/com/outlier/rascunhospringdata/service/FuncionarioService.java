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

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.outlier.rascunhospringdata.entity.Cargo;
import br.com.outlier.rascunhospringdata.entity.Funcionario;
import br.com.outlier.rascunhospringdata.entity.UnidadeTrabalho;
import br.com.outlier.rascunhospringdata.repository.FuncionarioRepository;
import br.com.outlier.rascunhospringdata.repository.specification.FuncionarioSpecification;

@Service
@Transactional
public class FuncionarioService extends GenericCrudService<Long, Funcionario, FuncionarioRepository> {

	private CargoService cargoService;
	private UnidadeTrabalhoService unidadeTrabalhoService;
	private static DateTimeFormatter dtft = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public FuncionarioService(FuncionarioRepository funcionarioRepository, CargoService cargoService, UnidadeTrabalhoService unidadeTrabalhoService) {
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

		Iterable<UnidadeTrabalho> unidadesTrabalho = unidadeTrabalhoService
				.findUnidadesTrabalhoPorListaId(Stream.of(listaIdUnidadeTrabalho.split(",")).map(Integer::valueOf).collect(Collectors.toList()));

		List<UnidadeTrabalho> listaUt = new ArrayList<>();
		unidadesTrabalho.forEach(listaUt::add);

		Funcionario funcionario = Funcionario.builder().nome(nome).cpf(cpf).salario(new BigDecimal(salario)).dataContratacao(LocalDate.parse(dataContratacao, dtft)).cargo(cargo)
				.unidadesTrabalho(listaUt).build();

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

			Iterable<UnidadeTrabalho> unidadesTrabalho = unidadeTrabalhoService
					.findUnidadesTrabalhoPorListaId(Stream.of(listaIdUnidadeTrabalho.split(",")).map(Integer::valueOf).collect(Collectors.toList()));

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
		print("3 - Pesquisar por vários campos");

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
		case "3": {
			pesquisarVariosCampos(scanner);
			break;
		}
		default:
			print("Opção inválida!");
		}
	}

	public void pesquisarVariosCampos(Scanner scanner) {
		print("Informe os campos desejados na seguinte ordem -> Nome, CPF, Cargo (id), Unidade de Trabalho (id), Data de Contratação (início) e Data de Contratação (fim):");

		String nome = scanner.nextLine();
		String cpf = scanner.nextLine();
		String cargoId = scanner.nextLine();
		String unidadeTrabalhoId = scanner.nextLine();
		String dataInicio = scanner.nextLine();
		String dataFim = scanner.nextLine();

		nome = !nome.isBlank() ? nome : null;
		cpf = !cpf.isBlank() ? cpf : null;
		Integer cargoIdInt = !cargoId.isBlank() ? Integer.valueOf(cargoId) : null;
		Integer unidadeTrabalhoIdInt = !unidadeTrabalhoId.isBlank() ? Integer.valueOf(unidadeTrabalhoId) : null;

		LocalDate dtInicio = !dataInicio.isBlank() ? LocalDate.parse(dataInicio, dtft) : null;
		LocalDate dtFim = !dataFim.isBlank() ? LocalDate.parse(dataFim, dtft) : null;

		List<Specification<Funcionario>> specs = new ArrayList<>();
		if (nome != null) {
			specs.add(FuncionarioSpecification.nomeLike(nome));
		}

		if (cpf != null) {
			specs.add(FuncionarioSpecification.cpf(cpf));
		}

		if (cargoIdInt != null) {
			specs.add(FuncionarioSpecification.cargo(Cargo.builder().id(cargoIdInt).build()));
		}

		if (unidadeTrabalhoIdInt != null) {
			specs.add(FuncionarioSpecification.unidadeTrabalho(UnidadeTrabalho.builder().id(unidadeTrabalhoIdInt).build()));
		}

		if (dtInicio != null) {
			specs.add(FuncionarioSpecification.dataContratacaoGe(dtInicio));
		}

		if (dtFim != null) {
			specs.add(FuncionarioSpecification.dataContratacaoLe(dtFim));
		}

		Specification<Funcionario> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
		for (Specification<Funcionario> specification : specs) {
			spec = spec.and(specification);
		}
		List<Funcionario> funcionarios = crudRepository.findAll(spec);

		print("Listando registros encontrados: ");
		funcionarios.forEach(f -> print(f + "\n"));
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