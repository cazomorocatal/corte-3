package servicios;

import excepciones.DatoInvalidoException;
import excepciones.ElementoNoEncontradoException;
import excepciones.OperacionNoPermitidaException;
import modelo.Cliente;
import modelo.ClienteRegular;
import modelo.ClienteVIP;
import java.util.ArrayList;

/** Administra la lista de clientes de la tienda. */
public class GestorClientes {

    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private int siguienteId = 1;

    public int obtenerSiguienteId() {
        return siguienteId;
    }

    public void agregar(Cliente cliente) throws DatoInvalidoException {
        if (cliente == null) {
            throw new DatoInvalidoException("No se puede agregar un cliente nulo.");
        }
        for (Cliente c : clientes) {
            if (c.getId() == cliente.getId()) {
                throw new DatoInvalidoException("Ya existe un cliente con el ID " + cliente.getId() + ".");
            }
        }
        clientes.add(cliente);
        if (cliente.getId() >= siguienteId) {
            siguienteId = cliente.getId() + 1;
        }
    }

    public Cliente buscarPorId(int id) throws ElementoNoEncontradoException {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new ElementoNoEncontradoException("el cliente", id);
    }

    public ArrayList<Cliente> buscarPorNombre(String texto) {
        ArrayList<Cliente> resultado = new ArrayList<>();
        String buscado = texto.toLowerCase();
        for (Cliente c : clientes) {
            if (c.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void eliminar(int id) throws ElementoNoEncontradoException {
        Cliente cliente = buscarPorId(id);
        clientes.remove(cliente);
    }

    /**
     * Convierte un cliente Regular en VIP (o al revés). Como son clases distintas,
     * se crea un objeto nuevo con los mismos datos y se reemplaza en la lista.
     */
    public void cambiarTipo(int id, boolean hacerVIP)
            throws ElementoNoEncontradoException, OperacionNoPermitidaException, DatoInvalidoException {
        Cliente actual = buscarPorId(id);
        boolean yaEsVIP = actual instanceof ClienteVIP;
        if (hacerVIP == yaEsVIP) {
            throw new OperacionNoPermitidaException("El cliente ya es de tipo " + actual.getTipoCliente() + ".");
        }

        Cliente nuevo;
        if (hacerVIP) {
            nuevo = new ClienteVIP(actual.getId(), actual.getNombre(), actual.getTelefono());
        } else {
            nuevo = new ClienteRegular(actual.getId(), actual.getNombre(), actual.getTelefono());
        }
        clientes.set(clientes.indexOf(actual), nuevo);
    }

    public ArrayList<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    public boolean estaVacio() {
        return clientes.isEmpty();
    }

    public int cantidad() {
        return clientes.size();
    }
}
