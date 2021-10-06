package br.edu.ifrn.siteifsol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.siteifsol.dominio.Arquivo;



public interface ArquivoRepository  extends JpaRepository<Arquivo, Long>{

}
