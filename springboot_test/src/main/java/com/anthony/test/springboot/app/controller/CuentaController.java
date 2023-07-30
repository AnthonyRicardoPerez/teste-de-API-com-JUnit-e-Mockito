package com.anthony.test.springboot.app.controller;

import com.anthony.test.springboot.app.models.Cuenta;
import com.anthony.test.springboot.app.models.TransaccionDto;
import com.anthony.test.springboot.app.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> listar(){
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable Long id){
        return cuentaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta){
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto){
        cuentaService.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(),dto.getMonto(), dto.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje","Transferencia realizada con exito");
        response.put("transaccion", dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        cuentaService.deleteById(id);
    }
}
