package com.mycompany.recargasproyectoex.clases;

import com.mycompany.recargasproyectoex.clases.Celular;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-05T23:28:37", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Recarga.class)
public class Recarga_ { 

    public static volatile SingularAttribute<Recarga, Integer> idReca;
    public static volatile SingularAttribute<Recarga, Integer> valor;
    public static volatile SingularAttribute<Recarga, Celular> celular;
    public static volatile SingularAttribute<Recarga, String> saldo;
    public static volatile SingularAttribute<Recarga, String> megas;

}