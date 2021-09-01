package br.edu.ifrn.siteifsol.controladores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.repository.Usuariorepository;

@Controller
@RequestMapping("/usuario") // URL PARA ACESSAR A PAGINA
public class CadastroUsuarioController {

	@GetMapping("/cadastro") // URL PARA ACESSAR A PAGINA
	public String entrarCadastro(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		return "cadastro";
	}

	@Autowired
	private Usuariorepository usuarioRepository;

	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERAÇÕES NO BANCO DE DADOS
	@PostMapping("/salvar") // URL PARA ACESSAR A METODO SALVAR E EDITAR
	public String salvar(Usuario usuario, ModelMap model, RedirectAttributes attr, HttpSession sessao) {

		// SE HAVER ALGUM DADO INVÁLIDO, ELE SERÁ COLOCADO DENTRO DA LISTA
		List<String> msgValidacao = validarDados(usuario);

		// SE A LISTA DE VALIDAÇÃO ESTIVER VAZIA, ENTÃO TUDO ESTÁ DE ACORDO
		if (msgValidacao.isEmpty()) {
			Optional<Usuario> u = usuarioRepository.findByEmail(usuario.getEmail());

			if (!u.isPresent()) {
				// CRIPTOGRAFANDO A SENHA
				String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
				usuario.setSenha(senhaCriptografada);
				
				// DEFINE TODOS OS USUÁRIOS CADASTRADOS COMO ADMINISTRADORES
				usuario.setPerfil("ADMIN");

				// CADASTRA O USUARIO DO BANDO DE DADOS E EDITA
				usuarioRepository.save(usuario);
				// RETORNA A MENSAGEM PARA O A PÁGINA , PARA O USUSARIO VER
				attr.addFlashAttribute("msgCadSucesso", "O peração realizada com sucesso!");
			} else {
				// RETORNA A MENSAGEM DE ERRO CASO O EMAIL JÁ ESTEJA CADASTRADO
				attr.addFlashAttribute("msgCadErro", "Email já cadastrado. por favor, informe um email válido!");
			}
		} else {
			// SE ELA ESTIVER COM ALGUM ERRO NÃO SERÁ POSSÍVEL CADASTRAR UM USUÁRIO
			model.addAttribute("msgCadErro", msgValidacao.get(0));
			return "cadastro";
		}
		return "redirect:/usuario/cadastro";
	}

	// LISTA CONTENDO AS OPÇÕES PARA O SELECT DO FORMULARIO
	@ModelAttribute("funcao")
	public List<String> getFuncao() {
		return Arrays.asList("Docente", "Bolsista", "Voluntário");
	}

	/*
	 * METODO DE VALIDAÇÃO DOS CAMPOS NOME,EMAIL,SENHA E SITUAÇÃO DO FORMULARIO DE
	 * CADASTRO, ELE VER SE OS CAMPOS ESTÃO PREENCHIDOS DE ACORDO COM E O EXIGIDO E
	 * RETORNA UMA MENSAGEM DE ERRO CASO NÃO
	 */

	private List<String> validarDados(Usuario usuario) {

		List<String> msgs = new ArrayList<>(); // LISTA DE MENSAGENS DE ERRO, POIS MUITOS CAMPOS PODE ESTAR

		if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
			msgs.add("O campo 'Nome' é obrigatório");
		}
		if (usuario.getNome().length() <= 5) { // NOME TEM QUER TER MAIS DE 5 CARACTERES
			msgs.add("Nome inválido");
		}
		if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
			msgs.add("O campo 'Email' é obrigatório");
		}
		if (usuario.getFuncao() == null || usuario.getFuncao().isEmpty()) {
			msgs.add("O campo 'Função' é obrigatório");
		}
		if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
			msgs.add("O campo 'Senha' é obrigatório");
		}
		if (usuario.getSenha().length() <= 6) {
			msgs.add("Sua senha precisar ter no minimo 6 caracteres"); // SENHA TEM QUE TER MAIS DE 6 CARACTERES
		}

		return msgs;
	}
}
