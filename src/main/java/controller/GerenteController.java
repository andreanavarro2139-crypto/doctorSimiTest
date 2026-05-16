package controller;

import model.Gerente;
import java.util.ArrayList;

public class GerenteController {
    private static ArrayList<Gerente> listaGerentes = new ArrayList<>();

    public static void guardarGerente(Gerente gerente) {
        listaGerentes.add(gerente);
        System.out.println("Gerente guardado exitosamente");
    }

    public static ArrayList<Gerente> getGerentes() {
        return listaGerentes;
    }

    public static boolean idEsUnico(int id) {
        for (Gerente gerente : listaGerentes) {
            if (gerente.getId() == id) {
                return false;
            }
        }
        return true;
    }

    public static void editarGerente(Gerente gerenteEditado) {
        for (Gerente gerente : listaGerentes) {
            if (gerente.getId() == gerenteEditado.getId()) {
                gerente.setName(gerenteEditado.getName());
                gerente.setEmail(gerenteEditado.getEmail());
                gerente.setPassword(gerenteEditado.getPassword());
                gerente.setSlary(gerenteEditado.getSlary());
                gerente.setPersonalACargoCount(gerenteEditado.getPersonalACargoCount());
            }
        }
    }

    public static boolean emailEsUnico(String email) {
        for (Gerente gerente : listaGerentes) {
            if (gerente.getEmail() != null && gerente.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        // also check cajeros to avoid duplicate emails across user types
        try {
            for (model.Cajero cajero : controller.CajeroController.getCajeros()) {
                if (cajero.getEmail() != null && cajero.getEmail().equalsIgnoreCase(email)) {
                    return false;
                }
            }
        } catch (Exception ignored) {}
        return true;
    }

    public static void eliminarGerente(int id) {
        listaGerentes.removeIf(gerente -> gerente.getId() == id);
    }
}
