package br.edu.ifrn.siteifsol.controladores;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Arquivo;
import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repository.ArquivoRepository;
import br.edu.ifrn.siteifsol.repository.NoticiaRepository;

@Controller
@RequestMapping("/noticia")
public class NoticiaController {

	@Autowired
	private ArquivoRepository arquivoRepository;

	@Autowired
	private NoticiaRepository noticiaRepository;

	@GetMapping("/nova")
	public String entrar(ModelMap modelo) {

		modelo.addAttribute("noticia", new Noticia());

		return "noticia/noticia";
	}

	@PostMapping("/salvar")
	public String salvar(Noticia noticia, @RequestParam("file") MultipartFile arquivo, RedirectAttributes attr) {

		List<String> msgValidacao = validaDados(noticia, arquivo); // RETORNA AS MENSAGENS DE ERRO NA VALITAÇÃO DO
																	// CAMPOS

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

				if (noticia.getFoto() != null && noticia.getFoto().getId() != null && noticia.getFoto().getId() > 0) {
					arquivoRepository.delete(noticia.getFoto());
				}
				noticia.setFoto(arquivoBD);// SALVA O NOVO ARQUIVO DO NOTICIA
			} else {
				noticia.setFoto(null);
			}

			noticia.setDataPublicacao(getDataNoticia());

			// CADASTRA E EDITA O NOTICIA NO BANCO DE DADOS
			noticiaRepository.save(noticia);

			// RETORNA A MENSAGEM PARA A PÁGINA
			attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/noticia/nova";
	}

	private List<String> validaDados(Noticia noticia, MultipartFile arquivo) {

		List<String> msgs = new ArrayList<>(); // LISTA DE MENSAGENS DE ERRO, POIS MUITOS CAMPOS PODE ESTAR INCORRETOS

		if (noticia.getTitulo() == null || noticia.getTitulo().isEmpty()) {
			msgs.add("O campo 'Título' é obrigatório!");
		}
		if (noticia.getTexto() == null || noticia.getTexto().isEmpty()) {
			msgs.add("O campo 'Texto' é origatório!");
		}
		if (noticia.getTexto().length() <= 30) { // TEXTO TEM QUER TER MAIS DE 30 CARACTERES
			msgs.add("O campo 'Texto' deve conter mais de 30 caracteres!");
		}
		/*
		 * if (arquivo == null || arquivo.isEmpty()) {
		 * msgs.add("O campo 'Foto' é origatório!"); }
		 */
		return msgs;
	}

	public static String getDataNoticia() {
		Calendar c = Calendar.getInstance();

		int ano = c.get(Calendar.YEAR);
		int mes = c.get(Calendar.MONTH);
		int dia = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);

		c.set(ano, mes, dia);
		Date data = c.getTime();

		DateFormat formataData = DateFormat.getDateInstance();

		return formataData.format(data);
	}

}
