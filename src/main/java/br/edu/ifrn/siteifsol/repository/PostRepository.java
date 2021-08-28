package br.edu.ifrn.siteifsol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Post;
import br.edu.ifrn.siteifsol.dominio.Usuario;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	// BUSCA OS POSTS PELO TÍTULO
	@Query("select p from Usuario p where p.titulo like %:titulo%")
	List<Usuario> findByTitulo(@Param("email") String email, @Param("nome") String nome);

}
