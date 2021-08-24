package br.edu.ifrn.siteifsol.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class InicioController {

	@GetMapping("/")
	public String inicio() {
		return "Inicio";
	}

	@GetMapping("/login")
	public String login() {
		return "Login";
	}
	
	@PostMapping("/login")
	public String logado() {
		return "ADM";
	}

	@GetMapping("/login-erro")
	public String loginErro(ModelMap model) {
		model.addAttribute("msgErro", "Login ou senha incorreta");
		return "Login";
	}
}
