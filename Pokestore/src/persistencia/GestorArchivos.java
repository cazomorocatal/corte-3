package persistencia;

import excepciones.DatoInvalidoException;
import modelo.Accesorio;
import modelo.CartaIndividual;
import modelo.Cliente;
import modelo.ClienteRegular;
import modelo.ClienteVIP;
import modelo.Condicion;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Rareza;
import modelo.SobreBooster;
import modelo.TipoPokemon;
import modelo.Venta;
import servicios.GestorClientes;
import servicios.GestorVentas;
import servicios.Inventario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Guarda y recupera la información en archivos CSV (texto separado por ';')
 * dentro de la carpeta "datos". Si una línea está dañada, se avisa y se sigue
 * con las demás en lugar de cerrar el programa.
 */
public class GestorArchivos {

    private static final String CARPETA = "datos";
    private static final Path ARCHIVO_PRODUCTOS = Paths.get(CARPETA, "productos.csv");
    private static final Path ARCHIVO_CLIENTES = Paths.get(CARPETA, "clientes.csv");
    private static final Path ARCHIVO_VENTAS = Paths.get(CARPETA, "ventas.csv");

    // ================= GUARDAR =================

    public void guardarTodo(Inventario inventario, GestorClientes clientes, GestorVentas ventas)
            throws IOException {
        Files.createDirectories(Paths.get(CARPETA));
        guardarProductos(inventario);
        guardarClientes(clientes);
        guardarVentas(ventas);
    }

    private void guardarProductos(Inventario inventario) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PRODUCTOS, StandardCharsets.UTF_8)) {
            for (Producto p : inventario.listarTodos()) {
                escritor.write(p.aLineaCSV());   // polimorfismo: cada tipo arma su propia línea
                escritor.newLine();
            }
        }
    }

    private void guardarClientes(GestorClientes clientes) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_CLIENTES, StandardCharsets.UTF_8)) {
            for (Cliente c : clientes.listarTodos()) {
                escritor.write(c.aLineaCSV());
                escritor.newLine();
            }
        }
    }

    /** Se escribe una línea por cada producto vendido; las líneas de una venta comparten el ID. */
    private void guardarVentas(GestorVentas ventas) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_VENTAS, StandardCharsets.UTF_8)) {
            for (Venta v : ventas.listarTodas()) {
                for (DetalleVenta d : v.getDetalles()) {
                    escritor.write(v.getId() + ";" + v.getFecha() + ";" + v.getClienteId() + ";"
                            + v.getNombreCliente() + ";" + v.getPorcentajeDescuento() + ";"
                            + d.getProductoId() + ";" + d.getNombreProducto() + ";"
                            + d.getCantidad() + ";" + d.getPrecioUnitario());
                    escritor.newLine();
                }
            }
        }
    }

    // ================= CARGAR =================

    /** Carga los tres archivos. Devuelve un mensaje-resumen para mostrar al usuario. */
    public String cargarTodo(Inventario inventario, GestorClientes clientes, GestorVentas ventas)
            throws IOException {
        int p = cargarProductos(inventario);
        int c = cargarClientes(clientes);
        int v = cargarVentas(ventas);
        return "Datos cargados: " + p + " productos, " + c + " clientes, " + v + " ventas.";
    }

    private int cargarProductos(Inventario inventario) throws IOException {
        if (!Files.exists(ARCHIVO_PRODUCTOS)) {
            return 0;   // primera ejecución: todavía no hay archivo
        }
        int cargados = 0;
        int numeroLinea = 0;
        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_PRODUCTOS, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                try {
                    inventario.agregar(convertirAProducto(linea));
                    cargados++;
                } catch (DatoInvalidoException e) {
                    System.out.println("  Aviso: línea " + numeroLinea + " de productos.csv ignorada ("
                            + e.getMessage() + ")");
                }
            }
        }
        return cargados;
    }

    private Producto convertirAProducto(String linea) throws DatoInvalidoException {
        String[] p = linea.split(";", -1);
        try {
            int id = Integer.parseInt(p[1]);
            String nombre = p[2];
            double precioBase = Double.parseDouble(p[3]);
            int stock = Integer.parseInt(p[4]);

            switch (p[0]) {
                case "CARTA":
                    return new CartaIndividual(id, nombre, precioBase, stock,
                            TipoPokemon.valueOf(p[5]), Rareza.valueOf(p[6]),
                            Condicion.valueOf(p[7]), p[8]);
                case "SOBRE":
                    return new SobreBooster(id, nombre, precioBase, stock, p[5], Integer.parseInt(p[6]));
                case "ACCESORIO":
                    return new Accesorio(id, nombre, precioBase, stock, p[5]);
                default:
                    throw new DatoInvalidoException("tipo de producto desconocido '" + p[0] + "'");
            }
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // IllegalArgumentException cubre números mal escritos y valores de enum inexistentes
            throw new DatoInvalidoException("formato incorrecto: " + linea);
        }
    }

    private int cargarClientes(GestorClientes clientes) throws IOException {
        if (!Files.exists(ARCHIVO_CLIENTES)) {
            return 0;
        }
        int cargados = 0;
        int numeroLinea = 0;
        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_CLIENTES, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                try {
                    clientes.agregar(convertirACliente(linea));
                    cargados++;
                } catch (DatoInvalidoException e) {
                    System.out.println("  Aviso: línea " + numeroLinea + " de clientes.csv ignorada ("
                            + e.getMessage() + ")");
                }
            }
        }
        return cargados;
    }

    private Cliente convertirACliente(String linea) throws DatoInvalidoException {
        String[] p = linea.split(";", -1);
        try {
            int id = Integer.parseInt(p[1]);
            switch (p[0]) {
                case "REGULAR":
                    return new ClienteRegular(id, p[2], p[3]);
                case "VIP":
                    return new ClienteVIP(id, p[2], p[3]);
                default:
                    throw new DatoInvalidoException("tipo de cliente desconocido '" + p[0] + "'");
            }
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new DatoInvalidoException("formato incorrecto: " + linea);
        }
    }

    private int cargarVentas(GestorVentas gestorVentas) throws IOException {
        if (!Files.exists(ARCHIVO_VENTAS)) {
            return 0;
        }
        int ventasCargadas = 0;
        int numeroLinea = 0;
        Venta ventaActual = null;   // la venta a la que pertenece la línea que se está leyendo

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_VENTAS, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] p = linea.split(";", -1);
                    int idVenta = Integer.parseInt(p[0]);

                    // Si cambia el ID, empieza una venta nueva
                    if (ventaActual == null || ventaActual.getId() != idVenta) {
                        ventaActual = new Venta(idVenta, LocalDate.parse(p[1]), Integer.parseInt(p[2]),
                                p[3], Double.parseDouble(p[4]));
                        gestorVentas.agregarVentaCargada(ventaActual);
                        ventasCargadas++;
                    }
                    ventaActual.agregarDetalle(new DetalleVenta(Integer.parseInt(p[5]), p[6],
                            Integer.parseInt(p[7]), Double.parseDouble(p[8])));
                } catch (DatoInvalidoException e) {
                    System.out.println("  Aviso: línea " + numeroLinea + " de ventas.csv ignorada ("
                            + e.getMessage() + ")");
                } catch (IllegalArgumentException | DateTimeException | ArrayIndexOutOfBoundsException e) {
                    // número mal escrito, fecha inválida o faltan columnas
                    System.out.println("  Aviso: línea " + numeroLinea + " de ventas.csv ignorada (formato incorrecto: "
                            + linea + ")");
                }
            }
        }
        return ventasCargadas;
    }
}
