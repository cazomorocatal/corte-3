package excepciones;

/** Se lanza cuando se busca un producto, cliente o venta por ID y no existe. */
public class ElementoNoEncontradoException extends TiendaException {
    public ElementoNoEncontradoException(String elemento, int id) {
        super("No se encontró " + elemento + " con ID " + id + ".");
    }
}
