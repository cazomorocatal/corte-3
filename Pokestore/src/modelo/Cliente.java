package modelo;

import excepciones.DatoInvalidoException;
import util.Validador;

/**
 * Clase abstracta: un cliente de la tienda. Según su tipo (Regular o VIP)
 * recibe un porcentaje de descuento diferente.
 */
public abstract class Cliente implements Persistible {

    private final int id;
    private String nombre;
    private String telefono;

    protected Cliente(int id, String nombre, String telefono) throws DatoInvalidoException {
        this.id = Validador.enteroPositivo(id, "ID");
        this.nombre = Validador.textoValido(nombre, "nombre");
        this.telefono = Validador.telefonoValido(telefono);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        this.nombre = Validador.textoValido(nombre, "nombre");
    }

    public void setTelefono(String telefono) throws DatoInvalidoException {
        this.telefono = Validador.telefonoValido(telefono);
    }

    /** Porcentaje de descuento (0 a 100). Cada tipo de cliente lo define. */
    public abstract double getPorcentajeDescuento();

    /** Nombre del tipo de cliente ("Regular" o "VIP"). */
    public abstract String getTipoCliente();

    /** Este método NO se sobrescribe: usa getPorcentajeDescuento(), que sí es polimórfico. */
    public double calcularDescuento(double subtotal) {
        return subtotal * getPorcentajeDescuento() / 100.0;
    }

    @Override
    public String aLineaCSV() {
        return getTipoCliente().toUpperCase() + ";" + id + ";" + nombre + ";" + telefono;
    }

    @Override
    public String toString() {
        return String.format("[ID %2d] %-8s | %-24s | Tel: %s | Descuento: %.0f%%",
                id, getTipoCliente(), nombre, telefono, getPorcentajeDescuento());
    }
}
