package br.edu.ifrn.siteifsol.repositories;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta interface tem o objetivo de auxiliar na manipulação dos dados da entidade {@link Usuario}
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

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Usuario;

public interface Usuariorepository extends JpaRepository<Usuario, Integer> {

	/**
	 * 
	 * @param email Recebe um email para buscar uma pessoa com o mesmo
	 * 
	 * @param nome  Recebe um nome para buscar uma pessoa com o mesmo
	 * 
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select u from Usuario u where u.email like %:email%  " + "and u.nome like %:nome%")
	List<Usuario> findByEmailAndNome(@Param("email") String email, @Param("nome") String nome);

	/**
	 * 
	 * @param email Recebe um email para buscar uma pessoa com o mesmo
	 * 
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select u from Usuario u where u.email like %:email%")
	Optional<Usuario> findByEmail(@Param("email") String email);

}
