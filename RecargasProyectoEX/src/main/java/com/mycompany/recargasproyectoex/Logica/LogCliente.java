
package com.mycompany.recargasproyectoex.Logica;

import com.mycompany.recargasproyectoex.clases.Cliente;
import com.mycompany.recargasproyectoex.percistencia.ClienteJpaController;

public class LogCliente {
    ClienteJpaController clienteJpa = new ClienteJpaController();
    
    public void registrarCliente(String cedula, String nombres, String apellidos) throws Exception {
        if (buscarPorCedula(cedula) != null) {
            throw new Exception("Ya existe un cliente con esa cédula.");
        }

        Cliente cliente = new Cliente();
        cliente.setCedula(cedula);
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);

        clienteJpa.create(cliente);
    }

    public Cliente buscarPorCedula(String cedula) {
        return clienteJpa.findByCedula(cedula);
    }

}
