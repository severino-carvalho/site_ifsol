package br.edu.ifrn.siteifsol.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifrn.siteifsol.repository.NoticiaRepository;
import br.edu.ifrn.siteifsol.repository.Usuariorepository;
import br.edu.ifrn.siteifsol.repository.empreendimentorepository;

@Controller
public class ADMController {
	
	@Autowired
	Usuariorepository usuariorepository;
	
	@Autowired
	empreendimentorepository empreendimentorepository;
	
	@Autowired
	NoticiaRepository noticiaRepository;
	
	@GetMapping("/adm")
	public String home(ModelMap modelo) {
		try {
			int totUsuarios = usuariorepository.findAll().size();
			int totEmpre = empreendimentorepository.findAll().size();
			int totNoticias = noticiaRepository.findAll().size();
			
			modelo.addAttribute("totUsuarios", totUsuarios);
			modelo.addAttribute("totEmpre", totEmpre);
			modelo.addAttribute("totNoticias", totNoticias);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ADM";
	}

}
