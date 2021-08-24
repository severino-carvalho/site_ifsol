package br.edu.ifrn.siteifsol.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * ENTIDADE USUARIO É A TABELA USUARIO NO BANDO DE DADOS
 * TODOS OS ATRIBUTOS AQUI SÃO COLINAS NO BANCO DE DADOS
 */

@Entity
public class Usuario {

	public static final String ADMIN = "ADMIN";
	public static final String USUARIO_COMUM = "COMUM";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String nome;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String email;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String senha;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String situacao;

	@Column(nullable = false)
	private String perfil = USUARIO_COMUM;
	

	
	/*
	 * CONSTRUTORES PARA SITEMA POSSA DIFERENCIAR UM ID DO OUTRO NÃO ADICIONANDO
	 * ASSIM IDS IGUAIS PARA USUARIOS DIFERENTES
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
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;

	}
	
	//ifsoljc@ifrn.edu.br
	//senha123

	/*
	 * METODOS GET`S E SET`S ONDE OS ATRIBUTO SÃO DA ENTIDADE PARA QUE OS ATRIBUTOS
	 * POSSAM SER ACESSADOS EM NA PÁGINA HMTL
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public static String getAdmin() {
		return ADMIN;
	}

	public static String getUsuarioComum() {
		return USUARIO_COMUM;
	}

}
