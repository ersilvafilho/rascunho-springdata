package br.com.outlier.rascunhospringdata.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Funcionario extends EntidadeBase<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cpf;
	private BigDecimal salario;
	private LocalDate dataContratacao;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cargo")
	private Cargo cargo;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "funcionario_unid", joinColumns = { @JoinColumn(name = "id_funcionario") }, inverseJoinColumns = { @JoinColumn(name = "id_unidade_trabalho") })
	private List<UnidadeTrabalho> unidadesTrabalho;

	@Override
	public String toString() {
		String f = "Funcionario [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", salario=" + salario + ", dataContratacao=" + dataContratacao + ", cargo=" + cargo + ", unidadesTrabalho="
				+ unidadesTrabalho + "]";
		String border = IntStream.rangeClosed(1, f.length()).boxed().map(i -> "").collect(Collectors.joining("-"));
		return "\n" + border + "\n" + f + "\n" + border;
	}

}
