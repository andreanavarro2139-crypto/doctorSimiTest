package controller;

import model.Cajero;
import java.util.ArrayList;

public class CajeroController {
    private static ArrayList<Cajero> listaCajeros = new ArrayList<>();

    public static void guardarCajero(Cajero cajero) {
        listaCajeros.add(cajero);
        System.out.println("Cajero guardado exitosamente");
    }

    public static ArrayList<Cajero> getCajeros() {
        return listaCajeros;
    }

    public static boolean idEsUnico(int id) {
        for (Cajero cajero : listaCajeros) {
            if (cajero.getId() == id) {
                return false;
            }
        }
        return true;
    }

    public static boolean emailEsUnico(String email) {
        for (Cajero cajero : listaCajeros) {
            if (cajero.getEmail() != null && cajero.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        // also check gerentes to avoid duplicate emails across user types
        try {
            for (model.Gerente gerente : controller.GerenteController.getGerentes()) {
                if (gerente.getEmail() != null && gerente.getEmail().equalsIgnoreCase(email)) {
                    return false;
                }
            }
        } catch (Exception ignored) {}
        return true;
    }

    public static void editarCajero(Cajero cajeroEditado) {
        for (Cajero cajero : listaCajeros) {
            if (cajero.getId() == cajeroEditado.getId()) {
                cajero.setName(cajeroEditado.getName());
                cajero.setEmail(cajeroEditado.getEmail());
                cajero.setPassword(cajeroEditado.getPassword());
                cajero.setSlary(cajeroEditado.getSlary());
                cajero.setTurno(cajeroEditado.getShift());
            }
        }
    }

    public static void eliminarCajero(int id) {
        listaCajeros.removeIf(cajero -> cajero.getId() == id);
    }
}
