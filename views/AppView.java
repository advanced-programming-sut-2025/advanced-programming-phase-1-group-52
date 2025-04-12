package views;

import java.util.Scanner;

public class AppView {

    public void runProgram(){
        try(Scanner scanner = new Scanner(System.in)){
            while(App.currentMenu != Menu.ExitMenu){
                App.getCurrentMenu.checkCommand(scanner);
            }
        }
    }
}
