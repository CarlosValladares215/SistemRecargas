
package com.mycompany.recargasproyectoex.clases;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Celular implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCel;

    private String numero;

    @Enumerated(EnumType.STRING)
    private EstadoCelular estado;

    private String saldo;
    private String megas;

    @ManyToOne
    @JoinColumn(name = "idClie")
    private Cliente cliente;

    @OneToMany(mappedBy = "celular", cascade = CascadeType.ALL)
    private List<Recarga> recargas;

    public Celular() {
    }

    public Celular(int idCel, String numero, EstadoCelular estado, String saldo, String megas, Cliente cliente, List<Recarga> recargas) {
        this.idCel = idCel;
        this.numero = numero;
        this.estado = estado;
        this.saldo = saldo;
        this.megas = megas;
        this.cliente = cliente;
        this.recargas = recargas;
    }

    public int getIdCel() {
        return idCel;
    }

    public void setIdCel(int idCel) {
        this.idCel = idCel;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public EstadoCelular getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelular estado) {
        this.estado = estado;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getMegas() {
        return megas;
    }

    public void setMegas(String megas) {
        this.megas = megas;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Recarga> getRecargas() {
        return recargas;
    }

    public void setRecargas(List<Recarga> recargas) {
        this.recargas = recargas;
    }
    
    
}   
