package br.edu.ifrn.siteifsol.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.siteifsol.dominio.Noticia;

public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {

	@Query("select u from Noticia u where u.titulo like %:titulo%  " + "and u.texto like %:texto%")
	List<Noticia> findByTituloAndTexto(@Param("titulo") String titulo, @Param("texto") String texto);

	@Query("select u from Noticia u where u.titulo like %:titulo%")
	Optional<Noticia> findByTitulo(@Param("titulo") String titulo);
}
