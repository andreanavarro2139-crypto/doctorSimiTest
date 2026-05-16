package controller;

import model.User;
import java.util.ArrayList;

public class UserController {
    private static ArrayList<User> listaUsuarios = new ArrayList<>();

    public static void guardarUsuario(User user) {
        listaUsuarios.add(user);
        System.out.println("Usuario guardado exitosamente");
    }

    public static ArrayList<User> getUsuarios() {
        return listaUsuarios;
    }

    public static boolean idEsUnico(int id) {
        for (User user : listaUsuarios) {
            if (user.getId() == id) {
                return false;
            }
        }
        return true;
    }

    public static void editarUsuario(User userEditado) {
        for (User user : listaUsuarios) {
            if (user.getId() == userEditado.getId()) {
                user.setName(userEditado.getName());
                user.setEmail(userEditado.getEmail());
                user.setPassword(userEditado.getPassword());
                user.setSlary(userEditado.getSlary());
                user.setTurno(userEditado.getShift());
            }
        }
    }

    public static void eliminarUsuario(int id) {
        listaUsuarios.removeIf(user -> user.getId() == id);
    }
}
