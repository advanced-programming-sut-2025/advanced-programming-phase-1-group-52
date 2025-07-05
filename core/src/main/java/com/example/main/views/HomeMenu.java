package com.example.main.views;

import java.util.Scanner;

import com.example.main.controller.GameMenuController;
import com.example.main.controller.HomeMenuController;
import com.example.main.enums.regex.HomeMenuCommands;

import java.util.regex.Matcher;

public class HomeMenu implements AppMenu {
    private final HomeMenuController controller = new HomeMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = HomeMenuCommands.CraftingShowRecipes.getMatcher(input)) != null) {
            System.out.println(controller.showCraftingRecipes().Message());
        } 
        else if ((matcher = HomeMenuCommands.Crafting.getMatcher(input)) != null) {
            System.out.println(controller.craftItem(matcher.group("itemName")).Message());
        } 
        else if ((matcher = HomeMenuCommands.CookingRefrigerator.getMatcher(input)) != null) {
            System.out.println(controller.refrigeratorHandler(matcher.group("action"), matcher.group("item")).Message());
        } 
        else if ((matcher = HomeMenuCommands.CookingShowRecipes.getMatcher(input)) != null) {
            System.out.println(controller.showCookingRecipes().Message());
        } 
        else if ((matcher = HomeMenuCommands.CookingPrepare.getMatcher(input)) != null) {
            System.out.println(controller.cook(matcher.group("recipeName")).Message());
        }
        else if((matcher = HomeMenuCommands.CheatAddCraftingRecipe.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddCraftingRecipe(matcher.group("name")).Message());
        }
        else if((matcher = HomeMenuCommands.CheatAddCookingRecipe.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddCookingRecipe(matcher.group("name")).Message());
        }
        else if((matcher = HomeMenuCommands.Back.getMatcher(input)) != null) {
            System.out.println(controller.back().Message());
        }
        else {
            System.out.println("Invalid command");
        }
    }
}
