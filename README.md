# SistemRecargas
Logica que el programa trabaja:

LogCelular:

```java
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

```

LogCliente:

```java
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
```

LogRecarga:

```java
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
```

Interfaces del proyecto:

Ingresar Celular:

![image](https://github.com/user-attachments/assets/bc4f3429-aa65-4c21-baf6-cf72d3128b38)

Codigo:

```java
    private void BTN_RegistrarActionPerformed(java.awt.event.ActionEvent evt) {                                              
        try {
            String numero = TXT_Numero.getText().trim();
            String estadoSeleccionado = (String) CMB_Estado.getSelectedItem();
            String saldo = TXT_Saldo.getText().trim();
            String megas = TXT_Megas.getText().trim();
            String cedulaCliente = TXT_CedulaCliente.getText().trim();

            if (numero.isEmpty() || estadoSeleccionado == null || saldo.isEmpty() || megas.isEmpty() || cedulaCliente.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LogCliente logCliente = new LogCliente();
            Cliente cliente = logCliente.buscarPorCedula(cedulaCliente);

            if (cliente == null) {
                JOptionPane.showMessageDialog(null, "No se encontró un cliente con esa cédula.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EstadoCelular estado = EstadoCelular.valueOf(estadoSeleccionado);

            LogCelular logCelular = new LogCelular();
            logCelular.ingresarNuevoCelular(numero, saldo, megas, estado, cliente);

            JOptionPane.showMessageDialog(null, "Celular registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            TXT_Numero.setText("");
            CMB_Estado.setSelectedIndex(0);
            TXT_Saldo.setText("");
            TXT_Megas.setText("");
            TXT_CedulaCliente.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                             

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new RecargaManual().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new RegistrarCliente().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }  
```
Recarga Manual:

Interfaz:
![image](https://github.com/user-attachments/assets/46ecaa34-1ec9-471d-ae43-61376ec8274f)

Codigo:

```java
private void BTN_RecargarManualActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        try {
            String numero = TXT_NumeroCelular.getText().trim();
            String valorTotalStr = TXT_Valor.getText().trim();
            String saldoParteStr = TXT_SaldoParte.getText().trim();
            String megasParteStr = TXT_MegasParte.getText().trim();

            if (numero.isEmpty() || valorTotalStr.isEmpty() || saldoParteStr.isEmpty() || megasParteStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int valorTotal = Integer.parseInt(valorTotalStr);
            int saldoParte = Integer.parseInt(saldoParteStr);
            int megasParte = Integer.parseInt(megasParteStr);

            if (saldoParte + megasParte != valorTotal) {
                JOptionPane.showMessageDialog(null, "La suma del saldo y megas no coincide con el valor total.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LogCelular logCelular = new LogCelular();
            Celular cel = logCelular.buscarPorNumero(numero);

            if (cel == null) {
                JOptionPane.showMessageDialog(null, "No se encontró el celular.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!cel.getEstado().equals(EstadoCelular.ACTIVO)) {
                JOptionPane.showMessageDialog(null, "El celular no está activo. No se puede hacer la recarga.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LogRecarga logRecarga = new LogRecarga();
            logRecarga.recargarManual(cel, valorTotal, saldoParte, megasParte);

            JOptionPane.showMessageDialog(null, "Recarga realizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            TXT_NumeroCelular.setText("");
            TXT_Valor.setText("");
            TXT_SaldoParte.setText("");
            TXT_MegasParte.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Los valores ingresados deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                  

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new RegistrarCliente().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new Recarga_automatica().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }  
```

Recarga Automatica

Interfaz:

![image](https://github.com/user-attachments/assets/c9143e8b-25ac-48c8-a87f-a2bf1fead13a)

Codigo:

```java
private void BTN_RecargarAutoActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        try {
            String numero = TXT_NumeroCelularAuto.getText().trim();
            String valorStr = TXT_ValorAuto.getText().trim();

            if (numero.isEmpty() || valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int valor = Integer.parseInt(valorStr);
            int saldoParte = (valor * 2) / 3;
            int megasParte = valor / 3;

            LogCelular logCelular = new LogCelular();
            Celular cel = logCelular.buscarPorNumero(numero);

            if (cel == null) {
                JOptionPane.showMessageDialog(null, "No se encontró el celular.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!cel.getEstado().equals(EstadoCelular.ACTIVO)) {
                JOptionPane.showMessageDialog(null, "El celular no está activo. No se puede hacer la recarga.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LogRecarga logRecarga = new LogRecarga();
            logRecarga.recargarManual(cel, valor, saldoParte, megasParte);

            JOptionPane.showMessageDialog(null,
                    "Recarga realizada exitosamente.\n"
                    + "Saldo: $" + saldoParte + "\n"
                    + "Megas: " + (megasParte * 5) + " GB",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            TXT_NumeroCelularAuto.setText("");
            TXT_ValorAuto.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "El valor ingresado debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new RecargaManual().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }  
```

Registrar Cliente

Interfaz grafica:

![image](https://github.com/user-attachments/assets/23153ab6-d99f-4ac3-a22e-119ef5d3db0a)

codigo:

```java
private void BTN_RegistrarClienteActionPerformed(java.awt.event.ActionEvent evt) {                                                     
         try {
            String cedula = TXT_Cedula.getText().trim();
            String nombres = TXT_Nombres.getText().trim();
            String apellidos = TXT_Apellidos.getText().trim();

            if (cedula.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LogCliente logCliente = new LogCliente();
            logCliente.registrarCliente(cedula, nombres, apellidos);

            JOptionPane.showMessageDialog(null, "Cliente registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            TXT_Cedula.setText("");
            TXT_Nombres.setText("");
            TXT_Apellidos.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                    

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new IngresarCelular().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        new RecargaManual().setVisible(true);
        this.dispose(); // Cierra la ventana actual
    }
```

