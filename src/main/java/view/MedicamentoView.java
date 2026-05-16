package view;

import controller.MedicamentoController;
import model.Medicamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedicamentoView extends JFrame {



    // Componentes de la interfaz
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblFechaCaducidad;
    private JLabel lblEnPromocion;
    private JLabel lblStock;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtFechaCaducidad;
    private JTextField txtStock;

    private JCheckBox chkEnPromocion;

    private JButton btnCrear;
    private JButton btnLeer;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnMenuPrincipal;

    private JTable tablaMedicamentos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public MedicamentoView() {
        configurarVentana();
        inicializarComponentes();
        agregarComponentes();
        agregarEventos();
    }

    private void configurarVentana() {
        setTitle("CRUD de Medicamentos");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Labels
        lblId = new JLabel("ID:");
        lblNombre = new JLabel("Nombre:");
        lblPrecio = new JLabel("Precio:");
        lblFechaCaducidad = new JLabel("Fecha de caducidad (AAAA-MM-DD):");
        lblEnPromocion = new JLabel("En promoción:");
        lblStock = new JLabel("Stock:");

        // TextFields
        txtId = new JTextField(10);
        txtNombre = new JTextField(15);
        txtPrecio = new JTextField(10);
        txtFechaCaducidad = new JTextField(12);
        txtStock = new JTextField(10);

        // CheckBox
        chkEnPromocion = new JCheckBox();

        // Botones
        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnMenuPrincipal = new JButton("Menu Principal");

        // Tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Fecha de caducidad");
        modeloTabla.addColumn("En promoción");
        modeloTabla.addColumn("Stock");

        tablaMedicamentos = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tablaMedicamentos);
    }

    private void agregarComponentes() {
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(6, 2, 10, 10));

        panelFormulario.add(lblId);
        panelFormulario.add(txtId);

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);

        panelFormulario.add(lblPrecio);
        panelFormulario.add(txtPrecio);

        panelFormulario.add(lblFechaCaducidad);
        panelFormulario.add(txtFechaCaducidad);

        panelFormulario.add(lblEnPromocion);
        panelFormulario.add(chkEnPromocion);

        panelFormulario.add(lblStock);
        panelFormulario.add(txtStock);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnMenuPrincipal);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void agregarEventos() {
        btnCrear.addActionListener(e -> crearMedicamento());
        btnActualizar.addActionListener(e -> actualizarMedicamento());
        btnEliminar.addActionListener(e -> eliminarMedicamento());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnMenuPrincipal.addActionListener(e -> {
            MainView.main(new String[]{});
            dispose();
        });

        tablaMedicamentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosDesdeTabla();
            }
        });
    }

    private void crearMedicamento() {
        // Validate fields
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String fechaStr = txtFechaCaducidad.getText().trim();
        boolean enPromocion = chkEnPromocion.isSelected();
        String stockStr = txtStock.getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || fechaStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        int id;
        double precio;
        int stock;
        LocalDate fecha = null;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID debe ser numérico");
            return;
        }
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio debe ser numérico");
            return;
        }
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock debe ser numérico");
            return;
        }
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La fecha no tiene formato válido (AAAA-MM-DD)");
            return;
        }

        if (precio < 0) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser negativo");
            return;
        }
        if (stock < 0) {
            JOptionPane.showMessageDialog(this, "El stock no puede ser negativo");
            return;
        }

        if (!MedicamentoController.idEsUnico(id)) {
            JOptionPane.showMessageDialog(this, "Ese ID ya está siendo utilizado");
            return;
        }
        if (!MedicamentoController.nombreEsUnico(nombre)) {
            JOptionPane.showMessageDialog(this, "Ese nombre de medicamento ya está en uso");
            return;
        }

        MedicamentoController.guardarMedicamento(id, nombre, precio, fecha, enPromocion, stock);
        llenarTablaMedicamentos();
        limpiarCampos();
    }

    private void llenarTablaMedicamentos() {
        ArrayList<Medicamento> medicamentos;
        medicamentos = MedicamentoController.getMedicamentos();

        //TODO: Tengo que iterar dentro de la lista de los medicamentos
        //para cada medicamento, lo tengo que mostrar en la tabla
        //la primera columna es el id, segunda nombre, tercera precio, etc

        tablaMedicamentos.removeAll();
        modeloTabla.setRowCount(0);

        for (Medicamento medicamento : medicamentos) {

            Object[] fila = new Object[6];
            fila[0] = medicamento.getId();
            fila[1] = medicamento.getNombre();
            fila[2] = medicamento.getPrecio();
            fila[3] = medicamento.getFechaDeCaducidad();
            fila[4] = medicamento.isEnPromocion();
            fila[5] = medicamento.getStock();
            modeloTabla.addRow(fila);
        }

        tablaMedicamentos.setModel(modeloTabla);

    }

    private void leerMedicamentos() {
        llenarTablaMedicamentos();
    }

    private void actualizarMedicamento() {
        // Validate inputs similar to create
        String idStr = txtId.getText().trim();
        String nuevoNombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String fechaStr = txtFechaCaducidad.getText().trim();
        boolean nuevoEnPromocion = chkEnPromocion.isSelected();
        String stockStr = txtStock.getText().trim();

        if (idStr.isEmpty() || nuevoNombre.isEmpty() || precioStr.isEmpty() || fechaStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        int id;
        double nuevoPrecio;
        int nuevoStock;
        LocalDate nuevaFecha;
        try {
            id = Integer.parseInt(idStr);
            nuevoPrecio = Double.parseDouble(precioStr);
            nuevoStock = Integer.parseInt(stockStr);
            nuevaFecha = LocalDate.parse(fechaStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ID, precio, stock y fecha deben tener formato válido");
            return;
        }
        if (nuevoPrecio < 0) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser negativo");
            return;
        }
        if (nuevoStock < 0) {
            JOptionPane.showMessageDialog(this, "El stock no puede ser negativo");
            return;
        }
        if (!MedicamentoController.nombreEsUnicoParaEdicion(id, nuevoNombre)) {
            JOptionPane.showMessageDialog(this, "Ese nombre de medicamento ya está en uso");
            return;
        }

        Medicamento medicamentoEditado = new Medicamento();
        medicamentoEditado.setId(id);
        medicamentoEditado.setNombre(nuevoNombre);
        medicamentoEditado.setPrecio(nuevoPrecio);
        medicamentoEditado.setFechaDeCaducidad(nuevaFecha);
        medicamentoEditado.setEnPromocion(nuevoEnPromocion);
        medicamentoEditado.setStock(nuevoStock);

        MedicamentoController.editarMedicamento(medicamentoEditado);
        llenarTablaMedicamentos();
        limpiarCampos();
    }

    private void eliminarMedicamento() {
        int id = Integer.parseInt(txtId.getText());
        MedicamentoController.eliminarMedicamento(id);
        llenarTablaMedicamentos();
        limpiarCampos();
    }

    // =========================
    // MÉTODOS DE APOYO
    // =========================

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtFechaCaducidad.setText("");
        txtStock.setText("");
        chkEnPromocion.setSelected(false);
        tablaMedicamentos.clearSelection();
        txtId.setEditable(true);
        txtId.setEnabled(true);
    }

    private void cargarDatosDesdeTabla() {
        int filaSeleccionada = tablaMedicamentos.getSelectedRow();

        if (filaSeleccionada != -1) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtId.setEditable(false);
            txtId.setEnabled(false);
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtPrecio.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtFechaCaducidad.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            chkEnPromocion.setSelected(Boolean.parseBoolean(modeloTabla.getValueAt(filaSeleccionada, 4).toString()));
            txtStock.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
        }
    }

    // Método para iniciar la ventana
    public static void inicial() {
        SwingUtilities.invokeLater(() -> {
            MedicamentoView vista = new MedicamentoView();
            vista.setVisible(true);
        });
    }
}