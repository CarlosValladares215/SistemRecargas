package com.mycompany.recargasproyectoex.Logica;

import com.mycompany.recargasproyectoex.clases.Celular;
import com.mycompany.recargasproyectoex.clases.Cliente;
import com.mycompany.recargasproyectoex.clases.EstadoCelular;
import com.mycompany.recargasproyectoex.percistencia.CelularJpaController;

public class LogCelular {

    CelularJpaController celularJpa = new CelularJpaController();

    public void ingresarNuevoCelular(String numero, String saldo, String megas, EstadoCelular estado, Cliente cliente) throws Exception {
        if (buscarPorNumero(numero) != null) {
            throw new Exception("Ya existe un celular con ese número.");
        }

        Celular cel = new Celular();
        cel.setNumero(numero);
        cel.setSaldo(saldo);
        cel.setMegas(megas);
        cel.setEstado(estado);
        cel.setCliente(cliente);
        celularJpa.create(cel);
    }

    public Celular buscarPorNumero(String numero) {
        return celularJpa.findByNumero(numero);
    }

}
