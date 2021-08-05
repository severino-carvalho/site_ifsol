package br.edu.ifrn.siteifsol.dominio;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;


/*
 * ENTIDADE ARQUIVO É A TABELA ARQUIVO NO BANDO DE DADOS
 * TODOS OS ATRIBUTOS AQUI SÃO COLINAS NO BANCO DE DADOS
 */
@Entity
public class Arquivo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String nomeArquivo;
	
	private String tipoArquivo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] dados;

	/*
	 * CONSTRUDOR QUE PERMITE QUE TODOS OS PARAMETROS SEJAM ACESSADOS/"SETADOS"
	 */
	public Arquivo(String nomeArquivo, String tipoArquivo, byte[] dados) {
		super();
		this.nomeArquivo = nomeArquivo;
		this.tipoArquivo = tipoArquivo;
		this.dados = dados;
	}
	
	//CONSTRUTOR VAZIO DA CLASSE ARQUIVO
	public Arquivo() {
		
	}


	/*
	 * METODOS GET`S E SET`S ONDE OS ATRIBUTO SÃO DA ENTIDADE 
	 * PARA QUE OS ATRIBUTOS POSSAM SER ACESSADOS EM NA
	 * PÁGINA HMTL
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
