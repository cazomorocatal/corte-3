package modelo;

import excepciones.DatoInvalidoException;
import util.Validador;

/** Protectores, carpetas, deckboxes... Productos que no son cartas. */
public class Accesorio extends Producto {

    private final String marca;

    public Accesorio(int id, String nombre, double precioBase, int stock,
                     String marca) throws DatoInvalidoException {
        super(id, nombre, precioBase, stock);
        this.marca = Validador.textoValido(marca, "marca");
    }

    public String getMarca() {
        return marca;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecioBase();
    }

    @Override
    public String getCategoria() {
        return "Accesorio";
    }

    @Override
    public String getDetalle() {
        return "Marca: " + marca;
    }

    @Override
    public String aLineaCSV() {
        return "ACCESORIO;" + camposBaseCSV() + ";" + marca;
    }
}
