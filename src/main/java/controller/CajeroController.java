package controller;

import model.Cajero;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.IOException;

public class CajeroController {
    private static ArrayList<Cajero> listaCajeros = new ArrayList<>();
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path DATA_FILE = DATA_DIR.resolve("cajeros.csv");
    private static final Path LEGACY_DATA_FILE = Paths.get("user.dir").resolve("data/cajeros.csv");

    static {
        loadFromFile();
    }

    public static void guardarCajero(Cajero cajero) {
        listaCajeros.add(cajero);
        System.out.println("Cajero guardado exitosamente");
        saveAllToFile();
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
                cajero.setPercentageBono(cajeroEditado.getPercentageBono());
            }
        }
        saveAllToFile();
    }

    public static void eliminarCajero(int id) {
        listaCajeros.removeIf(cajero -> cajero.getId() == id);
        saveAllToFile();
    }

    private static void saveAllToFile() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            List<String> lines = new ArrayList<>();
            for (Cajero c : listaCajeros) {
                String line = String.format("%d,%s,%s,%s,%.2f,%s,%.2f",
                        c.getId(), c.getName(), c.getEmail(), c.getPassword(), c.getSlary(), c.getShift(), c.getPercentageBono());
                lines.add(line);
            }
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
            System.out.println("Cajeros saved to " + DATA_FILE.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving cajeros: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        Path toLoad = null;
        if (Files.exists(DATA_FILE)) toLoad = DATA_FILE;
        else if (Files.exists(LEGACY_DATA_FILE)) toLoad = LEGACY_DATA_FILE;
        if (toLoad == null) return;
        try {
            List<String> lines = Files.readAllLines(toLoad, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length < 7) continue;
                try {
                    Cajero c = new Cajero();
                    c.setId(Integer.parseInt(p[0]));
                    c.setName(p[1]);
                    c.setEmail(p[2]);
                    c.setPassword(p[3]);
                    c.setSlary(Double.parseDouble(p[4]));
                    c.setTurno(p[5]);
                    c.setPercentageBono(Double.parseDouble(p[6]));
                    listaCajeros.add(c);
                } catch (Exception ignored) {}
            }
            if (toLoad.equals(LEGACY_DATA_FILE)) {
                try {
                    if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
                    Files.copy(LEGACY_DATA_FILE, DATA_FILE);
                    System.out.println("Migrated cajeros file to " + DATA_FILE.toAbsolutePath());
                } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Error loading cajeros: " + e.getMessage());
        }
    }
}
