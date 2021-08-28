package br.edu.ifrn.siteifsol.dominio;

import java.util.Calendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false)
	private String texto;

	@Column(nullable = false)
	private Calendar dataPublicacao;

	
	/*
	 * CONSTRUTOR PADRÃO DA CLASSE
	 * */
	
	public Post() {
		super();
	}
	
	/*
	 * CONSTRUTOR PARAMETRIZADO DA CLASSE
	 * */

	public Post(String titulo, String texto, Calendar dataPublicacao) {
		super();
		this.titulo = titulo;
		this.texto = texto;
		this.dataPublicacao = dataPublicacao;
	}
	
	/*
	 * METODOS GET'S E SET'S
	 * */

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

	public Calendar getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Calendar dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}
	
	/*
	 * IMPLEMENTAÇÃO DO EQUALS E HASHCODE
	 * SERVE PARA INDENTIFICAR CORRETAMENTE CADA POST QUE É CRIADO
	 * */

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
		Post other = (Post) obj;
		return id == other.id;
	}

}
