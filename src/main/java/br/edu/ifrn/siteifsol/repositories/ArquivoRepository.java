package br.edu.ifrn.siteifsol.repositories;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta interface tem o objetivo de auxiliar na manipulação dos dados da entidade {@link Arquivo}
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

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.siteifsol.dominio.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

}
