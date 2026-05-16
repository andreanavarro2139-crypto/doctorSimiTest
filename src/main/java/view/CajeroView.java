package view;

import controller.CajeroController;
import model.Cajero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CajeroView extends JFrame {
    private JLabel lblId, lblNombre, lblEmail, lblPassword, lblSalario, lblTurno, lblBono;
    private JTextField txtId, txtNombre, txtEmail, txtPassword, txtSalario, txtTurno, txtBono;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnMenuPrincipal;
    private JTable tablaCajeros;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public CajeroView() {
        setTitle("Gestión de Cajeros");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        inicializarComponentes();
        agregarComponentes();
        agregarEventos();
        // populate table with any saved cajeros
        llenarTablaCajeros();
    }

    private void inicializarComponentes() {
        lblId = new JLabel("ID:");
        lblNombre = new JLabel("Nombre:");
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");
        lblSalario = new JLabel("Salario:");
        lblTurno = new JLabel("Turno:");

        txtId = new JTextField(10);
        txtNombre = new JTextField(15);
        txtEmail = new JTextField(15);
        txtPassword = new JTextField(10);
        txtSalario = new JTextField(10);
        txtTurno = new JTextField(10);
        txtBono = new JTextField(8);

        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnMenuPrincipal = new JButton("Menu Principal");

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Password");
        modeloTabla.addColumn("Salario");
        modeloTabla.addColumn("Turno");
        modeloTabla.addColumn("Bono %");
        tablaCajeros = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tablaCajeros);
    }

    private void agregarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.add(lblId); panelFormulario.add(txtId);
        panelFormulario.add(lblNombre); panelFormulario.add(txtNombre);
        panelFormulario.add(lblEmail); panelFormulario.add(txtEmail);
        panelFormulario.add(lblPassword); panelFormulario.add(txtPassword);
        panelFormulario.add(lblSalario); panelFormulario.add(txtSalario);
        panelFormulario.add(lblTurno); panelFormulario.add(txtTurno);
        lblBono = new JLabel("Bono %:"); panelFormulario.add(lblBono); panelFormulario.add(txtBono);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar); panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar); panelBotones.add(btnMenuPrincipal);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void agregarEventos() {
        btnCrear.addActionListener(e -> crearCajero());
        btnActualizar.addActionListener(e -> actualizarCajero());
        btnEliminar.addActionListener(e -> eliminarCajero());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnMenuPrincipal.addActionListener(e -> {
            MainView.main(new String[]{});
            dispose();
        });
        tablaCajeros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosDesdeTabla();
        });
    }

    private void crearCajero() {
        // Validations and safe parsing
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String salarioStr = txtSalario.getText().trim();
        String turno = txtTurno.getText().trim();
        String bonoStr = txtBono.getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || salarioStr.isEmpty() || bonoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        int id;
        double salario;
        double bono;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID debe ser numérico");
            return;
        }
        try {
            salario = Double.parseDouble(salarioStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salario debe ser numérico");
            return;
        }
        try {
            bono = Double.parseDouble(bonoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bono debe ser numérico");
            return;
        }

        if (!CajeroController.idEsUnico(id)) {
            JOptionPane.showMessageDialog(this, "ID ya existe");
            return;
        }
        if (!CajeroController.emailEsUnico(email)) {
            JOptionPane.showMessageDialog(this, "El email ya está en uso");
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email no válido");
            return;
        }
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres");
            return;
        }
        if (salario < 0) {
            JOptionPane.showMessageDialog(this, "El salario no puede ser negativo");
            return;
        }
        if (bono < 0) {
            JOptionPane.showMessageDialog(this, "El porcentaje de bono no puede ser negativo");
            return;
        }

        Cajero cajero = new Cajero();
        cajero.setId(id);
        cajero.setName(nombre);
        cajero.setEmail(email);
        cajero.setPassword(password);
        cajero.setSlary(salario);
        cajero.setTurno(turno);
        cajero.setPercentageBono(bono);
        CajeroController.guardarCajero(cajero);
        llenarTablaCajeros();
        limpiarCampos();
    }

    private void leerCajeros() {
        llenarTablaCajeros();
    }

    private void actualizarCajero() {
        // Validate and update
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String salarioStr = txtSalario.getText().trim();
        String turno = txtTurno.getText().trim();
        String bonoStr = txtBono.getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || salarioStr.isEmpty() || bonoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }
        int id;
        double salario;
        double bono;
        try {
            id = Integer.parseInt(idStr);
            salario = Double.parseDouble(salarioStr);
            bono = Double.parseDouble(bonoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, salario y bono deben ser numéricos");
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email no válido");
            return;
        }
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres");
            return;
        }
        if (salario < 0) {
            JOptionPane.showMessageDialog(this, "El salario no puede ser negativo");
            return;
        }
        if (bono < 0) {
            JOptionPane.showMessageDialog(this, "El porcentaje de bono no puede ser negativo");
            return;
        }

        Cajero cajero = new Cajero();
        cajero.setId(id);
        cajero.setName(nombre);
        cajero.setEmail(email);
        cajero.setPassword(password);
        cajero.setSlary(salario);
        cajero.setTurno(turno);
        cajero.setPercentageBono(bono);
        CajeroController.editarCajero(cajero);
        llenarTablaCajeros();
        limpiarCampos();
    }

    private void eliminarCajero() {
        int id = Integer.parseInt(txtId.getText());
        CajeroController.eliminarCajero(id);
        llenarTablaCajeros();
        limpiarCampos();
    }

    private void llenarTablaCajeros() {
        ArrayList<Cajero> cajeros = CajeroController.getCajeros();
        modeloTabla.setRowCount(0);
        for (Cajero cajero : cajeros) {
            Object[] fila = new Object[7];
            fila[0] = cajero.getId();
            fila[1] = cajero.getName();
            fila[2] = cajero.getEmail();
            fila[3] = cajero.getPassword();
            fila[4] = cajero.getSlary();
            fila[5] = cajero.getShift();
            fila[6] = cajero.getPercentageBono();
            modeloTabla.addRow(fila);
        }
        tablaCajeros.setModel(modeloTabla);
    }

    private void limpiarCampos() {
        txtId.setText(""); txtNombre.setText(""); txtEmail.setText("");
        txtPassword.setText(""); txtSalario.setText(""); txtTurno.setText(""); txtBono.setText("");
        tablaCajeros.clearSelection();
        txtId.setEditable(true); txtId.setEnabled(true);
    }

    private void cargarDatosDesdeTabla() {
        int fila = tablaCajeros.getSelectedRow();
        if (fila != -1) {
            txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtId.setEditable(false); txtId.setEnabled(false);
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtEmail.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtPassword.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtSalario.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtTurno.setText(modeloTabla.getValueAt(fila, 5).toString());
            if (modeloTabla.getColumnCount() > 6) {
                Object bonoObj = modeloTabla.getValueAt(fila, 6);
                txtBono.setText(bonoObj != null ? bonoObj.toString() : "");
            }
        }
    }

    public static void inicial() {
        SwingUtilities.invokeLater(() -> {
            CajeroView vista = new CajeroView();
            vista.setVisible(true);
        });
    }
}
