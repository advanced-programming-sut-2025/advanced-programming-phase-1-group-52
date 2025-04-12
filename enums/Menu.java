package enums;

import views.*;

public enum Menu {
    GameMenu(new GameMenu()),
    InventoryMenu(new InventoryMenu()),
    LoginMenu(new LoginMenu()),
    MainMenu(new MainMenu()),
    ProfileMenu(new ProfileMenu()),
    SignUpMenu(new SignUpMenu()),
    StoreMenu(new StoreMenu()),
    ExitMenu(new ExitMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }
}
