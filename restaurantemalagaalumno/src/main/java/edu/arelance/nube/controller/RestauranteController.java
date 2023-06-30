package edu.arelance.nube.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.arelance.nube.repository.entity.Restaurante;
import edu.arelance.nube.services.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 
 * API WEB HTTP -> Deriva en la ejecución de un método
 *
 * GET -> Consultar TODOS GET -> Consultar Uno (por ID) POST -> Insertat un
 * restaurante nuevo PUT -> Modificar un restaurante que ya existe DELETE ->
 * Borrar un restaurante (por ID) GET -> Búsqueda -> por barrio, especialidad,
 * nombre, etc
 */

//@Controller //devolvemos una vista html/jsp
@RestController // devolvemos solo texto JSON http://localhost:8081/restaurante
@RequestMapping("/restaurante")

public class RestauranteController {

	@Autowired
	RestauranteService restauranteService;

	Logger logger = LoggerFactory.getLogger(RestauranteController.class);

	@GetMapping("/test") // get http://localhost:8081/restaurante/test
	public Restaurante obtenerRestauranteTest() {

		Restaurante restaurante = null;
		System.out.println("llamando a obtenerRestauranteTest");
		logger.debug("Estoy en obtenerRestauranteTest");
		restaurante = new Restaurante(1l, "Martinete", "Carlos Haya 33", "Carranque", "www.martinete.org",
				"http://google.xe", 33.65f, -2.3f, 10, "gazpachuelo", "paella", "sopa de marisco", LocalDateTime.now());
		return restaurante;
	}

	// GET -> Consultar TODOS GET http://localhost:8081/restaurante

	@GetMapping
	public ResponseEntity<?> listarTodos() {
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> lista_restaurante = null;

		/*
		 * String saludo ="Hola"; saludo.charAt(10);
		 */

		lista_restaurante = this.restauranteService.consultarTodos();
		responseEntity = ResponseEntity.ok(lista_restaurante);
		return responseEntity;
	}
	// GET -> Consultar Uno (por ID) http://localhost:8081/restaurante/12

	@Operation(description = "Servicio de consulta de un restaurante por ID")
	@GetMapping("/{id}")
	public ResponseEntity<?> listarPorId(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		Optional<Restaurante> or = null;

		logger.debug("En listarPorId " + id);

		or = this.restauranteService.consultarRestaurante(id);
		if (or.isPresent()) {
			// la consulta ha recuperado registro
			Restaurante restauranteLeido = or.get();
			responseEntity = ResponseEntity.ok(restauranteLeido);
			logger.debug("Recuperado el registro " + restauranteLeido);
		} else {
			///// la consulta NO ha recuperado registro
			responseEntity = ResponseEntity.noContent().build();
			logger.debug("El restaurante con " + id + "no existe");
		}

		logger.debug("Saliendo de ListrPorId");
		return responseEntity;

	}

	// *POST -> Insertar un restaurante nuevo http://localhost:8081/restaurante
	// (Body Restaurante)

	@PostMapping
	public ResponseEntity<?> insertarRestaurante(@Valid @RequestBody Restaurante restaurante,
			BindingResult bindingResult) {
		ResponseEntity<?> responseEntity = null;
		Restaurante restauranteNuevo = null;

		// TODO validar
		if (bindingResult.hasErrors()) {
			logger.debug("Errores en la entrada POST");
			responseEntity = generarRespuestaErroresValidacion(bindingResult);
		} else {

			logger.debug("Sin Errores en la entrada POST");
			restauranteNuevo = this.restauranteService.altaRestaurante(restaurante);
			responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(restauranteNuevo);

		}
		return responseEntity;
	}

