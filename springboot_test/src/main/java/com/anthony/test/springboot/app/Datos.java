package com.anthony.test.springboot.app;

import com.anthony.test.springboot.app.models.Banco;
import com.anthony.test.springboot.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {

    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L, "Anthony", new BigDecimal("1000")));
    }
    public static Optional<Cuenta> crearCuenta2(){
        return Optional.of(new Cuenta(2L, "Andres", new BigDecimal("2000")));
    }
    public static  Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L, "El banco fincanciero", 0));
    }
}
