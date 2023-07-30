package com.anthony.test.springboot.app.service;

import com.anthony.test.springboot.app.controller.CuentaController;
import com.anthony.test.springboot.app.models.Cuenta;
import org.apache.catalina.LifecycleState;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    Cuenta findById(Long id);
    int revisarTotalTransf(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);

    void deleteById(Long id);

}
