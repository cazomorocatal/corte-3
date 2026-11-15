package util;

import java.util.Locale;

/** Utilidad para mostrar valores en pantalla de forma uniforme. */
public class Formato {

    private Formato() {
        // Clase de utilidad: no se crean objetos de ella.
    }

    /** Convierte 15000 en "$15,000". */
    public static String dinero(double valor) {
        return String.format(Locale.US, "$%,.0f", valor);
    }
}
