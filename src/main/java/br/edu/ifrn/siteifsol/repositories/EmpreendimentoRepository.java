package br.edu.ifrn.siteifsol.repositories;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta interface tem o objetivo de auxiliar na manipulação dos dados da entidade {@link Empreendimento}
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
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Empreendimento;

public interface EmpreendimentoRepository extends JpaRepository<Empreendimento, Integer> {

	/**
	 * 
	 * @param email Recebe um email para buscar um empreendimento com o mesmo
	 * 
	 * @param nome  Recebe um nome para buscar um empreendimento com o mesmo
	 * 
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select u from Empreendimento u where u.email like %:email%  "
			+ "and u.nome like %:nome%")
	List<Empreendimento> findByEmailAndNome(@Param("email") String email, @Param("nome") String nome);

	/**
	 * 
	 * @param email Recebe um email para buscar um empreendimento com o mesmo
	 * 
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select u from Empreendimento u where u.email like %:email%")
	Optional<Empreendimento> findByEmail(@Param("email") String email);

}
