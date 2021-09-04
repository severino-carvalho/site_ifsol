package br.edu.ifrn.siteifsol.dominio;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/*
 * ENTIDADE EMPREENDIMENTO É A TABELA USUARIO NO BANDO DE DADOS
 * TODOS OS ATRIBUTOS AQUI SÃO COLINAS NO BANCO DE DADOS
 */

@Entity
public class empreendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String nome;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String email;

	@ManyToOne(optional = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private Cidade cidade;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String descricao;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String criadoPor;

	@Column(nullable = false) // DIZ QUE O ATRIBUTO É UMA COLUNA
	private String dataCriacao;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Arquivo foto;

	/*
	 * CONSTRUTORES PARA SITEMA POSSA DIFERENCIAR UM ID DO OUTRO NÃO ADICIONANDO
	 * ASSIM IDS IGUAIS PARA USUARIOS DIFERENTES
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
		empreendimento other = (empreendimento) obj;
		if (Id != other.Id)
			return false;
		return true;
	}

	/*
	 * METODOS GET`S E SET`S ONDE OS ATRIBUTO SÃO DA ENTIDADE PARA QUE OS ATRIBUTOS
	 * POSSAM SER ACESSADOS EM NA PÁGINA HMTL
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
