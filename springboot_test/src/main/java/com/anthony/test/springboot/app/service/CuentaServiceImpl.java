package com.anthony.test.springboot.app.service;

import com.anthony.test.springboot.app.models.Banco;
import com.anthony.test.springboot.app.models.Cuenta;
import com.anthony.test.springboot.app.repo.BancoRepo;
import com.anthony.test.springboot.app.repo.CuentaRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService{

    private CuentaRepo cuentaRepo;
    private BancoRepo bancoRepo;

    public CuentaServiceImpl(CuentaRepo cuentaRepo, BancoRepo bancoRepo) {
        this.cuentaRepo = cuentaRepo;
        this.bancoRepo = bancoRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepo.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransf(Long bancoId) {
        Banco banco = bancoRepo.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencia();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta= cuentaRepo.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId) {

        Cuenta cuentaOrigen = cuentaRepo.findById(numCuentaOrigen).orElseThrow();
        cuentaOrigen.debito(monto);
        cuentaRepo.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepo.findById(numCuentaDestino).orElseThrow();
        cuentaDestino.credito(monto);
        cuentaRepo.save(cuentaDestino);

        Banco banco = bancoRepo.findById(bancoId).orElseThrow();
        int totalTransferencia = banco.getTotalTransferencia();
        banco.setTotalTransferencia(++totalTransferencia);
        bancoRepo.save(banco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepo.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepo.save(cuenta);
    }

    @Override
    public void deleteById(Long id) {
        cuentaRepo.deleteById(id);
    }

}
