package com.example.main.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.main.auth.AuthManager;
import com.example.main.auth.AuthResult;
import com.example.main.enums.design.FarmThemes;
import com.example.main.models.App;
import com.example.main.models.User;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;
import com.example.main.service.NetworkService;

/**
 * Controller for network lobby functionality
 */
public class NetworkLobbyController {
    private final NetworkService networkService;
    private List<String> onlineUsers;
    private List<String> lobbyPlayers;
    private boolean isHost = false;

    public NetworkLobbyController() {
        this.networkService = new NetworkService();
        this.onlineUsers = new ArrayList<>();
        this.lobbyPlayers = new ArrayList<>();

        // Add current user to lobby
        if (App.getInstance().getCurrentUser() != null) {
            lobbyPlayers.add(App.getInstance().getCurrentUser().getUsername());
        }

        // Set up controller callback for online users updates
        this.onlineUsersUpdateCallback = new OnlineUsersUpdateCallback() {
            @Override
            public void onOnlineUsersUpdate(List<String> onlineUsers) {
                // This will be called when we receive online users from server
                onlineUsers.clear();
                onlineUsers.addAll(onlineUsers);
            }
        };
    }

    /**
     * Constructor that accepts an existing NetworkService
     * @param networkService The existing NetworkService to use
     */
    public NetworkLobbyController(NetworkService networkService) {
        this.networkService = networkService;
        this.onlineUsers = new ArrayList<>();
        this.lobbyPlayers = new ArrayList<>();

        // Add current user to lobby
        if (App.getInstance().getCurrentUser() != null) {
            lobbyPlayers.add(App.getInstance().getCurrentUser().getUsername());
        }

        // Set up controller callback for online users updates
        this.onlineUsersUpdateCallback = new OnlineUsersUpdateCallback() {
            @Override
            public void onOnlineUsersUpdate(List<String> onlineUsers) {
                // This will be called when we receive online users from server
                onlineUsers.clear();
                onlineUsers.addAll(onlineUsers);
            }
        };

        // Set this controller as the callback for the network service
        if (networkService != null) {
            networkService.setControllerCallback(this);
        }
    }

    /**
     * Connects to the game server
     * @param serverIp Server IP address
     * @param serverPort Server port
     * @return true if connection successful
     */
    public boolean connectToServer(String serverIp, int serverPort) {
        boolean connected = networkService.connectToServer(serverIp, serverPort);
        if (connected) {
            // Set the controller callback for online users updates
            networkService.setControllerCallback(this);
        }
        return connected;
    }

    /**
     * Authenticates with the server
     * @param username Username
     * @param password Password
     * @return true if authentication successful
     */
    public boolean authenticate(String username, String password) {
        // Authenticate directly with the server (not locally first)
        boolean success = networkService.authenticate(username, password);

        if (success) {
            // Set the current user locally after successful server authentication
            AuthManager authManager = AuthManager.getInstance();
            User user = authManager.getUser(username);
            if (user != null) {
                App.getInstance().setCurrentUser(user);
            }

            // Create a new lobby after authentication
            createLobby();
        } else {
            System.err.println("Server authentication failed");
        }

        return success;
    }

    /**
     * Creates a new lobby
     * @return true if lobby created successfully
     */
    public boolean createLobby() {
        HashMap<String, Object> joinData = new HashMap<>();
        // No lobbyId means create new lobby
        Message joinMessage = new Message(joinData, MessageType.LOBBY_JOIN);
        networkService.sendMessage(joinMessage);
        return true;
    }

    /**
     * Handles successful lobby join on client side
     * @param lobbyId The ID of the lobby that was joined
     */
    public void onLobbyJoinSuccess(String lobbyId) {
        System.out.println("DEBUG: NetworkLobbyController.onLobbyJoinSuccess called with lobbyId: " + lobbyId);
        System.out.println("DEBUG: lobbyPlayers.size(): " + lobbyPlayers.size());
        System.out.println("DEBUG: lobbyCreationCallback is null: " + (lobbyCreationCallback == null));

        // If we created the lobby (first player), we're the host
        if (lobbyPlayers.size() == 1 && App.getInstance().getCurrentUser() != null) {
            String currentUsername = App.getInstance().getCurrentUser().getUsername();
            if (lobbyPlayers.contains(currentUsername)) {
                setHost(true);
            }
        }

        // Notify lobby creation callback if set
        if (lobbyCreationCallback != null) {
            System.out.println("DEBUG: Triggering lobby creation callback");
            // For now, we'll use a placeholder name; in a real implementation, we'd get this from the server
            String lobbyName = "Lobby " + lobbyId.substring(0, 8);
            System.out.println("DEBUG: Calling lobbyCreationCallback.onLobbyCreationSuccess with lobbyId: " + lobbyId + ", lobbyName: " + lobbyName);
            lobbyCreationCallback.onLobbyCreationSuccess(lobbyId, lobbyName);
            System.out.println("DEBUG: Lobby creation callback completed");
        } else {
            System.out.println("DEBUG: No lobby creation callback set - this might be the issue!");
        }
    }

