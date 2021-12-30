package br.edu.ifrn.siteifsol.controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.repositories.Usuariorepository;

@Controller
@RequestMapping("/usuario") // URL PARA ACESSAR A PAGINA
public class CadastroUsuarioController {

	@Autowired
	private Usuariorepository usuarioRepository;

	@GetMapping("/cadastro") // URL PARA ACESSAR A PAGINA
	public String entrarCadastro(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		return "/admin/usuario/cadastro";
	}

	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERAÇÕES NO BANCO DE DADOS
	@PostMapping("/salvar") // URL PARA ACESSAR A METODO SALVAR E EDITAR
	public String salvar(Usuario usuario, ModelMap modelo, RedirectAttributes attr) {

		// SE HAVER ALGUM DADO INVÁLIDO, ELE SERÁ COLOCADO DENTRO DA LISTA
		List<String> msgValidacao = validarDados(usuario);

		// SE A LISTA DE VALIDAÇÃO ESTIVER VAZIA, ENTÃO TUDO ESTÁ DE ACORDO
		if (msgValidacao.isEmpty()) {

			// VERIFICA SE É INSERSÃO (0:INSERT)
			if (usuario.getId() == 0) {
				// VALIDA SE O EMAIL INFORMADO DO USUÁRIO JÁ ESTÁ CADASTRADO NO BANCO
				if (validarEmail(usuario)) {
					modelo.addAttribute("msgErro", "Email já cadastrado. Por favor, informe um email válido!");
					return "/admin/usuario/cadastro";
				}
			}

			// VERIFICA SE É ATUALIZAÇÃO (>0: UPDATE)
			if (usuario.getId() > 0) {
				// FAZ A BUSCA DO USUÁRIO PELO ID E RETORNA SEU EMAIL
				String emailUsuario = usuarioRepository.findById(usuario.getId()).get().getEmail();

				// SE O EMAIL DO USUÁRIO CADASTRADO NO BANCO FOR DIFERENTE DO EMAIL DO USUÁRIO
				// PASSADO PARA O MÉTODO
				// SIGNIFICA QUE ELE QUER ATUALIZAR
				if (!emailUsuario.equals(usuario.getEmail())) {
					// FAZEMOS A VALIDAÇÃO DESSE EMAIL NOVO
					// SE JÁ HOUVER UMA PESSOA COM ESSE EMAIL ENTÃO NÃO PODE SER CADASTRADO
					if (!validarEmail(usuario)) {
						modelo.addAttribute("msgErro", "Email já cadastrado. Por favor, informe um email válido!");
						return "/admin/usuario/cadastro";
					}
				}
			}

			// FAZ A BUSCA NO BD E RETORNA O NOME DO USUÁRIO LOGADO NO SISTEMA
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();
			String nomeUsuarioADM = usuarioRepository.findByEmail(currentPrincipalName).get().getNome();

			// CRIPTOGRAFANDO A SENHA
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());

			// DEPOIS DE CRIPTOGRAFA A SENHA É SALVADA NO OBJETO USUÁRIO ANTES DE SER
			// INSERIDO NO BANCO DE DADOS
			usuario.setSenha(senhaCriptografada);

			/*
			 * SE ESTIVER VAZIO, SIGNIFICA QUE O USUÁRIO ESTÁ SENDO CADASTRADO ENTÃO É
			 * COLOCADO O NOME DO USUÁRIO ADM QUE REALIZA O CADASTRO
			 * 
			 * SE JÁ CONTER ALGUM VALOR, SIGNIFICA QUE É UMA EDIÇÃO, ENTÃO NÃO PRECISA
			 */
			if (usuario.getCriadoPor() == null || usuario.getCriadoPor().isEmpty()) {
				// MODIFICA O USUÁRIO QUE CRIOU O EMPREENDIMENTO
				usuario.setCriadoPor(nomeUsuarioADM);
			}

			if (usuario.getDataCriacao() == null || usuario.getDataCriacao().isEmpty()) {
				// MODIFICA A DATA DE CRIAÇÃO
				usuario.setDataCriacao(getData());
			}

			// SALVA O OBJETO USUÁRIO NO BANCO DE DADOS
			usuarioRepository.save(usuario);

			List<Usuario> usuariosCadastrados = usuarioRepository.findAll();
			Collections.reverse(usuariosCadastrados);
			// RETORNA A LISTA PARA A PÁGINA
			attr.addFlashAttribute("usuariosEncontrados", usuariosCadastrados);

			// RETORNA A MENSAGEM PARA O A PÁGINA , PARA O USUSARIO VER
			attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

		} else {
			// SE ELA ESTIVER COM ALGUM ERRO NÃO SERÁ POSSÍVEL CADASTRAR UM USUÁRIO
			modelo.addAttribute("msgErro", msgValidacao);
			return "/admin/usuario/cadastro";
		}
		return "redirect:/usuario/cadastro";
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
		if (usuario.getNome().length() < 3) { // NOME TEM QUER TER MAIS DE 3 CARACTERES
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

	// LISTA CONTENDO AS OPÇÕES PARA O SELECT DO FORMULARIO
	@ModelAttribute("funcao")
	public List<String> getFuncao() {
		return Arrays.asList("Docente", "Bolsista", "Voluntário");
	}

	// FUNÇÃO PARA PEGAR A DATA ATUAL
	public static String getData() {
		Calendar c = Calendar.getInstance();

		Date data = c.getTime();
		DateFormat formataData = DateFormat.getDateInstance();

		return formataData.format(data);
	}

	// VALIDAÇÃO DE EMAIL PARA CADASTRO E ATUALIZAÇÃO DE DADOS
	@Transactional(readOnly = true)
	private Boolean validarEmail(Usuario usuario) {
		// PESQUISA NO BANCO POR UM EMAIL DO USUÁRIO EM QUESTÃO
		Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());

		// SE ESTIVER PRESENTE RETORNA FALSO SINALIZANDO UM ERRO
		if (user.isPresent()) {
			return true;
		}

		// SE ESTIVER TUDO OK RETORNA TRUE
		return false;
	}
}
