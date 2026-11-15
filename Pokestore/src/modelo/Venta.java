package modelo;

import excepciones.DatoInvalidoException;
import util.Formato;
import util.Validador;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Una venta ya realizada. Guarda el nombre del cliente y el porcentaje de
 * descuento del momento, para que el historial no cambie con el tiempo.
 */
public class Venta {

    private final int id;
    private final LocalDate fecha;
    private final int clienteId;
    private final String nombreCliente;
    private final double porcentajeDescuento;
    private final ArrayList<DetalleVenta> detalles = new ArrayList<>();

    public Venta(int id, LocalDate fecha, int clienteId, String nombreCliente,
                 double porcentajeDescuento) throws DatoInvalidoException {
        this.id = Validador.enteroPositivo(id, "ID de la venta");
        Validador.noNulo(fecha, "fecha");
        this.fecha = fecha;
        this.clienteId = Validador.enteroPositivo(clienteId, "ID del cliente");
        this.nombreCliente = Validador.textoValido(nombreCliente, "nombre del cliente");
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new DatoInvalidoException("El descuento debe estar entre 0 y 100.");
        }
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public void agregarDetalle(DetalleVenta detalle) throws DatoInvalidoException {
        Validador.noNulo(detalle, "detalle de venta");
        detalles.add(detalle);
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getClienteId() {
        return clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleVenta d : detalles) {
            subtotal += d.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularDescuento() {
        return calcularSubtotal() * porcentajeDescuento / 100.0;
    }

    public double calcularTotal() {
        return calcularSubtotal() - calcularDescuento();
    }

    /** Texto tipo recibo para mostrar en pantalla. */
    public String generarFactura() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== FACTURA #").append(id).append(" ==========\n");
        sb.append("Fecha:   ").append(fecha).append("\n");
        sb.append("Cliente: ").append(nombreCliente).append(" (ID ").append(clienteId).append(")\n");
        sb.append("----------------------------------------\n");
        for (DetalleVenta d : detalles) {
            sb.append(d).append("\n");
        }
        sb.append("----------------------------------------\n");
        sb.append("Subtotal:  ").append(Formato.dinero(calcularSubtotal())).append("\n");
        sb.append(String.format("Descuento (%.0f%%): -%s%n", porcentajeDescuento,
                Formato.dinero(calcularDescuento())));
        sb.append("TOTAL:     ").append(Formato.dinero(calcularTotal())).append("\n");
        sb.append("========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("[Venta %d] %s | Cliente: %-20s | Productos: %d | Total: %s",
                id, fecha, nombreCliente, detalles.size(), Formato.dinero(calcularTotal()));
    }
}
