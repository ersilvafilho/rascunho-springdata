package br.com.outlier.rascunhospringdata.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntidadeBase<T extends Serializable> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private T id;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;

		EntidadeBase<T> other = (EntidadeBase<T>) obj;

		return Objects.equals(id, other.id);
	}

}
