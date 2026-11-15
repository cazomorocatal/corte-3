package modelo;

import excepciones.DatoInvalidoException;
import excepciones.ElementoNoEncontradoException;
import excepciones.StockInsuficienteException;
import java.util.ArrayList;

/**
 * Lista temporal de productos que un cliente quiere comprar.
 * Todavía no descuenta stock: eso ocurre solo cuando la venta se confirma.
 */
public class Carrito {

    private final ArrayList<DetalleVenta> detalles = new ArrayList<>();

    public void agregar(Producto producto, int cantidad)
            throws DatoInvalidoException, StockInsuficienteException {
        if (producto == null) {
            throw new DatoInvalidoException("Debe indicarse un producto.");
        }
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que 0.");
        }

        DetalleVenta existente = buscarDetalle(producto.getId());
        int yaEnCarrito = (existente == null) ? 0 : existente.getCantidad();
        int totalDeseado = yaEnCarrito + cantidad;

        // Se revisa el stock contando lo que ya está en el carrito
        if (totalDeseado > producto.getStock()) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), totalDeseado);
        }

        if (existente == null) {
            detalles.add(new DetalleVenta(producto.getId(), producto.getNombre(),
                    cantidad, producto.calcularPrecioFinal()));
        } else {
            existente.sumarCantidad(cantidad);
        }
    }

    public void quitar(int productoId) throws ElementoNoEncontradoException {
        DetalleVenta detalle = buscarDetalle(productoId);
        if (detalle == null) {
            throw new ElementoNoEncontradoException("un producto en el carrito", productoId);
        }
        detalles.remove(detalle);
    }

    private DetalleVenta buscarDetalle(int productoId) {
        for (DetalleVenta d : detalles) {
            if (d.getProductoId() == productoId) {
                return d;
            }
        }
        return null;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return new ArrayList<>(detalles);   // copia: nadie de afuera modifica la lista real
    }

    public double calcularSubtotal() {
        double total = 0;
        for (DetalleVenta d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }

    public boolean estaVacio() {
        return detalles.isEmpty();
    }

    public void vaciar() {
        detalles.clear();
    }
}
