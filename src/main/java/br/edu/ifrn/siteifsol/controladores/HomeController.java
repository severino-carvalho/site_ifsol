package br.edu.ifrn.siteifsol.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifrn.siteifsol.dominio.Noticia;
import br.edu.ifrn.siteifsol.repository.NoticiaRepository;

@Controller
public class HomeController {

	@Autowired
	NoticiaRepository noticiaRepository;
	
	@GetMapping("/publico/home")
	public String home(ModelMap modelo) {
		List<Noticia> noticias = noticiaRepository.findAll();
		
		modelo.addAttribute("noticias", noticias);
		
		return "Home";
	}
	
}
