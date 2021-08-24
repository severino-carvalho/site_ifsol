package br.edu.ifrn.siteifsol.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.empreendimento;
import br.edu.ifrn.siteifsol.repository.empreendimentorepository;

@Controller
@RequestMapping("/usuarios") // URL PARA ACESSAR A PAGINA
public class BuscarEmpreendimentosController {

	@GetMapping("/buscaem") // URL PARA ACESSAR A PAGINA
	public String entrarBusca() {
		return "buscaem";
	}

	@Autowired
	private empreendimentorepository empreendimentosrepository;

	@Transactional(readOnly = true) // INFORMA QUE NÃO FAZ ALTERAÇÕES NO BANCO DE DADOS

	/*
	 * METODO A SEGUIR FAZ AS BUSCAS PELOS EMPREENDIMENTOS CADASTRADOS NO BANCO DE
	 * DADOS E RETONA ESSA LISTA DE USUARIOS CADASTRADOS PARA A PÁGINA WEB
	 */

	@GetMapping("/buscaempre")
	public String buscaempre(@RequestParam(name = "nome", required = false) String nome,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mostrarTodosDados", required = false) Boolean mostrarTodosDados, 
			HttpSession sessao,
			ModelMap model) {

		List<empreendimento> empreendimentosEncontrados = empreendimentosrepository.findByEmailAndNome(email, nome);

		model.addAttribute("empreendimentosEncontrados", empreendimentosEncontrados); // RETORNA OS USUARIOS ENCONTRADOS
																						// PARA A PÁGINA WEB

		if (mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		return "/buscaem";
	}

	/*
	 * METODO PARA FAZER A EDIÇÃO DE USUARIOS CADASTRADOS
	 */
	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/edita/{id}")
	public String iniciarEdição(@PathVariable("id") Integer idempre, ModelMap model, HttpSession sessao) {

		empreendimento u = empreendimentosrepository.findById(idempre).get();

		model.addAttribute("empre", u);

		return "/CadastroEmpre";
	}

	/*
	 * METODO FAZ A REMOÇÃO NO BANCO DE DADOS , DE EMPREENDIMENTOS CADASTRADOS
	 */

	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/remove/{id}")
	public String remover(@PathVariable("id") Integer idempree, HttpSession sessao, RedirectAttributes attr) {

		empreendimentosrepository.deleteById(idempree);// DELETA O EMPREENDIMENTO PELO ID
		attr.addAttribute("msgSucesso", "Usuario removido com sucesso!");

		return "redirect:/usuarios/buscaem";
	}
}
