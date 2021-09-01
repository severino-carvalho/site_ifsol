package br.edu.ifrn.siteifsol.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dto.*;
import br.edu.ifrn.siteifsol.dominio.Arquivo;
import br.edu.ifrn.siteifsol.dominio.Cidade;
import br.edu.ifrn.siteifsol.dominio.empreendimento;
import br.edu.ifrn.siteifsol.repository.ArquivoRepository;
import br.edu.ifrn.siteifsol.repository.Cidaderepository;
import br.edu.ifrn.siteifsol.repository.empreendimentorepository;

@Controller
@RequestMapping("/usuario") // URL PARA ACESSAR A PAGINA
public class CadastroEmpreController {

	@GetMapping("/cadastroem") // URL PARA ACESSAR A PAGINA
	public String entrarCadastro(ModelMap model) {
		model.addAttribute("empre", new empreendimento());
		return "cadastroEmpre";
	}

	@Autowired
	private empreendimentorepository empreendimentosrepository;

	@Autowired
	private Cidaderepository cidaderepository;

	@Autowired
	private ArquivoRepository arquivoRepository;

	@SuppressWarnings("unused")
	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERAÇÕES NO BANCO DE DADOS

	// URL PARA ACESSAR A METODO SALVAR E EDITAR OS EMPREENDIMENTOS
	@PostMapping("/salva")
	public String salvar(empreendimento empre, @RequestParam("file") MultipartFile arquivo, Model model,
			RedirectAttributes attr, HttpSession sessao) {

		List<String> msgValidacao = validaDados(empre); // RETORNA AS MENSAGENS DE ERRO NA VALITAÇÃO DO CAMPOS

		/*
		 * O CODIGO A SEGUIR FOI FEITO PARA UPLOAD E DOWNLOAD DE UM ARQUIVO NO BANCO DE
		 * DADOS E O TRY FOI NECESSARIO PARA CASO OCORRA UM ERRO SEJA LANÇADO UMA
		 * EXCECAO
		 * 
		 */

		try {

			if (arquivo != null && !arquivo.isEmpty()) {
				// NORMALIZANDO NOME DO ARQUIVO
				String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
				Arquivo arquivoBD = new Arquivo(nomeArquivo, arquivo.getContentType(), arquivo.getBytes());
				arquivoRepository.save(arquivoBD); // SalVA O ARQUIVO NO BANCO DE DADOS

				if (empre.getFoto() != null && empre.getFoto().getId() != null && empre.getFoto().getId() > 0)
					arquivoRepository.delete(empre.getFoto());

				empre.setFoto(arquivoBD);// SALVA O NOVO ARQUIVO DO EMPREENDIMENTO
			} else {
				empre.setFoto(null);
			}

			// CADASTRA E EDITA O EMPREENDIMENTO NO BANCO DE DADOS
			empreendimentosrepository.save(empre);

			// RETORNA A MENSAGEM PARA A PÁGINA
			attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/usuario/cadastroem";
	}

	/*
	 * O AUTOCOMPLETE SERVE PARA RECEBER A LISTA DE PROFISSOES CONTIDA NO BANCO DE
	 * DADOS E QUANDO O USUARIO DIGITAR ELE DAR SUGESTÕES DE COMPLEMENTO DO SE ESTA
	 * SENDO DIGITADO
	 */

	@GetMapping("/autocompleteCidade") // URL PARA SE TER ACESSO AO METODO
	@Transactional(readOnly = true) // PARA INFORMAR QUE NÃO FAZ ALTERAÇÕES NO BANCO DE DADOS
	@ResponseBody
	public List<AutocompleteDTO> autocompleteCidade(@RequestParam("term") String termo) {

		List<Cidade> cidade = cidaderepository.findByNome(termo);

		List<AutocompleteDTO> resultados = new ArrayList<>();

		cidade.forEach(p -> resultados.add(new AutocompleteDTO(p.getNome(), p.getId())));

		return resultados;
	}

	/*
	 * METODO DE VALIDAÇÃO DOS CAMPOS NOME,EMAIL,SENHA E SITUAÇÃO DO FORMULARIO DE
	 * CADASTRO, ELE VER SE OS CAMPOS ESTÃO PREENCHIDOS DE ACORDO COM E O EXIGIDO E
	 * RETORNA UMA MENSAGEM DE ERRO CASO NÃO
	 */
	private List<String> validaDados(empreendimento empre) {

		List<String> msgs = new ArrayList<>(); // LISTA DE MENSAGENS DE ERRO, POIS MUITOS CAMPOS PODE ESTAR INCORRETOS

		if (empre.getNome() == null || empre.getNome().isEmpty()) {
			msgs.add("O campo nome é obrigatório");
		}
		if (empre.getNome().length() <= 5) {// NOME TEM QUER TER MAIS DE 5 CARACTERES
			msgs.add("Nome inválido");
		}
		if (empre.getEmail() == null || empre.getEmail().isEmpty()) {
			msgs.add("O campo Email é obrigatório");
		}
		if (empre.getCidade().toString() == null || empre.getCidade().toString().isEmpty()) {
			msgs.add("O campo Cidade é obrigatório");
		}

		if (empre.getDescricao() == null || empre.getDescricao().isEmpty()) {
			msgs.add("O campo Descrição é obrigatório");
		}
		if (empre.getDescricao().length() <= 15) {
			msgs.add("Descrição inválido");
		}

		return msgs;
	}
}
