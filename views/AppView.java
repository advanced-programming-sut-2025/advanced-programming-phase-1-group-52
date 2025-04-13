package views;

import enums.Menu;
import models.App;

import java.util.Scanner;

public class AppView {

    public void runProgram(){
        App app =  App.getInstance();
        try(Scanner scanner = new Scanner(System.in)){
            while(app.getCurrentMenu() != Menu.ExitMenu){
                app.getCurrentMenu().checkCommand(scanner);
            }
        }
    }
}
