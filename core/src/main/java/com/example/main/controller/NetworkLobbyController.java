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


public class NetworkLobbyController {
    private final NetworkService networkService;
    private List<String> onlineUsers;
    private List<String> lobbyPlayers;
    private boolean isHost = false;

    public NetworkLobbyController() {
        this.networkService = new NetworkService();
        this.onlineUsers = new ArrayList<>();
        this.lobbyPlayers = new ArrayList<>();


        if (App.getInstance().getCurrentUser() != null) {
            lobbyPlayers.add(App.getInstance().getCurrentUser().getUsername());
        }


        this.onlineUsersUpdateCallback = new OnlineUsersUpdateCallback() {
            @Override
            public void onOnlineUsersUpdate(List<String> onlineUsers) {

                onlineUsers.clear();
                onlineUsers.addAll(onlineUsers);
            }
        };
    }


    public NetworkLobbyController(NetworkService networkService) {
        this.networkService = networkService;
        this.onlineUsers = new ArrayList<>();
        this.lobbyPlayers = new ArrayList<>();


        if (App.getInstance().getCurrentUser() != null) {
            lobbyPlayers.add(App.getInstance().getCurrentUser().getUsername());
        }


        this.onlineUsersUpdateCallback = new OnlineUsersUpdateCallback() {
            @Override
            public void onOnlineUsersUpdate(List<String> onlineUsers) {

                onlineUsers.clear();
                onlineUsers.addAll(onlineUsers);
            }
        };


        if (networkService != null) {
            networkService.setControllerCallback(this);
        }
    }


    public boolean connectToServer(String serverIp, int serverPort) {
        boolean connected = networkService.connectToServer(serverIp, serverPort);
        if (connected) {

            networkService.setControllerCallback(this);
        }
        return connected;
    }


    public boolean authenticate(String username, String password) {

        boolean success = networkService.authenticate(username, password);

        if (success) {

            AuthManager authManager = AuthManager.getInstance();
            User user = authManager.getUser(username);
            if (user != null) {
                App.getInstance().setCurrentUser(user);
            }


            createLobby();
        } else {
            System.err.println("Server authentication failed");
        }

        return success;
    }


    public boolean createLobby() {
        HashMap<String, Object> joinData = new HashMap<>();

        Message joinMessage = new Message(joinData, MessageType.LOBBY_JOIN);
        networkService.sendMessage(joinMessage);
        return true;
    }


    public void onLobbyJoinSuccess(String lobbyId) {
        System.out.println("DEBUG: NetworkLobbyController.onLobbyJoinSuccess called with lobbyId: " + lobbyId);
        System.out.println("DEBUG: lobbyPlayers.size(): " + lobbyPlayers.size());
        System.out.println("DEBUG: lobbyCreationCallback is null: " + (lobbyCreationCallback == null));


        if (lobbyPlayers.size() == 1 && App.getInstance().getCurrentUser() != null) {
            String currentUsername = App.getInstance().getCurrentUser().getUsername();
            if (lobbyPlayers.contains(currentUsername)) {
                setHost(true);
            }
        }


        if (lobbyCreationCallback != null) {
            System.out.println("DEBUG: Triggering lobby creation callback");

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


    public void requestAvailableLobbies() {
        System.out.println("[CLIENT LOG] Requesting available lobbies from server.");
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
            return;
        }
        HashMap<String, Object> body = new HashMap<>();
        body.put("lobbyId", lobbyId);
        Message message = new Message(body, MessageType.LOBBY_FIND_BY_ID);
        networkService.sendMessage(message);
    }


    public List<String> getOnlineUsers() {

        HashMap<String, Object> requestData = new HashMap<>();
        Message requestMessage = new Message(requestData, MessageType.REQUEST_ONLINE_USERS);
        networkService.sendMessage(requestMessage);



        return new ArrayList<>(onlineUsers);
    }


    public void updateOnlineUsersFromServer(java.util.List<Object> onlineUsers) {
        java.util.List<String> usernames = new java.util.ArrayList<>();

        for (Object userObj : onlineUsers) {
            if (userObj instanceof com.example.main.models.User) {
                com.example.main.models.User user = (com.example.main.models.User) userObj;
                usernames.add(user.getUsername());
            } else if (userObj instanceof String) {

                usernames.add((String) userObj);
            }
        }


        this.onlineUsers.clear();
        this.onlineUsers.addAll(usernames);


        if (onlineUsersUpdateCallback != null) {
            onlineUsersUpdateCallback.onOnlineUsersUpdate(usernames);
        }
    }


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


    public List<String> getLobbyPlayers() {


        return lobbyPlayers;
    }

    public void submitFarmChoice(FarmThemes theme) {
        System.out.println("[CONTROLLER LOG] Submitting farm choice to server: " + theme.name());
        HashMap<String, Object> body = new HashMap<>();
        body.put("farmTheme", theme.name());
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


    public boolean leaveLobby() {

        HashMap<String, Object> leaveData = new HashMap<>();
        Message leaveMessage = new Message(leaveData, MessageType.LOBBY_LEAVE);
        networkService.sendMessage(leaveMessage);

        lobbyPlayers.clear();
        isHost = false;
        return true;
    }


    public void disconnect() {
        leaveLobby();
        networkService.disconnect();
    }


    public boolean isConnected() {
        return networkService.isConnected();
    }


    public boolean isAuthenticated() {

        return true;
    }


    public boolean isHost() {
        return isHost;
    }


    public void setHost(boolean host) {
        this.isHost = host;
    }


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
