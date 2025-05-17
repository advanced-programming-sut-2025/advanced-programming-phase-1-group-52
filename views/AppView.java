package views;

import enums.Menu;
import java.util.Scanner;
import models.App;

public class AppView {

    public void runProgram(){
        App app =  App.getInstance();
        System.out.println("Welcome to the Sign Up Menu! you can sign up now and if you want to have powerful password enter \"generate password\"");
        App.getInstance().setCurrentMenu(Menu.SignUpMenu);
        try(Scanner scanner = new Scanner(System.in)){
            while(app.getCurrentMenu() != Menu.ExitMenu){
                app.getCurrentMenu().checkCommand(scanner);
            }
        }
    }
}
