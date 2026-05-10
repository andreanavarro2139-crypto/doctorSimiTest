package controller;

import model.Medicamento;

import java.time.LocalDate;
import java.util.ArrayList;

public class MedicamentoController {

    // Lista donde se guardarán los medicamentos
    private static ArrayList<Medicamento> listaMedicamentos = new ArrayList<>();

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
    }

    public static void eliminarMedicamento(int id){
        for (Medicamento medicamento : listaMedicamentos){
            if (medicamento.getId() == id) {
                listaMedicamentos.remove(medicamento);
                return;
            }
        }
    }


}