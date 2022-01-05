package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de busca e remorção de {@link Empreendimento}
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

import br.edu.ifrn.siteifsol.dominio.Cidade;
import br.edu.ifrn.siteifsol.dominio.Empreendimento;
import br.edu.ifrn.siteifsol.repositories.ArquivoRepository;
import br.edu.ifrn.siteifsol.repositories.Cidaderepository;
import br.edu.ifrn.siteifsol.repositories.EmpreendimentoRepository;

@Controller
@RequestMapping("/usuarios")
public class BuscarEmpreendimentosController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private EmpreendimentoRepository empreendimentosrepository;

	@Autowired
	private ArquivoRepository arquivoRepository;

	@Autowired
	private Cidaderepository cidaderepository;

	/**
	 * 
	 * @return a página de CRUD de Empreendimento
	 */
	@GetMapping("/buscaem")
	public String entrarBusca() {
		return "/admin/empreendimento/cadastroEmpre";
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
	 * @return a página de CRUD de Empreendimeto
	 */
	@Transactional(readOnly = true)
	@GetMapping("/buscaempre")
	public String buscaempre(@RequestParam(name = "nome", required = false) String nome,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mostrarTodosDados", required = false) Boolean mostrarTodosDados, ModelMap modelo) {

		try {
			/**
			 * Busca os usuários pelos parâmetros informados
			 */
			List<Empreendimento> empreendimentosEncontrados = empreendimentosrepository.findByEmailAndNome(email, nome);

			/**
			 * Se não tiver nenhum empreendimento buscado, há um retorno visual para o
			 * usuário
			 */
			if (empreendimentosEncontrados.isEmpty()) {
				modelo.addAttribute("msgErro", "Nenhuma notícia encontrada");
			} else {
				Collections.reverse(empreendimentosEncontrados);
				modelo.addAttribute("empreendimentosEncontrados", empreendimentosEncontrados);
				modelo.addAttribute("msgSucesso", "Busca concluída! Empreendimento(s) encontrado(s)");
			}

			if (mostrarTodosDados != null) {
				modelo.addAttribute("mostrarTodosDados", true);
			}

			modelo.addAttribute("empre", new Empreendimento());
			modelo.addAttribute("cidades", getCidades());

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/empreendimento/cadastroEmpre";
	}

	/**
	 * 
	 * @param idempre Id do empreendimento que vai para edição passado no path
	 * 
	 * @param model   Responsável pela criacao dos nomes de atributos que são
	 *                retornados para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Empreendimento
	 */
	@Transactional(readOnly = true)
	@GetMapping("/edita/{id}")
	public String iniciarEdição(@PathVariable("id") Integer idempre, ModelMap model) {

		try {
			/**
			 * Lista de empreendimentos para serem mostrados na página (BUSCA AUTOMÁTICA)
			 */
			List<Empreendimento> empEnc = empreendimentosrepository.findAll();
			Collections.reverse(empEnc);

			Empreendimento e = empreendimentosrepository.findById(idempre).get();

			/**
			 * Com isso, os dados do empreendimento vai ser carregado automaticamente pelo
			 * Thymeleaf
			 */
			model.addAttribute("empre", e);

			model.addAttribute("empreendimentosEncontrados", empEnc);
			model.addAttribute("cidades", getCidades());
		} catch (Exception e) {
			model.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}
		return "/admin/empreendimento/cadastroEmpre";
	}

	/**
	 * 
	 * @param idempree Id do empreendimento que vai para edição passado no path
	 *
	 * @param attr     Responsável pela criacao dos nomes de atributos que são
	 *                 retornados com o uso do 'redirect' para a página
	 *
	 * @return Retorna a mesma página de CRUD de Empreendimento
	 */
	@Transactional(readOnly = false)
	@GetMapping("/remove/{id}")
	public String remover(@PathVariable("id") Integer idempree, RedirectAttributes attr) {

		try {

			/**
			 * Removemos a foto do empreendimento antes de removelo
			 */
			Empreendimento em = empreendimentosrepository.findById(idempree).get();

			if (em.getFoto() != null) {
				arquivoRepository.deleteById(em.getFoto().getId());
			}

			empreendimentosrepository.deleteById(idempree);

			/**
			 * Lista de usuários para serem mostrados na página (BUSCA AUTOMÁTICA)
			 */
			List<Empreendimento> empEnc = empreendimentosrepository.findAll();
			Collections.reverse(empEnc);

			attr.addFlashAttribute("empreendimentosEncontrados", empEnc);
			attr.addFlashAttribute("cidades", getCidades());

			attr.addFlashAttribute("msgSucesso", "Empreendimento removido com sucesso!");
		} catch (Exception e) {
			attr.addFlashAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "redirect:/usuario/cadastroem";
	}

	/**
	 * 
	 * @return As cidades para a tela de CRUD de Empreendimento
	 */
	@Transactional(readOnly = true)
	public List<Cidade> getCidades() {
		List<Cidade> cidades = cidaderepository.findAll();
		return cidades;
	}
}
