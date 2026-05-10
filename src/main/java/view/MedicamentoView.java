package view;

import controller.MedicamentoController;
import model.Medicamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
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
        btnLeer = new JButton("Leer");
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
        panelBotones.add(btnLeer);
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
        btnLeer.addActionListener(e -> leerMedicamentos());
        btnActualizar.addActionListener(e -> actualizarMedicamento());
        btnEliminar.addActionListener(e -> eliminarMedicamento());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnMenuPrincipal.addActionListener(e -> {
            MainView.main(new String[]{});
            dispose();//destruye la vista actual
        });

        tablaMedicamentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosDesdeTabla();
            }
        });
    }

    // =========================
    // MÉTODOS CRUD (ESQUELETO)
    // =========================

    private void crearMedicamento() {
        boolean fechaValida;
        int id = Integer.parseInt(txtId.getText());
        String nombre = txtNombre.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        LocalDate fecha = null;
        boolean enPromocion = chkEnPromocion.isSelected();
        int stock = Integer.parseInt(txtStock.getText());

        try{
            fecha = LocalDate.parse(txtFechaCaducidad.getText());
            fechaValida = true;
        }catch(Exception e){
            JOptionPane miAlerta = new JOptionPane();
            JOptionPane.showMessageDialog(miAlerta, "La fecha no tiene formato válido");
            fechaValida = false;
        }

        if(fechaValida) {
            //validar que el id no esté guardado
            boolean EsUnico = MedicamentoController.idEsUnico(id);

            if(EsUnico) {
                MedicamentoController.guardarMedicamento(id,nombre,precio,fecha,enPromocion,stock);
                // Mostrar en tabla
                llenarTablaMedicamentos();
                limpiarCampos();
            }else{
                System.out.println("No puedes guardar ese id porque ya está siendo utilizado");
                //TODO: mostrar una alerta en la vista

                JOptionPane miAlerta = new JOptionPane();
                JOptionPane.showMessageDialog(miAlerta, "Ese id ya está siendo utilizado");

            }
        }


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
        // Aquí debes programar la lógica para mostrar en la tabla
        // todos los medicamentos que estén guardados en la lista.

        /*
        Idea:
        1. Limpiar las filas actuales de la tabla
        2. Recorrer el ArrayList
        3. Agregar cada medicamento como una nueva fila
        */
    }

    private void actualizarMedicamento() {
        int id = Integer.parseInt(txtId.getText());
        String nuevoNombre = txtNombre.getText();
        double nuevoPrecio = Double.parseDouble(txtPrecio.getText());
        LocalDate nuevaFecha = LocalDate.parse(txtFechaCaducidad.getText());
        boolean nuevoEnPromocion = chkEnPromocion.isSelected();
        int nuevoStock = Integer.parseInt(txtStock.getText());

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