package excepciones;

/**
 * Excepción base de la aplicación. Todas las excepciones propias heredan de ella,
 * así el menú puede capturar cualquier error de la tienda con un solo catch.
 */
public class TiendaException extends Exception {
    public TiendaException(String mensaje) {
        super(mensaje);
    }
}
