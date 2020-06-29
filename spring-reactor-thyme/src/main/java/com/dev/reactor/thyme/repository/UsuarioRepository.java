package com.dev.reactor.thyme.repository;

import org.springframework.stereotype.Repository;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.document.Usuario;

import reactor.core.publisher.Mono;

@Repository("usuarioRepository")
public interface UsuarioRepository extends BaseRepository<Usuario, String> {

	Mono<Usuario> findOneByUsuario(String usuario);
}