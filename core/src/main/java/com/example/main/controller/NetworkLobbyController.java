package com.example.main.controller;

import com.example.main.models.User;
import com.example.main.models.App;
import com.example.main.service.NetworkService;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    }
    
    /**
     * Connects to the game server
     * @param serverIp Server IP address
     * @param serverPort Server port
     * @return true if connection successful
     */
    public boolean connectToServer(String serverIp, int serverPort) {
        return networkService.connectToServer(serverIp, serverPort);
    }
    
    /**
     * Authenticates with the server
     * @param username Username
     * @param password Password
     * @return true if authentication successful
     */
    public boolean authenticate(String username, String password) {
        boolean success = networkService.authenticate(username, password);
        if (success) {
            // Send lobby join message
            HashMap<String, Object> joinData = new HashMap<>();
            joinData.put("username", username);
            joinData.put("action", "JOIN_LOBBY");
            Message joinMessage = new Message(joinData, MessageType.PLAYER_ACTION);
            networkService.sendMessage(joinMessage);
        }
        return success;
    }
    
    /**
     * Gets the list of online users
     * @return List of online usernames
     */
    public List<String> getOnlineUsers() {
        // In a real implementation, this would be updated by server messages
        // For now, return a static list
        onlineUsers.clear();
        onlineUsers.add("player1");
        onlineUsers.add("player2");
        onlineUsers.add("player3");
        onlineUsers.add("player4");
        return onlineUsers;
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
        
        // Send invitation message to server
        HashMap<String, Object> inviteData = new HashMap<>();
        inviteData.put("username", username);
        inviteData.put("action", "INVITE_TO_LOBBY");
        Message inviteMessage = new Message(inviteData, MessageType.PLAYER_ACTION);
        networkService.sendMessage(inviteMessage);
        
        return true;
    }
    
    /**
     * Accepts an invitation to join a lobby
     * @param hostUsername Username of the host
     * @return true if accepted successfully
     */
    public boolean acceptInvitation(String hostUsername) {
        // Send accept invitation message to server
        HashMap<String, Object> acceptData = new HashMap<>();
        acceptData.put("hostUsername", hostUsername);
        acceptData.put("action", "ACCEPT_INVITATION");
        Message acceptMessage = new Message(acceptData, MessageType.PLAYER_ACTION);
        networkService.sendMessage(acceptMessage);
        
        return true;
    }
    
    /**
     * Declines an invitation to join a lobby
     * @param hostUsername Username of the host
     * @return true if declined successfully
     */
    public boolean declineInvitation(String hostUsername) {
        // Send decline invitation message to server
        HashMap<String, Object> declineData = new HashMap<>();
        declineData.put("hostUsername", hostUsername);
        declineData.put("action", "DECLINE_INVITATION");
        Message declineMessage = new Message(declineData, MessageType.PLAYER_ACTION);
        networkService.sendMessage(declineMessage);
        
        return true;
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
        startData.put("action", "START_GAME");
        startData.put("players", lobbyPlayers);
        Message startMessage = new Message(startData, MessageType.PLAYER_ACTION);
        networkService.sendMessage(startMessage);
        
        return true;
    }
    
    /**
     * Leaves the current lobby
     */
    public void leaveLobby() {
        // Send leave lobby message to server
        HashMap<String, Object> leaveData = new HashMap<>();
        leaveData.put("action", "LEAVE_LOBBY");
        Message leaveMessage = new Message(leaveData, MessageType.PLAYER_ACTION);
        networkService.sendMessage(leaveMessage);
        
        lobbyPlayers.clear();
        isHost = false;
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
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return networkService.isAuthenticated();
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