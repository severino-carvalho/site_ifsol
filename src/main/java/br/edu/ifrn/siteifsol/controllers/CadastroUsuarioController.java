package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de cadastro e edição de {@link Usuario}
 * 
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * 
 * Data de Cricação:	04/07/2021
 * 
 * #####################################
 * 
 * Última alteração:	
 * 
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * Data:	04/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import java.text.DateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.repositories.Usuariorepository;

@Controller
@RequestMapping("/usuario")
public class CadastroUsuarioController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private Usuariorepository usuarioRepository;

	/**
	 * 
	 * @param modelo Responsável pela criacao dos nomes de atributos que são
	 *               retornados para a página
	 *
	 * @return a página de CRUD de Usuários
	 */
	@GetMapping("/cadastro")
	public String entrarCadastro(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		return "/admin/usuario/cadastro";
	}

	/**
	 * 
	 * @param usuario        Recebe do formulário o objeto usuario
	 * 
	 * @param modelo         Responsável pela criacao dos nomes de atributos que são
	 *                       retornados para a página
	 * 
	 * @param senhaAntiga    Responsável por receber o valor do campo senha antiga
	 * 
	 * @param confirmarSenha Responsável por receber o valor do campo confirmar
	 *                       senha
	 * 
	 * @param attr           Responsável pela criacao dos nomes de atributos que são
	 *                       retornados com o uso do 'redirect' para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Usuários
	 */
	@Transactional(readOnly = false)
	@PostMapping("/salvar")
	public String salvar(Usuario usuario, ModelMap modelo,
			@RequestParam(name = "senhaAntiga", required = false) String senhaAntiga,
			@RequestParam(name = "confirmarSenha", required = false) String confirmarSenha,
			RedirectAttributes attr) {

		/**
		 * Lista com todos os erros do Objeto Usuário
		 */
		List<String> msgValidacao = validarDados(usuario);

		/**
		 * Se não houver nenhum erro segue o fluxo para o cadastro
		 */
		if (msgValidacao.isEmpty()) {

			/**
			 * Verifica se é cadastro
			 */
			if (usuario.getId() == 0) {
				/**
				 * Faz a validação do email
				 */
				if (validarEmail(usuario)) {
					modelo.addAttribute("msgErro", "Email já cadastrado. Por favor, informe um email válido!");
					return "/admin/usuario/cadastro";
				}
			}

			/**
			 * Verifica se é atualização
			 */
			if (usuario.getId() > 0) {

				String emailUsuario = usuarioRepository.findById(usuario.getId()).get().getEmail();

				/**
				 * Se o usuário quiser mudar o email, faz a validação do novo email
				 */
				if (!emailUsuario.equals(usuario.getEmail())) {
					/**
					 * Se existir algum usuário com o novo email dele, é pedido um outro email
					 */
					if (!validarEmail(usuario)) {
						modelo.addAttribute("msgErro", "Email já cadastrado. Por favor, informe um email válido!");
						return "/admin/usuario/cadastro";
					}
				}
			}

			/**
			 * Como o username para login é o email, pegamos o email do usuário logado no
			 * sistema para sabermos quem quer criar um novo usuário
			 */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();
			String nomeUsuarioADM = usuarioRepository.findByEmail(currentPrincipalName).get().getNome();

			if (usuario.getId() == 0) {
				BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
				String senhaCript = bCrypt.encode(usuario.getSenha());

				/**
				 * Se for a criação de um novo usuário, verificamos se a senha é igual a
				 * confirmar senha
				 */
				if (!bCrypt.matches(confirmarSenha, senhaCript)) {
					modelo.addAttribute("msgErro", "Senhas diferentes.");
					return "/admin/usuario/cadastro";
				}

				usuario.setSenha(senhaCript);
			}

			if (usuario.getId() != 0) {

				int idUserLogado = usuarioRepository.findByEmail(currentPrincipalName).get().getId();

				/**
				 * Se for uma alteração, verificamos se o usuário que está logado quer modificar
				 * a sua senha, se não for a sua não pode deixar
				 */
				if (usuario.getId() == idUserLogado) {
					BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
					String senhaCript = bCrypt.encode(usuario.getSenha());

					String senhaBanco = usuarioRepository.findById(usuario.getId()).get().getSenha();

					if (!bCrypt.matches(senhaAntiga, senhaBanco)) {
						modelo.addAttribute("msgErro", "Senha antiga incorreta.");
						return "/admin/usuario/cadastro";
					}

					if (!bCrypt.matches(confirmarSenha, senhaCript)) {
						modelo.addAttribute("msgErro", "Senha e confirmar senha estão diferentes.");
						return "/admin/usuario/cadastro";
					}

					usuario.setSenha(senhaCript);
				} else {
					modelo.addAttribute("msgErro", "Você não pode modificar a senha de outro usuário.");
					return "/admin/usuario/cadastro";
				}

			}

			/**
			 * 
			 * Quando quer criar o usuário, colocamos o nome de quem está criando no campo
			 * 'Criado Por'
			 * 
			 * E colocamos no campo 'Data de Criação' a data que o usuário foi criado
			 */
			if (usuario.getCriadoPor() == null || usuario.getCriadoPor().isEmpty()) {
				usuario.setCriadoPor(nomeUsuarioADM);
			}

			if (usuario.getDataCriacao() == null || usuario.getDataCriacao().isEmpty()) {
				usuario.setDataCriacao(getData());
			}

			usuarioRepository.save(usuario);

			List<Usuario> usuariosCadastrados = usuarioRepository.findAll();
			Collections.reverse(usuariosCadastrados);
			attr.addFlashAttribute("usuariosEncontrados", usuariosCadastrados);
			attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

		} else {
			modelo.addAttribute("msgErro", msgValidacao);
			return "/admin/usuario/cadastro";
		}
		return "redirect:/usuario/cadastro";
	}

	/**
	 * 
	 * @param usuario Objeto usuário para a validação de seus atributos
	 * 
	 * @return Lista de erros que existe no Objeto Usuário
	 */
	private List<String> validarDados(Usuario usuario) {

		List<String> msgs = new ArrayList<>();

		if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
			msgs.add("O campo 'Nome' é obrigatório");
		}
		if (usuario.getNome().length() < 3) {
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
			msgs.add("Sua senha precisar ter no minimo 6 caracteres");
		}

		return msgs;
	}

	/**
	 * 
	 * @return A data formatada "dd de mm (ex: jan) de yyyy"
	 */
	public static String getData() {
		Calendar c = Calendar.getInstance();

		Date data = c.getTime();
		DateFormat formataData = DateFormat.getDateInstance();

		return formataData.format(data);
	}

	/**
	 * 
	 * @param usuario Objeto usuario do formulário para validar email
	 * 
	 * @return True se já existir um usuário no banco de dados com o email
	 *         informado, se não False
	 */
	@Transactional(readOnly = true)
	private Boolean validarEmail(Usuario usuario) {
		Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());

		if (user.isPresent()) {
			return true;
		}

		return false;
	}
}
