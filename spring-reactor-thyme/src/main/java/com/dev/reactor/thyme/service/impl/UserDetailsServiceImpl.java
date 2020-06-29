package com.dev.reactor.thyme.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dev.reactor.thyme.document.Usuario;
import com.dev.reactor.thyme.repository.RolRepository;
import com.dev.reactor.thyme.repository.UsuarioRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

	private final UsuarioRepository usuarioRepository;
	
	private final RolRepository rolRepository;
	
	public UserDetailsServiceImpl(@Autowired @Qualifier("usuarioRepository") UsuarioRepository usuarioRepository,
			@Autowired @Qualifier("rolRepository") RolRepository rolRepository) {
		this.usuarioRepository = usuarioRepository;
		this.rolRepository = rolRepository;
	}
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		Mono<Usuario> usuarioMono = usuarioRepository.findOneByUsuario(username);
		List<GrantedAuthority> authorities = new ArrayList<>();
		return usuarioMono.flatMap((usuario) -> {
			return Flux.fromIterable(usuario.getRoles())
					.flatMap((rol) -> {
						return rolRepository.findById(rol.getId())
								.map(r -> {
									authorities.add(new SimpleGrantedAuthority(r.getNombre()));
									return r;
								});
					})
					.collectList()
					.flatMap(lista -> {
						usuario.setRoles(lista);
						return Mono.just(usuario);
					});
		})
		.flatMap(usuario -> {
			return Mono.just(
					new User(usuario.getUsuario(), usuario.getClave(), usuario.getEstado(), 
							true, true, true, authorities));
		});
	}
}