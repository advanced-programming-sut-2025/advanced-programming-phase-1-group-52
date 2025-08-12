package com.example.main.views;

import java.util.Scanner;

public class NetworkLobbyMenu implements AppMenu {

    @Override
    public void checkInput(Scanner scanner) {
                            System.out.println("Network Lobby - Use the GUI version for full functionality");
        System.out.println("Available commands:");
        System.out.println("1. back - Return to main menu");
        System.out.println("2. exit - Exit the application");

        String input = scanner.nextLine().trim().toLowerCase();

        switch (input) {
            case "back":
            case "1":
                                  System.out.println("Returning to main menu...");
                break;
            case "exit":
            case "2":
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Please try again.");
                break;
        }
    }
}
