package br.edu.ifrn.siteifsol.dominio;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser o modelo da entidade Arquivo
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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Arquivo {

	/**
	 * Atributo identificador da classe
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Mapeamento de colunas para o Banco de Dados
	 */
	private String nomeArquivo;

	private String tipoArquivo;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] dados;

	/**
	 * Métodos Construtores
	 */
	public Arquivo() {
		super();
	}

	public Arquivo(String nomeArquivo, String tipoArquivo, byte[] dados) {
		super();
		this.nomeArquivo = nomeArquivo;
		this/*
			 * Implementação dos métodos hashCode e equals
			 */.tipoArquivo = tipoArquivo;
		this.dados = dados;
	}

	/*
	 * Implementação dos métodos hashCode e equals
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Arquivo other = (Arquivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Implementação dos métodos get's e set's
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public byte[] getDados() {
		return dados;
	}

	public void setDados(byte[] dados) {
		this.dados = dados;
	}
}
