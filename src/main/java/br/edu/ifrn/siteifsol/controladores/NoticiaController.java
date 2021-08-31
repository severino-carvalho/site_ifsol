package br.edu.ifrn.siteifsol.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@PostMapping("/salva")
	public String salvar(Noticia noticia, @RequestParam("file") MultipartFile arquivo,
			RedirectAttributes attr, HttpSession sessao) {

		List<String> msgValidacao = validaDados(noticia); // RETORNA AS MENSAGENS DE ERRO NA VALITAÇÃO DO CAMPOS

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

			// CADASTRA E EDITA O NOTICIA NO BANCO DE DADOS
			noticiaRepository.save(noticia);

			// RETORNA A MENSAGEM PARA A PÁGINA
			attr.addFlashAttribute("msgSucesso", "O peração realizada com sucesso!");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/usuario/cadastroem";
	}

	private List<String> validaDados(Noticia noticia) {

		List<String> msgs = new ArrayList<>(); // LISTA DE MENSAGENS DE ERRO, POIS MUITOS CAMPOS PODE ESTAR INCORRETOS

		if (noticia.getTitulo() == null || noticia.getTitulo().isEmpty()) {
			msgs.add("O campo título é obrigatório!");
		}
		if (noticia.getTexto() == null || noticia.getTexto().isEmpty()) {
			msgs.add("O campo texto é origatório!");
		}
		if (noticia.getTexto().length() <= 30) { // TEXTO TEM QUER TER MAIS DE 30 CARACTERES
			msgs.add("O campo texto é origatório!");
		}
		return msgs;
	}

}
