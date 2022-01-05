package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de cadastro e edição de {@link Empreendimento}
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.repositories.ArquivoRepository;
import br.edu.ifrn.siteifsol.repositories.Cidaderepository;
import br.edu.ifrn.siteifsol.repositories.Usuariorepository;
import br.edu.ifrn.siteifsol.repositories.Empreendimentorepository;
import br.edu.ifrn.siteifsol.dominio.Arquivo;
import br.edu.ifrn.siteifsol.dominio.Cidade;
import br.edu.ifrn.siteifsol.dominio.Empreendimento;

@Controller
@RequestMapping("/usuario")
public class CadastroEmpreController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private Usuariorepository usuariorepository;

	@Autowired
	private Empreendimentorepository empreendimentosrepository;

	@Autowired
	private Cidaderepository cidaderepository;

	@Autowired
	private ArquivoRepository arquivoRepository;

	/**
	 * 
	 * @param modelo Responsável pela criacao dos nomes de atributos que são
	 *               retornados para a página
	 * 
	 * @return a página de CRUD de Empreendimentos
	 */
	@GetMapping("/cadastroem")
	public String entrarCadastro(ModelMap model) {
		model.addAttribute("empre", new Empreendimento());
		model.addAttribute("cidades", getCidades());

		return "/admin/empreendimento/cadastroEmpre";
	}

	/**
	 * 
	 * @param empre   Recebe do formulário o Objeto {@link Empreendimento}
	 * 
	 * @param arquivo Recebe do formulário o arquivo anexado no input com
	 *                name 'file'
	 * 
	 * @param model   Responsável pela criacao dos nomes de atributos que são
	 *                retornados para a página
	 * 
	 * @param attr    Responsável pela criacao dos nomes de atributos que são
	 *                retornados com o uso do 'redirect' para a página
	 * 
	 * @return Retorna a mesma página de CRUD de empreendimentos
	 */
	@Transactional(readOnly = false)
	@PostMapping("/salva")
	public String salvar(Empreendimento empre, @RequestParam("file") MultipartFile arquivo, Model model,
			RedirectAttributes attr) {

		/**
		 * Lista com todos os erros do Objeto {@link Empreendimento}
		 */
		List<String> msgValidacao = validaDados(empre);

		/**
		 * Se não houver nenhum erro segue o fluxo para o cadastro
		 */
		if (msgValidacao.isEmpty()) {

			try {

				/**
				 * Verifica se é cadastro ou atualização
				 */
				if (empre.getId() == 0) {
					/**
					 * Faz a validação e insere a imagem
					 */
					if (arquivo.isEmpty()) {
						model.addAttribute("msgErro", "Não foi selecionada nenhuma imagem!");
						model.addAttribute("empre", empre);
						model.addAttribute("cidades", getCidades());
						return "/admin/empreendimento/cadastroEmpre";
					}

					inserirFoto(empre, arquivo);

				} else {
					/**
					 * Se for atualização e tiver um arquivo então ele quer atualizar a imagem
					 */
					if (!arquivo.isEmpty()) {
						arquivoRepository.deleteById(empre.getFoto().getId());
						inserirFoto(empre, arquivo);
					}
				}

				empre.setDescricao(empre.getDescricao().trim());

				/**
				 * sistema para sabermos quem quer criar um novo empreendimento
				 * Como o username para login é o email, pegamos o email do usuário logado no
				 */
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String currentPrincipalName = authentication.getName();
				String nomeUsuarioADM = usuariorepository.findByEmail(currentPrincipalName).get().getNome();

				/**
				 * 
				 * Quando quer criar o empreendimento, colocamos o nome de quem está criando no
				 * campo 'Criado Por'
				 * 
				 * E colocamos no campo 'Data de Criação' a data que o empreendimento foi criado
				 */
				if (empre.getCriadoPor() == null || empre.getCriadoPor().isEmpty()) {
					empre.setCriadoPor(nomeUsuarioADM);
				}

				if (empre.getDataCriacao() == null || empre.getDataCriacao().isEmpty()) {
					empre.setDataCriacao(getData());
				}

				/**
				 * Trocamos o nome da cidade selecionada pelo ID dela
				 */
				Optional<Cidade> cidade = cidaderepository.findById(empre.getCidade().getId());
				Cidade c = cidade.get();

				empre.getCidade().setNome(c.getNome());

				empreendimentosrepository.save(empre);

				/**
				 * Após o cadastro é retornado todos os empreendimentos cadastrados para a
				 * página
				 */
				List<Empreendimento> empEnc = empreendimentosrepository.findAll();
				Collections.reverse(empEnc);
				attr.addFlashAttribute("empreendimentosEncontrados", empEnc);
				attr.addFlashAttribute("cidades", getCidades());
				attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

			} catch (Exception e) {
				attr.addFlashAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
			}
		} else {
			model.addAttribute("msgErro", msgValidacao);
			/**
			 * Sem o Objeto {@link Empreendimento} e as {@link Cidade} o documento não
			 * carrega
			 */
			model.addAttribute("empre", empre);
			model.addAttribute("cidades", getCidades());
			return "/admin/empreendimento/cadastroEmpre";

		}
		return "redirect:/usuario/cadastroem";
	}

	/**
	 * 
	 * @param empre Objeto {@link Empreendimento} para a validação de seus atributos
	 * 
	 * @return Lista de erros que existe no Objeto {@link Empreendimento}
	 */
	private List<String> validaDados(Empreendimento empre) {

		List<String> msgs = new ArrayList<>();

		if (empre.getNome() == null || empre.getNome().isEmpty()) {
			msgs.add("O campo nome é obrigatório");
		}
		if (empre.getNome().trim().length() <= 5) {
			msgs.add("Nome inválido");
		}
		if (empre.getEmail() == null || empre.getEmail().isEmpty()) {
			msgs.add("O campo Email é obrigatório");
		}

		if (empre.getCidade().getId() == 0) {
			msgs.add("O campo Cidade é obrigatório");
		}

		if (empre.getDescricao() == null || empre.getDescricao().isEmpty()) {
			msgs.add("O campo Descrição é obrigatório");
		}

		if (empre.getDescricao().trim().length() > 255) {
			msgs.add("O Tamanho máximo da descrição é 255 caracteres");
		}

		if (empre.getDescricao().trim().length() <= 15) {
			msgs.add("Descrição inválido");
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
	 * @param empre   Objeto {@link Empreendimento} para inserir a foto
	 * @param arquivo Arquivo de imagem selecionado pelo usuário
	 */
	@Transactional(readOnly = false)
	public void inserirFoto(Empreendimento empre, MultipartFile arquivo) {

		try {
			String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
			Arquivo arquivoBD = new Arquivo(nomeArquivo, arquivo.getContentType(), arquivo.getBytes());

			arquivoRepository.save(arquivoBD);

			empre.setFoto(arquivoBD);

		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @return As {@link Cidade} que estão armazenadas no Banco de Dados
	 */
	@Transactional(readOnly = true)
	public List<Cidade> getCidades() {
		List<Cidade> cidades = cidaderepository.findAll();
		return cidades;
	}
}
