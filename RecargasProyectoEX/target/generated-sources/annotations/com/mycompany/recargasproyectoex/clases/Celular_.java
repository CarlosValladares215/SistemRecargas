package com.mycompany.recargasproyectoex.clases;

import com.mycompany.recargasproyectoex.clases.Cliente;
import com.mycompany.recargasproyectoex.clases.EstadoCelular;
import com.mycompany.recargasproyectoex.clases.Recarga;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T23:28:37", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Celular.class)
public class Celular_ { 

    public static volatile SingularAttribute<Celular, Cliente> cliente;
    public static volatile SingularAttribute<Celular, EstadoCelular> estado;
    public static volatile SingularAttribute<Celular, String> numero;
    public static volatile SingularAttribute<Celular, Integer> idCel;
    public static volatile SingularAttribute<Celular, String> saldo;
    public static volatile SingularAttribute<Celular, String> megas;
    public static volatile ListAttribute<Celular, Recarga> recargas;

}