package br.com.outlier.rascunhospringdata.repository.projection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface ResumoFuncionario {

	public String getNome();

	public String getCpf();

	public LocalDate getDataContratacao();

	public String getCargo();

	public String getUnidade();

	default String print() {
		return String.format("%s-%s-%s-%s-%s", getNome(), getCpf(), getDataContratacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), getCargo(), getUnidade());
	}
}
