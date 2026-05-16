package view;

import controller.GerenteController;
import model.Gerente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GerenteView extends JFrame {
    private JLabel lblId, lblNombre, lblEmail, lblPassword, lblSalario, lblStaff;
    private JTextField txtId, txtNombre, txtEmail, txtPassword, txtSalario, txtStaff;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnMenuPrincipal;
    private JTable tablaGerentes;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    public GerenteView() {
        setTitle("Gestión de Gerentes");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        inicializarComponentes();
        agregarComponentes();
        agregarEventos();
        // populate table with any saved gerentes
        llenarTablaGerentes();
    }

    private void inicializarComponentes() {
        lblId = new JLabel("ID:");
        lblNombre = new JLabel("Nombre:");
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");
        lblSalario = new JLabel("Salario:");

        txtId = new JTextField(10);
        txtNombre = new JTextField(15);
        txtEmail = new JTextField(15);
        txtPassword = new JTextField(10);
        txtSalario = new JTextField(10);
        txtStaff = new JTextField(10);

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
        modeloTabla.addColumn("Staff");
        tablaGerentes = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tablaGerentes);
    }

    private void agregarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.add(lblId); panelFormulario.add(txtId);
        panelFormulario.add(lblNombre); panelFormulario.add(txtNombre);
        panelFormulario.add(lblEmail); panelFormulario.add(txtEmail);
        panelFormulario.add(lblPassword); panelFormulario.add(txtPassword);
        panelFormulario.add(lblSalario); panelFormulario.add(txtSalario);
        lblStaff = new JLabel("Personal a cargo (cantidad):"); panelFormulario.add(lblStaff); panelFormulario.add(txtStaff);

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
        btnCrear.addActionListener(e -> crearGerente());
        btnActualizar.addActionListener(e -> actualizarGerente());
        btnEliminar.addActionListener(e -> eliminarGerente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnMenuPrincipal.addActionListener(e -> {
            MainView.main(new String[]{});
            dispose();
        });
        tablaGerentes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosDesdeTabla();
        });
    }

    private void crearGerente() {
        // Validations and safe parsing
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String salarioStr = txtSalario.getText().trim();
        String staffStr = txtStaff.getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || salarioStr.isEmpty() || staffStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        int id;
        double salario;
        int staff;
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
            staff = Integer.parseInt(staffStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Staff debe ser numérico");
            return;
        }

        if (!GerenteController.idEsUnico(id)) {
            JOptionPane.showMessageDialog(this, "ID ya existe");
            return;
        }
        if (!GerenteController.emailEsUnico(email)) {
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
        if (staff < 0) {
            JOptionPane.showMessageDialog(this, "La cantidad de personal a cargo no puede ser negativa");
            return;
        }

        Gerente gerente = new Gerente();
        gerente.setId(id);
        gerente.setName(nombre);
        gerente.setEmail(email);
        gerente.setPassword(password);
        gerente.setSlary(salario);
        gerente.setPersonalACargoCount(staff);
        GerenteController.guardarGerente(gerente);
        llenarTablaGerentes();
        limpiarCampos();
    }

    private void leerGerentes() {
        llenarTablaGerentes();
    }

    private void actualizarGerente() {
        // Validate and update
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String salarioStr = txtSalario.getText().trim();
        String staffStr = txtStaff.getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || salarioStr.isEmpty() || staffStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }
        int id;
        double salario;
        int staff;
        try {
            id = Integer.parseInt(idStr);
            salario = Double.parseDouble(salarioStr);
            staff = Integer.parseInt(staffStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, salario y staff deben ser numéricos");
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
        if (staff < 0) {
            JOptionPane.showMessageDialog(this, "La cantidad de personal a cargo no puede ser negativa");
            return;
        }

        Gerente gerente = new Gerente();
        gerente.setId(id);
        gerente.setName(nombre);
        gerente.setEmail(email);
        gerente.setPassword(password);
        gerente.setSlary(salario);
        gerente.setPersonalACargoCount(staff);
        GerenteController.editarGerente(gerente);
        llenarTablaGerentes();
        limpiarCampos();
    }

    private void eliminarGerente() {
        int id = Integer.parseInt(txtId.getText());
        GerenteController.eliminarGerente(id);
        llenarTablaGerentes();
        limpiarCampos();
    }

    private void llenarTablaGerentes() {
        ArrayList<Gerente> gerentes = GerenteController.getGerentes();
        modeloTabla.setRowCount(0);
        for (Gerente gerente : gerentes) {
            Object[] fila = new Object[6];
            fila[0] = gerente.getId();
            fila[1] = gerente.getName();
            fila[2] = gerente.getEmail();
            fila[3] = gerente.getPassword();
            fila[4] = gerente.getSlary();
            fila[5] = gerente.getPersonalACargoCount();
            modeloTabla.addRow(fila);
        }
        tablaGerentes.setModel(modeloTabla);
    }

    private void limpiarCampos() {
        txtId.setText(""); txtNombre.setText(""); txtEmail.setText("");
        txtPassword.setText(""); txtSalario.setText(""); txtStaff.setText("");
        tablaGerentes.clearSelection();
        txtId.setEditable(true); txtId.setEnabled(true);
    }

    private void cargarDatosDesdeTabla() {
        int fila = tablaGerentes.getSelectedRow();
        if (fila != -1) {
            txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtId.setEditable(false); txtId.setEnabled(false);
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtEmail.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtPassword.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtSalario.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtStaff.setText(modeloTabla.getValueAt(fila, 5).toString());
        }
    }

    public static void inicial() {
        SwingUtilities.invokeLater(() -> {
            GerenteView vista = new GerenteView();
            vista.setVisible(true);
        });
    }
}