    public void createLobby(String lobbyName, boolean isPrivate, String password, boolean isVisible) {
        HashMap<String, Object> lobbySettings = new HashMap<>();
        lobbySettings.put("lobbyName", lobbyName);
        lobbySettings.put("isPrivate", isPrivate);
        lobbySettings.put("password", password);
        lobbySettings.put("isVisible", isVisible);
        Message message = new Message(lobbySettings, MessageType.CREATE_LOBBY);
        networkService.sendMessage(message);
    }

    /**
     * Requests available lobbies from the server
     */
    public void requestAvailableLobbies() {
        System.out.println("[CLIENT LOG] Requesting available lobbies from server."); // Add this log
        HashMap<String, Object> requestData = new HashMap<>();
        Message requestMessage = new Message(requestData, MessageType.REQUEST_AVAILABLE_LOBBIES);
        networkService.sendMessage(requestMessage);
    }


    public boolean joinLobby(String lobbyId) {
        HashMap<String, Object> joinData = new HashMap<>();
        joinData.put("lobbyId", lobbyId);
        Message joinMessage = new Message(joinData, MessageType.LOBBY_JOIN);
        networkService.sendMessage(joinMessage);
        return true;
    }

    /**
     * Joins a lobby by ID with password
     * @param lobbyId The lobby ID to join
     * @param password Password for private lobbies
     * @return true if join message sent successfully
     */
    public void joinLobby(String lobbyId, String password) {
        HashMap<String, Object> joinData = new HashMap<>();
        joinData.put("lobbyId", lobbyId);
        if (password != null) {
            joinData.put("password", password);
        }
        Message joinMessage = new Message(joinData, MessageType.LOBBY_JOIN);
        networkService.sendMessage(joinMessage);
    }

    public void findLobbyById(String lobbyId) {
        if (lobbyId == null || lobbyId.trim().isEmpty()) {
            return; // Don't send empty requests
        }
        HashMap<String, Object> body = new HashMap<>();
        body.put("lobbyId", lobbyId);
        Message message = new Message(body, MessageType.LOBBY_FIND_BY_ID);
        networkService.sendMessage(message);
    }

    /**
     * Gets the list of online users
     * @return List of online usernames
     */
    public List<String> getOnlineUsers() {
        // Request online users from server
        HashMap<String, Object> requestData = new HashMap<>();
        Message requestMessage = new Message(requestData, MessageType.REQUEST_ONLINE_USERS);
        networkService.sendMessage(requestMessage);

        // For now, return current online users list
        // This will be updated when server responds
        return new ArrayList<>(onlineUsers);
    }

    /**
     * Updates the online users list from server response
     * @param onlineUsers List of online users from server
     */
    public void updateOnlineUsersFromServer(java.util.List<Object> onlineUsers) {
        java.util.List<String> usernames = new java.util.ArrayList<>();

        for (Object userObj : onlineUsers) {
            if (userObj instanceof com.example.main.models.User) {
                com.example.main.models.User user = (com.example.main.models.User) userObj;
                usernames.add(user.getUsername());
            } else if (userObj instanceof String) {
                // Already a username string
                usernames.add((String) userObj);
            }
        }

        // Update the online users list
        this.onlineUsers.clear();
        this.onlineUsers.addAll(usernames);

        // Notify any UI components that need to update
        if (onlineUsersUpdateCallback != null) {
            onlineUsersUpdateCallback.onOnlineUsersUpdate(usernames);
        }
    }

    /**
     * Callback interface for online users updates
     */
    public interface OnlineUsersUpdateCallback {
        void onOnlineUsersUpdate(java.util.List<String> onlineUsers);
    }

    public interface InvitationCallback {
        void onInvitationReceived(String lobbyId, String inviterUsername);
    }

    public interface LobbyCreationCallback {
        void onLobbyCreationSuccess(String lobbyId, String lobbyName);
        void onLobbyCreationFailed(String reason);
    }

    private OnlineUsersUpdateCallback onlineUsersUpdateCallback;
    private InvitationCallback invitationCallback;
    private LobbyCreationCallback lobbyCreationCallback;

