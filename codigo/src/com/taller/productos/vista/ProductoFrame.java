package com.taller.productos.vista;

import com.taller.productos.dao.ProductoDAO;
import com.taller.productos.modelo.Producto;
import com.taller.productos.util.ValidacionException;
import com.taller.productos.util.Validador;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

/*Ventana principal*/

public class ProductoFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final ProductoDAO dao = new ProductoDAO();

    private final JTextField txtCodigo = new JTextField(12);
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtDescripcion = new JTextField(20);
    private final JTextField txtPrecio = new JTextField(10);
    private final JTextField txtCantidad = new JTextField(10);
    private final JTextField txtBuscar = new JTextField(15);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Código", "Nombre", "Descripción", "Precio", "Cantidad"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public ProductoFrame() {
        super("Sistema de Gestión de Productos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearBusqueda(), BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarFilaSeleccionada();
            }
        });

        refrescarTabla(null);
    }

    // interfaz

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        agregarCampo(panel, gbc, 0, 0, "Código:", txtCodigo);
        agregarCampo(panel, gbc, 2, 0, "Nombre:", txtNombre);
        agregarCampo(panel, gbc, 0, 1, "Descripción:", txtDescripcion);
        agregarCampo(panel, gbc, 2, 1, "Precio:", txtPrecio);
        agregarCampo(panel, gbc, 0, 2, "Cantidad:", txtCantidad);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(botones, gbc);

        btnRegistrar.addActionListener(e -> registrar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int x, int y,
                              String etiqueta, JTextField campo) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = x + 1;
        panel.add(campo, gbc);
    }

    private JPanel crearBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Consulta"));
        JButton btnBuscar = new JButton("Buscar");
        JButton btnTodos = new JButton("Mostrar todos");
        panel.add(new JLabel("Código o nombre:"));
        panel.add(txtBuscar);
        panel.add(btnBuscar);
        panel.add(btnTodos);

        btnBuscar.addActionListener(e -> refrescarTabla(txtBuscar.getText()));
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            refrescarTabla(null);
        });
        return panel;
    }

    // CRUD

    private void registrar() {
        try {
            Producto p = Validador.construirProducto(txtCodigo.getText(), txtNombre.getText(),
                    txtDescripcion.getText(), txtPrecio.getText(), txtCantidad.getText());
            if (dao.buscarPorCodigo(p.getCodigo()) != null) {
                mostrarAdvertencia("Ya existe un producto con el código " + p.getCodigo() + ".");
                return;
            }
            dao.insertar(p);
            mostrarInfo("Producto registrado correctamente.");
            limpiarFormulario();
            refrescarTabla(null);
        } catch (ValidacionException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void actualizar() {
        try {
            Producto p = Validador.construirProducto(txtCodigo.getText(), txtNombre.getText(),
                    txtDescripcion.getText(), txtPrecio.getText(), txtCantidad.getText());
            if (dao.actualizar(p)) {
                mostrarInfo("Producto actualizado correctamente.");
                limpiarFormulario();
                refrescarTabla(null);
            } else {
                mostrarAdvertencia("No existe un producto con el código " + p.getCodigo() + ".");
            }
        } catch (ValidacionException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void eliminar() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            mostrarAdvertencia("Ingrese o seleccione el código del producto a eliminar.");
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto " + codigo + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.eliminar(codigo)) {
                mostrarInfo("Producto eliminado correctamente.");
                limpiarFormulario();
                refrescarTabla(null);
            } else {
                mostrarAdvertencia("No existe un producto con el código " + codigo + ".");
            }
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    private void refrescarTabla(String filtro) {
        try {
            List<Producto> lista = (filtro == null || filtro.trim().isEmpty())
                    ? dao.listar() : dao.buscar(filtro);
            modeloTabla.setRowCount(0);
            for (Producto p : lista) {
                modeloTabla.addRow(new Object[]{p.getCodigo(), p.getNombre(),
                        p.getDescripcion(), p.getPrecio(), p.getCantidad()});
            }
            if (filtro != null && !filtro.trim().isEmpty() && lista.isEmpty()) {
                mostrarInfo("No se encontraron productos para \"" + filtro.trim() + "\".");
            }
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    // utilidades

    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        txtCodigo.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        Object desc = modeloTabla.getValueAt(fila, 2);
        txtDescripcion.setText(desc == null ? "" : desc.toString());
        txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtCantidad.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        tabla.clearSelection();
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarErrorBD(SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Error de base de datos: " + ex.getMessage()
                        + "\nVerifique que la carpeta lib/ contenga el controlador H2 y que pueda escribir en la carpeta data/.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
