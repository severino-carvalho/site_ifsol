package br.edu.ifrn.siteifsol.controllers;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de ser uma classe controladora para a parte de Download de {@link Arquivo}
 * 
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * 
 * Data de Cricação:	05/07/2021
 * 
 * #####################################
 * 
 * Última alteração:	
 * 
 * @author Felipe Barros	(primariaconta22@gmail.com)
 * Data:	05/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.edu.ifrn.siteifsol.dominio.Arquivo;
import br.edu.ifrn.siteifsol.repositories.ArquivoRepository;

@Controller
public class DownloadArquivoController {

	/**
	 * Repositórios JPA par a auxiliar na manipulação dos dados
	 */
	@Autowired
	private ArquivoRepository arquivoRepository;

	/**
	 * 
	 * @param idArquivo Id do arquivo que vai ser realizado o Download enviado no
	 *                  path
	 * 
	 * @param salvar    Verifica se quer salvar na máquina ou somente exibir na tela
	 * 
	 * @return O arquivo para ser mostrado na tela ou salvo na máquina
	 */
	@GetMapping("/download/{idArquivo}")
	public ResponseEntity<?> downloadFile(@PathVariable Long idArquivo, @PathParam("salvar") String salvar) {

		Arquivo arquivoBD = arquivoRepository.findById(idArquivo).get();

		String texto = (salvar == null || salvar.equals("true"))
				? "attachment; filename=\"" + arquivoBD.getNomeArquivo() + "\""
				: "inline; filename=\"" + arquivoBD.getNomeArquivo() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(arquivoBD.getTipoArquivo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, texto).body(new ByteArrayResource(arquivoBD.getDados()));
	}

}
