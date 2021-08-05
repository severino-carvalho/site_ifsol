package br.edu.ifrn.siteifsol.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ADMController {

	@GetMapping("/adm")
	public String home() {
		return "ADM";
	}
}
