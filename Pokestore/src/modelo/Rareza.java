package modelo;

/** Rareza de una carta. Cada rareza multiplica el precio base. */
public enum Rareza {
    COMUN("Común", 1.0),
    NO_COMUN("No común", 1.5),
    RARA("Rara", 3.0),
    HOLOGRAFICA("Holográfica", 5.0),
    ULTRA_RARA("Ultra rara", 10.0),
    SECRETA("Secreta", 20.0);

    private final String nombreVisible;
    private final double multiplicador;

    Rareza(String nombreVisible, double multiplicador) {
        this.nombreVisible = nombreVisible;
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}
