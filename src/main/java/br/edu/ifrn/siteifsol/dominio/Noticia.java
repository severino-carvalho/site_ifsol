package br.edu.ifrn.siteifsol.dominio;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser o modelo da entidade Notícia
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

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Noticia {

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
	private String titulo;

	@Column(nullable = false, length = 5000)
	private String texto;

	@Column(nullable = false)
	private String dataPublicacao;

	@Column(nullable = false)
	private String criadoPor;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Arquivo foto;

	/**
	 * Métodos Construtores
	 */
	public Noticia() {
		super();
	}

	public Noticia(String titulo, String texto, String dataPublicacao, String criadoPor, Arquivo foto) {
		this.titulo = titulo;
		this.texto = texto;
		this.dataPublicacao = dataPublicacao;
		this.criadoPor = criadoPor;
		this.foto = foto;
	}

	/*
	 * Implementação dos métodos hashCode e equals
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Noticia other = (Noticia) obj;
		return id == other.id;
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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(String criadoPor) {
		this.criadoPor = criadoPor;
	}

	public Arquivo getFoto() {
		return foto;
	}

	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}

}
