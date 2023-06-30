package edu.arelance.nube.services;

import java.util.Optional;

import edu.arelance.nube.repository.entity.Restaurante;

public interface RestauranteService {

	Iterable<Restaurante> consultarTodos(); // cabecera del método. Iterable=recorrer<Restaurantes>

	Optional<Restaurante> consultarRestaurante(Long id); // devuelve un objeto que nunca es null, como un huevo kinder.
															// Devuelve pero puede llevar datos o no.

	Restaurante altaRestaurante(Restaurante restaurante);

	void borrarRestaurante(Long id);

	Optional<Restaurante> modificarRestaurante(Long id, Restaurante restaurante);

	Iterable<Restaurante> buscarPorRangoPrecio (int preciomin, int preciomax);
	
	Iterable<Restaurante> buscarRestaurantePorClave (String clave);
}
