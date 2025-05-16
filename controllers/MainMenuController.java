package controllers;

import enums.Menu;
import models.App;
import models.Result;

public class MainMenuController {
    public Result menuEnter(String menuName) {
            Menu menu = Menu.valueOf(menuName);
            if (menu == Menu.GameMenu || menu == Menu.ProfileMenu) {
                App.getInstance().setCurrentMenu(menu);
                return new Result(true, "Entered " + menu.name() + " successfully.");
            } else {
                return new Result(false, "Invalid menu entered.");
            }
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }

    public void showCurrentMenu() {
        System.out.println("Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public Result userLogout() {
        App.getInstance().setCurrentUser(null);
        App.getInstance().setCurrentMenu(Menu.LoginMenu);
        return new Result(true, "You are logged out.");
    }
}
