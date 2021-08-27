package br.edu.ifrn.siteifsol.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * ENTIDADE CIDADE É A TABELA CIDADE NO BANDO DE DADOS
 *  QUE REPERESENTA AS CIDADES DOS EMPREENDIMENTOS
 *  OU SEJA TEM UM RELACIONAMENTO COM A TABELA EMPREENDIMENTOS
 * TODOS OS ATRIBUTOS AQUI SÃO COLINAS NO BANCO DE DADOS
 */

@Entity
public class Cidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String nome;

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

	@Override
	public String toString() {
		return nome;
	}

}