    /**
     * Sets the callback for online users updates
     * @param callback The callback to notify when online users update
     */
    public void setOnlineUsersUpdateCallback(OnlineUsersUpdateCallback callback) {
        this.onlineUsersUpdateCallback = callback;
    }

    public void setInvitationCallback(InvitationCallback callback) {
        this.invitationCallback = callback;
    }

    public void createLobbyWithSettings(String lobbyName, boolean isPrivate, String password, boolean isVisible) {
        if (networkService != null) {
            java.util.HashMap<String, Object> lobbySettings = new java.util.HashMap<>();
            lobbySettings.put("lobbyName", lobbyName);
            lobbySettings.put("isPrivate", isPrivate);
            lobbySettings.put("password", password);
            lobbySettings.put("isVisible", isVisible);

            com.example.main.network.common.Message message = new com.example.main.network.common.Message(lobbySettings, com.example.main.network.common.MessageType.CREATE_LOBBY);
            networkService.sendMessage(message);
        }
    }

    public void setLobbyCreationCallback(LobbyCreationCallback callback) {
        this.lobbyCreationCallback = callback;
    }

    public void notifyInvitationReceived(String lobbyId, String inviterUsername) {
        if (invitationCallback != null) {
            invitationCallback.onInvitationReceived(lobbyId, inviterUsername);
        }
    }

    /**
     * Gets the list of lobby players
     * @return List of lobby player usernames
     */
    public List<String> getLobbyPlayers() {
        // In a real implementation, this would be updated by server messages
        // For now, return the current lobby players
        return lobbyPlayers;
    }

    public void submitFarmChoice(FarmThemes theme) {
        System.out.println("[CONTROLLER LOG] Submitting farm choice to server: " + theme.name());
        HashMap<String, Object> body = new HashMap<>();
        body.put("farmTheme", theme.name()); // Send the name of the enum value as a string
        Message message = new Message(body, MessageType.SUBMIT_FARM_CHOICE);
        networkService.sendMessage(message);
    }

    public void setHostStatus(boolean isHost) {
        this.isHost = isHost;
        System.out.println("[CONTROLLER LOG] Host status set to: " + this.isHost);
    }

    public boolean startGame() {
        if (!isHost) {
            System.err.println("[CONTROLLER LOG] Start game failed: isHost is false.");
            return false;
        }

        if (lobbyPlayers.size() < 2) {
            System.err.println("[CONTROLLER LOG] Start game failed: Not enough players. Required: 2, Found: " + lobbyPlayers.size());
            return false;
        }

        System.out.println("[CONTROLLER LOG] Sending START_GAME message to server.");
        HashMap<String, Object> startData = new HashMap<>();
        Message startMessage = new Message(startData, MessageType.LOBBY_START_GAME);
        networkService.sendMessage(startMessage);

        return true;
    }

    public void updateLobbyPlayers(List<String> newPlayerNames) {
        if (this.lobbyPlayers != null) {
            this.lobbyPlayers.clear();
            this.lobbyPlayers.addAll(newPlayerNames);
            System.out.println("[CONTROLLER LOG] Internal lobbyPlayers list updated. Size is now: " + this.lobbyPlayers.size());
        }
    }

    /**
     * Leaves the current lobby
     * @return true if leave message sent successfully
     */
    public boolean leaveLobby() {
        // Send leave lobby message to server
        HashMap<String, Object> leaveData = new HashMap<>();
        Message leaveMessage = new Message(leaveData, MessageType.LOBBY_LEAVE);
        networkService.sendMessage(leaveMessage);

        lobbyPlayers.clear();
        isHost = false;
        return true;
    }

    /**
     * Disconnects from the server
     */
    public void disconnect() {
        leaveLobby();
        networkService.disconnect();
    }

    /**
     * Checks if connected to server
     * @return true if connected
     */
    public boolean isConnected() {
        return networkService.isConnected();
    }

    /**
     * Checks if authenticated with server
     * @return true if authenticated (always true for lobby operations)
     */
    public boolean isAuthenticated() {
        // No authentication required for lobby operations
        return true;
    }

    /**
     * Checks if this client is the lobby host
     * @return true if host
     */
    public boolean isHost() {
        return isHost;
    }

    /**
     * Sets this client as the lobby host
     * @param host true to make this client the host
     */
    public void setHost(boolean host) {
        this.isHost = host;
    }

    /**
     * Gets the network service for advanced operations
     * @return NetworkService instance
     */
    public NetworkService getNetworkService() {
        return networkService;
    }

    public void sendPlayerMove(int x, int y) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("x", x);
        body.put("y", y);
        Message message = new Message(body, MessageType.PLAYER_MOVE);
        networkService.sendMessage(message);
    }
}
