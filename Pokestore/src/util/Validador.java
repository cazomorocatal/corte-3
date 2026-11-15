package util;

import excepciones.DatoInvalidoException;

/**
 * Reglas de validación reutilizables. Tanto el modelo como la consola
 * las usan, así las reglas están escritas en un solo lugar.
 */
public class Validador {

    private static final int LARGO_MAXIMO_TEXTO = 50;
    private static final double PRECIO_MAXIMO = 100_000_000;

    private Validador() {
    }

    /** Texto obligatorio: no vacío, máximo 50 caracteres y sin ';' (separador del CSV). */
    public static String textoValido(String valor, String campo) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede estar vacío.");
        }
        String limpio = valor.trim();
        if (limpio.length() > LARGO_MAXIMO_TEXTO) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede superar "
                    + LARGO_MAXIMO_TEXTO + " caracteres.");
        }
        if (limpio.contains(";")) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede contener el carácter ';'.");
        }
        return limpio;
    }

    /** Precio: número real mayor que cero y razonable. */
    public static double precioValido(double valor, String campo) throws DatoInvalidoException {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
            throw new DatoInvalidoException("El campo '" + campo + "' debe ser un número mayor que 0.");
        }
        if (valor > PRECIO_MAXIMO) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede superar "
                    + Formato.dinero(PRECIO_MAXIMO) + ".");
        }
        return valor;
    }

    public static int enteroPositivo(int valor, String campo) throws DatoInvalidoException {
        if (valor <= 0) {
            throw new DatoInvalidoException("El campo '" + campo + "' debe ser mayor que 0.");
        }
        return valor;
    }

    public static int enteroNoNegativo(int valor, String campo) throws DatoInvalidoException {
        if (valor < 0) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede ser negativo.");
        }
        return valor;
    }

    /** Teléfono: solo dígitos, entre 7 y 10 caracteres. */
    public static String telefonoValido(String valor) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("El teléfono no puede estar vacío.");
        }
        String limpio = valor.trim();
        if (!limpio.matches("\\d{7,10}")) {
            throw new DatoInvalidoException("El teléfono debe tener solo números (entre 7 y 10 dígitos).");
        }
        return limpio;
    }

    /** Evita guardar objetos nulos (por ejemplo, una rareza sin elegir). */
    public static void noNulo(Object valor, String campo) throws DatoInvalidoException {
        if (valor == null) {
            throw new DatoInvalidoException("El campo '" + campo + "' es obligatorio.");
        }
    }
}
