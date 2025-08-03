package com.example.main.enums;

import java.util.Scanner;

import com.example.main.views.AppMenu;
import com.example.main.views.ExitMenu;
import com.example.main.views.GameMenu;
import com.example.main.views.HomeMenu;
import com.example.main.views.InventoryMenu;
import com.example.main.views.LoginMenu;
import com.example.main.views.MainMenu;
import com.example.main.views.NetworkLobbyMenu;
import com.example.main.views.PreGameMenu;
import com.example.main.views.ProfileMenu;
import com.example.main.views.SignUpMenu;
import com.example.main.views.StoreMenu;
import com.example.main.views.TradeMenu;

public enum Menu {
    LoginMenu(new LoginMenu()),
    ExitMenu(new ExitMenu()),
    SignUpMenu(new SignUpMenu()),
    StoreMenu(new StoreMenu()),
    ProfileMenu(new ProfileMenu()),
    MainMenu(new MainMenu()),
    GameMenu(new GameMenu()),
    PreGameMenu(new PreGameMenu()),
    InventoryMenu(new InventoryMenu()),
    TradeMenu(new TradeMenu()),
    HomeMenu(new HomeMenu()),
    NetworkLobby(new NetworkLobbyMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.checkInput(scanner);
    }
}
