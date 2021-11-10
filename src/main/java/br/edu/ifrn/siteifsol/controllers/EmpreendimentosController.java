package br.edu.ifrn.siteifsol.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifrn.siteifsol.dominio.empreendimento;
import br.edu.ifrn.siteifsol.repositories.empreendimentorepository;

@Controller
public class EmpreendimentosController {

	@Autowired
	private empreendimentorepository empreendimentorepository;

	@GetMapping("/publico/empreendimentos")
	@Transactional(readOnly = true)
	public String openEmpreendimentos(ModelMap modelo) {

		try {
			
			List<empreendimento> empreendimentos = empreendimentorepository.findAll();
			Collections.reverse(empreendimentos);
			modelo.addAttribute("empEncontrados", empreendimentos);

		} catch (Exception e) {
			modelo.addAttribute("msgErro", "ERRO INTERNO NO SERVIDOR");
		}

		return "/visitantes/Empreendimentos";
	}

}
