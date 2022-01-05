package br.edu.ifrn.siteifsol.dominio;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser o modelo da entidade Cidade
 * 
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * 
 * Data de Cricação:	04/07/2021
 * 
 * #####################################
 * 
 * Última alteração:	
 * 
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * Data:	04/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cidade {

	/**
	 * Atributo identificador da classe
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Mapeamento de colunas para o Banco de Dados
	 */
	@Column(nullable = false)
	private String nome;

	/**
	 * Método Construtor
	 */
	public Cidade() {
		super();
	}

	/*
	 * Implementação dos métodos hashCode e equals
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * Implementação dos métodos get's e set's
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}

}
