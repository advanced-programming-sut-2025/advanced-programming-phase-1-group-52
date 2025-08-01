package com.example.main.views;

import java.util.Scanner;

public class NetworkLobbyMenu implements AppMenu {
    
    @Override
    public void checkInput(Scanner scanner) {
        // This is a placeholder for the terminal version
        // The actual network lobby functionality is in GDXNetworkLobby
        System.out.println("Network Lobby - Use the GUI version for full functionality");
        System.out.println("Available commands:");
        System.out.println("1. back - Return to main menu");
        System.out.println("2. exit - Exit the application");
        
        String input = scanner.nextLine().trim().toLowerCase();
        
        switch (input) {
            case "back":
            case "1":
                // Return to main menu - this would need to be handled by the menu system
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