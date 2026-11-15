package servicios;

import excepciones.DatoInvalidoException;
import excepciones.ElementoNoEncontradoException;
import excepciones.OperacionNoPermitidaException;
import excepciones.StockInsuficienteException;
import excepciones.TiendaException;
import modelo.Carrito;
import modelo.Cliente;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Venta;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Registra las ventas y mantiene el historial.
 * Las ventas confirmadas no se editan ni se borran (son registros contables).
 */
public class GestorVentas {

    private final ArrayList<Venta> ventas = new ArrayList<>();
    private int siguienteId = 1;

    /**
     * Confirma una compra: verifica el stock de TODO el carrito primero y,
     * solo si todo está bien, descuenta las unidades y guarda la venta.
     */
    public Venta registrarVenta(Cliente cliente, Carrito carrito, Inventario inventario)
            throws TiendaException {
        if (cliente == null) {
            throw new DatoInvalidoException("Debe indicarse un cliente para la venta.");
        }
        if (carrito.estaVacio()) {
            throw new OperacionNoPermitidaException("No se puede confirmar una venta con el carrito vacío.");
        }

        // Paso 1: verificar (no se modifica nada todavía)
        for (DetalleVenta d : carrito.getDetalles()) {
            Producto producto = inventario.buscarPorId(d.getProductoId());
            if (d.getCantidad() > producto.getStock()) {
                throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), d.getCantidad());
            }
        }

        // Paso 2: ahora sí, descontar stock y armar la venta
        Venta venta = new Venta(siguienteId, LocalDate.now(), cliente.getId(),
                cliente.getNombre(), cliente.getPorcentajeDescuento());
        for (DetalleVenta d : carrito.getDetalles()) {
            inventario.buscarPorId(d.getProductoId()).reducirStock(d.getCantidad());
            venta.agregarDetalle(d);
        }

        ventas.add(venta);
        siguienteId++;
        carrito.vaciar();
        return venta;
    }

    /** Usado al cargar el historial desde el archivo CSV. */
    public void agregarVentaCargada(Venta venta) {
        ventas.add(venta);
        if (venta.getId() >= siguienteId) {
            siguienteId = venta.getId() + 1;
        }
    }

    public Venta buscarPorId(int id) throws ElementoNoEncontradoException {
        for (Venta v : ventas) {
            if (v.getId() == id) {
                return v;
            }
        }
        throw new ElementoNoEncontradoException("la venta", id);
    }

    public double calcularTotalVendido() {
        double total = 0;
        for (Venta v : ventas) {
            total += v.calcularTotal();
        }
        return total;
    }

    public ArrayList<Venta> listarTodas() {
        return new ArrayList<>(ventas);
    }

    public int cantidad() {
        return ventas.size();
    }
}
