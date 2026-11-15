package excepciones;

/** Se lanza cuando se intenta vender más unidades de las que hay disponibles. */
public class StockInsuficienteException extends TiendaException {
    public StockInsuficienteException(String producto, int disponible, int solicitado) {
        super("Stock insuficiente para '" + producto + "': disponible " + disponible
                + ", solicitado " + solicitado + ".");
    }
}
