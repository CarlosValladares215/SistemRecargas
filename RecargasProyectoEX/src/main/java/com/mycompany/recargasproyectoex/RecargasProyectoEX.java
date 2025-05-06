/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.recargasproyectoex;

import com.mycompany.recargasproyectoex.Interfaz.IngresarCelular;
import javax.swing.SwingUtilities;

/**
 *
 * @author vcarl
 */
public class RecargasProyectoEX {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IngresarCelular frame = new IngresarCelular();
            frame.setVisible(true);
        });
    }
}
