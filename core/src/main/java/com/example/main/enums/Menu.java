package com.example.main.enums;

import java.util.Scanner;

import com.example.main.views.*;

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
    HomeMenu(new HomeMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.checkInput(scanner);
    }
}
