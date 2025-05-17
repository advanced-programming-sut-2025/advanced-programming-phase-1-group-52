package views;

import enums.Menu;
import java.util.Scanner;
import models.App;

public class AppView {

    public void runProgram(){
        App app =  App.getInstance();
        App.getInstance().setCurrentMenu(Menu.SignUpMenu);
        try(Scanner scanner = new Scanner(System.in)){
            while(app.getCurrentMenu() != Menu.ExitMenu){
                app.getCurrentMenu().checkCommand(scanner);
            }
        }
    }
}
