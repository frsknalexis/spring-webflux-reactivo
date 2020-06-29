package com.dev.reactor.thyme.repository;

import org.springframework.stereotype.Repository;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.document.Rol;

@Repository("rolRepository")
public interface RolRepository extends BaseRepository<Rol, String> {

}