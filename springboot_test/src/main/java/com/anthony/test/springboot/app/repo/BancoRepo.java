package com.anthony.test.springboot.app.repo;

import com.anthony.test.springboot.app.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BancoRepo extends JpaRepository<Banco,Long> {
    //List<Banco> findAll();
    //Banco findById(Long id);
    //void update(Banco banco);
}
