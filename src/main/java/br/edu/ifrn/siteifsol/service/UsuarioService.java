package br.edu.ifrn.siteifsol.service;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo ser um serviço de {@link Usuario} 
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.repositories.Usuariorepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private Usuariorepository repository;

	/**
	 * Esse método especifica como deve ser o processo de login. No caso, usando o
	 * email do usuário
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

		return new User(usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(usuario.getPerfil()));
	}

}
