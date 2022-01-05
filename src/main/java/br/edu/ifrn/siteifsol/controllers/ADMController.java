package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora de parte do sistema administrativo da aplicação
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
 * @author Felipe Barros (primariaconta22@gmail.com)
 * Data:	04/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifrn.siteifsol.repositories.NoticiaRepository;
import br.edu.ifrn.siteifsol.repositories.Usuariorepository;
import br.edu.ifrn.siteifsol.repositories.Empreendimentorepository;

@Controller
public class ADMController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private Usuariorepository usuariorepository;

	@Autowired
	private Empreendimentorepository empreendimentoRepository;

	@Autowired
	private NoticiaRepository noticiaRepository;

	/**
	 * 
	 * @param modelo Responsável pela criacao dos nomes de atributos que são
	 *               retornados para a página
	 * 
	 * @return a página principal da parte administrativa
	 */
	@GetMapping("/adm")
	public String home(ModelMap modelo) {
		try {
			modelo.addAttribute("totUsuarios", usuariorepository.findAll().size());
			modelo.addAttribute("totEmpre", empreendimentoRepository.findAll().size());
			modelo.addAttribute("totNoticias", noticiaRepository.findAll().size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/admin/ADM";
	}

}
