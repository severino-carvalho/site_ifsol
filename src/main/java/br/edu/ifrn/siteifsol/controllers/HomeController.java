package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte pública da aplicação
 *				Responável pelas URLs 'publico/home' e 'publico/noticia/{id}'			

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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repositories.NoticiaRepository;

@Controller
public class HomeController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private NoticiaRepository noticiaRepository;

	/**
	 * 
	 * @return Redireciona para a página home da parte pública
	 */
	@GetMapping("/publico/")
	public String padrao() {
		return "redirect:/publico/home";
	}

	/**
	 * 
	 * @param modelo Responsável pela criacao dos nomes de atributos que
	 *               são retornados para a página
	 * 
	 * @return A página home da parte pública com as notícias que estão cadastradas
	 *         no sistema
	 */
	@GetMapping("/publico/home")
	@Transactional(readOnly = true)
	public String home(ModelMap modelo) {
		List<Noticia> noticias = noticiaRepository.findAll();

		Collections.reverse(noticias);

		modelo.addAttribute("noticias", noticias);

		return "/visitantes/Home";
	}

	/**
	 * 
	 * @param idNoticia Id da Notícia que vai ser lida com detalhe passado no path
	 * 
	 * @param modelo    Responsável pela criacao dos nomes de atributos que são
	 *                  retornados para a página
	 * 
	 * @param attr      Responsável pela criacao dos nomes de atributos que são
	 *                  retornados com o uso do 'redirect' para a página
	 * 
	 * @return Para a página de leia mais mostrando a notícia que quer ser lida com
	 *         detalhe
	 */
	@GetMapping("/publico/noticia/{id}")
	@Transactional(readOnly = true)
	public String buscarNoticia(@PathVariable("id") Integer idNoticia, ModelMap modelo, RedirectAttributes attr) {

		try {
			List<Noticia> noticias = noticiaRepository.findAll();
			Optional<Noticia> noticiaEncontrada = noticiaRepository.findById(idNoticia);

			if (noticiaEncontrada.isPresent()) {
				modelo.addAttribute("noticia", noticiaEncontrada.get());

				Collections.reverse(noticias);
				modelo.addAttribute("noticias", noticias);
			} else {
				attr.addFlashAttribute("msgErro", "");
				return "redirect:/publico/home";
			}
		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/noticia/buscarNoticia";
	}

}
