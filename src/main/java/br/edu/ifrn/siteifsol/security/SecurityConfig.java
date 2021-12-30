package br.edu.ifrn.siteifsol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.edu.ifrn.siteifsol.dominio.Usuario;
import br.edu.ifrn.siteifsol.service.UsuarioService;

//CLASSE PARA CRIÇÃO SEGURA DO LOGIN
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioService service;

	/*
	 * METODO QUE CONFIGURA O ACESSO DE PÁGINAS ATRAVES DO LOGIN ONDE É DADO
	 * PERMISÃO DE ACESSO A PÁGINAS ATRAVES DO LOGIN OU SEM LOGIN
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				// QUE PODEM SER ACESSADAS SEM LOGIN
				.antMatchers("/css/**", "/Imagens/**", "/js/**").permitAll()
				.antMatchers("/publico/**", "/download/**").permitAll()

				.antMatchers(
						"/usuario/cadastro",
						"/usuario/salvar",
						"usuario/editar/**",
						"/usuario/remover/**",
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
					.rememberMe(); // DESFAzER O LOGIN, SAIR
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
}
