package br.edu.ifrn.siteifsol.security;

/**
 * 
 * #####################################
 * 
 * Objetivo:	Esta classe tem o objetivo de gerenciar a segurança da aplicação
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * UsuarioService
	 */
	@Autowired
	private UsuarioService service;

	/**
	 * Configurações de segurança da aplicação
	 * Autorizações e Restrinções
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				// QUE PODEM SER ACESSADAS SEM LOGIN
				.antMatchers("/css/**", "/Imagens/**", "/js/**").permitAll()
				.antMatchers("/publico/**", "/download/**").permitAll()

				.antMatchers(
						"/usuario/**",
						"/usuarios/**",
						"/noticia/**")
				.hasAuthority(Usuario.ADMIN)

				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/adm", true)
				.failureUrl("/login-erro").permitAll()

				.and()
				.logout()
				.logoutSuccessUrl("/adm")
				.and()
				.rememberMe();
	}

	/**
	 * Informa ao Spring como a senha está sendo Encriptada
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
}
