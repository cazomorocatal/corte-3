package modelo;

import excepciones.DatoInvalidoException;

/** Cliente frecuente: recibe 10% de descuento en cada compra. */
public class ClienteVIP extends Cliente {

    private static final double DESCUENTO_VIP = 10;

    public ClienteVIP(int id, String nombre, String telefono) throws DatoInvalidoException {
        super(id, nombre, telefono);
    }

    @Override
    public double getPorcentajeDescuento() {
        return DESCUENTO_VIP;
    }

    @Override
    public String getTipoCliente() {
        return "VIP";
    }
}
