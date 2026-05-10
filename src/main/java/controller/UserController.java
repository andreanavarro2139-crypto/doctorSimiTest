package controller;

import model.User;

public class UserController {

    public static void main(String[] args) {

        User myUser = new User();
        myUser.setId(1);
        myUser.setPassword("123456");
        myUser.setEmail("leo01morales@gmail.com");

        System.out.println (myUser.toString());

    }
}
