package br.edu.ifrn.siteifsol.dominio;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser o modelo da entidade {@link Empreendimento}
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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Empreendimento {

	/**
	 * Atributo identificador da classe
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;

	/**
	 * Mapeamento de colunas para o Banco de Dados
	 */
	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String email;

	@ManyToOne(optional = false)
	private Cidade cidade;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private String criadoPor;

	@Column(nullable = false)
	private String dataCriacao;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Arquivo foto;

	/**
	 * Métodos Construtores
	 */
	public Empreendimento() {
		super();
	}

	/*
	 * Implementação dos métodos hashCode e equals
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
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
		Empreendimento other = (Empreendimento) obj;
		if (Id != other.Id)
			return false;
		return true;
	}

	/**
	 * Implementação dos métodos get's e set's
	 */
	public Arquivo getFoto() {
		return foto;
	}

	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(String criadoPor) {
		this.criadoPor = criadoPor;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

}
