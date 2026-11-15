package modelo;

import excepciones.DatoInvalidoException;
import util.Validador;

/** Un sobre cerrado con cartas sorpresa. Su precio es fijo: el precio base. */
public class SobreBooster extends Producto {

    private static final int MAXIMO_CARTAS = 20;

    private final String expansion;
    private final int cartasPorSobre;

    public SobreBooster(int id, String nombre, double precioBase, int stock,
                        String expansion, int cartasPorSobre) throws DatoInvalidoException {
        super(id, nombre, precioBase, stock);
        this.expansion = Validador.textoValido(expansion, "expansión");
        Validador.enteroPositivo(cartasPorSobre, "cartas por sobre");
        if (cartasPorSobre > MAXIMO_CARTAS) {
            throw new DatoInvalidoException("Un sobre no puede tener más de " + MAXIMO_CARTAS + " cartas.");
        }
        this.cartasPorSobre = cartasPorSobre;
    }

    public String getExpansion() {
        return expansion;
    }

    public int getCartasPorSobre() {
        return cartasPorSobre;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecioBase();
    }

    @Override
    public String getCategoria() {
        return "Sobre";
    }

    @Override
    public String getDetalle() {
        return expansion + ", " + cartasPorSobre + " cartas";
    }

    @Override
    public String aLineaCSV() {
        return "SOBRE;" + camposBaseCSV() + ";" + expansion + ";" + cartasPorSobre;
    }
}
