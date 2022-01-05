package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de cadastro e edição de {@link Noticia}
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Arquivo;
import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repositories.ArquivoRepository;
import br.edu.ifrn.siteifsol.repositories.NoticiaRepository;

@Controller
@RequestMapping("/noticia")
public class CadastroNoticiaController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private ArquivoRepository arquivoRepository;

	@Autowired
	private NoticiaRepository noticiaRepository;

	/**
	 * 
	 * @param modelo Responsável pela criacao dos nomes de atributos que são
	 *               retornados para a página
	 * 
	 * @return a página de CRUD de Notícias
	 */
	@GetMapping("/config")
	public String entrar(ModelMap modelo) {

		modelo.addAttribute("noticia", new Noticia());

		return "/admin/noticia/cadastrarNoticia";
	}

	/**
	 * 
	 * @param noticia Recebe do formulário o objeto noticia
	 * 
	 * @param arquivo Recebe do formulário o arquivo anexado no input com
	 *                name 'file'
	 * 
	 * @param modelo  Responsável pela criacao dos nomes de atributos que são
	 *                retornados para a página
	 * 
	 * @param attr    Responsável pela criacao dos nomes de atributos que são
	 *                retornados com o uso do 'redirect' para a página
	 * 
	 * @return Retorna a mesma página de CRUD de Notícias
	 */
	@Transactional(readOnly = false)
	@PostMapping("/salvar")
	public String salvar(Noticia noticia, @RequestParam("file") MultipartFile arquivo,
			ModelMap modelo, RedirectAttributes attr) {

		/**
		 * Lista com todos os erros do Objeto Noticia
		 */
		List<String> msgValidacao = validaDados(noticia);

		/**
		 * Se não houver nenhum erro segue o fluxo para o cadastro
		 */
		if (msgValidacao.isEmpty()) {
			try {

				/**
				 * Verifica se é cadastro ou atualização
				 */
				if (noticia.getId() == 0) {
					/**
					 * Faz a validação e insere a imagem
					 */
					if (arquivo.isEmpty()) {
						modelo.addAttribute("msgErro", "Não foi selecionada nenhuma imagem!");
						return "/admin/noticia/cadastrarNoticia";
					}

					insertFoto(noticia, arquivo, modelo);

				} else {
					/**
					 * Se for atualização e tiver um arquivo então ele quer atualizar a imagem
					 */
					if (!arquivo.isEmpty()) {
						arquivoRepository.deleteById(noticia.getFoto().getId());
						insertFoto(noticia, arquivo, modelo);
					}
				}

				/**
				 * 
				 * Colocamos no campo 'Data de Publicação' a data que a notícia foi criada
				 */
				if (noticia.getDataPublicacao() == null || noticia.getDataPublicacao().isEmpty()) {
					noticia.setDataPublicacao(getData());
				}

				noticiaRepository.save(noticia);

				attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

				/**
				 * Após o cadastro é retornado todas as notícias cadastradas para a
				 * página
				 */
				List<Noticia> noticias = noticiaRepository.findAll();
				Collections.reverse(noticias);
				attr.addFlashAttribute("noticias", noticias);

			} catch (Exception e) {
				attr.addFlashAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
				return "redirect:/noticia/config";
			}
		} else {
			modelo.addAttribute("msgErro", msgValidacao);
			return "/admin/noticia/cadastrarNoticia";
		}

		return "redirect:/noticia/config";
	}

	/**
	 * 
	 * @param noticia Objeto {@link Noticia} para a validação de seus atributos
	 *
	 * @return Lista de erros que existe no Objeto {@link Noticia}
	 */
	private List<String> validaDados(Noticia noticia) {

		List<String> msgs = new ArrayList<>();

		if (noticia.getTitulo() == null || noticia.getTitulo().isEmpty()) {
			msgs.add("O campo 'Título' é obrigatório!");
		}
		if (noticia.getTexto() == null || noticia.getTexto().isEmpty()) {
			msgs.add("O campo 'Texto' é origatório!");
		}
		if (noticia.getTexto().length() <= 15) {
			msgs.add("O campo 'Texto' deve conter mais de 15 caracteres!");
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
	 * @param noticia Objeto {@link Noticia} para inserir a foto
	 * @param arquivo Arquivo de imagem selecionado pelo usuário
	 * @param modelo  Responsável pela criacao dos nomes de atributos que são
	 *                retornados para a página
	 */
	@Transactional(readOnly = false)
	public void insertFoto(Noticia noticia, MultipartFile arquivo, ModelMap modelo) {
		try {
			String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
			Arquivo arquivoBD = new Arquivo(nomeArquivo, arquivo.getContentType(), arquivo.getBytes());

			arquivoRepository.save(arquivoBD);

			noticia.setFoto(arquivoBD);

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR!");
		}
	}

}