	// *PUT -> Modificar un restaurante que ya existe
	// http://localhost:8081/restaurante/id (Body Restaurante)
	@PutMapping("/{id}")
	public ResponseEntity<?> modificarRestaurante(@Valid @RequestBody Restaurante restaurante, @PathVariable Long id,
			BindingResult bindingResult) {

		ResponseEntity<?> responseEntity = null;
		Optional<Restaurante> opRest = null;
		if (bindingResult.hasErrors()) {
			logger.debug("Error en la entrada al PUT");
			responseEntity = generarRespuestaErroresValidacion(bindingResult);

		} else {
			logger.debug("SIN Error en la entrada al PUT");
			opRest = this.restauranteService.modificarRestaurante(id, restaurante);
			if (opRest.isPresent()) {
				Restaurante rm = opRest.get(); // rm -> restaurante modificado que nos llega del service
				responseEntity = ResponseEntity.ok(rm);
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}
		return responseEntity;
	}

	// *DELETE -> Borrar un restaurante (por ID)
	// http://localhost:8081/restaurante/id

	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrarPorId(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;

		this.restauranteService.borrarRestaurante(id);
		responseEntity = ResponseEntity.ok().build();

		return responseEntity;

	}

	private ResponseEntity<?> generarRespuestaErroresValidacion(BindingResult bindingResult) {
		
		ResponseEntity<?> responseEntity = null;
		List<ObjectError> listaErrores = null;

		listaErrores = bindingResult.getAllErrors();
		//imprimir errores por el LOG
		listaErrores.forEach(e -> logger.error(e.toString()));
		
		responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaErrores);

		return responseEntity;

	}

	/*
	 * // GET -> Consultar Restaurante por rango de precio
	 * http://localhost:8081/restaurante/1/10
	 * 
	 * @Operation(description =
	 * "Servicio de consulta de un restaurante por rango de precio")
	 * 
	 * @GetMapping("/{preciomin}/{preciomax}") public ResponseEntity<?>
	 * listarPorRangoPrecio(@PathVariable int preciomin, @PathVariable int
	 * preciomax) { ResponseEntity<?> responseEntity = null; Iterable<Restaurante>
	 * listaR = null;
	 * 
	 * listaR = this.restauranteService.consultarRangoPorPrecio(preciomin,
	 * preciomax); responseEntity = ResponseEntity.ok(listaR);
	 * 
	 * return responseEntity;
	 * 
	 * }
	 */
	// GET
	// http://localhost:8081/restaurante/buscarPorPrecio?preciomin=10&preciomax=40
	@GetMapping("/buscarPorPrecio")
	public ResponseEntity<?> listarPorRangoPrecio(@RequestParam(name = "preciomin") int preciomin,
			@RequestParam(name = "preciomax") int preciomax) {
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> lista_Restaurantes = null;

		lista_Restaurantes = this.restauranteService.buscarPorRangoPrecio(preciomin, preciomax);
		responseEntity = ResponseEntity.ok(lista_Restaurantes);

		return responseEntity;
	}

	/*
	 * GET Listar por tres especialidades
	 * 
	 * public ResponseEntity<?> listarPorTresEspecialidades (
	 * 
	 * @RequestParam(name = "preciomin") int preciomin,
	 * 
	 * @RequestParam(name = "preciomax") int preciomax) { ResponseEntity<?>
	 * responseEntity = null; Iterable<Restaurante> lista_Restaurantes = null;
	 * 
	 * lista_Restaurantes = this.restauranteService.buscarPorRangoPrecio(preciomin,
	 * preciomax); responseEntity = ResponseEntity.ok(lista_Restaurantes);
	 * 
	 * return responseEntity; }
	 */

	// Get ver restaurantes por clave GET
	// http://localhost:8081/restaurante/buscarPorClave/pescado
	@GetMapping("/buscarPorBarrioNombreOEspecialidad")
	public ResponseEntity<?> buscarPorClave(@RequestParam(name = "clave") String clave) {
		ResponseEntity<?> responseEntity = null;
		Iterable<Restaurante> listaPorPalabra = null;

		listaPorPalabra = this.restauranteService.buscarRestaurantePorClave(clave);
		responseEntity = ResponseEntity.ok(listaPorPalabra);

		return responseEntity;
	}

}