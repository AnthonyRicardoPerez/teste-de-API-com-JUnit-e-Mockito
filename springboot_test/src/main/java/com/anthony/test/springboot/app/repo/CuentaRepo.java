package com.anthony.test.springboot.app.repo;

import com.anthony.test.springboot.app.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CuentaRepo extends JpaRepository<Cuenta, Long> {
    @Query("select c from Cuenta c where c.persona=?1")
    Optional<Cuenta> findByPersona(String persona);


   // List<Cuenta> findAll();
   // Cuenta findByid(Long id);
   // void update(Cuenta cuenta);
}
