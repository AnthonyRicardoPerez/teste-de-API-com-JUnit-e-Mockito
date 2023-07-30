package com.anthony.test.springboot.app.controller;

import com.anthony.test.springboot.app.Datos;
import com.anthony.test.springboot.app.models.Cuenta;
import com.anthony.test.springboot.app.models.TransaccionDto;
import com.anthony.test.springboot.app.service.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.ResultMatcher.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService cuentaService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDetalleId() throws Exception {
        //Given o contexto
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());

        //When
        mvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Anthony"))
                .andExpect(jsonPath("$.saldo").value("1000"));

        //Verifacndo que en realidad se ejecute el metodo findById
        verify(cuentaService).findById(1L);
    }

    @Test
    void testTransferir() throws Exception {

        //Given
        TransaccionDto dto= new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        System.out.println(objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje","Transferencia realizada con exito");
        response.put("transaccion", dto);

        System.out.println(objectMapper.writeValueAsString(response));

        //When
        mvc.perform(post("/api/cuentas/transferencia").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }


    @Test
    void testListar() throws Exception {
        //Given
        List<Cuenta> cuentas= Arrays.asList(Datos.crearCuenta001().orElseThrow(),Datos.crearCuenta2().orElseThrow());
        when(cuentaService.findAll()).thenReturn(cuentas);

        //When
        mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("Anthony"))
                .andExpect(jsonPath("$[1].persona").value("Andres"))
                .andExpect(jsonPath("$[0].saldo").value("1000"))
                .andExpect(jsonPath("$[1].saldo").value("2000"))
                .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));

        verify(cuentaService).findAll();
    }

    @Test
    void testGuardar() throws Exception {
        //Given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        when(cuentaService.save(any())).then(invocation ->{
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        //When
        mvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))

                //Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.persona").value("Pepe"))
                .andExpect(jsonPath("$.saldo").value("3000"));

        verify(cuentaService).save(any());
    }
}