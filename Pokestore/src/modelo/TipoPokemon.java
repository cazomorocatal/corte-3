package modelo;

/** Tipos de energía del juego de cartas Pokémon. */
public enum TipoPokemon {
    PLANTA("Planta"),
    FUEGO("Fuego"),
    AGUA("Agua"),
    ELECTRICO("Eléctrico"),
    PSIQUICO("Psíquico"),
    LUCHA("Lucha"),
    OSCURO("Oscuro"),
    METAL("Metal"),
    DRAGON("Dragón"),
    HADA("Hada"),
    INCOLORO("Incoloro");

    private final String nombreVisible;

    TipoPokemon(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}
