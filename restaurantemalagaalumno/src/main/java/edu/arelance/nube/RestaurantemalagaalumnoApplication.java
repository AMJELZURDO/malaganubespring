package edu.arelance.nube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication

//@EnableEurekaClient  //Activamos el cliente eureka

/**
 * PARA CONFIGURAR EL CLIENTE EUREKA * 
 * 1) Add starters, eureka client
 * 2) Add Anotación @EnableEurekaClient en el main
 * 3) Configuramos las properties relativas a eureka  * 
 *
 */

public class RestaurantemalagaalumnoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantemalagaalumnoApplication.class, args);
	}

}
