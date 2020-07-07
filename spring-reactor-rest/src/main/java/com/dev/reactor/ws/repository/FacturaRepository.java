package com.dev.reactor.ws.repository;

import com.dev.reactor.ws.document.Factura;

import org.springframework.stereotype.Repository;

import com.dev.reactor.ws.base.repository.BaseRepository;

@Repository("facturaRepository")
public interface FacturaRepository extends BaseRepository<Factura, String> {

}