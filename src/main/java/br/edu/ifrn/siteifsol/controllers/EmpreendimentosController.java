package br.edu.ifrn.siteifsol.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpreendimentosController {
	
	@GetMapping("/publico/empreendimentos")
	public String empreendimentos() {
		return "/visitantes/Empreendimentos";
	}

}
