package br.edu.ifrn.siteifsol.dominio;

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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false)
	private String texto;

	@Column(nullable = false)
	private String dataPublicacao;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Arquivo foto;

	/*
	 * CONSTRUTOR PADRÃO DA CLASSE
	 */

	public Noticia() {
		super();
	}

	/*
	 * CONSTRUTOR PARAMETRIZADO DA CLASSE
	 */

	public Noticia(String titulo, String texto, String dataPublicacao, Arquivo foto) {
		super();
		this.titulo = titulo;
		this.texto = texto;
		this.dataPublicacao = dataPublicacao;
		this.foto = foto;
	}

	/*
	 * METODOS GET'S E SET'S
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

	public Arquivo getFoto() {
		return foto;
	}

	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}

	/*
	 * IMPLEMENTAÇÃO DO EQUALS E HASHCODE SERVE PARA INDENTIFICAR CORRETAMENTE CADA
	 * POST QUE É CRIADO
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

}
