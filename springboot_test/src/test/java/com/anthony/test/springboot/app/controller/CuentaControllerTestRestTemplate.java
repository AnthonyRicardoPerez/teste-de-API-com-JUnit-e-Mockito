package com.anthony.test.springboot.app.controller;

import com.anthony.test.springboot.app.models.Cuenta;
import com.anthony.test.springboot.app.models.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate client;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }
    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        TransaccionDto dto= new TransaccionDto();
        dto.setMonto(new BigDecimal("100"));
        dto.setCuentaDestinoId(2L);
        dto.setCuentaOrigenId(1L);
        dto.setBancoId(1L);

        ResponseEntity<String> response = client.postForEntity("/api/cuentas/transferencia", dto, String.class);

        String json = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con exito"));

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con exito", jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigenId").asLong());


        Map<String, Object> response2 = new HashMap<>();
        response2.put("date", LocalDate.now().toString());
        response2.put("status", "ok");
        response2.put("mensaje","Transferencia realizada con exito");
        response2.put("transaccion", dto);

        assertEquals(objectMapper.writeValueAsString(response2), json);
        System.out.println(json);
    }

    @Test
    @Order(2)
    void testDetalle() {

        ResponseEntity<Cuenta> respuesta = client.getForEntity("/api/cuentas/1", Cuenta.class);
        Cuenta cuenta = respuesta.getBody();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        assertNotNull(cuenta);
        assertEquals(1L, cuenta.getId());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
        assertEquals(new Cuenta(1L, "Anthony", new BigDecimal("900.00")), cuenta);
    }

    @Test
    @Order(3)
    void testListar() throws JsonProcessingException {

        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(2, cuentas.size());
        assertEquals(1L, cuentas.get(0).getId());
        assertEquals("Anthony", cuentas.get(0).getPersona());
        assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());

        assertEquals(2L, cuentas.get(1).getId());
        assertEquals("Andres", cuentas.get(1).getPersona());
        assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());


        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(cuentas));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Anthony", json.get(0).path("persona").asText());
        assertEquals("900.0", json.get(0).path("saldo").asText());

        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Andres", json.get(1).path("persona").asText());
        assertEquals("2100.0", json.get(1).path("saldo").asText());
    }

    @Test
    @Order(4)
    void testGuardar() {
        Cuenta cuenta = new Cuenta(null,"Pepe", new BigDecimal("3000"));

        ResponseEntity<Cuenta> response = client.postForEntity("/api/cuentas", cuenta, Cuenta.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        Cuenta cuentaCreada = response.getBody();
        assertNotNull(cuentaCreada);
        assertEquals(3L,cuentaCreada.getId());
        assertEquals("Pepe",cuentaCreada.getPersona());
        assertEquals("3000",cuentaCreada.getSaldo().toPlainString());
    }

    @Test
    @Order(5)
    void testDelete() {

        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());
        assertEquals(3, cuentas.size());

        client.delete("/api/cuentas/3");
        /*ResponseEntity<Void> exchange = client.exchange("/api/cuentas/3", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());*/

        response = client.getForEntity("/api/cuentas", Cuenta[].class);
        cuentas = Arrays.asList(response.getBody());
        assertEquals(2, cuentas.size());

    }
}