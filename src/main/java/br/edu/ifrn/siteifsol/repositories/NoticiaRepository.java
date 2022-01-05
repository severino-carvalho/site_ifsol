package br.edu.ifrn.siteifsol.repositories;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta interface tem o objetivo de auxiliar na manipulação dos dados da entidade {@link Noticia}
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

import br.edu.ifrn.siteifsol.dominio.Noticia;

public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {

	/**
	 * 
	 * @param titulo Recebe um titulo para buscar uma noticia com o mesmo
	 * 
	 * @param texto  Recebe um texto para buscar uma noticia com o mesmo
	 * 
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select u from Noticia u where u.titulo like %:titulo%  " + "and u.texto like %:texto%")
	List<Noticia> findByTituloAndTexto(@Param("titulo") String titulo, @Param("texto") String texto);

	/**
	 * 
	 * @param titulo Recebe um titulo para buscar uma noticia com o mesmo
	 *
	 * @return É retornado o resultado da {@link Query}
	 */
	@Query("select u from Noticia u where u.titulo like %:titulo%")
	Optional<Noticia> findByTitulo(@Param("titulo") String titulo);
}
