package enums;

import views.*;

import java.util.Scanner;

public enum Menu {
    LoginMenu(new LoginMenu()),
    ExitMenu(new ExitMenu()),
    SignUpMenu(new SignUpMenu()),
    StoreMenu(new StoreMenu()),
    ProfileMenu(new ProfileMenu()),
    MainMenu(new MainMenu()),
    GameMenu(new GameMenu()),
    InventoryMenu(new InventoryMenu()),
    TradeMenu(new TradeMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.checkInput(scanner);
    }
}
