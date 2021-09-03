package br.edu.ifrn.siteifsol.controladores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repository.NoticiaRepository;

@Controller
@RequestMapping("/publico/noticia")
public class BuscaNoticiaController {

	@Autowired
	NoticiaRepository noticiaRepository;

	@GetMapping("/{id}")
	public String buscarNoticia(@PathVariable("id") Integer idNoticia, ModelMap modelo, RedirectAttributes attr) {

		try {
			Optional<Noticia> noticiaEncontrada = noticiaRepository.findById(idNoticia);
			
				if (noticiaEncontrada.isPresent()) {
					modelo.addAttribute("noticia", noticiaEncontrada.get()); // RETORNA A NOTICIA ENCONTRADA PARA A PÁGINA					
				} else {
					attr.addFlashAttribute("msgNotErro", "");
					return "redirect:/publico/home";
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "noticia/buscaNoticia";
	}
}
