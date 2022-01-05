package br.edu.ifrn.siteifsol.repositories;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta interface tem o objetivo de auxiliar na manipulação dos dados da entidade {@link Cidade}
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
 * @author Severino Carvalho	(severinocarvalho14@gmail.com)
 * Data:	05/01/2022
 * Alteração:	Implementação de documentação da classe
 * 
 * #####################################	 			
 * 
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Cidade;

public interface Cidaderepository extends JpaRepository<Cidade, Integer> {

	/**
	 * 
	 * @param nome Recebe um nome para buscar a cidade com o mesmo
	 * 
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select p from Cidade p where p.nome like %:nome%")
	List<Cidade> findByNome(@Param("nome") String nome);

}
