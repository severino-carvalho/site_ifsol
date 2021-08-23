package br.edu.ifrn.siteifsol.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ADMController {
	
	@PostMapping("/admin")
	public String logado() {
		return "ADM";
	}

}
