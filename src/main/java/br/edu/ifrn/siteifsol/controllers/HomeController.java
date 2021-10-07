package br.edu.ifrn.siteifsol.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repositories.NoticiaRepository;

@Controller
public class HomeController {

	@Autowired
	private NoticiaRepository noticiaRepository;

	@GetMapping("/publico/home")
	@Transactional(readOnly = true)
	public String home(ModelMap modelo) {
		List<Noticia> noticias = noticiaRepository.findAll();

		Collections.reverse(noticias);

		modelo.addAttribute("noticias", noticias);

		return "/visitantes/Home";
	}

	@GetMapping("/publico/noticia/{id}")
	@Transactional(readOnly = true)
	public String buscarNoticia(@PathVariable("id") Integer idNoticia, ModelMap modelo, RedirectAttributes attr) {

		try {
			List<Noticia> noticias = noticiaRepository.findAll();
			Optional<Noticia> noticiaEncontrada = noticiaRepository.findById(idNoticia);

			if (noticiaEncontrada.isPresent()) {
				// RETORNA A NOTICIA ENCONTRADA PARA A PÁGINA
				modelo.addAttribute("noticia", noticiaEncontrada.get());

				// RETORNA AS DEMAIS NOTICIA PARA A PÁGINA
				Collections.reverse(noticias);
				modelo.addAttribute("noticias", noticias);
			} else {
				attr.addFlashAttribute("msgErro", "");
				return "redirect:/publico/home";
			}
		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/admin/noticia/buscarNoticia";
	}

}
