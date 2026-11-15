package modelo;

import excepciones.DatoInvalidoException;
import util.Validador;

/**
 * Una carta suelta (por ejemplo, un Charizard holográfico).
 * Su precio final depende de la rareza y de la condición física.
 */
public class CartaIndividual extends Producto {

    private final TipoPokemon tipo;
    private final Rareza rareza;
    private Condicion condicion;
    private final String expansion;

    public CartaIndividual(int id, String nombre, double precioBase, int stock,
                           TipoPokemon tipo, Rareza rareza, Condicion condicion,
                           String expansion) throws DatoInvalidoException {
        super(id, nombre, precioBase, stock);   // el constructor de Producto valida lo común
        Validador.noNulo(tipo, "tipo de Pokémon");
        Validador.noNulo(rareza, "rareza");
        Validador.noNulo(condicion, "condición");
        this.tipo = tipo;
        this.rareza = rareza;
        this.condicion = condicion;
        this.expansion = Validador.textoValido(expansion, "expansión");
    }

    public TipoPokemon getTipo() {
        return tipo;
    }

    public Rareza getRareza() {
        return rareza;
    }

    public Condicion getCondicion() {
        return condicion;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setCondicion(Condicion condicion) throws DatoInvalidoException {
        Validador.noNulo(condicion, "condición");
        this.condicion = condicion;
    }

    /** precio final = precio base x multiplicador de rareza x factor de condición. */
    @Override
    public double calcularPrecioFinal() {
        return Math.round(getPrecioBase() * rareza.getMultiplicador() * condicion.getFactor());
    }

    @Override
    public String getCategoria() {
        return "Carta";
    }

    @Override
    public String getDetalle() {
        return tipo + ", " + rareza + ", " + condicion + ", " + expansion;
    }

    @Override
    public String aLineaCSV() {
        return "CARTA;" + camposBaseCSV() + ";" + tipo.name() + ";" + rareza.name() + ";"
                + condicion.name() + ";" + expansion;
    }
}
