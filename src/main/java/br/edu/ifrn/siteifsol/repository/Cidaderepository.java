package br.edu.ifrn.siteifsol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Cidade;

/*
 * CLASSE REPOSITORY CONTEM OS CODIGOS PARA EXECUÇÃO DE
 * AÇÕES NO BANCO DE DADOS ATRAVES DA NOTAÇÃO @QUERY
 */

public interface Cidaderepository  extends JpaRepository<Cidade, Integer>{

	//BUSCA NO BANCO DE DADOS PELAS CIDADES ATRAVES DO NOME
	@Query("select p from Cidade p where p.nome like %:nome%")
	List<Cidade> findByNome(@Param("nome") String nome);

	
	
}
