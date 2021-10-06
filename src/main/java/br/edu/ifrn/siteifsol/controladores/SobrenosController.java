package br.edu.ifrn.siteifsol.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SobrenosController {

	@GetMapping("/publico/sobrenos")
	public String sobrenos() {
		return "/visitantes/Sobrenos";
	}

}
