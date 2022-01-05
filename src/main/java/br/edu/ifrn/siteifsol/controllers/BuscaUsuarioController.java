package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de busca e remorção de {@link Usuario}
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
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * Data:	05/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.repositories.Usuariorepository;

@Controller
@RequestMapping("/usuarios")
public class BuscaUsuarioController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private Usuariorepository usuarioRepository;

	/**
	 * 
	 * @return a página de CRUD de Usuário
	 */
	@GetMapping("/busca")
	public String entrarBusca() {
		return "/admin/busca";
	}

	/**
	 * 
	 * @param nome              Parâmetro de busca
	 * 
	 * @param email             Parâmetro de busca
	 * 
	 * @param mostrarTodosDados Informa se ele quer que mostre todos os dados
	 * 
	 * @param modelo            Responsável pela criacao dos nomes de atributos que
	 *                          são retornados para a página
	 * 
	 * @return A página de CRUD de Usuário
	 */
	@Transactional(readOnly = true)
	@GetMapping("/buscar")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String buscar(@RequestParam(name = "nome", required = false) String nome,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mostrarTodosDados", required = false) Boolean mostrarTodosDados, ModelMap modelo) {

		try {
			/**
			 * Busca os usuários pelos parâmetros informados
			 */
			List<Usuario> usuariosEncontrados = usuarioRepository.findByEmailAndNome(email, nome);

			/**
			 * Se não tiver nenhum usuário buscado, há um retorno visual para o usuário
			 */
			if (usuariosEncontrados.isEmpty()) {
				modelo.addAttribute("msgErro", "Nenhum usuário encontrado");
			} else {
				Collections.reverse(usuariosEncontrados);
				modelo.addAttribute("usuariosEncontrados", usuariosEncontrados);
				modelo.addAttribute("msgSucesso", "Busca concluída! Usuário(s) encontrado(s)");
			}

			if (mostrarTodosDados != null) {
				modelo.addAttribute("mostrarTodosDados", true);
			}

			modelo.addAttribute("usuario", new Usuario());

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/usuario/cadastro";
	}

	/**
	 * 
	 * @param idUsuario Id do usuário que vai para edição passado no path
	 * 
	 * @param model     Responsável pela criacao dos nomes de atributos que são
	 *                  retornados para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Usuários
	 */
	@Transactional(readOnly = true)
	@GetMapping("/editar/{id}")
	public String iniciarEdição(@PathVariable("id") Integer idUsuario, ModelMap model) {

		try {
			/**
			 * Lista de usuários para serem mostrados na página (BUSCA AUTOMÁTICA)
			 */
			List<Usuario> usuarios = usuarioRepository.findAll();

			Usuario u = usuarioRepository.findById(idUsuario).get();

			/**
			 * Com isso, os dados do usuário vai ser carregado automaticamente pelo
			 * Thymeleaf
			 */
			model.addAttribute("usuario", u);

			model.addAttribute("usuariosEncontrados", usuarios);
		} catch (Exception e) {
			model.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
			model.addAttribute("usuario", new Usuario());
		}

		return "/admin/usuario/cadastro";
	}

	/**
	 * 
	 * @return
	 */
	@ModelAttribute("funcao")
	public List<String> getFuncao() {
		return Arrays.asList("Docente", "Bolsista", "Voluntário");
	}

	/**
	 * 
	 * @param idUsuario Id do usuário que vai para edição passado no path
	 * 
	 * @param model     Responsável pela criacao dos nomes de atributos que são
	 *                  retornados para a página
	 * 
	 * @param attr      Responsável pela criacao dos nomes de atributos que são
	 *                  retornados com o uso do 'redirect' para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Usuários
	 */
	@Transactional(readOnly = false)
	@GetMapping("/remover/{id}")
	public String remover(@PathVariable("id") Integer idUsuario, ModelMap model, RedirectAttributes attr) {

		try {
			usuarioRepository.deleteById(idUsuario);

			attr.addFlashAttribute("msgSucesso", "Usuario removido com sucesso!");

			/**
			 * Lista de usuários para serem mostrados na página (BUSCA AUTOMÁTICA)
			 */
			List<Usuario> usuarios = usuarioRepository.findAll();

			attr.addFlashAttribute("usuariosEncontrados", usuarios);

		} catch (Exception e) {
			attr.addFlashAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "redirect:/usuario/buscar";
	}

}
