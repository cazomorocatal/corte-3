package excepciones;

/** Se lanza cuando un dato no cumple las reglas (vacío, negativo, formato incorrecto...). */
public class DatoInvalidoException extends TiendaException {
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
