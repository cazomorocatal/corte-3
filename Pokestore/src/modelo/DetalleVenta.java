package modelo;

import excepciones.DatoInvalidoException;
import util.Formato;
import util.Validador;

/**
 * Una línea de una venta: "2 unidades de Charizard a $75,000 c/u".
 * Guarda una copia del nombre y del precio del momento de la venta,
 * así el historial no cambia aunque después se modifique o elimine el producto.
 */
public class DetalleVenta {

    private final int productoId;
    private final String nombreProducto;
    private int cantidad;
    private final double precioUnitario;

    public DetalleVenta(int productoId, String nombreProducto, int cantidad,
                        double precioUnitario) throws DatoInvalidoException {
        this.productoId = Validador.enteroPositivo(productoId, "ID del producto");
        this.nombreProducto = Validador.textoValido(nombreProducto, "nombre del producto");
        this.cantidad = Validador.enteroPositivo(cantidad, "cantidad");
        this.precioUnitario = Validador.precioValido(precioUnitario, "precio unitario");
    }

    public int getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void sumarCantidad(int extra) throws DatoInvalidoException {
        this.cantidad += Validador.enteroPositivo(extra, "cantidad");
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }

    @Override
    public String toString() {
        return String.format("[ID %2d] %-26s x%-3d %10s c/u = %s",
                productoId, nombreProducto, cantidad,
                Formato.dinero(precioUnitario), Formato.dinero(calcularSubtotal()));
    }
}
