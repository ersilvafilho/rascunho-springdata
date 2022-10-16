package br.com.outlier.rascunhospringdata;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import br.com.outlier.rascunhospringdata.service.CargoService;

@SpringBootApplication
@EnableCaching
public class RascunhoSpringDataApplication implements CommandLineRunner {

	private CargoService cargoService;

	public RascunhoSpringDataApplication(CargoService cargoService) {
		this.cargoService = cargoService;
	}

	public static void main(String[] args) {
		SpringApplication.run(RascunhoSpringDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		boolean sair = false;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bem vindo ao Outlier Spring Data!");

		while (!sair) {
			System.out.println("Selecione a opção desejada");
			System.out.println("0 - Sair");
			System.out.println("1 - Registro de Cargos");
			System.out.println("2 - Registro de Funcionários");

			String opcao = scanner.nextLine();
			if (opcao.equals("0")) {
				sair = true;

			} else {

				switch (opcao) {

				case "1": {
					cargoService.menu(scanner);
					break;
				}

				default:
					System.out.println("Opção inválida!");
				}
			}
		}

	}
}
