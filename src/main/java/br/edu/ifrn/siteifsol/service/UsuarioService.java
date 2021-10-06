package br.edu.ifrn.siteifsol.service;

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

	/*
	 * FAZ O BUSCA E CONFERE SE O USUARIO QUE DESEJA FAZER LOGIN ESTA NO BANCO DE
	 * DADOS OU NÃO
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

		return new User(usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(usuario.getPerfil()));
	}

}
