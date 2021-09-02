package br.edu.ifrn.siteifsol.controladores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repository.NoticiaRepository;

@Controller
@RequestMapping("/noticia")
public class BuscaNoticiaController {

	@Autowired
	NoticiaRepository noticiaRepository;

	@GetMapping("/{id}")
	public String buscarNoticia(@PathVariable("id") Integer idNoticia, ModelMap modelo) {

		try {
			Optional<Noticia> noticiaEncontrada = noticiaRepository.findById(idNoticia);
			
		

			modelo.addAttribute("noticia", noticiaEncontrada.get()); // RETORNA A NOTICIA ENCONTRADA PARA A PÁGINA

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "noticia/buscaNoticia";
	}
}
