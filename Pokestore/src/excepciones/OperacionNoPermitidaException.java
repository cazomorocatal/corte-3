package excepciones;

/** Se lanza cuando la operación pedida no tiene sentido en el estado actual del sistema. */
public class OperacionNoPermitidaException extends TiendaException {
    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
