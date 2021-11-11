package br.edu.ifrn.siteifsol.controllers;

import java.util.Collections;
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

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repositories.ArquivoRepository;
import br.edu.ifrn.siteifsol.repositories.NoticiaRepository;

@Controller
@RequestMapping("/noticias")
public class BuscaNoticiaController {

	@Autowired
	private NoticiaRepository noticiaRepository;

	@Autowired
	private ArquivoRepository arquivoRepository;

	@Transactional(readOnly = true)
	@GetMapping("/buscarnoticia")
	public String buscarNoticia(@RequestParam(name = "titulo", required = false) String titulo,
			@RequestParam(name = "texto", required = false) String texto,
			@RequestParam(name = "mostrarTodosDados", required = false) Boolean mostrarTodosDados, ModelMap modelo) {

		try {
			List<Noticia> noticiasEncontradas = noticiaRepository.findByTituloAndTexto(titulo, texto);
			
			if (noticiasEncontradas.isEmpty()) {
				modelo.addAttribute("msgErro", "Nenhuma notícia encontrada");
			} else {
				Collections.reverse(noticiasEncontradas);
				modelo.addAttribute("noticias", noticiasEncontradas);
			}

			if (mostrarTodosDados != null) {
				modelo.addAttribute("mostrarTodosDados", true);
			}

			modelo.addAttribute("noticia", new Noticia());

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/noticia/cadastrarNoticia";
	}

	@Transactional(readOnly = true) // INFORMA QUE NÃO FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/edita/{id}")
	public String iniciarEdicao(@PathVariable("id") Integer idNoticia, ModelMap modelo, HttpSession sessao) {

		try {
			Noticia n = noticiaRepository.findById(idNoticia).get();

			modelo.addAttribute("noticia", n);
		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/noticia/cadastrarNoticia";
	}

	@Transactional(readOnly = false) // INFORMA QUE FAZ ALTERARÇÕES NO BANCO DE DADOS
	@GetMapping("/remove/{id}")
	public String remover(@PathVariable("id") Integer idNoticia, RedirectAttributes attr) {

		try {
			// GUARDA A NOTICIA QUE O ADM QUER REMOVER NA VARIÁVEL
			Noticia noticia = noticiaRepository.findById(idNoticia).get();

			if (noticia.getFoto() != null) {
				// ANTES DE REMOVER A NOTÍCIA, FAZ A REMORÇÃO DA IMAGEM
				arquivoRepository.deleteById(noticia.getFoto().getId());

			}

			// DELETA A NOTÍCIA PELO ID
			noticiaRepository.deleteById(idNoticia);

			// LISTA TODAS AS NOTÍCIAS QUE ESTÃO NO BANCO
			List<Noticia> noticias = noticiaRepository.findAll();

			// ENVIA UMA MENSAGEM DE SUCESSO PARA A PÁGINA
			attr.addFlashAttribute("msgSucesso", "Noticia removida com sucesso!");

			if (!noticias.isEmpty()) {
				// ENVIA TODAS AS NOTÍCIAS QUE ESTÃO NO BANCO PARA A PÁGINA
				attr.addFlashAttribute("noticias", noticias);
			}

		} catch (Exception e) {
			attr.addFlashAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "redirect:/noticia/config";
	}
}
