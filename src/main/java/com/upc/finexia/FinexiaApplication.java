package com.upc.finexia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Punto de entrada de la aplicacion Finexia (backend de gestion financiera familiar).
// Las User Stories implementadas estan documentadas en los controllers y servicios respectivos.
@SpringBootApplication
public class FinexiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinexiaApplication.class, args);
    }
}
