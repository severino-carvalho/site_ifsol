package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte pública da aplicação
 *				Responável pela URL '/publico/empreendimentos'		
 *
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * 
 * Data de Cricação:	05/07/2021
 * 
 * #####################################
 * 
 * Última alteração:	
 * 
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * Data:	05/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifrn.siteifsol.dominio.Empreendimento;
import br.edu.ifrn.siteifsol.repositories.EmpreendimentoRepository;

@Controller
public class EmpreendimentosController {

	/**
	 * Repositório JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private EmpreendimentoRepository empreendimentorepository;

	/**
	 * 
	 * @param modelo Responsável pela criacao dos nomes de atributos que são
	 *               retornados para a página
	 * 
	 * @return A página 'Empreendimentos'
	 */
	@GetMapping("/publico/empreendimentos")
	@Transactional(readOnly = true)
	public String openEmpreendimentos(ModelMap modelo) {
		try {

			List<Empreendimento> empreendimentos = empreendimentorepository.findAll();
			Collections.reverse(empreendimentos);
			modelo.addAttribute("empEncontrados", empreendimentos);

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/visitantes/Empreendimentos";
	}

}
