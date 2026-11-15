package app;

import excepciones.OperacionNoPermitidaException;
import excepciones.TiendaException;
import modelo.Accesorio;
import modelo.CartaIndividual;
import modelo.ClienteRegular;
import modelo.ClienteVIP;
import modelo.Condicion;
import modelo.Rareza;
import modelo.SobreBooster;
import modelo.TipoPokemon;
import servicios.GestorClientes;
import servicios.Inventario;

/** Carga unos productos y clientes de prueba para poder demostrar el programa rápidamente. */
public class DatosEjemplo {

    private DatosEjemplo() {
    }

    public static void cargar(Inventario inv, GestorClientes cli) throws TiendaException {
        if (!inv.estaVacio() || !cli.estaVacio()) {
            throw new OperacionNoPermitidaException(
                    "Los datos de ejemplo solo se pueden cargar si no hay productos ni clientes.");
        }

        inv.agregar(new CartaIndividual(1, "Pikachu", 2000, 20, TipoPokemon.ELECTRICO, Rareza.COMUN, Condicion.NUEVA, "Base Set"));
        inv.agregar(new CartaIndividual(2, "Charizard", 15000, 3, TipoPokemon.FUEGO, Rareza.HOLOGRAFICA, Condicion.NUEVA, "Base Set"));
        inv.agregar(new CartaIndividual(3, "Mewtwo", 12000, 2, TipoPokemon.PSIQUICO, Rareza.ULTRA_RARA, Condicion.CASI_NUEVA, "Evoluciones"));
        inv.agregar(new CartaIndividual(4, "Bulbasaur", 1500, 15, TipoPokemon.PLANTA, Rareza.COMUN, Condicion.USADA, "Base Set"));
        inv.agregar(new CartaIndividual(5, "Squirtle", 2500, 10, TipoPokemon.AGUA, Rareza.NO_COMUN, Condicion.NUEVA, "Base Set"));
        inv.agregar(new CartaIndividual(6, "Rayquaza", 25000, 1, TipoPokemon.DRAGON, Rareza.SECRETA, Condicion.NUEVA, "Cielos Evolutivos"));
        inv.agregar(new SobreBooster(7, "Sobre Escarlata y Púrpura", 18000, 30, "Escarlata y Púrpura", 10));
        inv.agregar(new SobreBooster(8, "Sobre Evoluciones", 15000, 25, "Evoluciones", 10));
        inv.agregar(new Accesorio(9, "Protectores x100", 35000, 12, "Dragon Shield"));
        inv.agregar(new Accesorio(10, "Carpeta 9 bolsillos", 45000, 5, "Ultra Pro"));

        cli.agregar(new ClienteRegular(1, "Valentina Gómez", "3101234567"));
        cli.agregar(new ClienteVIP(2, "Andrés Pérez", "3209876543"));
        cli.agregar(new ClienteRegular(3, "Camila Rojas", "3005551234"));
    }
}
