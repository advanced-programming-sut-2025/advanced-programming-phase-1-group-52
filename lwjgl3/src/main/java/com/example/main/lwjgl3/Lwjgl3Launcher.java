package com.example.main.lwjgl3;

import java.util.Scanner;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.example.main.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        
        // Check if we're in a headless environment (like WSL without X11)
        boolean isHeadless = isHeadlessEnvironment();
        
        if (isHeadless) {
            System.out.println("Headless environment detected (WSL without X11).");
            System.out.println("Running in terminal mode...");
            Main.main(args);
        } else {
            // Show mode selection
            showModeSelection();
        }
    }
    
    private static void showModeSelection() {
        System.out.println("=== Stardew Valley - Hybrid Mode ===");
        System.out.println("Choose your preferred mode:");
        System.out.println("1. Graphics Mode (libGDX window with terminal integration)");
        System.out.println("2. Terminal Mode (console-based gameplay)");
        System.out.println("3. Auto-detect (try graphics, fallback to terminal)");
        System.out.print("Enter your choice (1-3): ");
        
        try (Scanner scanner = new Scanner(System.in)) {
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    startGraphicsMode();
                    break;
                case "2":
                    startTerminalMode();
                    break;
                case "3":
                    autoDetectMode();
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to auto-detect mode.");
                    autoDetectMode();
            }
        }
    }
    
    private static void startGraphicsMode() {
        System.out.println("Starting Graphics Mode...");
        try {
            createApplication();
        } catch (Exception e) {
            System.err.println("Failed to create graphics application: " + e.getMessage());
            System.out.println("Graphics mode failed. Switching to terminal mode...");
            startTerminalMode();
        }
    }
    
    private static void startTerminalMode() {
        System.out.println("Starting Terminal Mode...");
        Main.main(new String[0]);
    }
    
    private static void autoDetectMode() {
        System.out.println("Auto-detecting best mode...");
        if (isHeadlessEnvironment()) {
            System.out.println("Headless environment detected. Using terminal mode.");
            startTerminalMode();
        } else {
            System.out.println("Graphics environment detected. Trying graphics mode...");
            startGraphicsMode();
        }
    }
    
    private static boolean isHeadlessEnvironment() {
        // Check for DISPLAY environment variable - if present, X11 should be available
        String display = System.getenv("DISPLAY");
        
        // If DISPLAY is set and not empty, we have graphics capability
        if (display != null && !display.isEmpty()) {
            return false;
        }
        
        // If no DISPLAY, then we're truly headless
        return true;
    }
    
    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }
    
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Stardew Valley - Hybrid Mode");
        configuration.setWindowedMode(Main.WIDTH, Main.HEIGHT);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        configuration.setResizable(true);
        configuration.setDecorated(true);
        
        // Add some safety configurations for WSL
        configuration.useVsync(false);
        configuration.setIdleFPS(30);
        configuration.setForegroundFPS(60);
        
        return configuration;
    }
}