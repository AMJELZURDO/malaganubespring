package edu.arelance.nube.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.arelance.nube.repository.entity.Restaurante;

@Repository
public interface RestauranteRepository extends CrudRepository<Restaurante, Long> {
	
	//Métodos de consulta:
	//1 KEY WORD QUERIES - consultas por palabra clave
	//obtener un restaurante en un rango de precio según DOCS.SPRING.IO QUERY Creation web
	
	Iterable<Restaurante> findByPrecioBetween (int preciomin, int preciomax);
	
	
	//2 JPQL - HQL - Pseudo SQL pero de Java -"Agnóstico"
	//3 NATIVAS - SQL
	@Query(value = "SELECT * FROM bdrestaurantes.restaurantes WHERE barrio LIKE %?1% OR nombre LIKE %?1% OR especialidad LIKE %?1% OR especialidad2 LIKE %?1% OR especialidad3 LIKE %?1%", nativeQuery = true)
	Iterable<Restaurante> buscarPorBarrioNombreOEspecialidad (String clave); //cabecera
	//4 STORED PROCEDURES - Procedimientos Almacenados
	//5 CRITERIA API - SQL pero con métodos de JAVA https://www.arquitecturajava.com/
	

	
}
