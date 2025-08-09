package com.example.main.network.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.example.main.GDXviews.GDXLobbyScreen;
import com.example.main.GDXviews.GDXMainMenu;
import com.example.main.GDXviews.GDXOnlineLobbiesMenu;
import com.example.main.GDXviews.GDXPreGameMenu;
import com.example.main.Main;
import com.example.main.controller.NetworkLobbyController;
import com.example.main.events.EventBus;
import com.example.main.events.GameMapSyncEvent;
import com.example.main.events.NavigateToGameScreenEvent;
import com.example.main.events.PlayerDisconnectedEvent;
import com.example.main.events.RequestMapSnapshotEvent;
import com.example.main.models.App;
import com.example.main.models.Game;
import com.example.main.models.GameMapSnapshot;
import com.example.main.models.GameStateSnapshot;
import com.example.main.models.Player;
import com.example.main.models.User; // <-- Add this import
import com.example.main.network.common.Message; // <-- Add this import
import com.example.main.network.common.MessageType; // <-- Add this import
import com.google.gson.Gson;

/**
 * Handles incoming messages from the server
 */
public class ClientMessageHandler {
    private GameClient client;
    private final Gson gson; // <-- Add a Gson instance

    public ClientMessageHandler(GameClient client) {
        this.client = client;
        this.gson = new Gson();
    }

    public void handleMessage(Message message) {
        if(!message.getType().equals(MessageType.HEARTBEAT)){
            System.out.println("[CLIENT LOG] Received message from server. Type: " + message.getType());
        }
        switch (message.getType()) {
            case AUTH_SUCCESS:
                handleAuthSuccess(message);
                break;
            case AUTH_FAILED:
                handleAuthFailed(message);
                break;
            case GAME_STATE:
                handleGameState(message);
                break;
            case PLAYER_ACTION:
                handlePlayerAction(message);
                break;
            case HEARTBEAT:
                handleHeartbeat();
                break;
            case ERROR:
                handleError(message);
                break;
            case LOBBY_JOIN:
                handleLobbyJoin(message);
                break;
            case LOBBY_JOIN_SUCCESS:
                handleLobbyJoinSuccess(message);
                break;
            case LOBBY_LEAVE:
                handleLobbyLeave(message);
                break;
            case LOBBY_JOIN_FAILED:
                handleLobbyJoinFailed(message);
                break;
            case LOBBY_FIND_RESULT:
                handleLobbyFindResult(message);
                break;
            case LOBBY_ADMIN_CHANGE:
                handleLobbyAdminChange(message);
                break;
            case LOBBY_PLAYERS_UPDATE:
                handleLobbyPlayersUpdate(message);
                break;
            case LOBBY_READY:
                handleLobbyReady(message);
                break;
            case LOBBY_START_GAME:
                handleLobbyStartGame(message);
                break;
            case UPDATE_PLAYER_POSITIONS:
                handleUpdatePlayerPositions(message);
                break;
            case ONLINE_USERS_UPDATE:
                handleOnlineUsersUpdate(message);
                break;
            case AVAILABLE_LOBBIES_UPDATE:
                handleAvailableLobbiesUpdate(message);
                break;
            case NAVIGATE_TO_PREGAME:
                handleNavigateToPregame();
                break;
            case INITIALIZE_GAME:
                handleInitializeGame(message);
                break;
            case PLAYER_LEAVE: // <-- ADD THIS CASE
                handlePlayerLeave(message);
                break;
            case REQUEST_GAME_MAP:
                handleRequestGameMap(message);
                break;
            case SYNC_GAME_MAP:
                handleSyncGameMap(message);
                break;
            default:
                handleCustomMessage(message);
        }
    }

