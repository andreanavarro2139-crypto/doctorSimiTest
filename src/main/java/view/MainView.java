package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{

    private JLabel mensajeLabel;

    private JButton botonMedicamento;
    private JButton botonGerente;
    private JButton botonCajero;
    private JButton botonSalir;

    public MainView(){

        configurarVentana();
        inicializarComponentes();
        agregarComponenetes();
        agregarEventos();

    }

    public static void main(String[] strings) {
    }

    private void agregarEventos(){
        botonCajero.addActionListener(e->{
            //mostrar la vista de
        });

    }

    private void agregarComponenetes(){
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonMedicamento);
        panelBotones.add(botonCajero);
        panelBotones.add(botonGerente);
        panelBotones.add(botonSalir);
    }

    private void inicializarComponentes(){
        setTitle("Menu principal");

    }
}
