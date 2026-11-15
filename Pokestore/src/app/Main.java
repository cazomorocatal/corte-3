package app;

import persistencia.GestorArchivos;
import servicios.GestorClientes;
import servicios.GestorVentas;
import servicios.Inventario;
import vista.VentanaPrincipal;

import javax.swing.*;

/** Punto de entrada: inicializa los servicios y lanza la interfaz gráfica de PokéStore. */
public class Main {

    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        GestorClientes clientes = new GestorClientes();
        GestorVentas ventas = new GestorVentas();
        GestorArchivos archivos = new GestorArchivos();

        // Aplicar aspecto nativo del sistema operativo para que luzca moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Lanzar la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(inventario, clientes, ventas, archivos);
            ventana.setVisible(true);
        });
    }
}