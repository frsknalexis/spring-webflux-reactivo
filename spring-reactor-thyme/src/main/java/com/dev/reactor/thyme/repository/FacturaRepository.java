package com.dev.reactor.thyme.repository;

import org.springframework.stereotype.Repository;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.document.Factura;

@Repository("facturaRepository")
public interface FacturaRepository extends BaseRepository<Factura, String> {

}