package controller;

import model.Gerente;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.IOException;

public class GerenteController {
    private static ArrayList<Gerente> listaGerentes = new ArrayList<>();
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path DATA_FILE = DATA_DIR.resolve("gerentes.csv");
    private static final Path LEGACY_DATA_FILE = Paths.get("user.dir").resolve("data/gerentes.csv");

    static {
        loadFromFile();
    }

    public static void guardarGerente(Gerente gerente) {
        listaGerentes.add(gerente);
        System.out.println("Gerente guardado exitosamente");
        saveAllToFile();
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
        saveAllToFile();
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

    private static void saveAllToFile() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            List<String> lines = new ArrayList<>();
            for (Gerente g : listaGerentes) {
                String line = String.format("%d,%s,%s,%s,%.2f,%d",
                        g.getId(), g.getName(), g.getEmail(), g.getPassword(), g.getSlary(), g.getPersonalACargoCount());
                lines.add(line);
            }
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
            System.out.println("Gerentes saved to " + DATA_FILE.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving gerentes: " + e.getMessage());
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
                if (p.length < 6) continue;
                try {
                    Gerente g = new Gerente();
                    g.setId(Integer.parseInt(p[0]));
                    g.setName(p[1]);
                    g.setEmail(p[2]);
                    g.setPassword(p[3]);
                    g.setSlary(Double.parseDouble(p[4]));
                    g.setPersonalACargoCount(Integer.parseInt(p[5]));
                    listaGerentes.add(g);
                } catch (Exception ignored) {}
            }
            if (toLoad.equals(LEGACY_DATA_FILE)) {
                try {
                    if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
                    Files.copy(LEGACY_DATA_FILE, DATA_FILE);
                    System.out.println("Migrated gerentes file to " + DATA_FILE.toAbsolutePath());
                } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Error loading gerentes: " + e.getMessage());
        }
    }
}
