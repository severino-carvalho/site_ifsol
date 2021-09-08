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
import br.edu.ifrn.siteifsol.repository.ArquivoRepository;
import br.edu.ifrn.siteifsol.repository.empreendimentorepository;

@Controller
@RequestMapping("/usuarios") // URL PARA ACESSAR A PAGINA
public class BuscarEmpreendimentosController {

	@Autowired
	private empreendimentorepository empreendimentosrepository;

	@Autowired
	private ArquivoRepository arquivoRepository;

	@GetMapping("/buscaem") // URL PARA ACESSAR A PAGINA
	public String entrarBusca() {
		return "cadastroEmpre";
	}

	/*
	 * METODO A SEGUIR FAZ AS BUSCAS PELOS EMPREENDIMENTOS CADASTRADOS NO BANCO DE
	 * DADOS E RETONA ESSA LISTA DE USUARIOS CADASTRADOS PARA A PÁGINA WEB
	 */

	@Transactional(readOnly = true) // INFORMA QUE NÃO FAZ ALTERAÇÕES NO BANCO DE DADOS
	@GetMapping("/buscaempre")
	public String buscaempre(@RequestParam(name = "nome", required = false) String nome,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mostrarTodosDados", required = false) Boolean mostrarTodosDados, HttpSession sessao,
			ModelMap model) {

		// LISTA DE EMPREENDIMENTOS
		List<empreendimento> empreendimentosEncontrados = empreendimentosrepository.findByEmailAndNome(email, nome);

		// RETORNA PARA A PÁGINA UM NOVO EMPREENDIMENTO
		model.addAttribute("empre", new empreendimento());
		model.addAttribute("empreendimentosEncontrados", empreendimentosEncontrados); // RETORNA OS EMPREENDIMENTOS ENCONTRADOS PARA A PÁGINA WEB

		if (mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}
		
		return "/cadastroEmpre";
	}

	/*
	 * METODO PARA FAZER A EDIÇÃO DE USUARIOS CADASTRADOS
	 */
	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/edita/{id}")
	public String iniciarEdição(@PathVariable("id") Integer idempre, ModelMap model, HttpSession sessao) {

		try {
			// LISTA DE EMPREENDIMENTOS ENCONTRADOS
			List<empreendimento> empEnc = empreendimentosrepository.findAll();

			// BUSCA O EMPREENDIMENTO ESPECÍFICO DA EDIÇÃO
			empreendimento e = empreendimentosrepository.findById(idempre).get();

			// RETORNA O EMPREENDIMENTO DE EDIÇÃO PARA A PÁGINA
			model.addAttribute("empre", e);

			// RETORNA A LISTA DE EMPREENDIMENTO PARA A PÁGINA
			model.addAttribute("empreendimentosEncontrados", empEnc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cadastroEmpre";
	}

	/*
	 * METODO FAZ A REMOÇÃO NO BANCO DE DADOS , DE EMPREENDIMENTOS CADASTRADOS
	 */

	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/remove/{id}")
	public String remover(@PathVariable("id") Integer idempree, HttpSession sessao, RedirectAttributes attr) {

		try {
			// FAZ A BUSCA DO EMPREENDIMENTO SOLICITADO DA REMOÇÃO
			empreendimento em = empreendimentosrepository.findById(idempree).get();

			// DELETA A FOTO DO EMPREENDIMENTO PELO ID
			arquivoRepository.deleteById(em.getFoto().getId());
			
			// DELETA O EMPREENDIMENTO PELO ID
			empreendimentosrepository.deleteById(idempree);

			
			// APÓS A REMOÇÃO, LISTA OS EMPREENDIMENTOS
			List<empreendimento> empEnc = empreendimentosrepository.findAll();
			
			// RETORNA A LISTA DE EMPREENDIMENTO PARA A PÁGINA
			attr.addFlashAttribute("empreendimentosEncontrados", empEnc);
	
			// RETORNA A MENSAGEM DE SUCESSO PARA A PÁGINA
			attr.addFlashAttribute("msgSucesso", "Usuario removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/usuario/cadastroem";
	}
}
