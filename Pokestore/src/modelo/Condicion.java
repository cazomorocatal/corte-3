package modelo;

/** Estado físico de una carta. Una carta más gastada vale menos. */
public enum Condicion {
    NUEVA("Nueva", 1.00),
    CASI_NUEVA("Casi nueva", 0.85),
    USADA("Usada", 0.60),
    DANADA("Dañada", 0.30);

    private final String nombreVisible;
    private final double factor;

    Condicion(String nombreVisible, double factor) {
        this.nombreVisible = nombreVisible;
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}