    private void handleAuthSuccess(Message message) {
        try {
            System.out.println("Handling AUTH_SUCCESS message: " + message.getBody());
            // Check if this is a registration success or authentication success
            String regMessage = message.getFromBody("message");
            if (regMessage != null && regMessage.equals("User registered successfully")) {
                System.out.println("Registration successful: " + regMessage);
                // For registration, we might want to automatically log in the user
                // or redirect to login screen
                return;
            }

            // Handle authentication success
            // The server sends individual user fields
            String username = message.getFromBody("username");
            String nickname = message.getFromBody("nickname");
            String email = message.getFromBody("email");

            System.out.println("Parsed user data - Username: " + username + ", Nickname: " + nickname + ", Email: " + email);
            client.setAuthenticated(true);

            if (username != null && !username.isEmpty()) {
                com.example.main.service.NetworkService networkService = com.example.main.models.App.getInstance().getNetworkService();
                if (networkService != null) {
                    User user = new User();
                    user.setUsername(username);
                    user.setNickname(nickname);
                    user.setEmail(email);
                    App.getInstance().setCurrentUser(user);
                    networkService.setCurrentUser(user);
                    System.out.println("Authenticated user set in App singleton: " + user.getUsername());

                    // Switch to the main menu screen on the main GDX thread
                    Gdx.app.postRunnable(() -> {
                        Main.getInstance().setScreen(new GDXMainMenu(networkService));
                    });
                } else {
                    System.err.println("NetworkService is null, cannot set authenticated user.");
                }
            } else {
                System.err.println("Username is null in authentication success message");
            }
        } catch (Exception e) {
            System.err.println("Error parsing authentication success: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAuthFailed(Message message) {
        try {
            String reason = message.getFromBody("reason");
            System.err.println("Authentication failed: " + reason);
            client.setAuthenticatedUser(null);
        } catch (Exception e) {
            System.err.println("Error parsing authentication failed message: " + e.getMessage());
        }
    }

    private void handleGameState(Message message) {
        try {
            Game game = message.getFromBody("game");
            client.setCurrentGame(game);
            System.out.println("Received game state update");
        } catch (Exception e) {
            System.err.println("Error parsing game state: " + e.getMessage());
        }
    }

    private void handlePlayerAction(Message message) {
        try {
            String action = message.getFromBody("action");
            Object actionData = message.getFromBody("actionData");
            String senderId = message.getFromBody("senderId");
            System.out.println("Received player action: " + action + " from " + senderId);

            // Integrate with game logic for synced actions
            if ("talk".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String receiverUsername = (String) map.get("receiverUsername");
                String text = (String) map.get("message");

                // Apply on the main GDX thread to safely touch UI/state
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        System.out.println("[CLIENT] Applying remote talk from " + senderUsername + " to " + receiverUsername + ": " + text);
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteTalk(senderUsername, receiverUsername, text);
                    }
                });
            }
            else if ("build_barn_or_coop".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String buildingKey = (String) map.get("buildingKey");
                int x = ((Number) map.get("x")).intValue();
                int y = ((Number) map.get("y")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteBuildBarnOrCoop(senderUsername, buildingKey, x, y);
                    }
                });
            }
            else if ("purchase".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String name = (String) map.get("name");
                int amount = ((Number) map.get("amount")).intValue();
                Integer shopX = map.get("shopX") instanceof Number ? ((Number) map.get("shopX")).intValue() : null;
                Integer shopY = map.get("shopY") instanceof Number ? ((Number) map.get("shopY")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemotePurchase(senderUsername, name, amount, shopX, shopY);
                    }
                });
            }
            else if ("buy_animal".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String animalKey = (String) map.get("animalKey");
                int housingId = ((Number) map.get("housingId")).intValue();
                String givenName = (String) map.get("givenName");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteBuyAnimal(senderUsername, animalKey, housingId, givenName);
                    }
                });
            }
            else if ("sell".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String itemName = (String) map.get("itemName");
                int amount = ((Number) map.get("amount")).intValue();
                Integer x = map.get("x") instanceof Number ? ((Number) map.get("x")).intValue() : null;
                Integer y = map.get("y") instanceof Number ? ((Number) map.get("y")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteSell(senderUsername, itemName, amount, x, y);
                    }
                });
            }
            else if ("cheat_add_item".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String itemName = (String) map.get("itemName");
                int quantity = ((Number) map.get("quantity")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCheatAddItem(senderUsername, itemName, quantity);
                    }
                });
            }
            else if ("cheat_add_crafting_recipe".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String recipeName = (String) map.get("recipeName");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCheatAddCraftingRecipe(senderUsername, recipeName);
                    }
                });
            }
            else if ("cheat_add_cooking_recipe".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String recipeName = (String) map.get("recipeName");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCheatAddCookingRecipe(senderUsername, recipeName);
                    }
                });
            }
            else if ("craft_item".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String recipeName = (String) map.get("recipeName");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCraftItem(senderUsername, recipeName);
                    }
                });
            }
            else if ("cook_food".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String recipeName = (String) map.get("recipeName");
                Integer senderX = map.get("senderX") != null ? ((Number) map.get("senderX")).intValue() : null;
                Integer senderY = map.get("senderY") != null ? ((Number) map.get("senderY")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCookFood(senderUsername, recipeName, senderX, senderY);
                    }
                });
            }
            else if ("eat".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String foodName = (String) map.get("foodName");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteEat(senderUsername, foodName);
                    }
                });
            }
            else if ("start_artisan_process".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                int machineX = ((Number) map.get("machineX")).intValue();
                int machineY = ((Number) map.get("machineY")).intValue();
                String recipeEnumName = (String) map.get("recipeEnumName");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteStartArtisanProcess(senderUsername, machineX, machineY, recipeEnumName);
                    }
                });
            }
            else if ("cancel_artisan_process".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                int machineX = ((Number) map.get("machineX")).intValue();
                int machineY = ((Number) map.get("machineY")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCancelArtisanProcess(senderUsername, machineX, machineY);
                    }
                });
            }
            else if ("collect_artisan_product".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                int machineX = ((Number) map.get("machineX")).intValue();
                int machineY = ((Number) map.get("machineY")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteCollectArtisanProduct(senderUsername, machineX, machineY);
                    }
                });
            }
            else if ("finish_artisan_process_now".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                int machineX = ((Number) map.get("machineX")).intValue();
                int machineY = ((Number) map.get("machineY")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteFinishArtisanProcessNow(senderUsername, machineX, machineY);
                    }
                });
            }
            else if ("place_machine".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String machineEnumName = (String) map.get("machineEnumName");
                int tileX = ((Number) map.get("tileX")).intValue();
                int tileY = ((Number) map.get("tileY")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemotePlaceMachine(senderUsername, machineEnumName, tileX, tileY);
                    }
                });
            }
            else if ("move_random_food_to_refrigerator".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteMoveRandomFoodToRefrigerator(senderUsername);
                    }
                });
            }
            else if ("hug".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String receiverUsername = (String) map.get("receiverUsername");
                Integer senderX = map.get("senderX") instanceof Number ? ((Number) map.get("senderX")).intValue() : null;
                Integer senderY = map.get("senderY") instanceof Number ? ((Number) map.get("senderY")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteHug(senderUsername, receiverUsername, senderX, senderY);
                    }
                });
            }
            else if ("flower_someone".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String receiverUsername = (String) map.get("receiverUsername");
                Integer senderX = map.get("senderX") instanceof Number ? ((Number) map.get("senderX")).intValue() : null;
                Integer senderY = map.get("senderY") instanceof Number ? ((Number) map.get("senderY")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteFlower(senderUsername, receiverUsername, senderX, senderY);
                    }
                });
            }
            else if ("gift_player".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String receiverUsername = (String) map.get("receiverUsername");
                String itemName = (String) map.get("itemName");
                int amount = ((Number) map.get("amount")).intValue();
                Integer senderX = map.get("senderX") instanceof Number ? ((Number) map.get("senderX")).intValue() : null;
                Integer senderY = map.get("senderY") instanceof Number ? ((Number) map.get("senderY")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteGift(senderUsername, receiverUsername, itemName, amount, senderX, senderY);
                    }
                });
            }
            else if ("rate_gift".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String giftId = (String) map.get("giftId");
                int rate = ((Number) map.get("rate")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteRateGift(senderUsername, giftId, rate);
                    }
                });
            }
            else if ("ask_marriage".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String receiverUsername = (String) map.get("receiverUsername");
                Integer senderX = map.get("senderX") instanceof Number ? ((Number) map.get("senderX")).intValue() : null;
                Integer senderY = map.get("senderY") instanceof Number ? ((Number) map.get("senderY")).intValue() : null;
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteAskMarriage(senderUsername, receiverUsername, senderX, senderY);
                        com.example.main.GDXviews.GDXGameScreen screen = (com.example.main.GDXviews.GDXGameScreen) current;
                        try {
                            java.lang.reflect.Field stateField = com.example.main.GDXviews.GDXGameScreen.class.getDeclaredField("currentFriendsMenuState");
                            stateField.setAccessible(true);
                            Object state = stateField.get(screen);
                            if (state != null) {
                                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                                    try {
                                        java.lang.reflect.Method m = com.example.main.GDXviews.GDXGameScreen.class.getDeclaredMethod("createFriendsMenuUI");
                                        m.setAccessible(true);
                                        m.invoke(screen);
                                    } catch (Exception ignored) {}
                                });
                            }
                        } catch (Exception ignored) {}
                    }
                });
            }
            else if ("respond_marriage".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                String proposerUsername = (String) map.get("proposerUsername");
                String response = (String) map.get("response");
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteRespondMarriage(senderUsername, proposerUsername, response);
                    }
                });
            }
            else if ("finish_quest".equals(action) && actionData instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) actionData;
                String senderUsername = (String) map.get("senderUsername");
                int questId = ((Number) map.get("questId")).intValue();
                com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.Screen current = com.example.main.Main.getInstance().getScreen();
                    if (current instanceof com.example.main.GDXviews.GDXGameScreen) {
                        ((com.example.main.GDXviews.GDXGameScreen) current).applyRemoteFinishQuest(senderUsername, questId);
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error parsing player action: " + e.getMessage());
        }
    }

    private void handleTurnChange(Message message) {
        String playerId = message.getFromBody("playerId");
        System.out.println("Turn changed to player: " + playerId);
        // Here you would update the UI to show whose turn it is
    }

    private void handleHeartbeat() {
        // Simply acknowledge the heartbeat
        sendHeartbeatMessage();
    }

    private void handleError(Message message) {
        String error = message.getFromBody("error");
        System.err.println("Server error: " + error);
    }

    private void handleCustomMessage(Message message) {
        // Override this method in your game-specific implementation
        // to handle custom game messages
        System.out.println("Received custom message: " + message.getType());
    }

    private void sendHeartbeatMessage() {
        HashMap<String, Object> heartbeatData = new HashMap<>();
        Message heartbeatMessage = new Message(heartbeatData, MessageType.HEARTBEAT);
        client.sendMessage(heartbeatMessage);
    }

    private void handleLobbyJoin(Message message) {
        try {
            System.out.println("Handling lobby join success message: " + message.getBody());
            String lobbyId = message.getFromBody("lobbyId");
            if (lobbyId == null || lobbyId.isEmpty()) {
                System.err.println("Invalid lobbyId in lobby join message");
                return;
            }

            System.out.println("Joined lobby: " + lobbyId);

            // Notify the controller about successful lobby join
            if (client.getControllerCallback() instanceof NetworkLobbyController) {
                NetworkLobbyController controller = (NetworkLobbyController) client.getControllerCallback();
                controller.onLobbyJoinSuccess(lobbyId);
            } else {
                System.err.println("Controller callback is not NetworkLobbyController");
            }
        } catch (Exception e) {
            System.err.println("Error handling lobby join: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLobbyLeave(Message message) {
        System.out.println("Left lobby");
        // You can add lobby state management here
    }

    private void handleUpdatePlayerPositions(Message message) {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return;
        }

        try {
            Map<String, Map<String, Double>> positions = message.getFromBody("positions");

            for (Map.Entry<String, Map<String, Double>> entry : positions.entrySet()) {
                String username = entry.getKey();
                Map<String, Double> coords = entry.getValue();

                User userToUpdate = currentGame.getUserByUsername(username);

                if (userToUpdate != null && userToUpdate.currentPlayer() != null) {
                    int newX = coords.get("x").intValue();
                    int newY = coords.get("y").intValue();
                    userToUpdate.currentPlayer().setCurrentX(newX);
                    userToUpdate.currentPlayer().setCurrentY(newY);
                }
            }
        } catch (Exception e) {
            System.err.println("[CLIENT ERROR] Failed to process player position update: " + e.getMessage());
        }
    }

    private void handleLobbyAdminChange(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            String newAdminUsername = message.getFromBody("newAdminUsername");
            System.out.println("Lobby admin changed for lobby: " + lobbyId + " - New admin: " + newAdminUsername);
            // You can update the lobby admin in the UI
        } catch (Exception e) {
            System.err.println("Error handling lobby admin change: " + e.getMessage());
        }
    }

    private void handleLobbyPlayersUpdate(Message message) {
        try {
            Map<String, Object> playersMap = message.getFromBody("players");
            if (playersMap == null) return;

            List<String> playerNames = new ArrayList<>();
            for (Object userObject : playersMap.values()) {
                if (userObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> userMap = (Map<String, Object>) userObject;
                    if (userMap.containsKey("username")) {
                        playerNames.add((String) userMap.get("username"));
                    }
                }
            }

            // **THE FIX IS HERE**

            // 1. Get the persistent controller instance.
            NetworkLobbyController controller = App.getInstance().getNetworkService().getLobbyController();
            if (controller != null) {
                // 2. Update the controller's internal state.
                controller.updateLobbyPlayers(playerNames);
            }

            // 3. Get the current screen and update the UI.
            Screen currentScreen = Main.getInstance().getScreen();
            if (currentScreen instanceof GDXLobbyScreen) {
                Gdx.app.postRunnable(() -> {
                    ((GDXLobbyScreen) currentScreen).updatePlayerList(playerNames);
                });
            }
        } catch (Exception e) {
            System.err.println("Error handling lobby players update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLobbyReady(Message message) {
        try {
            boolean ready = message.getFromBody("ready");
            System.out.println("Ready status set to: " + ready);
            // You can update the ready status UI here
        } catch (Exception e) {
            System.err.println("Error handling lobby ready: " + e.getMessage());
        }
    }

    private void handleLobbyStartGame(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            System.out.println("Game starting for lobby: " + lobbyId);
            // You can transition to the game here
        } catch (Exception e) {
            System.err.println("Error handling lobby start game: " + e.getMessage());
        }
    }

    private void handleLobbyJoinSuccess(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            String lobbyName = message.getFromBody("lobbyName");
            Boolean isAdmin = message.getFromBody("isAdmin");

            if (lobbyId == null || lobbyName == null || isAdmin == null) { /* ... error handling ... */ return; }

            // **FIX 2**: Update the controller's state *before* switching screens.
            NetworkLobbyController controller = App.getInstance().getNetworkService().getLobbyController();
            if (controller != null) {
                controller.setHostStatus(isAdmin);
            } else {
                System.err.println("CRITICAL: NetworkService or LobbyController is null.");
                return; // Abort if controller can't be found
            }

            // Now that the state is set, switch to the new screen.
            Gdx.app.postRunnable(() -> {
                Main.getInstance().setScreen(new GDXLobbyScreen(lobbyId, lobbyName));
            });

        } catch (Exception e) { /* ... error handling ... */ }
    }

    private void handleOnlineUsersUpdate(Message message) {
        try {
            String usersJson = message.getFromBody("users");
            System.out.println("Received online users update: " + usersJson);

            // Parse the JSON string to extract user information
            java.util.List<Object> onlineUsers = new java.util.ArrayList<>();

            // Simple JSON parsing - remove brackets and split by comma
            if (usersJson != null && !usersJson.isEmpty() && !usersJson.equals("[]")) {
                // Remove outer brackets
                String innerJson = usersJson.substring(1, usersJson.length() - 1);

                // Split by "},{" to get individual user objects
                String[] userStrings = innerJson.split("\\},\\{");

                for (int i = 0; i < userStrings.length; i++) {
                    String userString = userStrings[i];
                    // Fix brackets if needed
                    if (i == 0 && !userString.startsWith("{")) {
                        userString = "{" + userString;
                    }
                    if (i == userStrings.length - 1 && !userString.endsWith("}")) {
                        userString = userString + "}";
                    }

                    // Extract username from the JSON string
                    // This is a simple approach - in a real implementation you might want to use a proper JSON parser
                    String username = "Unknown";
                    if (userString.contains("\"username\":\"")) {
                        int start = userString.indexOf("\"username\":\"") + 13; // length of ""username":""
                        int end = userString.indexOf("\"", start);
                        if (end > start) {
                            username = userString.substring(start, end);
                        }
                    }

                    System.out.println("Online user: " + username);
                    onlineUsers.add(username);
                }
            }

            System.out.println("Online users count: " + onlineUsers.size());

            // Update controller if available
            Object callback = client.getControllerCallback();
            if (callback instanceof com.example.main.controller.NetworkLobbyController) {
                com.example.main.controller.NetworkLobbyController controller =
                    (com.example.main.controller.NetworkLobbyController) callback;
                controller.updateOnlineUsersFromServer(onlineUsers);
            }
        } catch (Exception e) {
            System.err.println("Error handling online users update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAvailableLobbiesUpdate(Message message) {
        try {
            List<Map<String, Object>> lobbies = message.getFromBody("lobbies");
            System.out.println("[CLIENT LOG] Received AVAILABLE_LOBBIES_UPDATE with " + (lobbies != null ? lobbies.size() : "null") + " lobbies.");

            Screen currentScreen = Main.getInstance().getScreen();
            System.out.println("[CLIENT LOG] Current screen is: " + (currentScreen != null ? currentScreen.getClass().getSimpleName() : "null"));

            // *** THE FIX IS HERE ***
            // We now check for the correct screen: GDXOnlineLobbiesMenu
            if (currentScreen instanceof GDXOnlineLobbiesMenu) {
                System.out.println("[CLIENT LOG] Screen is GDXOnlineLobbiesMenu. Posting UI update to main thread.");
                Gdx.app.postRunnable(() -> {
                    // And we cast to GDXOnlineLobbiesMenu to call its update method
                    ((GDXOnlineLobbiesMenu) currentScreen).updateLobbyList(lobbies);
                });
            } else {
                System.err.println("[CLIENT LOG] Current screen is not GDXOnlineLobbiesMenu, cannot update lobby list.");
            }
        } catch (Exception e) {
            System.err.println("[CLIENT LOG] Error in handleAvailableLobbiesUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLobbyJoinFailed(Message message) {
        String reason = message.getFromBody("reason");
        System.err.println("Failed to join lobby: " + reason);

        Screen currentScreen = Main.getInstance().getScreen();
        if (currentScreen instanceof GDXOnlineLobbiesMenu) {
            Gdx.app.postRunnable(() -> {
                ((GDXOnlineLobbiesMenu) currentScreen).showStatus(reason);
            });
        }
    }

    private void handleLobbyFindResult(Message message) {
        boolean found = message.getFromBody("found");
        Screen currentScreen = Main.getInstance().getScreen();

        if (currentScreen instanceof GDXOnlineLobbiesMenu) {
            GDXOnlineLobbiesMenu lobbyMenu = (GDXOnlineLobbiesMenu) currentScreen;
            if (found) {
                String lobbyId = message.getFromBody("lobbyId");
                boolean isPrivate = message.getFromBody("isPrivate");
                // The screen will handle the next step (joining or showing password dialog)
                Gdx.app.postRunnable(() -> lobbyMenu.handleFoundLobby(lobbyId, isPrivate));
            } else {
                // Tell the screen the lobby was not found
                Gdx.app.postRunnable(() -> lobbyMenu.showStatus("Lobby with that ID not found."));
            }
        }
    }

    private void handleNavigateToPregame() {
        System.out.println("[CLIENT LOG] Received NAVIGATE_TO_PREGAME command from server.");
        // Use postRunnable to ensure the screen change happens on the main LibGDX thread.
        Gdx.app.postRunnable(() -> {
            Main game = Main.getInstance();
            // This will correctly switch to the pre-game menu screen.
            game.setScreen(new GDXPreGameMenu(game, App.getInstance().getNetworkService()));
        });
    }

    private void handleInitializeGame(Message message) {
        try {
            System.out.println("[CLIENT LOG] Received INITIALIZE_GAME. Deserializing snapshot.");

            // --- THIS IS THE CORRECTED LOGIC ---

            // 1. Get the raw, generic object from the message body. At this point, it's a LinkedTreeMap.
            Object rawSnapshotObject = message.getFromBody("snapshot");

            // 2. Create a new Gson instance to perform a second, explicit conversion.
            Gson gson = new Gson();

            // 3. Convert the generic object into a specific GameStateSnapshot object.
            // This is the standard and correct way to handle nested objects with Gson.
            GameStateSnapshot snapshot = gson.fromJson(gson.toJson(rawSnapshotObject), GameStateSnapshot.class);

            // --- END OF CORRECTION ---

            if (snapshot == null) {
                System.err.println("[CLIENT ERROR] GameStateSnapshot was null after conversion.");
                return;
            }

            // The rest of the logic remains the same.
            Game newGame = createGameFromSnapshot(snapshot);
            App.getInstance().setCurrentGame(newGame);

            System.out.println("[CLIENT LOG] Game state created. Publishing navigation event.");
            EventBus.getInstance().publish(new NavigateToGameScreenEvent());

        } catch (Exception e) {
            System.err.println("[CLIENT ERROR] Failed to initialize game from snapshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handlePlayerLeave(Message message) {
        try {
            String username = message.getFromBody("username");
            if (username != null) {
                System.out.println("[CLIENT LOG] Player " + username + " has left the game.");
                Game currentGame = App.getInstance().getCurrentGame();
                if (currentGame != null) {
                    // Use Gdx.app.postRunnable to ensure the game state is modified
                    // on the main LibGDX thread, which is safe for rendering.
                    Gdx.app.postRunnable(() -> {
                        currentGame.removePlayer(username);
                    });
                }
                System.out.println("[CLIENT LOG] Player " + username + " left. Publishing event.");
                // Publish the event that the GameMenuController is listening for.
                EventBus.getInstance().publish(new PlayerDisconnectedEvent(username));
            }
        } catch (Exception e) {
            System.err.println("[CLIENT ERROR] Failed to process PLAYER_LEAVE message: " + e.getMessage());
        }
    }

    private void handleRequestGameMap(Message message) {
        System.out.println("[CLIENT LOG] Received REQUEST_GAME_MAP from server. Publishing local event.");
        // The message handler's job is done. It just announces that the request happened.
        EventBus.getInstance().publish(new RequestMapSnapshotEvent());
    }

    private void handleSyncGameMap(Message message) {
        System.out.println("[CLIENT LOG] Received SYNC_GAME_MAP from server. Deserializing snapshot.");
        try {
            // --- THIS IS THE CORRECTED LOGIC ---

            // 1. Get the raw object, which is a LinkedTreeMap.
            Object rawSnapshotObject = message.getFromBody("gameMapSnapshot");

            // 2. Create a new Gson instance to perform the explicit conversion.
            Gson gson = new Gson();

            // 3. Convert the generic map into our specific GameMapSnapshot object.
            GameMapSnapshot snapshot = gson.fromJson(gson.toJson(rawSnapshotObject), GameMapSnapshot.class);

            // --- END OF CORRECTION ---

            if (snapshot != null) {
                // Publish the event. The GDXGameScreen is listening for this.
                System.out.println("[CLIENT LOG] Snapshot converted successfully. Publishing sync event.");
                EventBus.getInstance().publish(new GameMapSyncEvent(snapshot));
            } else {
                System.err.println("[CLIENT ERROR] GameMapSnapshot was null after conversion on guest client.");
            }
        } catch (Exception e) {
            System.err.println("[CLIENT ERROR] Failed to process SYNC_GAME_MAP message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Game createGameFromSnapshot(GameStateSnapshot snapshot) {
        ArrayList<User> playersInGame = new ArrayList<>();
        User hostUser = null;
        User thisClientUser = null; // A variable to hold our local user

        // Get the username of the person running this instance of the game.
        String myUsername = App.getInstance().getCurrentUser().getUsername();

        for (GameStateSnapshot.PlayerSnapshot pSnap : snapshot.getPlayers()) {
            // Create the User and Player objects for everyone in the match
            User user = new User(pSnap.getUsername(), "", "", "", pSnap.getGender());
            Player player = new Player(pSnap.getUsername(), pSnap.getGender());

            int index = pSnap.getPlayerIndex();
            player.setOriginX(4 + (index % 2) * 80);
            player.setOriginY(4 + (index / 2) * 30);
            player.setCurrentX(player.originX());
            player.setCurrentY(player.originY());
            user.setCurrentPlayer(player);
            user.setFarmTheme(snapshot.getFarmThemes().get(index));
            playersInGame.add(user);

            // --- THIS IS THE FIX ---
            // Check if the user we just created is the one playing on this machine.
            if (user.getUsername().equals(myUsername)) {
                thisClientUser = user; // If so, we've found ourself!
            }
            // --- END OF FIX ---

            if (user.getUsername().equals(snapshot.getHostUsername())) {
                hostUser = user;
            }
        }

        // Create the main Game object
        Game newGame = new Game(playersInGame);
        newGame.setMainPlayer(hostUser); // Set the host (mainPlayer)
        newGame.setLocalPlayerUser(thisClientUser); // Set THIS client's user

        // CRITICAL: Set the current user/player on this client to the local player
        if (thisClientUser != null && thisClientUser.currentPlayer() != null) {
            newGame.setCurrentUser(thisClientUser);
            newGame.setCurrentPlayer(thisClientUser.currentPlayer());
        }

        return newGame;
    }
}
