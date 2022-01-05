package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte pública da aplicação
 *				Responável pelas URLs '/', 'login'	e 'login-erro'		

 * Autor(es):	Felipe Barros	(primariaconta22@gmail.com)
 * 			 	Severino Carvalho	(severinocarvalho14@gmail.com)
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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

	/**
	 * 
	 * @return Redireciona para a página home da parte pública
	 */
	@GetMapping("/")
	public String inicio() {
		return "redirect:/publico/home";
	}

	/**
	 * 
	 * @return A página de login
	 */
	@GetMapping("/login")
	public String login() {
		return "/admin/Login";
	}

	/**
	 * 
	 * @param model Responsável pela criacao dos nomes de atributos que
	 *              são retornados para a página
	 * 
	 * @return A página de login retornando um erro
	 */
	@GetMapping("/login-erro")
	public String loginErro(ModelMap model) {
		model.addAttribute("msgErro", "Login ou senha incorreta");
		return "/admin/Login";
	}
}
