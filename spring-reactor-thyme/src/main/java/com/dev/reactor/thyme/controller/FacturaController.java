package com.dev.reactor.thyme.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.reactor.thyme.document.Cliente;
import com.dev.reactor.thyme.document.Factura;
import com.dev.reactor.thyme.document.FacturaItem;
import com.dev.reactor.thyme.document.Plato;
import com.dev.reactor.thyme.pagination.PageSupport;
import com.dev.reactor.thyme.service.ClienteService;
import com.dev.reactor.thyme.service.FacturaService;
import com.dev.reactor.thyme.service.PlatoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

	private final ClienteService clienteService;
	
	private final FacturaService facturaService;
	
	private final PlatoService platoService;
	
	public FacturaController(@Autowired @Qualifier("clienteService") ClienteService clienteService,
			@Autowired @Qualifier("facturaService") FacturaService facturaService, @Autowired
			@Qualifier("platoService") PlatoService platoService) {
		this.clienteService = clienteService;
		this.facturaService = facturaService;
		this.platoService = platoService;
	}
	
	@GetMapping("/listar")
	public Mono<String> listarPageable(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
		int pageSize = 1;
		Pageable pageable = PageRequest.of(page, pageSize);
		Mono<PageSupport<Cliente>> monoPage = clienteService.listarPageable(pageable);
		model.addAttribute("pageSp", monoPage);
		model.addAttribute("url", "/facturas/listar");
		return Mono.just("facturas/listar");
	}
	
	@GetMapping("/form/{clienteId}")
	public Mono<String> nuevaFactura(@PathVariable(value = "clienteId") String clienteId, Model model) {
		return clienteService.listarPorId(clienteId)
				.defaultIfEmpty(new Cliente())
				.flatMap((c) -> {
					if (c.getId() == null) {
						return Mono.error(() -> new InterruptedException("No existe el Cliente!"));
					}
					return Mono.just(c);
				})
				.doOnNext((c) -> {
					Factura factura = new Factura();
					factura.setCliente(c);
					model.addAttribute("factura", factura);
					model.addAttribute("titulo", "Nueva Factura");
				})
				.then(Mono.just("facturas/form"))
				.onErrorResume((ex) -> Mono.just("redirect:/facturas/listar?error=No+existe+el+cliente"))
				.onErrorReturn("redirect:/facturas/listar?error=No+existe+el+cliente");
	}
	
	@GetMapping(value = "/operar")
	public Mono<String> generarFactura(Factura factura, Model model,
			@RequestParam(value = "clienteId") String clienteId,
			@RequestParam(value = "item_id[]", name = "item_id[]", required = false) String[] itemId,
			@RequestParam(value = "quantity[]", name = "quantity[]", required = false) Integer[] quantity) throws InterruptedException {
		Cliente cliente = new Cliente();
		cliente.setId(clienteId);
		factura.setCliente(cliente);
		factura.setCreadoEn(LocalDateTime.now());
		
		List<FacturaItem> items = new ArrayList<FacturaItem>();
		
		for (int i = 0; i < itemId.length; i++) {
			FacturaItem facturaItem = new FacturaItem();
			facturaItem.setCantidad(quantity[i]);
			facturaItem.setPlato(new Plato(itemId[i]));
			items.add(facturaItem);
		}
		
		factura.setItems(items);
		model.addAttribute("success", "Factura Generada");
		return facturaService.registrar(factura)
				.then(Mono.just("redirect:/facturas/listar?success=se+registro+correctamente"))
				.onErrorResume((ex) -> Mono.just("redirect:/facturas/form?error=no+se+guardo"))
				.onErrorReturn("redirect:/facturas/form?error=no+se+guardo");
	}
	
	// Para consultar facturas
	@GetMapping(value = "/detalle/listar")
	public Mono<String> detalleListarFactura(Model model) {
		//Cambio a 2.3.1
		Flux<Factura> facturasFlux = facturaService.listar()
				// Obteniendo el Cliente
				.flatMap(factura -> {
					return Mono.just(factura)
							.zipWith(clienteService.listarPorId(factura.getCliente()
									.getId()), (f, c) -> {
										f.setCliente(c);
										return f;
									});
				});
		model.addAttribute("facturas", facturasFlux);
		model.addAttribute("titulo", "Facturas");
		return Mono.just("facturas/detalle-listar");
	}
	
	@GetMapping("/detalle/{id}")
	public Mono<String> detalleFactura(@PathVariable(value = "id") String facturaId, Model model) {
		return facturaService.listarPorId(facturaId)
				.defaultIfEmpty(new Factura())
				// Obteniendo el Cliente
				.flatMap(factura -> {
					return Mono.just(factura)
							.zipWith(clienteService.listarPorId(factura.getCliente()
									.getId()), (f, c) -> {
										f.setCliente(c);
										return f;
									});
				})
				// Obteniendo para cada plato
				.flatMap(factura -> {
					return Flux.fromIterable(factura.getItems())
							.flatMap(item -> {
								return Mono.just(item)
										.zipWith(platoService.listarPorId(item.getPlato()
												.getId()), (i, p) -> {
													i.setPlato(p);
													return i;
												});
							})
							.collectList()
							.flatMap(lista -> {
								// seteando la lista a la factura
								factura.setItems(lista);
								return Mono.just(factura); // devolviendo el objeto factura
							});
				})
				.doOnNext((f) -> {
					model.addAttribute("factura", f);
					model.addAttribute("titulo", "Factura");
				})
				.then(Mono.just("facturas/detalle"));
	}
}