package servicios;

import excepciones.DatoInvalidoException;
import excepciones.ElementoNoEncontradoException;
import modelo.Producto;
import java.util.ArrayList;

/** Administra la lista de productos de la tienda (alta, búsqueda, baja y consultas). */
public class Inventario {

    private final ArrayList<Producto> productos = new ArrayList<>();
    private int siguienteId = 1;

    /** ID que recibirá el próximo producto registrado. */
    public int obtenerSiguienteId() {
        return siguienteId;
    }

    public void agregar(Producto producto) throws DatoInvalidoException {
        if (producto == null) {
            throw new DatoInvalidoException("No se puede agregar un producto nulo.");
        }
        for (Producto p : productos) {
            if (p.getId() == producto.getId()) {
                throw new DatoInvalidoException("Ya existe un producto con el ID " + producto.getId() + ".");
            }
        }
        productos.add(producto);
        if (producto.getId() >= siguienteId) {
            siguienteId = producto.getId() + 1;
        }
    }

    public Producto buscarPorId(int id) throws ElementoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ElementoNoEncontradoException("el producto", id);
    }

    /** Busca productos cuyo nombre contenga el texto (sin distinguir mayúsculas). */
    public ArrayList<Producto> buscarPorNombre(String texto) {
        ArrayList<Producto> resultado = new ArrayList<>();
        String buscado = texto.toLowerCase();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public ArrayList<Producto> filtrarPorCategoria(String categoria) {
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equals(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public ArrayList<Producto> productosConStockBajo(int limite) throws DatoInvalidoException {
        if (limite < 0) {
            throw new DatoInvalidoException("El límite de stock no puede ser negativo.");
        }
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getStock() <= limite) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public void eliminar(int id) throws ElementoNoEncontradoException {
        Producto producto = buscarPorId(id);   // si no existe, lanza la excepción
        productos.remove(producto);
    }

    /** Suma precio final x stock de todos los productos (cada uno calcula su propio precio). */
    public double calcularValorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.calcularPrecioFinal() * p.getStock();
        }
        return total;
    }

    public ArrayList<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }

    public boolean estaVacio() {
        return productos.isEmpty();
    }

    public int cantidad() {
        return productos.size();
    }
}
