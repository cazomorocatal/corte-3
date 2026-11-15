package vista;

import app.DatosEjemplo;
import excepciones.TiendaException;
import modelo.*;
import persistencia.GestorArchivos;
import servicios.GestorClientes;
import servicios.GestorVentas;
import servicios.Inventario;
import util.Formato;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
    private final Inventario inv;
    private final GestorClientes cli;
    private final GestorVentas ven;
    private final GestorArchivos archivos;
    private final Carrito carrito = new Carrito();

    private JTable tProd, tCli, tCarrito, tHistorial;
    private DefaultTableModel mProd, mCli, mCarrito, mHistorial;
    private JComboBox<Cliente> cbCli;
    private JComboBox<Producto> cbProd;
    private JSpinner spCant;
    private JLabel lblTotal;
    
    private final Color ROJO_POKE = new Color(227, 53, 13);

    public VentanaPrincipal(Inventario inv, GestorClientes cli, GestorVentas ven, GestorArchivos archivos) {
        this.inv = inv; this.cli = cli; this.ven = ven; this.archivos = archivos;
        setTitle("PokéStore - Sistema Pokémon");
        setSize(1024, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try { archivos.cargarTodo(inv, cli, ven); } catch (IOException ignored) {}

        // Añadimos el encabezado perfectamente centrado en la parte superior
        add(panelEncabezado(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Productos", panelProductos());
        tabs.add("Clientes", panelClientes());
        tabs.add("Punto de Venta (POS)", panelPOS());
        tabs.add("Historial Ventas", panelHistorial());
        tabs.add("Sistema", panelSistema());

        add(tabs, BorderLayout.CENTER);
        actualizarTodo();
    }

    private JPanel panelEncabezado() {
        // GridBagLayout centra el contenido exactamente a la mitad (horizontal y vertical)
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        try {
            // Carga la imagen desde la misma carpeta 'vista'
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("pokemon-pokeball-legue-seeklogo.png"));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            
            JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
            header.add(lblLogo);
        } catch (Exception e) {
            // Respaldo por si hay algún fallo con el archivo
            JLabel lblFallback = new JLabel("🔴 PokéStore ⚪");
            lblFallback.setFont(new Font("Arial", Font.BOLD, 16));
            header.add(lblFallback);
        }
        
        header.setPreferredSize(new Dimension(1024, 55));
        return header;
    }

    private JPanel panelProductos() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        mProd = new DefaultTableModel(new String[]{"ID", "Categoría", "Nombre", "Precio Base", "Precio Final", "Stock", "Detalles / Atributos"}, 0);
        tProd = new JTable(mProd);
        tProd.setRowHeight(24);
        
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bNuevo = new JButton("Nuevo Producto");
        JButton bElim = new JButton("Eliminar Seleccionado");
        
        bNuevo.setBackground(ROJO_POKE); 
        bNuevo.setForeground(Color.BLACK);
        bNuevo.setFocusPainted(false);
        
        bNuevo.addActionListener(e -> registrarProducto());
        bElim.addActionListener(e -> {
            try { 
                int row = tProd.getSelectedRow();
                if(row == -1) throw new Exception("Selecciona un producto de la tabla.");
                inv.eliminar((int) mProd.getValueAt(row, 0)); 
                actualizarTodo(); 
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        bot.add(bNuevo); bot.add(bElim);

        p.add(new JScrollPane(tProd), BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);
        return p;
    }

    private JPanel panelClientes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        mCli = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono", "Tipo", "Dto"}, 0);
        tCli = new JTable(mCli);
        tCli.setRowHeight(24);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bNuevo = new JButton("Nuevo Cliente");
        JButton bTipo = new JButton("Cambiar VIP/Regular");
        JButton bElim = new JButton("Eliminar");
        
        bNuevo.setBackground(ROJO_POKE); 
        bNuevo.setForeground(Color.BLACK);
        bNuevo.setFocusPainted(false);

        bNuevo.addActionListener(e -> {
            try {
                String nom = JOptionPane.showInputDialog("Nombre:");
                if (nom == null || nom.isBlank()) return;
                String tel = JOptionPane.showInputDialog("Teléfono:");
                boolean vip = JOptionPane.showConfirmDialog(this, "¿Es VIP?") == 0;
                cli.agregar(vip ? new ClienteVIP(cli.obtenerSiguienteId(), nom, tel) : new ClienteRegular(cli.obtenerSiguienteId(), nom, tel));
                actualizarTodo();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        
        bTipo.addActionListener(e -> {
            try {
                int row = tCli.getSelectedRow();
                if(row == -1) throw new Exception("Selecciona un cliente.");
                int id = (int) mCli.getValueAt(row, 0);
                boolean hacerVip = mCli.getValueAt(row, 3).equals("Regular");
                cli.cambiarTipo(id, hacerVip);
                actualizarTodo();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        bElim.addActionListener(e -> {
            try {
                int row = tCli.getSelectedRow();
                if(row == -1) throw new Exception("Selecciona un cliente.");
                cli.eliminar((int) mCli.getValueAt(row, 0));
                actualizarTodo();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        bot.add(bNuevo); bot.add(bTipo); bot.add(bElim);
        p.add(new JScrollPane(tCli), BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);
        return p;
    }

    private JPanel panelPOS() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel top = new JPanel(new GridBagLayout());
        top.setBorder(BorderFactory.createTitledBorder("Configuración de Venta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        cbCli = new JComboBox<>();
        cbProd = new JComboBox<>();
        spCant = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JButton bAdd = new JButton("Agregar al Carrito");
        bAdd.setBackground(ROJO_POKE); 
        bAdd.setForeground(Color.BLACK);
        bAdd.setFocusPainted(false);

        bAdd.addActionListener(e -> {
            try {
                Producto pSel = (Producto) cbProd.getSelectedItem();
                if (pSel == null) throw new Exception("Selecciona un producto.");
                carrito.agregar(pSel, (int) spCant.getValue()); 
                actualizarCarrito();
            } catch (TiendaException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
              catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        gbc.gridx = 0; gbc.gridy = 0; 
        JLabel lblCli = new JLabel("Cliente:"); lblCli.setForeground(Color.BLACK); top.add(lblCli, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; top.add(cbCli, gbc);

        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0; gbc.gridy = 1; 
        JLabel lblProd = new JLabel("Producto:"); lblProd.setForeground(Color.BLACK); top.add(lblProd, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; top.add(cbProd, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; 
        JLabel lblCant = new JLabel(" Cant:"); lblCant.setForeground(Color.BLACK); top.add(lblCant, gbc);
        
        gbc.gridx = 3; 
        JPanel pCantBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pCantBtn.add(spCant);
        pCantBtn.add(bAdd);
        top.add(pCantBtn, gbc);

        mCarrito = new DefaultTableModel(new String[]{"ID", "Nombre", "Cant", "Subtotal"}, 0);
        tCarrito = new JTable(mCarrito);
        tCarrito.setRowHeight(24);

        JPanel bot = new JPanel(new BorderLayout());
        bot.setBorder(new EmptyBorder(5, 0, 0, 0));
        lblTotal = new JLabel(" Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(Color.BLACK);
        
        JButton bProc = new JButton("Procesar Venta");
        bProc.setBackground(new Color(40, 167, 69)); 
        bProc.setForeground(Color.BLACK);
        bProc.setFocusPainted(false);
        
        bProc.addActionListener(e -> {
            try {
                Cliente cSel = (Cliente) cbCli.getSelectedItem();
                if(cSel == null) throw new Exception("Selecciona un cliente.");
                ven.registrarVenta(cSel, carrito, inv);
                actualizarTodo();
                JOptionPane.showMessageDialog(this, "¡Venta completada con éxito!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        
        bot.add(lblTotal, BorderLayout.WEST); 
        bot.add(bProc, BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(tCarrito), BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);
        return p;
    }

    private JPanel panelHistorial() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        mHistorial = new DefaultTableModel(new String[]{"ID Venta", "Fecha", "Cliente", "Total"}, 0);
        tHistorial = new JTable(mHistorial);
        tHistorial.setRowHeight(24);
        p.add(new JScrollPane(tHistorial), BorderLayout.CENTER);
        return p;
    }

    private JPanel panelSistema() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JButton bEj = new JButton("Cargar Ejemplos");
        JButton bGuardar = new JButton("Guardar Archivos CSV");
        
        bEj.setBackground(ROJO_POKE); 
        bEj.setForeground(Color.BLACK);
        bEj.setFocusPainted(false);
        
        bGuardar.setBackground(ROJO_POKE); 
        bGuardar.setForeground(Color.BLACK);
        bGuardar.setFocusPainted(false);

        bEj.addActionListener(e -> { 
            try { DatosEjemplo.cargar(inv, cli); actualizarTodo(); JOptionPane.showMessageDialog(this, "¡Ejemplos cargados!"); } 
            catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        bGuardar.addActionListener(e -> { 
            try { archivos.guardarTodo(inv, cli, ven); JOptionPane.showMessageDialog(this, "Guardado con éxito"); } 
            catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        p.add(bEj); p.add(bGuardar);
        return p;
    }

    private void registrarProducto() {
        try {
            String[] opciones = {"Carta Individual", "Sobre Booster", "Accesorio"};
            String tipoSel = (String) JOptionPane.showInputDialog(this, "Selecciona el tipo de producto a registrar:", 
                    "Nuevo Producto", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (tipoSel == null) return;

            String nom = JOptionPane.showInputDialog("Nombre del producto:");
            if (nom == null || nom.isBlank()) return;
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio Base:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock inicial:"));
            int nuevoId = inv.obtenerSiguienteId();

            if (tipoSel.equals("Carta Individual")) {
                TipoPokemon tipo = (TipoPokemon) JOptionPane.showInputDialog(this, "Tipo de Pokémon:", 
                        "Carta", JOptionPane.QUESTION_MESSAGE, null, TipoPokemon.values(), TipoPokemon.ELECTRICO);
                if (tipo == null) return;
                Rareza rareza = (Rareza) JOptionPane.showInputDialog(this, "Rareza:", 
                        "Carta", JOptionPane.QUESTION_MESSAGE, null, Rareza.values(), Rareza.COMUN);
                if (rareza == null) return;
                Condicion condicion = (Condicion) JOptionPane.showInputDialog(this, "Condición:", 
                        "Carta", JOptionPane.QUESTION_MESSAGE, null, Condicion.values(), Condicion.NUEVA);
                if (condicion == null) return;
                String expansion = JOptionPane.showInputDialog("Expansión (ej. Base Set):");
                if (expansion == null || expansion.isBlank()) return;

                inv.agregar(new CartaIndividual(nuevoId, nom, precio, stock, tipo, rareza, condicion, expansion));

            } else if (tipoSel.equals("Sobre Booster")) {
                String expansion = JOptionPane.showInputDialog("Expansión del sobre:");
                if (expansion == null || expansion.isBlank()) return;
                int cartas = Integer.parseInt(JOptionPane.showInputDialog("Cartas por sobre (1-20):"));

                inv.agregar(new SobreBooster(nuevoId, nom, precio, stock, expansion, cartas));

            } else {
                String marca = JOptionPane.showInputDialog("Marca del accesorio:");
                if (marca == null || marca.isBlank()) return;

                inv.agregar(new Accesorio(nuevoId, nom, precio, stock, marca));
            }

            actualizarTodo();
            JOptionPane.showMessageDialog(this, "¡Producto registrado con éxito!");
        } catch (Exception ex) { 
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage()); 
        }
    }

    private void actualizarCarrito() {
        mCarrito.setRowCount(0);
        for (DetalleVenta d : carrito.getDetalles())
            mCarrito.addRow(new Object[]{d.getProductoId(), d.getNombreProducto(), d.getCantidad(), Formato.dinero(d.calcularSubtotal())});
        Cliente c = (Cliente) cbCli.getSelectedItem();
        double sub = carrito.calcularSubtotal();
        lblTotal.setText(String.format(" Total: %s", Formato.dinero(c != null ? sub - c.calcularDescuento(sub) : sub)));
    }

    private void actualizarTodo() {
        mProd.setRowCount(0); 
        for (Producto p : inv.listarTodos()) {
            mProd.addRow(new Object[]{
                p.getId(), 
                p.getCategoria(), 
                p.getNombre(), 
                Formato.dinero(p.getPrecioBase()), 
                Formato.dinero(p.calcularPrecioFinal()), 
                p.getStock(), 
                p.getDetalle()
            });
        }
        
        mCli.setRowCount(0); 
        for (Cliente c : cli.listarTodos()) 
            mCli.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono(), c.getTipoCliente(), c.getPorcentajeDescuento()+"%"});
        
        mHistorial.setRowCount(0); 
        for (Venta v : ven.listarTodas()) 
            mHistorial.addRow(new Object[]{v.getId(), v.getFecha(), v.getNombreCliente(), Formato.dinero(v.calcularTotal())});
        
        cbCli.removeAllItems(); for (Cliente c : cli.listarTodos()) cbCli.addItem(c);
        cbProd.removeAllItems(); for (Producto p : inv.listarTodos()) cbProd.addItem(p);
        carrito.vaciar();
        actualizarCarrito();
    }
}