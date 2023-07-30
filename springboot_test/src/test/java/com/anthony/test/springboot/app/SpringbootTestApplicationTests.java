package com.anthony.test.springboot.app;

import com.anthony.test.springboot.app.exception.DineroInsuficienteException;
import com.anthony.test.springboot.app.models.Banco;
import com.anthony.test.springboot.app.models.Cuenta;
import com.anthony.test.springboot.app.repo.BancoRepo;
import com.anthony.test.springboot.app.repo.CuentaRepo;
import com.anthony.test.springboot.app.service.CuentaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import  static org.mockito.Mockito.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SpringbootTestApplicationTests {

	@MockBean
	CuentaRepo cuentaRepo;
	@MockBean
	BancoRepo bancoRepo;
	@Autowired
	CuentaService service;

	@BeforeEach
	void setUp() {
		//cuentaRepo = mock(CuentaRepo.class);
		//bancoRepo = mock(BancoRepo.class);
		// service = new CuentaServiceImpl(cuentaRepo, bancoRepo);
		Datos.crearCuenta001();
		Datos.crearCuenta2();
		Datos.crearBanco();
	}

	@Test
	void transferencias() {
		when(cuentaRepo.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepo.findById(2L)).thenReturn(Datos.crearCuenta2());
		when(bancoRepo.findById(1L)).thenReturn(Datos.crearBanco());

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		service.transferir(1L, 2L, new BigDecimal("100"), 1L);

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino= service.revisarSaldo(2L);

		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());

		int total = service.revisarTotalTransf(1L);
		assertEquals(1, total);

		verify(cuentaRepo, times(3)).findById(1L);
		verify(cuentaRepo, times(3)).findById(2L);
		verify(cuentaRepo, times(2)).save(any(Cuenta.class));

		verify(bancoRepo, times(2)).findById(1L);
		verify(bancoRepo).save(any(Banco.class));

		verify(cuentaRepo, times(6)).findById(anyLong());
		verify(cuentaRepo, never()).findAll();
		}

	@Test
	void transferencias2() {
		when(cuentaRepo.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepo.findById(2L)).thenReturn(Datos.crearCuenta2());
		when(bancoRepo.findById(1L)).thenReturn(Datos.crearBanco());

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		assertThrows(DineroInsuficienteException.class, ()-> {
			service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino= service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		int total = service.revisarTotalTransf(1L);
		assertEquals(0, total);

		verify(cuentaRepo, times(3)).findById(1L);
		verify(cuentaRepo, times(2)).findById(2L);
		verify(cuentaRepo, never()).save(any(Cuenta.class));

		verify(bancoRepo, times(1)).findById(1L);
		verify(bancoRepo,never()).save(any(Banco.class));

		verify(cuentaRepo, never()).findAll();
		verify(cuentaRepo, times(5)).findById(anyLong());
	}

	@Test
	void comparandoInstanciasCuentas() {
		when(cuentaRepo.findById(1L)).thenReturn(Datos.crearCuenta001());

		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);

		//Se usa para comparar que dos instancias sean iguales
		assertSame(cuenta1, cuenta2);
		assertEquals("Anthony", cuenta1.getPersona());
		assertEquals("Anthony", cuenta2.getPersona());

		verify(cuentaRepo, times(2)).findById(1L);
	}

	@Test
	void testFindAll() {

		//Given
		List<Cuenta> datos = Arrays.asList(Datos.crearCuenta001().orElseThrow(), Datos.crearCuenta2().orElseThrow());
		when(cuentaRepo.findAll()).thenReturn(datos);

		//When
		List<Cuenta> cuentas = service.findAll();

		//Then
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		assertTrue(cuentas.contains(Datos.crearCuenta001().orElseThrow()));

		verify(cuentaRepo).findAll();
	}

	@Test
	void testSave() {

		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

		when(cuentaRepo.save(any())).then(invocatiom -> {
			Cuenta c = invocatiom.getArgument(0);
			c.setId(3L);
			return c;
		});

		//Then
		Cuenta cuenta = service.save(cuentaPepe);
		//then
		assertEquals("Pepe", cuenta.getPersona());
		assertEquals(3L, cuenta.getId());
		assertEquals("3000", cuenta.getSaldo().toPlainString());

		verify(cuentaRepo).save(any());
	}
}


