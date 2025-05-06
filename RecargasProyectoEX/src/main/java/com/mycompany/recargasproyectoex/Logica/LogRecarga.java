
package com.mycompany.recargasproyectoex.Logica;

import com.mycompany.recargasproyectoex.clases.Celular;
import com.mycompany.recargasproyectoex.clases.Recarga;
import com.mycompany.recargasproyectoex.percistencia.RecargaJpaController;

public class LogRecarga {
    RecargaJpaController recargaJpa = new RecargaJpaController();
    // Opción 1: recarga manual
    public void recargarManual(Celular cel, int valor, int saldoParte, int megasParte) throws Exception {
        if (!cel.getEstado().equals("ACTIVO")) throw new Exception("El celular no está activo.");

        Recarga rec = new Recarga();
        rec.setValor(valor);
        rec.setSaldo(String.valueOf(saldoParte));
        rec.setMegas(String.valueOf(megasParte));
        rec.setCelular(cel);

        // Actualizamos celular
        int nuevoSaldo = Integer.parseInt(cel.getSaldo()) + saldoParte;
        int nuevasMegas = Integer.parseInt(cel.getMegas()) + (megasParte * 5);
        cel.setSaldo(String.valueOf(nuevoSaldo));
        cel.setMegas(String.valueOf(nuevasMegas));

        recargaJpa.create(rec);
    }

    // Opción 2: recarga automática (2/3 saldo, 1/3 megas)
    public void recargarAuto(Celular cel, int valor) throws Exception {
        if (!cel.getEstado().equals("ACTIVO")) throw new Exception("El celular no está activo.");

        int saldoParte = (valor * 2) / 3;
        int megasParte = (valor / 3) * 5;

        Recarga rec = new Recarga();
        rec.setValor(valor);
        rec.setSaldo(String.valueOf(saldoParte));
        rec.setMegas(String.valueOf(megasParte));
        rec.setCelular(cel);

        int nuevoSaldo = Integer.parseInt(cel.getSaldo()) + saldoParte;
        int nuevasMegas = Integer.parseInt(cel.getMegas()) + megasParte;
        cel.setSaldo(String.valueOf(nuevoSaldo));
        cel.setMegas(String.valueOf(nuevasMegas));

        recargaJpa.create(rec);
    }
}
