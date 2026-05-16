package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{

    private JButton botonMedicamento;
    private JButton botonGerente;
    private JButton botonCajero;
    private JButton botonSalir;

    public MainView(){
        inicializarComponentes();
        agregarComponenetes();
        agregarEventos();
    }

    public static void main(String[] strings) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }

    private void agregarEventos(){
        botonMedicamento.addActionListener(e -> {
            MedicamentoView.inicial();
            dispose();
        });
        botonCajero.addActionListener(e -> {
            CajeroView.inicial();
            dispose();
        });
        botonGerente.addActionListener(e -> {
            GerenteView.inicial();
            dispose();
        });
        botonSalir.addActionListener(e -> System.exit(0));
    }

    private void agregarComponenetes(){
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonMedicamento);
        panelBotones.add(botonCajero);
        panelBotones.add(botonGerente);
        panelBotones.add(botonSalir);
        add(panelBotones, java.awt.BorderLayout.CENTER);
    }

    private void inicializarComponentes(){
        setTitle("Menu principal");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new java.awt.BorderLayout());
        JLabel mensajeLabel = new JLabel("Bienvenido al sistema Doctor Simi", SwingConstants.CENTER);
        add(mensajeLabel, java.awt.BorderLayout.NORTH);
        botonMedicamento = new JButton("Medicamentos");
        botonGerente = new JButton("Gerente");
        botonCajero = new JButton("Cajero");
        botonSalir = new JButton("Salir");
    }
}
