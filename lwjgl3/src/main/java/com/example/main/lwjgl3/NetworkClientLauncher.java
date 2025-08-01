package com.example.main.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.example.main.Main;
import com.example.main.network.NetworkConstants;

/**
 * Launches the network game client
 */
public class NetworkClientLauncher {
    
    public static void main(String[] args) {
        String serverIp = NetworkConstants.DEFAULT_HOST;
        int serverPort = NetworkConstants.DEFAULT_PORT;
        
        // Parse command line arguments
        if (args.length >= 1) {
            serverIp = args[0];
        }
        if (args.length >= 2) {
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[1]);
                System.err.println("Usage: NetworkClientLauncher [server_ip] [server_port]");
                System.exit(1);
            }
        }
        
        System.out.println("=== Stardew Valley Network Client ===");
        System.out.println("Connecting to server: " + serverIp + ":" + serverPort);
        
        // Set the server connection info for the main application
        Main.setNetworkInfo(serverIp, serverPort);
        
        // Always try graphics mode first for clients
        System.out.println("Starting graphics mode...");
        try {
            Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
            configuration.setTitle("Stardew Valley - Network Client");
            configuration.setWindowedMode(Main.WIDTH, Main.HEIGHT);
            configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
            configuration.setResizable(true);
            configuration.setDecorated(true);
            configuration.useVsync(false);
            configuration.setIdleFPS(30);
            configuration.setForegroundFPS(60);
            
            new Lwjgl3Application(new Main(), configuration);
        } catch (Exception e) {
            System.err.println("Failed to create graphics application: " + e.getMessage());
            System.err.println("Graphics mode is required for network clients.");
            System.err.println("Please ensure you have a display environment available.");
            System.exit(1);
        }
    }
} 