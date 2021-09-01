package br.edu.ifrn.siteifsol.controladores;

import java.util.Arrays;
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
import br.edu.ifrn.siteifsol.repository.Usuariorepository;

@Controller
@RequestMapping("/usuarios") // URL PARA ACESSAR A PAGINA
public class BuscaUsuarioController {

	@GetMapping("/busca") // URL PARA ACESSAR A PAGINA
	public String entrarBusca() {
		return "/busca";
	}

	@Autowired
	private Usuariorepository usuarioRepository;

	/*
	 * METODO A SEGUIR FAZ AS BUSCAS PELOS USUARIOS CADASTRADOS NO BANCO DE DADOS E
	 * RETONA ESSA LISTA DE USUARIOS CADASTRADOS PARA A PÁGINA WEB
	 */
	@Transactional(readOnly = true) // INFORMA QUE NÃO FAZ ALTERAÇÕES NO BANCO DE DADOS
	@GetMapping("/buscar") // URL PARA ACESSAR A METODO BUSCA DE EMPREENDIMENTOS
	@PreAuthorize("hasAuthority('ADMIN')")
	public String buscar(
			@RequestParam(name = "nome", required = false) String nome,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mostrarTodosDados", required = false) 
			Boolean mostrarTodosDados, ModelMap model) {

		// BUSCA OS USUARIOS NO BANCO DE DADOS ATRAVES DO NOME E EMAIL
		List<Usuario> usuariosEncontrados = usuarioRepository.findByEmailAndNome(email, nome);

		model.addAttribute("usuario", new Usuario());
		model.addAttribute("usuariosEncontrados", usuariosEncontrados); // RETORNA OS USUARIOS ENCONTRADOS PARA A PÁGINA
																		// WEB
		if (mostrarTodosDados != null) {
			model.addAttribute("mostrarTodosDados", true);
		}

		return "cadastro";
	}

	/*
	 * METODO PARA FAZER A EDIÇÃO DE USUARIOS CADASTRADOS
	 */
	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/editar/{id}") // URL PARA ACESSAR O METODO
	public String iniciarEdição(@PathVariable("id") Integer idUsuario, ModelMap model) {

		Usuario u = usuarioRepository.findById(idUsuario).get();

		model.addAttribute("usuario", u);

		return "/cadastro";
	}

	@ModelAttribute("situacao")
	public List<String> getSituacao() {
		return Arrays.asList("Docente", "Bolsista", "Voluntário");
	}

	/*
	 * METODO FAZ A REMOÇÃO NO BANCO DE DADOS , DE USUARIOS CADASTRADOS
	 */

	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/remover/{id}")
	public String remover(@PathVariable("id") Integer idUsuario, ModelMap model, RedirectAttributes attr) {

		
		usuarioRepository.deleteById(idUsuario);
		attr.addAttribute("msgSucesso", "Usuario removido com sucesso!");
		
		return "redirect:/usuarios/buscar";
	}

}
