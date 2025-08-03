import com.example.main.network.client.GameClient;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

import java.util.HashMap;

public class TestClient {
    public static void main(String[] args) {
        try {
            // Test registration
            System.out.println("Testing registration...");
            testRegistration();
            
            // Wait a bit
            Thread.sleep(2000);
            
            // Test authentication
            System.out.println("Testing authentication...");
            testAuthentication();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testRegistration() {
        try {
            GameClient client = new GameClient("localhost", 8080);
            if (client.connect()) {
                // Create registration message
                HashMap<String, Object> regData = new HashMap<>();
                regData.put("username", "testuser123");
                regData.put("password", "testpass123");
                regData.put("nickname", "Test User 123");
                regData.put("email", "test123@example.com");
                regData.put("gender", "Male");
                
                Message regMessage = new Message(regData, MessageType.REGISTER);
                client.sendMessage(regMessage);
                
                // Wait a bit for the response
                Thread.sleep(2000);
                
                client.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testAuthentication() {
        try {
            GameClient client = new GameClient("localhost", 8080);
            if (client.connect()) {
                System.out.println("Sending authentication message...");
                boolean result = client.authenticate("testuser123", "testpass123");
                System.out.println("Authentication result: " + result);
                // Wait a bit for the authentication to be processed
                Thread.sleep(2000);
                System.out.println("Checking authentication status...");
                // Also check if the user is actually authenticated
                if (client.isAuthenticated() && client.getAuthenticatedUser() != null) {
                    System.out.println("User is authenticated: " + client.getAuthenticatedUser().getUsername());
                } else {
                    System.out.println("User is not authenticated");
                }
                client.disconnect();
            }
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
