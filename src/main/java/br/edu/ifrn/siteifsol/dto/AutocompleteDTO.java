package br.edu.ifrn.siteifsol.dto;

/*
 * CLASSE FAZ PARTE DO AUTOCOMPLETE DA CLASSE DE EMPREENDIMENTO
 * PARA QUE SEJA POSSIVEL FAZER O AUTOCOMPLETE NA PAGINA HTML
 */

public class AutocompleteDTO {
	
	private String label;
	
	private Integer value;

	// CONSTRUDOR PERMITE TER ACESSO AOS DADOS DA CLASSE
	public AutocompleteDTO(String label, Integer value) {
		super();
		this.label = label;
		this.value = value;
	}

	
	/*
	 * METODOS GET`S E SET`S ONDE OS ATRIBUTO SÃO DA ENTIDADE 
	 * PARA QUE OS ATRIBUTOS POSSAM SER ACESSADOS EM NA
	 * PÁGINA HMTL
	 */

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
