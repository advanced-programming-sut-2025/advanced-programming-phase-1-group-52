package com.example.main.controller;

import com.example.main.Main;
import com.example.main.enums.Menu;
import com.example.main.models.App;
import com.example.main.models.Result;
import com.example.main.network.client.GameClient;

public class MainMenuController {
    public Result menuEnter(String menuName) {
            Menu menu = Menu.valueOf(menuName);
            if (menu == Menu.GameMenu || menu == Menu.ProfileMenu || menu == Menu.PreGameMenu || menu == Menu.NetworkLobby || menu == Menu.MainMenu) {
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
        GameClient client = Main.getInstance().getGameClient();
        if (client != null && client.isAuthenticated()) {
            client.logout();
            return new Result(true, "Logout successful.");
        } else {
            return new Result(false, "No user was logged in.");
        }
    }
}
