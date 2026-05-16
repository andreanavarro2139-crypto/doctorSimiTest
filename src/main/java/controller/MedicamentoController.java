package controller;

import model.Medicamento;

import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class MedicamentoController {

    // Lista donde se guardarán los medicamentos
    private static ArrayList<Medicamento> listaMedicamentos = new ArrayList<>();
    // Prefer user home directory for stable storage across run configurations
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path DATA_FILE = DATA_DIR.resolve("medicamentos.csv");
    // legacy relative location (project-relative) - used to migrate if present
    private static final Path LEGACY_DATA_FILE = Paths.get("user.dir").resolve("data/medicamentos.csv");

    static {
        loadFromFile();
    }

    public static void guardarMedicamento(int id, String nombre, double precio, LocalDate fecha, boolean enPromocion, int stock) {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(id);
        medicamento.setNombre(nombre);
        medicamento.setPrecio(precio);
        medicamento.setEnPromocion(enPromocion);
        medicamento.setStock(stock);
        medicamento.setFechaDeCaducidad(fecha);

        listaMedicamentos.add(medicamento);
        System.out.println("Medicamento guardado exitosamente");
        saveAllToFile();
    }

    public static ArrayList<Medicamento> getMedicamentos() { return listaMedicamentos; }

    public static boolean idEsUnico(int id) {

        for (Medicamento medicamento : listaMedicamentos) {
            if (medicamento.getId() == id) {
                return false;
            }
        }
        return true;
    }

    public static boolean nombreEsUnico(String nombre) {
        if (nombre == null) return false;
        for (Medicamento medicamento : listaMedicamentos) {
            if (nombre.equalsIgnoreCase(medicamento.getNombre())) {
                return false;
            }
        }
        return true;
    }

    public static boolean nombreEsUnicoParaEdicion(int id, String nombre) {
        if (nombre == null) return false;
        for (Medicamento medicamento : listaMedicamentos) {
            if (nombre.equalsIgnoreCase(medicamento.getNombre()) && medicamento.getId() != id) {
                return false;
            }
        }
        return true;
    }

    public static void editarMedicamento (Medicamento medicamentoEditado) {
        for (Medicamento medicamento : listaMedicamentos) {
            if (medicamento.getId() == medicamentoEditado.getId()){
                medicamento.setNombre(medicamentoEditado.getNombre());
                medicamento.setPrecio(medicamentoEditado.getPrecio());
                medicamento.setFechaDeCaducidad(medicamentoEditado.getFechaDeCaducidad());
                medicamento.setEnPromocion(medicamentoEditado.isEnPromocion());
                medicamento.setStock(medicamentoEditado.getStock());
            }

        }
        saveAllToFile();
    }

    public static void eliminarMedicamento(int id){
        boolean removed = listaMedicamentos.removeIf(medicamento -> medicamento.getId() == id);
        if (removed) {
            saveAllToFile();
        }
    }

    private static void saveAllToFile() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            List<String> lines = new ArrayList<>();
            for (Medicamento m : listaMedicamentos) {
                String line = String.format("%d,%s,%.2f,%s,%b,%d",
                        m.getId(), m.getNombre(), m.getPrecio(), m.getFechaDeCaducidad() != null ? m.getFechaDeCaducidad().toString() : "", m.isEnPromocion(), m.getStock());
                lines.add(line);
            }
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
            System.out.println("Medicamentos saved to " + DATA_FILE.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving medicamentos to file: " + e.getMessage());
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
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                try {
                    int id = Integer.parseInt(parts[0]);
                    String nombre = parts[1];
                    double precio = Double.parseDouble(parts[2]);
                    LocalDate fecha = parts[3].isEmpty() ? null : LocalDate.parse(parts[3]);
                    boolean promo = Boolean.parseBoolean(parts[4]);
                    int stock = Integer.parseInt(parts[5]);
                    Medicamento m = new Medicamento();
                    m.setId(id);
                    m.setNombre(nombre);
                    m.setPrecio(precio);
                    m.setFechaDeCaducidad(fecha);
                    m.setEnPromocion(promo);
                    m.setStock(stock);
                    listaMedicamentos.add(m);
                } catch (Exception ignored) {}
            }
            // if we loaded from legacy, ensure it's copied to new location
            if (toLoad.equals(LEGACY_DATA_FILE)) {
                try {
                    if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
                    Files.copy(LEGACY_DATA_FILE, DATA_FILE);
                    System.out.println("Migrated medicamentos file to " + DATA_FILE.toAbsolutePath());
                } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Error loading medicamentos from file: " + e.getMessage());
        }
    }


}