package modelo;

import excepciones.DatoInvalidoException;
import excepciones.StockInsuficienteException;
import util.Formato;
import util.Validador;

/**
 * Clase abstracta: representa "cualquier cosa que la tienda vende".
 * No se puede crear un Producto "genérico"; solo cartas, sobres o accesorios.
 *
 * Los atributos son privados (encapsulamiento): solo se cambian mediante
 * métodos que validan el dato antes de aceptarlo.
 */
public abstract class Producto implements Persistible {

    private final int id;          // el ID nunca cambia, por eso no tiene setter
    private String nombre;
    private double precioBase;
    private int stock;

    protected Producto(int id, String nombre, double precioBase, int stock) throws DatoInvalidoException {
        this.id = Validador.enteroPositivo(id, "ID");
        this.nombre = Validador.textoValido(nombre, "nombre");
        this.precioBase = Validador.precioValido(precioBase, "precio base");
        this.stock = Validador.enteroNoNegativo(stock, "stock");
    }

    // ---------- Consultas ----------
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getStock() {
        return stock;
    }

    // ---------- Modificaciones controladas ----------
    public void setNombre(String nombre) throws DatoInvalidoException {
        this.nombre = Validador.textoValido(nombre, "nombre");
    }

    public void setPrecioBase(double precioBase) throws DatoInvalidoException {
        this.precioBase = Validador.precioValido(precioBase, "precio base");
    }

    public void aumentarStock(int cantidad) throws DatoInvalidoException {
        Validador.enteroPositivo(cantidad, "cantidad a reabastecer");
        this.stock += cantidad;
    }

    public void reducirStock(int cantidad) throws DatoInvalidoException, StockInsuficienteException {
        Validador.enteroPositivo(cantidad, "cantidad");
        if (cantidad > stock) {
            throw new StockInsuficienteException(nombre, stock, cantidad);
        }
        this.stock -= cantidad;
    }

    // ---------- Métodos abstractos: cada subclase los implementa a su manera ----------

    /** Precio que realmente paga el cliente por una unidad. */
    public abstract double calcularPrecioFinal();

    /** Nombre de la categoría ("Carta", "Sobre", "Accesorio"). */
    public abstract String getCategoria();

    /** Texto con los datos propios de cada tipo de producto. */
    public abstract String getDetalle();

    // ---------- Métodos comunes ----------

    /** Parte de la línea CSV que comparten todos los productos. */
    protected String camposBaseCSV() {
        return id + ";" + nombre + ";" + precioBase + ";" + stock;
    }

    @Override
    public String toString() {
        // Aquí se ve el polimorfismo: getCategoria(), getDetalle() y calcularPrecioFinal()
        // ejecutan el código de la subclase real (Carta, Sobre o Accesorio).
        return String.format("[ID %2d] %-9s | %-26s | %s | Precio: %s | Stock: %d",
                id, getCategoria(), nombre, getDetalle(), Formato.dinero(calcularPrecioFinal()), stock);
    }
}
