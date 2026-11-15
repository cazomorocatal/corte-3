package modelo;

import excepciones.DatoInvalidoException;

/** Cliente normal: no recibe descuento. */
public class ClienteRegular extends Cliente {

    public ClienteRegular(int id, String nombre, String telefono) throws DatoInvalidoException {
        super(id, nombre, telefono);
    }

    @Override
    public double getPorcentajeDescuento() {
        return 0;
    }

    @Override
    public String getTipoCliente() {
        return "Regular";
    }
}
