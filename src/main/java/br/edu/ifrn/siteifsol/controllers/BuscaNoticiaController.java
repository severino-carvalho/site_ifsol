package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de busca e remorção de {@link Noticia}
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repositories.ArquivoRepository;
import br.edu.ifrn.siteifsol.repositories.NoticiaRepository;

@Controller
@RequestMapping("/noticias")
public class BuscaNoticiaController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private NoticiaRepository noticiaRepository;

	@Autowired
	private ArquivoRepository arquivoRepository;

	/**
	 * 
	 * @param titulo            Parâmetro de busca
	 *
	 * @param texto             Parâmetro de busca
	 * 
	 * @param mostrarTodosDados Informa se ele quer que mostre todos os dados
	 * 
	 * @param modelo            Responsável pela criacao dos nomes de atributos que
	 *                          são retornados para a página
	 * 
	 * @return página de CRUD de Noticia
	 */
	@Transactional(readOnly = true)
	@GetMapping("/buscarnoticia")
	public String buscarNoticia(@RequestParam(name = "titulo", required = false) String titulo,
			@RequestParam(name = "texto", required = false) String texto,
			@RequestParam(name = "mostrarTodosDados", required = false) Boolean mostrarTodosDados, ModelMap modelo) {

		try {
			/**
			 * Busca os usuários pelos parâmetros informados
			 */
			List<Noticia> noticiasEncontradas = noticiaRepository.findByTituloAndTexto(titulo, texto);

			/**
			 * Se não tiver nenhum notícia buscada, há um retorno visual para o
			 * usuário
			 */
			if (noticiasEncontradas.isEmpty()) {
				modelo.addAttribute("msgErro", "Nenhuma notícia encontrada");
			} else {
				Collections.reverse(noticiasEncontradas);
				modelo.addAttribute("noticias", noticiasEncontradas);
				modelo.addAttribute("msgSucesso", "Busca concluída! Notícia(s) encontrada(s)");
			}

			if (mostrarTodosDados != null) {
				modelo.addAttribute("mostrarTodosDados", true);
			}

			modelo.addAttribute("noticia", new Noticia());

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/noticia/cadastrarNoticia";
	}

	/**
	 * 
	 * @param idNoticia Id da Noticia que vai para edição passado no path
	 * 
	 * @param modelo    Responsável pela criacao dos nomes de atributos que são
	 *                  retornados para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Noticia
	 */
	@Transactional(readOnly = true)
	@GetMapping("/edita/{id}")
	public String iniciarEdicao(@PathVariable("id") Integer idNoticia, ModelMap modelo) {

		try {
			/**
			 * Lista de empreendimentos para serem mostrados na página (BUSCA AUTOMÁTICA)
			 */
			List<Noticia> noticias = noticiaRepository.findAll();
			Collections.reverse(noticias);

			Noticia n = noticiaRepository.findById(idNoticia).get();
			/**
			 * Com isso, os dados da notícias vai ser carregado automaticamente pelo
			 * Thymeleaf
			 */
			modelo.addAttribute("noticia", n);
			modelo.addAttribute("noticias", noticias);
		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/noticia/cadastrarNoticia";
	}

	/**
	 * 
	 * @param idNoticia Id da Noticia que vai para edição passado no path
	 * 
	 * @param attr      Responsável pela criacao dos nomes de atributos que são
	 *                  retornados com o uso do 'redirect' para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Noticia
	 */
	@Transactional(readOnly = false)
	@GetMapping("/remove/{id}")
	public String remover(@PathVariable("id") Integer idNoticia, RedirectAttributes attr) {

		try {
			/**
			 * Removemos a foto da Noticia antes de removela
			 */
			Noticia noticia = noticiaRepository.findById(idNoticia).get();

			if (noticia.getFoto() != null) {
				arquivoRepository.deleteById(noticia.getFoto().getId());

			}

			noticiaRepository.deleteById(idNoticia);

			/**
			 * Lista de usuários para serem mostrados na página (BUSCA AUTOMÁTICA)
			 */
			List<Noticia> noticias = noticiaRepository.findAll();
			Collections.reverse(noticias);

			attr.addFlashAttribute("noticias", noticia);
			attr.addFlashAttribute("msgSucesso", "Noticia removida com sucesso!");

			if (!noticias.isEmpty()) {
				attr.addFlashAttribute("noticias", noticias);
			}

		} catch (Exception e) {
			attr.addFlashAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "redirect:/noticia/config";
	}
}
