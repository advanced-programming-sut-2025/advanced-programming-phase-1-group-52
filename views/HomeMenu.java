package views;

import java.util.Scanner;

import controllers.GameMenuController;
import enums.regex.HomeMenuCommands;

import java.util.regex.Matcher;

public class HomeMenu implements AppMenu {
    private final GameMenuController controller = new GameMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = HomeMenuCommands.CraftingShowRecipes.getMatcher(input)) != null) {
        
        } 
        else if ((matcher = HomeMenuCommands.Crafting.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = HomeMenuCommands.CookingRefrigerator.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = HomeMenuCommands.CookingShowRecipes.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = HomeMenuCommands.CookingPrepare.getMatcher(input)) != null) {
            
        } 
        else {
            System.out.println("Invalid command");
        }
    }
}
