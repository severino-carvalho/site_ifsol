package br.edu.ifrn.siteifsol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Empreendimento;

/*
 * CLASSE REPOSITORY CONTEM OS CODIGOS PARA EXECUÇÃO DE
 * AÇÕES NO BANCO DE DADOS ATRAVES DA NOTAÇÃO @QUERY
 */

public interface empreendimentorepository  extends JpaRepository<Empreendimento, Integer>{
	
	//BUSCA OS USUARIOS PELO NOME E EMAIL
	@Query("select u from empreendimento u where u.email like %:email%  "
			+ "and u.nome like %:nome%")
	List<Empreendimento> findByEmailAndNome(@Param("email") String email,@Param("nome") String nome);
	
	
	//BUSCA SO PELO EMAIL
	@Query("select u from empreendimento u where u.email like %:email%")
	Optional<Empreendimento> findByEmail(@Param("email") String email);

}
