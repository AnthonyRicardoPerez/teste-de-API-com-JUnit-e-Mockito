package com.anthony.test.springboot.app;

import com.anthony.test.springboot.app.models.Cuenta;
import com.anthony.test.springboot.app.repo.CuentaRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepo cuentaRepo;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepo.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Anthony", cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepo.findByPersona("Anthony");
        assertTrue(cuenta.isPresent());
        assertEquals("Anthony", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowsException() {
        Optional<Cuenta> cuenta = cuentaRepo.findByPersona("Rod");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepo.findAll();

        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {

        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));


        //when- cuando
        Cuenta cuenta = cuentaRepo.save(cuentaPepe);
        //Cuenta cuenta = cuentaRepo.findByPersona("Pepe").orElseThrow();
        //Cuenta cuenta = cuentaRepo.findById(save.getId()).orElseThrow();

        //then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        //assertEquals(3,cuenta.getId());
    }

    @Test
    void testUpdate() {

        //save
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));


        //when- cuando
        Cuenta cuenta = cuentaRepo.save(cuentaPepe);
        //Cuenta cuenta = cuentaRepo.findByPersona("Pepe").orElseThrow();
        //Cuenta cuenta = cuentaRepo.findById(save.getId()).orElseThrow();

        //then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        //assertEquals(3,cuenta.getId());

        //when
        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepo.save(cuenta);

        //then
        assertEquals("Pepe", cuentaActualizada.getPersona());
        assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());

    }

    @Test
    void testDelete() {

        Cuenta cuenta=cuentaRepo.findById(2L).orElseThrow();
        assertEquals("Andres",cuenta.getPersona());

        cuentaRepo.delete(cuenta);

        assertThrows(NoSuchElementException.class, ()->{
         //  cuentaRepo.findByPersona("Andres").orElseThrow();
           cuentaRepo.findById(2L).orElseThrow();
        });

        assertEquals(1, cuentaRepo.findAll().size());

    }
}
