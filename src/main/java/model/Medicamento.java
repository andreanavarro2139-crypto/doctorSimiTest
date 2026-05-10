package model;

import java.time.LocalDate;
import java.util.Date;

public class Medicamento {
    private int id;
    private String nombre;
    private double precio;
    private LocalDate fechaDeCaducidad;
    private boolean enPromocion;
    public static int stock;

//CONSTRUCTOR
    public Medicamento() {
    }
//METODOS SETTER Y GETTER

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public LocalDate getFechaDeCaducidad() {
        return fechaDeCaducidad;
    }

    public boolean isEnPromocion() {
        return enPromocion;
    }

    public static int getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setFechaDeCaducidad(LocalDate fechaDeCaducidad) {
        this.fechaDeCaducidad = fechaDeCaducidad;
    }

    public void setEnPromocion(boolean enPromocion) {
        this.enPromocion = enPromocion;
    }

    public static void setStock(int stock) {
        Medicamento.stock = stock;
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", fechaDeCaducidad=" + fechaDeCaducidad +
                ", enPromocion=" + enPromocion +
                '}';
    }

}
