package com.example.main.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.main.auth.AuthManager;
import com.example.main.auth.AuthResult;
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
        // If we created the lobby (first player), we're the host
        if (lobbyPlayers.size() == 1 && App.getInstance().getCurrentUser() != null) {
            String currentUsername = App.getInstance().getCurrentUser().getUsername();
            if (lobbyPlayers.contains(currentUsername)) {
                setHost(true);
            }
        }
        
        // Notify lobby creation callback if set
        if (lobbyCreationCallback != null) {
            // For now, we'll use a placeholder name; in a real implementation, we'd get this from the server
            String lobbyName = "Lobby " + lobbyId.substring(0, 8);
            lobbyCreationCallback.onLobbyCreationSuccess(lobbyId, lobbyName);
        }
    }
    
    /**
     * Creates a lobby with specific settings
     * @param lobbyId Unique lobby ID
     * @param lobbyName Name of the lobby
     * @param isPrivate Whether the lobby is private
     * @param password Password for private lobbies
     * @param isVisible Whether the lobby is visible to others
     * @return true if lobby created successfully
     */
    public boolean createLobbyWithSettings(String lobbyId, String lobbyName, boolean isPrivate, String password, boolean isVisible) {
        HashMap<String, Object> createData = new HashMap<>();
        createData.put("lobbyId", lobbyId);
        createData.put("lobbyName", lobbyName);
        createData.put("isPrivate", isPrivate);
        createData.put("password", password);
        createData.put("isVisible", isVisible);
        Message createMessage = new Message(createData, MessageType.LOBBY_JOIN);
        networkService.sendMessage(createMessage);
        return true;
    }
    
    /**
     * Requests available lobbies from the server
     */
    public void requestAvailableLobbies() {
        HashMap<String, Object> requestData = new HashMap<>();
        Message requestMessage = new Message(requestData, MessageType.REQUEST_AVAILABLE_LOBBIES);
        networkService.sendMessage(requestMessage);
    }
    
    /**
     * Joins a lobby by ID
     * @param lobbyId The lobby ID to join
     * @return true if join message sent successfully
     */
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
    public boolean joinLobby(String lobbyId, String password) {
        HashMap<String, Object> joinData = new HashMap<>();
        joinData.put("lobbyId", lobbyId);
        joinData.put("password", password);
        Message joinMessage = new Message(joinData, MessageType.LOBBY_JOIN);
        networkService.sendMessage(joinMessage);
        return true;
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
    
    /**
     * Invites a user to join the lobby
     * @param username Username to invite
     * @return true if invitation sent successfully
     */
    public boolean inviteUser(String username) {
        if (lobbyPlayers.size() >= 4) {
            return false; // Lobby is full
        }
        
        if (lobbyPlayers.contains(username)) {
            return false; // User already in lobby
        }
        
        // For now, we'll need to get the client ID of the target user
        // This is a simplified implementation
        HashMap<String, Object> inviteData = new HashMap<>();
        inviteData.put("targetClientId", username); // This should be the actual client ID
        Message inviteMessage = new Message(inviteData, MessageType.LOBBY_INVITE);
        networkService.sendMessage(inviteMessage);
        
        return true;
    }
    
    /**
     * Invites a player to the current lobby
     * @param playerUsername The username of the player to invite
     * @return true if invitation sent successfully
     */
    public boolean invitePlayer(String playerUsername) {
        HashMap<String, Object> inviteData = new HashMap<>();
        inviteData.put("targetUsername", playerUsername);
        Message inviteMessage = new Message(inviteData, MessageType.LOBBY_INVITE);
        networkService.sendMessage(inviteMessage);
        return true;
    }
    
    public boolean acceptInvitation(String lobbyId, String inviterUsername) {
        HashMap<String, Object> acceptData = new HashMap<>();
        acceptData.put("lobbyId", lobbyId);
        acceptData.put("inviterUsername", inviterUsername);
        Message acceptMessage = new Message(acceptData, MessageType.LOBBY_INVITE_ACCEPT);
        networkService.sendMessage(acceptMessage);
        return true;
    }
    
    public boolean acceptInvitation(String hostUsername) {
        // This method is for backward compatibility
        // The new method signature is above
        return false;
    }
    
    public boolean rejectInvitation(String lobbyId, String inviterUsername) {
        HashMap<String, Object> rejectData = new HashMap<>();
        rejectData.put("lobbyId", lobbyId);
        rejectData.put("inviterUsername", inviterUsername);
        Message rejectMessage = new Message(rejectData, MessageType.LOBBY_INVITE_DECLINE);
        networkService.sendMessage(rejectMessage);
        return true;
    }
    
    public boolean rejectInvitation(String hostUsername) {
        // This method is for backward compatibility
        // The new method signature is above
        return false;
    }
    
    /**
     * Starts the game (only works if you're the host and have 4 players)
     * @return true if game started successfully
     */
    public boolean startGame() {
        if (!isHost) {
            return false; // Only host can start game
        }
        
        if (lobbyPlayers.size() < 4) {
            return false; // Need 4 players
        }
        
        // Send start game message to server
        HashMap<String, Object> startData = new HashMap<>();
        Message startMessage = new Message(startData, MessageType.LOBBY_START_GAME);
        networkService.sendMessage(startMessage);
        
        return true;
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
} 