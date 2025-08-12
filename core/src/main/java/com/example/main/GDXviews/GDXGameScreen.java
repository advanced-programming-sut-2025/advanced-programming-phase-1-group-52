package com.example.main.GDXviews;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.GDXmodels.TextureManager;
import com.example.main.Main;
import com.example.main.controller.GameMenuController;
import com.example.main.controller.NetworkLobbyController;
import com.example.main.controller.StoreMenuController;
import com.example.main.enums.design.FarmThemes;
import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.ShopType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.AnimalType;
import com.example.main.enums.items.ArtisanProductType;
import com.example.main.enums.items.CageType;
import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.CraftingRecipes;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.TrashCanType;
import com.example.main.enums.items.TreeType;
import com.example.main.enums.player.Skills;
import static com.example.main.enums.player.Skills.Farming;
import static com.example.main.enums.player.Skills.Fishing;
import static com.example.main.enums.player.Skills.Foraging;
import static com.example.main.enums.player.Skills.Mining;
import com.example.main.events.EventBus;
import com.example.main.events.GameMapSyncEvent;
import com.example.main.events.RequestMapSnapshotEvent;
import com.example.main.models.*;
import com.example.main.models.Date;
import com.example.main.models.FishingMinigame;
import com.example.main.models.Friendship;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.GameMapSnapshot;
import com.example.main.models.NPC;
import com.example.main.models.NPCFriendship;
import com.example.main.models.Notification;
import com.example.main.models.Player;
import com.example.main.models.Quest;
import com.example.main.models.Result;
import com.example.main.models.Tile;
import com.example.main.models.Time;
import com.example.main.models.Trade;
import com.example.main.models.TradeRequest;
import com.example.main.models.Tree;
import com.example.main.models.User;
import com.example.main.models.building.Housing;
import com.example.main.models.item.CookingRecipe;
import com.example.main.models.item.CraftingMachine;
import com.example.main.models.item.CraftingRecipe;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fish;
import com.example.main.models.item.Food;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.Item;
import com.example.main.models.item.PlacedMachine;
import com.example.main.models.item.PurchasedAnimal;
import com.example.main.models.item.Seed;
import com.example.main.models.item.Tool;
import com.example.main.models.item.TrashCan;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;
import com.example.main.service.NetworkService;

public class GDXGameScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private GameMap gameMap;
    private GameMenuController controller;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;

    private final NetworkLobbyController networkController;
    private final Player localPlayer;
    private int lastSentX = -1;
    private int lastSentY = -1;

    private float cameraSpeed = 200f;
    private boolean cameraFollowsPlayer = true;

    private boolean showMinimap = false;
    private boolean mKeyPressed = false;

          private boolean showTradeMenu = false;
    private boolean tKeyPressed = false;
    private Table tradeMenuTable;

          private enum TradeMenuState {
        NEW_TRADE_NOTIFICATIONS,
        MAIN_MENU,
        PLAYER_SELECTION,
        TRADE_TYPE_SELECTION,
        TRADE_DETAILS,
        ACTIVE_TRADES,
        TRADE_HISTORY
    }

    private TradeMenuState currentTradeMenuState = TradeMenuState.MAIN_MENU;
    private List<String> newTradeNotifications = new ArrayList<>();

          private String selectedPlayerForTrade = null;
    private String selectedTradeType = null;
    private String itemName = "";
    private int itemAmount = 1;
    private int itemPrice = 1;
    private String givingItemName = "";
    private int givingItemAmount = 1;
    private String receivingItemName = "";
    private int receivingItemAmount = 1;
    private String tradeErrorMessage = "";

          private boolean showShopMenu = false;
    private Table shopMenuTable;
    private ShopType currentShopType = null;
    private StoreMenuController shopController;

          private boolean isBuildingPlacementMode = false;
    private String buildingToPlace = null;
    private Texture buildingPlacementTexture = null;
    private float buildingPlacementX = 0f;
    private float buildingPlacementY = 0f;

          private enum ShopMenuState {
        MAIN_MENU,
        ALL_PRODUCTS,
        AVAILABLE_PRODUCTS
    }

    private ShopMenuState currentShopMenuState = ShopMenuState.MAIN_MENU;
    private String shopResultMessage = "";
    private boolean shopResultSuccess = false;

          private float timeAccumulator = 0f;
    private static final float SECONDS_PER_10_IN_GAME_MINUTES = 5f;

    private TextureManager textureManager;

          private Texture clockTexture;
    private BitmapFont hudFont;

    private Texture rainTexture;
    private Texture snowTexture;
    private Array<Rectangle> rainParticles;
    private Array<Rectangle> snowParticles;
    private float weatherStateTime = 0f;

          private boolean isInventoryOpen = false;
    private Stage inventoryStage;
    private Texture inventoryBackground;
    private Table menuContentTable;
    private Table mainInventoryContainer;

          private boolean isToolMenuOpen = false;
    private Stage toolMenuStage;
    private ArrayList<Tool> playerTools;
    private int currentToolIndex = 0;
    private Image currentToolImage;
    private Label currentToolLabel;

          private float stormEffectTimer = 0f;
    private float nextLightningTime = 0f;
    private boolean lightningActive = false;
    private float lightningDuration = 0f;
    private boolean cheatLightningActive = false;

    private boolean isPlantingMode = false;
    private Tile plantingTargetTile = null;
    private Label plantingPromptLabel;

          private boolean isCheatMenuOpen = false;
    private Stage cheatMenuStage;
    private TextField cheatItemNameField;
    private TextField cheatItemQuantityField;

    private InputMultiplexer multiplexer;

    private Label generalMessageLabel;
    private float generalMessageTimer = 0f;
    private static final float GENERAL_MESSAGE_DURATION = 3.0f;

    private boolean isPlantingSelectionOpen = false;
    private Stage plantingStage;
    private ArrayList<Item> plantableItems;
    private int selectedPlantableIndex = 0;
    private Label plantingItemNameLabel;
    private Image plantingSelectionHighlight;
    private HorizontalGroup plantingItemsGroup;

    private Texture crowTexture;
    private ArrayList<CrowAnimation> crowAnimations;

    private PlayerActionState playerActionState = PlayerActionState.IDLE;
    private float actionTimer = 0f;
    private static final float ACTION_ANIMATION_DURATION = 0.5f;
          private boolean isCraftingMenuOpen = false;
    private Stage craftingStage;
    private Table craftingMenuContentTable;

          private boolean isCookingMenuOpen = false;
    private Stage cookingStage;
    private Table cookingMenuContentTable;
    private Table fridgeContentTable;

          private boolean isCraftingCheatMenuOpen = false;
    private Stage craftingCheatMenuStage;
    private TextField cheatCraftingRecipeField;

    private boolean isCookingCheatMenuOpen = false;
    private Stage cookingCheatMenuStage;
    private TextField cheatCookingRecipeField;

          private boolean isEatMenuOpen = false;
    private Stage eatMenuStage;
    private ArrayList<Food> playerFoodItems;
    private int selectedFoodIndex = 0;

          private boolean isTradeMenuOpen = false;
    private Stage tradeMenuStage;
    private Table tradeMenuRoot;

    private enum TradeMenuPage {
        MAIN,
        SELECT_PLAYER,
        HISTORY
    }
    private TradeMenuPage tradeMenuPage = TradeMenuPage.MAIN;

          private boolean isTradeWaitingForResponse = false;
    private String tradeInviteTargetUsername = null;
    private String tradeInviteSenderUsername = null;

    private boolean isLiveTradeOpen = false;
    private String liveTradePartnerUsername = null;
    private String liveTradeInitiatorUsername = null;
    private boolean liveTradeAmInitiator = false;
    private Table liveTradeRoot;
    private Table liveTradeLeftList;
    private Table liveTradeRightList;
    private Label liveTradeGivingLabel;
    private Label liveTradeReceivingLabel;
    private String liveGivingItemName = null;
    private int liveGivingAmount = 0;
    private String liveReceivingItemName = null;
    private int liveReceivingAmount = 0;
    private int liveTradeIdInitiator = -1;
    private int liveTradeIdReceiver = -1;
    private Dialog tradeWaitingDialog;
    private Window tradeInvitePopup;

    private void showTradeMainMenu() {
        if (tradeMenuStage == null) tradeMenuStage = new Stage(new ScreenViewport());
        if (tradeMenuRoot != null) tradeMenuRoot.remove();
        tradeMenuPage = TradeMenuPage.MAIN;

        tradeMenuRoot = new Table();
        tradeMenuRoot.setFillParent(true);
        tradeMenuRoot.center();

        Stack stack = new Stack();
        stack.add(new Image(inventoryBackground));

        Table content = new Table();
        Label title = new Label("Trade", skin);
        title.setFontScale(1.2f);
        content.add(title).pad(20).row();

        TextButton startTrade = new TextButton("Start Trade", skin);
        TextButton tradeHistoryBtn = new TextButton("Trade History", skin);
        TextButton closeBtn = new TextButton("Close", skin);

        startTrade.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x, float y){ showTradeSelectPlayer(); }
        });
        tradeHistoryBtn.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x, float y){ showTradeHistory(); }
        });
        closeBtn.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x, float y){ hideTradeMenu(); isTradeMenuOpen = false; Gdx.input.setInputProcessor(multiplexer); }
        });

        content.add(startTrade).width(260).pad(10).row();
        content.add(tradeHistoryBtn).width(260).pad(10).row();
        content.add(closeBtn).width(260).pad(10).row();

        stack.add(content);
        tradeMenuRoot.add(stack).width(Gdx.graphics.getWidth() * 0.6f).height(Gdx.graphics.getHeight() * 0.7f);
        tradeMenuStage.addActor(tradeMenuRoot);
    }

    private void showTradeSelectPlayer() {
        if (tradeMenuStage == null) tradeMenuStage = new Stage(new ScreenViewport());
        if (tradeMenuRoot != null) tradeMenuRoot.remove();
        tradeMenuPage = TradeMenuPage.SELECT_PLAYER;

        tradeMenuRoot = new Table();
        tradeMenuRoot.setFillParent(true);
        tradeMenuRoot.center();

        Stack stack = new Stack();
        stack.add(new Image(inventoryBackground));

        Table content = new Table();
        content.add(new Label("Select a player to trade with", skin)).pad(20).row();

        Table list = new Table();
        for (User user : game.getPlayers()) {
            if (user == null || user.getPlayer() == null) continue;
            if (game.getCurrentPlayer() != null && user.getUsername().equals(game.getCurrentPlayer().getUsername())) continue;
            if (user.getUsername().startsWith("empty_slot_")) continue;
            TextButton playerBtn = new TextButton(user.getUsername(), skin);
            playerBtn.addListener(new ClickListener(){
                @Override public void clicked(InputEvent event, float x, float y){
                                          com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("senderUsername", game.getCurrentPlayer().getUsername());
                        data.put("targetUsername", user.getUsername());
                        ns.sendPlayerAction("trade_invite", data);
                        isTradeWaitingForResponse = true;
                        tradeInviteTargetUsername = user.getUsername();
                        if (tradeWaitingDialog != null) tradeWaitingDialog.hide();
                        tradeWaitingDialog = new Dialog("Waiting", skin);
                        tradeWaitingDialog.text("Waiting for " + user.getUsername() + " to respond...");
                        tradeWaitingDialog.button("Cancel", true);
                        tradeWaitingDialog.show(tradeMenuStage);
                        tradeWaitingDialog.setModal(true);
                        tradeWaitingDialog.setMovable(false);
                                                  final String targetName = user.getUsername();
                        Dialog dlgRef = tradeWaitingDialog;
                        tradeWaitingDialog = new Dialog("Waiting", skin) {
                            @Override
                            protected void result(Object obj) {
                                boolean cancel = Boolean.TRUE.equals(obj);
                                if (cancel) {
                                    isTradeWaitingForResponse = false;
                                    if (dlgRef != null) dlgRef.hide();
                                }
                            }
                        };
                    }
                }
            });
            list.add(playerBtn).width(240).pad(6).row();
        }

        ScrollPane scroll = new ScrollPane(list, skin);
        scroll.setFadeScrollBars(false);
        content.add(scroll).width(320).height(320).pad(10).row();

        Table bottom = new Table();
        TextButton backBtn = new TextButton("Back", skin);
        TextButton closeBtn = new TextButton("Close", skin);
        backBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ showTradeMainMenu(); }});
        closeBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ hideTradeMenu(); isTradeMenuOpen=false; Gdx.input.setInputProcessor(multiplexer);} });
        bottom.add(backBtn).pad(10);
        bottom.add(closeBtn).pad(10);
        content.add(bottom).padBottom(10);

        stack.add(content);
        tradeMenuRoot.add(stack).width(Gdx.graphics.getWidth() * 0.6f).height(Gdx.graphics.getHeight() * 0.7f);
        tradeMenuStage.addActor(tradeMenuRoot);
    }

    private void showTradeHistory() {
        if (tradeMenuStage == null) tradeMenuStage = new Stage(new ScreenViewport());
        if (tradeMenuRoot != null) tradeMenuRoot.remove();
        tradeMenuPage = TradeMenuPage.HISTORY;

        tradeMenuRoot = new Table();
        tradeMenuRoot.setFillParent(true);
        tradeMenuRoot.center();

        Stack stack = new Stack();
        stack.add(new Image(inventoryBackground));

        Table content = new Table();
        content.add(new Label("Your Trades", skin)).pad(20).row();

        com.example.main.controller.TradeMenuController tradeController = new com.example.main.controller.TradeMenuController();
        com.example.main.models.Result res = tradeController.listTrades();
        String tradesText = res != null ? res.Message() : "";

        Table tradesList = new Table();
        if (tradesText != null && !tradesText.isEmpty()) {
            String[] entries = tradesText.split("\\r?\\n\\r?\\n");
            for (String entry : entries) {
                if (entry.trim().isEmpty()) continue;
                Table card = new Table(skin);
                card.setBackground("default-round");
                card.add(new Label(entry.trim(), skin)).left().pad(10).row();
                tradesList.add(card).expandX().fillX().pad(6).row();
            }
        } else {
            tradesList.add(new Label("No trades to show.", skin)).pad(10).row();
        }

        ScrollPane scroll = new ScrollPane(tradesList, skin);
        scroll.setFadeScrollBars(false);
        content.add(scroll).width(500).height(360).pad(10).row();

        Table bottom = new Table();
        TextButton backBtn = new TextButton("Back", skin);
        TextButton closeBtn = new TextButton("Close", skin);
        backBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ showTradeMainMenu(); }});
        closeBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ hideTradeMenu(); isTradeMenuOpen=false; Gdx.input.setInputProcessor(multiplexer);} });
        bottom.add(backBtn).pad(10);
        bottom.add(closeBtn).pad(10);
        content.add(bottom).padBottom(10);

        stack.add(content);
        tradeMenuRoot.add(stack).width(Gdx.graphics.getWidth() * 0.7f).height(Gdx.graphics.getHeight() * 0.75f);
        tradeMenuStage.addActor(tradeMenuRoot);
    }

    private void hideTradeMenu() {
        if (tradeMenuRoot != null) { tradeMenuRoot.remove(); tradeMenuRoot = null; }
    }

          public void onTradeInviteReceived(String senderUsername, String targetUsername) {
        if (game == null || controller == null) return;
        if (game.getCurrentPlayer() == null) return;
        if (!game.getCurrentPlayer().getUsername().equals(targetUsername)) return;

                  if (tradeInvitePopup != null) {
            tradeInvitePopup.remove();
            tradeInvitePopup = null;
        }
        tradeInvitePopup = new Window("Trade Request", skin);
        tradeInvitePopup.setModal(false);
        tradeInvitePopup.setMovable(false);
        Table content = new Table();
        content.add(new Label(senderUsername + " wants to trade with you.", skin)).pad(8).row();
        Table buttons = new Table();
        TextButton acceptBtn = new TextButton("Accept", skin);
        TextButton rejectBtn = new TextButton("Reject", skin);
        buttons.add(acceptBtn).pad(5);
        buttons.add(rejectBtn).pad(5);
        content.add(buttons).padBottom(6).row();
        tradeInvitePopup.add(content).pad(6);
        tradeInvitePopup.pack();
        float x = Gdx.graphics.getWidth() - tradeInvitePopup.getWidth() - 20f;
        float y = 20f;
        tradeInvitePopup.setPosition(x, y);
        stage.addActor(tradeInvitePopup);
        tradeInvitePopup.toFront();

        acceptBtn.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x, float y){
                if (tradeInvitePopup != null) { tradeInvitePopup.remove(); tradeInvitePopup = null; }
                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("requesterUsername", senderUsername);
                    data.put("responderUsername", game.getCurrentPlayer().getUsername());
                    data.put("accepted", true);
                    ns.sendPlayerAction("trade_invite_response", data);
                }
                openLiveTrade(senderUsername, game.getCurrentPlayer().getUsername(), false);
            }
        });
        rejectBtn.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x2, float y2){
                if (tradeInvitePopup != null) { tradeInvitePopup.remove(); tradeInvitePopup = null; }
                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("requesterUsername", senderUsername);
                    data.put("responderUsername", game.getCurrentPlayer().getUsername());
                    data.put("accepted", false);
                    ns.sendPlayerAction("trade_invite_response", data);
                }
            }
        });
    }

    public void onTradeInviteResponse(String requesterUsername, String responderUsername, boolean accepted) {
        if (game == null || controller == null) return;
        if (game.getCurrentPlayer() == null) return;
        if (!game.getCurrentPlayer().getUsername().equals(requesterUsername)) return;

        isTradeWaitingForResponse = false;
        if (!accepted) {
            generalMessageLabel.setText(responderUsername + " rejected your trade request.");
            generalMessageLabel.setVisible(true);
            generalMessageTimer = 4f;
            showTradeMainMenu();
            return;
        }
                  openLiveTrade(requesterUsername, responderUsername, true);
    }

    private void openLiveTrade(String initiatorUsername, String partnerUsername, boolean amInitiator) {
        isLiveTradeOpen = true;
        liveTradeInitiatorUsername = initiatorUsername;
        liveTradePartnerUsername = partnerUsername;
        liveTradeAmInitiator = amInitiator;
        isTradeMenuOpen = true;
        if (tradeMenuStage == null) tradeMenuStage = new Stage(new ScreenViewport());
        if (liveTradeRoot != null) liveTradeRoot.remove();

        liveGivingItemName = null; liveGivingAmount = 0;
        liveReceivingItemName = null; liveReceivingAmount = 0;

        liveTradeRoot = new Table();
        liveTradeRoot.setFillParent(true);
        liveTradeRoot.center();

        Stack stack = new Stack();
        stack.add(new Image(inventoryBackground));

        Table content = new Table();
        content.add(new Label("Live Trade: " + initiatorUsername + " ↔ " + partnerUsername, skin)).pad(10).row();

        Table lists = new Table();
        liveTradeLeftList = new Table();
        liveTradeRightList = new Table();
        String selfUsername = game.getCurrentPlayer().getUsername();
        String leftOwner = amInitiator ? selfUsername : partnerUsername;
        String rightOwner = amInitiator ? partnerUsername : selfUsername;
        boolean leftEnableAdd = amInitiator;
        boolean rightEnableAdd = amInitiator;
        populateInventoryList(liveTradeLeftList, leftOwner, leftEnableAdd);
        populateInventoryList(liveTradeRightList, rightOwner, rightEnableAdd);
        ScrollPane leftScroll = new ScrollPane(liveTradeLeftList, skin);
        ScrollPane rightScroll = new ScrollPane(liveTradeRightList, skin);
        leftScroll.setFadeScrollBars(false);
        rightScroll.setFadeScrollBars(false);
        lists.add(new Label(amInitiator ? "Your Inventory" : partnerUsername + "'s Inventory", skin)).pad(5);
        lists.add().width(20f);
        lists.add(new Label(amInitiator ? partnerUsername + "'s Inventory" : "Your Inventory", skin)).pad(5).row();
        lists.add(leftScroll).width(380).height(360).pad(10);
        lists.add().width(20f);
        lists.add(rightScroll).width(380).height(360).pad(10).row();
        content.add(lists).row();

        Table summary = new Table();
        liveTradeGivingLabel = new Label("Giving: -", skin);
        liveTradeReceivingLabel = new Label("Receiving: -", skin);
        summary.add(liveTradeGivingLabel).pad(8);
        summary.add(liveTradeReceivingLabel).pad(8).row();
        content.add(summary).row();

        Table bottom = new Table();
        TextButton closeBtn = new TextButton("Close", skin);
        closeBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ closeLiveTrade(); }});
        bottom.add(closeBtn).pad(10);
        if (amInitiator) {
            TextButton tradeBtn = new TextButton("Trade", skin);
            tradeBtn.addListener(new ClickListener(){
                @Override public void clicked(InputEvent e, float x, float y){
                                          com.example.main.controller.TradeMenuController tmc = new com.example.main.controller.TradeMenuController();
                    com.example.main.models.Result createRes = tmc.tradeRequest(partnerUsername, liveGivingItemName, String.valueOf(liveGivingAmount), liveReceivingItemName, String.valueOf(liveReceivingAmount));
                                          int tradeId = findMatchingTradeId(liveTradeInitiatorUsername, liveTradePartnerUsername, liveGivingItemName, liveGivingAmount, liveReceivingItemName, liveReceivingAmount);
                    liveTradeIdInitiator = tradeId;
                                          com.example.main.service.NetworkService nsInit = com.example.main.models.App.getInstance().getNetworkService();
                    if (nsInit != null) {
                        java.util.HashMap<String, Object> req = new java.util.HashMap<>();
                        req.put("senderUsername", liveTradeInitiatorUsername);
                        req.put("receiverUsername", liveTradePartnerUsername);
                        req.put("givingItemName", liveGivingItemName);
                        req.put("givingAmount", liveGivingAmount);
                        req.put("receivingItemName", liveReceivingItemName);
                        req.put("receivingAmount", liveReceivingAmount);
                        nsInit.sendPlayerAction("trade_request", req);
                    }
                                          com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("initiator", initiatorUsername);
                        data.put("partner", partnerUsername);
                        data.put("givingItemName", liveGivingItemName);
                        data.put("givingAmount", liveGivingAmount);
                        data.put("receivingItemName", liveReceivingItemName);
                        data.put("receivingAmount", liveReceivingAmount);
                        data.put("tradeId", tradeId);
                        ns.sendPlayerAction("trade_finalize_request", data);
                    }
                }
            });
            bottom.add(tradeBtn).pad(10);
        }
        content.add(bottom).pad(10).row();

        stack.add(content);
        liveTradeRoot.add(stack).width(Gdx.graphics.getWidth() * 0.9f).height(Gdx.graphics.getHeight() * 0.85f);
        tradeMenuStage.addActor(liveTradeRoot);
        Gdx.input.setInputProcessor(tradeMenuStage);
    }

    private void populateInventoryList(Table list, String ownerUsername, boolean enableAdd) {
        User u = game.getUserByUsername(ownerUsername);
        if (u == null || u.getPlayer() == null) return;
        java.util.ArrayList<Item> items = u.getPlayer().getInventory().getItems();
        list.clear();
        for (Item item : items) {
            if (item == null) continue;
            Table row = new Table(skin);
            row.setBackground("default-round");
            row.add(new Label(item.getName(), skin)).left().pad(6);
            TextField amountField = new TextField("1", skin);
            amountField.setMessageText("amount");
            row.add(amountField).width(60).pad(6);
            TextButton addBtn = new TextButton("Add", skin);
            addBtn.setDisabled(!enableAdd);
            addBtn.addListener(new ClickListener(){
                @Override public void clicked(InputEvent e, float x, float y){
                    if (!enableAdd) return;
                    int amt = 1;
                    try { amt = Integer.parseInt(amountField.getText()); } catch (Exception ignored) {}
                    if (ownerUsername.equals(game.getCurrentPlayer().getUsername())) {
                        liveGivingItemName = item.getName();
                        liveGivingAmount = amt;
                    } else {
                        liveReceivingItemName = item.getName();
                        liveReceivingAmount = amt;
                    }
                    updateLiveTradeSummary();
                                          com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("initiator", liveTradeInitiatorUsername);
                        data.put("partner", liveTradePartnerUsername);
                        data.put("givingItemName", liveGivingItemName);
                        data.put("givingAmount", liveGivingAmount);
                        data.put("receivingItemName", liveReceivingItemName);
                        data.put("receivingAmount", liveReceivingAmount);
                        ns.sendPlayerAction("trade_live_update", data);
                    }
                }
            });
            row.add(addBtn).pad(6);
            list.add(row).expandX().fillX().pad(4).row();
        }
    }

    private void updateLiveTradeSummary() {
        if (liveTradeGivingLabel != null) {
            String g = (liveGivingItemName != null) ? (liveGivingAmount + " x " + liveGivingItemName) : "-";
            liveTradeGivingLabel.setText("Giving: " + g);
        }
        if (liveTradeReceivingLabel != null) {
            String r = (liveReceivingItemName != null) ? (liveReceivingAmount + " x " + liveReceivingItemName) : "-";
            liveTradeReceivingLabel.setText("Receiving: " + r);
        }
    }

    private void closeLiveTrade() {
        isLiveTradeOpen = false;
        if (liveTradeRoot != null) { liveTradeRoot.remove(); liveTradeRoot = null; }
        isTradeMenuOpen = false;
        Gdx.input.setInputProcessor(multiplexer);
    }

          public void onTradeLiveUpdate(String initiator, String partner, String givingItemName, int givingAmount, String receivingItemName, int receivingAmount) {
                  if (!isLiveTradeOpen) return;
        if (!(initiator.equals(liveTradeInitiatorUsername) && partner.equals(liveTradePartnerUsername))) return;
                  liveGivingItemName = givingItemName;
        liveGivingAmount = givingAmount;
        liveReceivingItemName = receivingItemName;
        liveReceivingAmount = receivingAmount;
        updateLiveTradeSummary();
    }

    public void onTradeFinalizeRequest(String initiator, String partner, String givingItemName, int givingAmount, String receivingItemName, int receivingAmount, int tradeIdFromInitiator) {
        if (game == null || controller == null) return;
        if (game.getCurrentPlayer() == null) return;
        String me = game.getCurrentPlayer().getUsername();
                  if (!me.equals(partner)) return;
        liveGivingItemName = givingItemName;
        liveGivingAmount = givingAmount;
        liveReceivingItemName = receivingItemName;
        liveReceivingAmount = receivingAmount;
        liveTradeIdReceiver = tradeIdFromInitiator;
        updateLiveTradeSummary();

        Dialog dialog = new Dialog("Finalize Trade", skin) {
            @Override
            protected void result(Object obj) {
                boolean accept = Boolean.TRUE.equals(obj);
                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("initiator", initiator);
                    data.put("partner", partner);
                    data.put("accepted", accept);
                    ns.sendPlayerAction("trade_finalize_response", data);
                }
                if (!accept) {
                    closeLiveTrade();
                }
            }
        };
        dialog.text("Accept trade: You receive " + receivingAmount + " x " + receivingItemName + " and give " + givingAmount + " x " + givingItemName + "?");
        dialog.button("Accept", true);
        dialog.button("Reject", false);
        dialog.setModal(true);
        dialog.show(tradeMenuStage != null ? tradeMenuStage : stage);
    }

    public void onTradeFinalizeResponse(String initiator, String partner, boolean accepted) {
        if (game == null || controller == null) return;
        if (game.getCurrentPlayer() == null) return;
        String me = game.getCurrentPlayer().getUsername();
        if (!me.equals(initiator)) return;
        if (!accepted) {
            generalMessageLabel.setText(partner + " rejected your trade.");
            generalMessageLabel.setVisible(true);
            generalMessageTimer = 4f;
            closeLiveTrade();
            return;
        }
                  if (liveReceivingItemName == null || liveGivingItemName == null || liveGivingAmount <= 0 || liveReceivingAmount <= 0) {
            generalMessageLabel.setText("Incomplete trade selection.");
            generalMessageLabel.setVisible(true);
            generalMessageTimer = 3f;
            return;
        }
                  int tradeId = liveTradeIdInitiator > 0 ? liveTradeIdInitiator : findMatchingTradeId(liveTradeInitiatorUsername, liveTradePartnerUsername, liveGivingItemName, liveGivingAmount, liveReceivingItemName, liveReceivingAmount);
        if (tradeId <= 0) {
            generalMessageLabel.setText("Trade not found.");
            generalMessageLabel.setVisible(true);
            generalMessageTimer = 3f;
            return;
        }
        com.example.main.controller.TradeMenuController tmc = new com.example.main.controller.TradeMenuController();
        com.example.main.models.Result res = tmc.respondToTrade("accept", String.valueOf(tradeId));
        generalMessageLabel.setText(res.Message());
        generalMessageLabel.setVisible(true);
        generalMessageTimer = 4f;
        closeLiveTrade();

                  com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
        if (ns != null) {
            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
            data.put("initiator", initiator);
            data.put("partner", partner);
            data.put("accepted", true);
            data.put("givingItemName", liveGivingItemName);
            data.put("givingAmount", liveGivingAmount);
            data.put("receivingItemName", liveReceivingItemName);
            data.put("receivingAmount", liveReceivingAmount);
            ns.sendPlayerAction("trade_respond", data);
        }
    }

    private int findMatchingTradeId(String initiator, String partner, String givingItem, int givingAmt, String receivingItem, int receivingAmt) {
                  User initiatorUser = game.getUserByUsername(initiator);
        if (initiatorUser != null && initiatorUser.getPlayer() != null) {
            java.util.ArrayList<Trade> trades = initiatorUser.getPlayer().getTrades();
            for (int i = trades.size() - 1; i >= 0; i--) {
                Trade t = trades.get(i);
                if (t instanceof TradeRequest) {
                    TradeRequest tr = (TradeRequest) t;
                    if (!t.isAnswered()
                        && t.getBuyer() != null && t.getSeller() != null
                        && ((t.getBuyer().getUsername().equals(initiator) && t.getSeller().getUsername().equals(partner))
                            || (t.getBuyer().getUsername().equals(partner) && t.getSeller().getUsername().equals(initiator)))
                        && tr.getGivingItemName().equals(givingItem)
                        && tr.getGivingAmount() == givingAmt
                        && tr.getReceivingItemName().equals(receivingItem)
                        && tr.getReceivingAmount() == receivingAmt) {
                        return t.getTradeId();
                    }
                }
            }
        }
        return -1;
    }

          public void applyRemoteTradeRequest(String senderUsername, String receiverUsername, String givingItemName, int givingAmount, String receivingItemName, int receivingAmount) {
        if (game == null || controller == null) return;
        com.example.main.models.User prevUser = game.getUrrentUser();
        com.example.main.models.Player prevPlayer = game.getCurrentPlayer();
        com.example.main.models.User senderUser = game.getUserByUsername(senderUsername);
        com.example.main.models.Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            com.example.main.controller.TradeMenuController tmc = new com.example.main.controller.TradeMenuController();
            tmc.tradeRequest(receiverUsername, givingItemName, String.valueOf(givingAmount), receivingItemName, String.valueOf(receivingAmount));
                          com.example.main.models.User receiverUser = game.getUserByUsername(receiverUsername);
            if (receiverUser != null && receiverUser.getPlayer() != null) {
                              }
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }


    public void applyRemoteRespondToTrade(String initiatorUsername, String partnerUsername, boolean accepted,
                                          String givingItemName, int givingAmount, String receivingItemName, int receivingAmount) {
        if (!accepted) { closeLiveTrade(); return; }
        if (game == null) return;

        com.example.main.models.User prevUser = game.getUrrentUser();
        com.example.main.models.Player prevPlayer = game.getCurrentPlayer();
        com.example.main.models.User initiatorUser = game.getUserByUsername(initiatorUsername);
        com.example.main.models.Player initiatorPlayer = initiatorUser != null ? initiatorUser.getPlayer() : null;
        if (initiatorUser == null || initiatorPlayer == null) return;
        int tradeId = -1;
        try {
            game.setCurrentUser(initiatorUser);
            game.setCurrentPlayer(initiatorPlayer);
            tradeId = findMatchingTradeId(initiatorUsername, partnerUsername, givingItemName, givingAmount, receivingItemName, receivingAmount);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
        if (tradeId <= 0) return;


        com.example.main.models.User partnerUser = game.getUserByUsername(partnerUsername);
        com.example.main.models.Player partnerPlayer = partnerUser != null ? partnerUser.getPlayer() : null;
        if (partnerUser == null || partnerPlayer == null) return;
        try {
            game.setCurrentUser(partnerUser);
            game.setCurrentPlayer(partnerPlayer);
            com.example.main.controller.TradeMenuController tmc = new com.example.main.controller.TradeMenuController();
            tmc.respondToTrade("accept", String.valueOf(tradeId));
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
        closeLiveTrade();
    }
    private Image selectedFoodImage;
    private Label selectedFoodLabel;


    private Texture eatingTexture;
    private float eatingAnimationTimer = 0f;
    private static final float EATING_ANIMATION_DURATION = 0.75f;

    private boolean isMachinePlacementMode = false;
    private CraftingMachine machineToPlace = null;
    private Texture machinePlacementTexture = null;
    private Stage machineUiStage;
    private PlacedMachine activeMachine = null;
    private int activeMachineTileX = -1;
    private int activeMachineTileY = -1;


    private boolean isFishingMinigameActive = false;
    private Stage fishingStage;
    private Texture fishingBarBg, playerBarTexture, fishIconTexture, legendaryCrownTexture;
    private Texture progressBarBg, progressBarFill;
    private boolean isTrashModeActive = false;

    private boolean isInfoMenuOpen = false;
    private Stage infoStage;
    private boolean isQuestMenuOpen = false;
    private boolean isJournalMenuOpen = false;

    private boolean showReactionMenu = false;
    private HorizontalGroup emojiGroup;
    private Texture selectedEmojiTexture;
    private float emojiDisplayTime = 0;
    private static final float EMOJI_DISPLAY_DURATION = 5.0f;
    private ArrayList<Image> emojiImages = new ArrayList<>();
    private int selectedEmojiIndex = 0;
    private ScrollPane emojiScrollPane;
    private TextButton leftArrowButton;
    private TextButton rightArrowButton;

    private static class ReactionState { Texture texture; float remainingSeconds; }
    private java.util.Map<String, ReactionState> usernameToReaction = new java.util.HashMap<>();

    private Texture ground1Texture;
    private Texture ground2Texture;
    private Texture grass1Texture;
    private Texture grass2Texture;
    private Texture shoveledTexture;
    private Texture tree1Texture;
    private Texture tree2Texture;
    private Texture tree3Texture;
    private Texture house1Texture;
    private Texture house2Texture;
    private Texture house3Texture;
    private Texture npcHouse1Texture;
    private Texture npcHouse2Texture;
    private Texture npcHouse3Texture;
    private Texture npcHouse4Texture;
    private Texture npcHouse5Texture;
    private Texture bushTexture;
    private Texture stone1Texture;
    private Texture stone2Texture;
    private Texture copperStoneTexture;
    private Texture ironStoneTexture;
    private Texture goldStoneTexture;
    private Texture iridiumStoneTexture;
    private Texture jewelStoneTexture;

    private Texture blacksmithTexture;
    private Texture jojamartTexture;
    private Texture pierresShopTexture;
    private Texture carpentersShopTexture;
    private Texture fishShopTexture;
    private Texture ranchTexture;
    private Texture saloonTexture;

    private Texture lake1Texture;
    private Texture lake2Texture;
    private Texture lake3Texture;

    private Texture barnTexture;
    private Texture coopTexture;

   private Texture trashCanTexture;
    private Texture copperTrashCanTexture;
    private Texture steelTrashCanTexture;
    private Texture goldTrashCanTexture;
    private Texture iridiumTrashCanTexture;

   private Map<AnimalType, Texture> animalTextures = new HashMap<>();

   private boolean showHousingMenu = false;
    private Housing selectedHousing = null;
    private Table housingMenuTable = null;

   private float animalMovementTimer = 0f;
    private static final float ANIMAL_MOVEMENT_UPDATE_INTERVAL = 3.0f;

    private Texture maleIdleTexture;
    private Texture maleDown1Texture, maleDown2Texture;
    private Texture maleUp1Texture, maleUp2Texture;
    private Texture maleLeft1Texture, maleLeft2Texture;
    private Texture maleRight1Texture, maleRight2Texture;

    private Texture femaleIdleTexture;
    private Texture femaleDown1Texture, femaleDown2Texture;
    private Texture femaleUp1Texture, femaleUp2Texture;
    private Texture femaleLeft1Texture, femaleLeft2Texture;
    private Texture femaleRight1Texture, femaleRight2Texture;

   private Texture maleHoeIdle1Texture, maleHoeIdle2Texture;
    private Texture maleScytheIdle1Texture, maleScytheIdle2Texture;
    private Texture maleWatercanIdle1Texture, maleWatercanIdle2Texture;
    private Texture malePlantingIdleTexture;

    private Texture femaleHoeIdle1Texture, femaleHoeIdle2Texture;
    private Texture femaleScytheIdle1Texture, femaleScytheIdle2Texture;
    private Texture femaleWatercanIdle1Texture, femaleWatercanIdle2Texture;
    private Texture femalePlantingIdleTexture;

   private Texture sebastianTexture;
    private Texture abigailTexture;
    private Texture harveyTexture;
    private Texture liaTexture;
    private Texture robinTexture;

   private Texture dialogBoxTexture;
    private Texture menuBackgroundTexture;

    private Texture maxEnergyBuffTexture;
    private Texture miningBuffTexture;
    private Texture farmingBuffTexture;
    private Texture foragingBuffTexture;
    private Texture fishingBuffTexture;


    private ArrayList<NPC> nearbyNPCs = new ArrayList<>();
    private NPC currentDialogNPC = null;
    private String currentDialogMessage = "";
    private boolean showDialog = false;
    private ArrayList<NPC> clickedNPCs = new ArrayList<>();


    private boolean showNPCMenu = false;
    private NPC selectedNPC = null;
    private Table npcMenuTable;


    private enum NPCMenuState {
        MAIN_MENU,
        FRIENDSHIP,
        QUESTS,
        GIFT
    }

    private NPCMenuState currentNPCMenuState = NPCMenuState.MAIN_MENU;


    private String questResultMessage = "";
    private boolean questResultSuccess = false;

    private int[][] baseGroundMap;
    private int[][] grassVariantMap;
    private int[][] treeVariantMap;
    private int[] playerHouseVariants;
    private int[] npcHouseVariants;
    private int[][] stoneVariantMap;
    private int[][] waterVariantMap;
    private Random random;

    private enum PlayerDirection {
        DOWN, UP, LEFT, RIGHT, DOWN_LEFT, DOWN_RIGHT, UP_LEFT, UP_RIGHT
    }

    private enum PlayerActionState {
        IDLE,
        WALKING,
        WATERING,
        TILLING,
        HARVESTING,
        PLANTING
    }

    private PlayerDirection playerDirection = PlayerDirection.DOWN;
    private boolean playerMoving = false;
    private float playerAnimationTime = 0f;
    private static final float ANIMATION_SPEED = 0.15f;
    private static final float PLAYER_MOVE_SPEED = 4.5f;
    private float playerMoveProgress = 0f;
    private int playerTargetX, playerTargetY;
    private float toolRotation = 0f;

    private float scoreboardUpdateTimer = 0f;
    private static final float SCOREBOARD_UPDATE_INTERVAL = 0.5f;

    private static final int[][] HOUSE_POSITIONS = {
        {1, 1},
        {81, 1},
        {1, 31},
        {81, 31}
    };

    private static final int[][] HOUSE_AREAS = {
        {1, 8, 1, 8},
        {81, 88, 1, 8},
        {1, 8, 31, 38},
        {81, 88, 31, 38}
    };

    private static final int[][] NPC_HOUSE_POSITIONS = {
        {32, 40},
        {42, 40},
        {52, 40},
        {37, 50},
        {47, 50}
    };

    private static final int[][] NPC_HOUSE_AREAS = {
        {42, 46, 40, 46},
        {52, 56, 40, 46},
        {32, 36, 50, 56},
        {42, 46, 50, 56},
        {52, 56, 50, 56}
    };

    private static final int[][] SHOP_POSITIONS = {
        {33, 3},
        {47, 3},
        {33, 13},
        {47, 13},
        {33, 23},
        {47, 23},
        {33, 33}
    };

    private static final int[][] SHOP_AREAS = {
        {32, 39, 2, 9},
        {46, 53, 2, 9},
        {32, 39, 12, 19},
        {46, 53, 12, 19},
        {32, 39, 22, 29},
        {46, 53, 22, 29},
        {32, 39, 32, 39}
    };

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 90;
    private static final int MAP_HEIGHT = 60;


    private boolean isGiftMode = false;
    private String giftResultMessage = "";
    private boolean giftResultSuccess = false;


    private boolean showAnimalMenu = false;
    private PurchasedAnimal selectedAnimal = null;
    private Table animalMenuTable = null;


    private boolean isShepherdMode = false;
    private String animalToShepherd = null;


    private ArrayList<AnimationEffect> activeAnimations = new ArrayList<>();


    private static class AnimationEffect {
        float x, y;
        float duration;
        float elapsed;
        String type;
        float bounceHeight = 0f;
        float bounceSpeed = 0f;

        AnimationEffect(float x, float y, float duration, String type) {
            this.x = x;
            this.y = y;
            this.duration = duration;
            this.elapsed = 0f;
            this.type = type;
            if (type.equals("bounce")) {
                this.bounceSpeed = 8f;
            }
        }
    }


    private boolean showFriendsMenu = false;
    private boolean fKeyPressed = false;
    private Table friendsMenuTable;


    private enum FriendsMenuState {
        MAIN_MENU,
        PLAYER_ACTIONS,
        FRIENDSHIP_DETAILS,
        CHAT_ROOM,
        GIFT_MENU,
        RECEIVED_GIFTS,
        GIFT_HISTORY,
        SEND_GIFT,
        MARRIAGE_MENU,
        MARRIAGE_RESPOND
    }

    private FriendsMenuState currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
    private String selectedPlayerForActions = null;
    private String friendshipResultMessage = "";
    private boolean friendshipResultSuccess = false;


    private TextField chatMessageField;
    private ScrollPane chatScrollPane;
    private Table chatMessagesTable;


    private Map<String, Boolean> unreadTalkNotifications = new HashMap<>();

    private Map<String, Boolean> unreadGiftNotifications = new HashMap<>();


    private Table giftMenuTable;
    private Table receivedGiftsTable;
    private Table giftHistoryTable;
    private ScrollPane receivedGiftsScrollPane;
    private ScrollPane giftHistoryScrollPane;
    private TextField giftRatingField;
    private String selectedGiftId = null;


    private boolean showSellMenu = false;
    private Table sellMenuTable;
    private ScrollPane sellMenuScrollPane;
    private String sellErrorMessage = "";
    private boolean showSellError = false;


    private Table sendGiftTable;
    private ScrollPane sendGiftScrollPane;
    private String giftErrorMessage = "";
    private boolean showGiftError = false;


    private String marriageErrorMessage = "";
    private boolean showMarriageError = false;
    private String marriageProposer = null;
    private Label loadingLabel;

    private Table scoreboardContainer;
    private Table scoreboardDisplayTable;
    private Label scoreboardTitle;

    private enum SortMode {
        BALANCE,
        SKILL_EXPERIENCE,
        FINISHED_QUESTS
    }
    private SortMode currentSortMode = SortMode.BALANCE;

    public GDXGameScreen() {
        System.out.println("[GAMESCREEN] Constructor START");
        controller = new GameMenuController();
        shopController = new StoreMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));


        loadingLabel = new Label("Loading Map...", skin);
        Table loadingTable = new Table();
        loadingTable.setFillParent(true);
        loadingTable.center();
        loadingTable.add(loadingLabel);
        stage.addActor(loadingTable);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        progressBarStyle.knobBefore = skin.newDrawable("white", Color.GREEN);

        skin.add("default-horizontal", progressBarStyle);
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        shapeRenderer = new ShapeRenderer();

        float worldWidth = MAP_WIDTH * TILE_SIZE;
        float worldHeight = MAP_HEIGHT * TILE_SIZE;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenWidth, screenHeight);
        hudCamera.setToOrtho(false, screenWidth, screenHeight);
        camera.zoom = 0.85f;
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();

        generalMessageLabel = new Label("", skin);
        generalMessageLabel.setVisible(false);
        plantingPromptLabel = new Label("Double-click a seed to plant", skin);
        plantingPromptLabel.setColor(Color.LIME);
        plantingPromptLabel.setVisible(false);

        Table messageTable = new Table();
        messageTable.top().padTop(20);
        messageTable.setFillParent(true);
        messageTable.add(generalMessageLabel);
        messageTable.add(plantingPromptLabel);
        stage.addActor(messageTable);

        random = new Random();
        baseGroundMap = new int[MAP_WIDTH][MAP_HEIGHT];
        grassVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        treeVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        playerHouseVariants = new int[4];
        npcHouseVariants = new int[5];
        stoneVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        waterVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        craftingStage = new Stage(new ScreenViewport());
        cookingStage = new Stage(new ScreenViewport());
        craftingCheatMenuStage = new Stage(new ScreenViewport());
        cookingCheatMenuStage = new Stage(new ScreenViewport());
        eatMenuStage = new Stage(new ScreenViewport());
        machineUiStage = new Stage(new ScreenViewport());
        fishingStage = new Stage(new ScreenViewport());
        infoStage = new Stage(new ScreenViewport());

        textureManager = new TextureManager();
        textureManager.loadAllItemTextures();
        loadTextures();
        loadHudAssets();
        loadWeatherAssets();


        NetworkService networkService = App.getInstance().getNetworkService();
        this.networkController = networkService.getLobbyController();
        this.game = App.getInstance().getCurrentGame();
        controller.setGame(game);
        this.gameMap = null;
        this.localPlayer = game.getLocalPlayerUser().getPlayer();

        initializePlayerPosition();


        String myUsername = App.getInstance().getCurrentUser().getUsername();
        String hostUsername = game.getMainPlayer().getUsername();

        if (myUsername.equals(hostUsername)) {


            System.out.println("[GAMESCREEN - HOST] I am the host. Proactively generating and sending map.");


            ArrayList<FarmThemes> themes = new ArrayList<>();
            for (User user : game.getPlayers()) {
                if (!user.getUsername().startsWith("empty_slot_")) themes.add(user.getFarmTheme());
            }
            while (themes.size() < 4) themes.add(FarmThemes.Neutral);
            GameMap newMasterMap = new GameMap(game.getPlayers(), themes);
            game.setGameMap(newMasterMap);

            controller.setMap(newMasterMap);


            generateRandomMaps();


            GameMapSnapshot snapshot = createMapSnapshot();


            Message mapMessage = new Message(new HashMap<>(), MessageType.SEND_GAME_MAP);
            mapMessage.putInBody("gameMapSnapshot", snapshot);
            networkService.getClient().sendMessage(mapMessage);


            this.gameMap = newMasterMap;
            controller.setMap(this.gameMap);
            this.loadingLabel.setVisible(false);

        } else {


            System.out.println("[GAMESCREEN - GUEST] I am a guest. Waiting for map from server.");
        }


        inventoryStage = new Stage(new ScreenViewport());
        inventoryBackground = new Texture("content/Cut/menu_background.png");
        setupInventoryUI();
        setupToolMenuUI();
        setupCheatMenuUI();
        setupPlantingUI();
        setupCraftingUI();
        setupCookingUI();
        setupCraftingCheatMenuUI();
        setupCookingCheatMenuUI();
        setupEatMenuUI();
        setupInfoMenuUI();
        setupReactionMenu();
        setupScoreboardUI();

        crowAnimations = new ArrayList<>();
        playerFoodItems = new ArrayList<>();

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inventoryStage);
        multiplexer.addProcessor(toolMenuStage);
        multiplexer.addProcessor(cheatMenuStage);

        if (tradeMenuStage == null) tradeMenuStage = new Stage(new ScreenViewport());
        multiplexer.addProcessor(tradeMenuStage);
        multiplexer.addProcessor(craftingStage);
        multiplexer.addProcessor(cookingStage);
        multiplexer.addProcessor(cookingCheatMenuStage);
        multiplexer.addProcessor(craftingCheatMenuStage);
        multiplexer.addProcessor(eatMenuStage);
        multiplexer.addProcessor(machineUiStage);
        multiplexer.addProcessor(fishingStage);
        multiplexer.addProcessor(infoStage);
        plantableItems = new ArrayList<>();
        Gdx.input.setInputProcessor(multiplexer);


        subscribeToEvents();
        System.out.println("[GAMESCREEN] Constructor END. Now listening for events.");
    }

    private void subscribeToEvents() {
        EventBus.getInstance().subscribe(RequestMapSnapshotEvent.class, this::onHostNeedsToSendMap);
        EventBus.getInstance().subscribe(GameMapSyncEvent.class, this::onGuestNeedsToBuildMap);
    }

    private void loadHudAssets() {
        clockTexture = new Texture("content/clock/Clock.png");
        hudFont = new BitmapFont();
        hudFont.setColor(Color.WHITE);
        hudFont.getData().setScale(1.5f);
    }

    private void updateTime(float delta) {
        timeAccumulator += delta;
        if (timeAccumulator >= SECONDS_PER_10_IN_GAME_MINUTES) {
            timeAccumulator -= SECONDS_PER_10_IN_GAME_MINUTES;
            if (game != null) {
                game.advanceTimeByMinutes(10);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (gameMap != null) {
            if (scoreboardContainer != null && scoreboardContainer.isVisible()) {
                scoreboardUpdateTimer -= delta;
                if (scoreboardUpdateTimer <= 0f) {
                    updateScoreboard();
                    scoreboardUpdateTimer = SCOREBOARD_UPDATE_INTERVAL;
                }
            }

            if (eatingAnimationTimer > 0) {
                eatingAnimationTimer -= delta;
            }

            if (game.isCrowAttackHappened()) {
                triggerCrowAttackAnimation();
                game.resetCrowAttackFlag();
            }

            if (actionTimer > 0) {
                actionTimer -= delta;
                if (actionTimer <= 0) {
                    playerActionState = PlayerActionState.IDLE;
                }
            }
            handleInput(delta);
            updateAnimalMovement(delta);
            updateAnimations(delta);
            updateFishingMinigame(delta);

            if (!isInventoryOpen) {
                updateTime(delta);
            }


            if (generalMessageTimer > 0) {
                generalMessageTimer -= delta;
                if (generalMessageTimer <= 0) {
                    generalMessageLabel.setVisible(false);
                }
            }

            Player currentPlayer = game.getCurrentPlayer();
            if (currentPlayer != null && !currentPlayer.getNotifications().isEmpty()) {
                Notification notif = currentPlayer.getNotifications().get(0);
                generalMessageLabel.setText(notif.getMessage());
                generalMessageLabel.setColor(Color.CYAN);
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
            }

            if (currentPlayer != null && currentPlayer.getCurrentTool() != null) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.input.getY();
                com.badlogic.gdx.math.Vector3 mouseInWorld = camera.unproject(new com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0));
                float playerCenterX = (currentPlayer.currentX() * TILE_SIZE) + (TILE_SIZE / 2f);
                float playerCenterY = ((MAP_HEIGHT - 1 - currentPlayer.currentY()) * TILE_SIZE) + (TILE_SIZE / 2f);
                float deltaX = mouseInWorld.x - playerCenterX;
                float deltaY = mouseInWorld.y - playerCenterY;
                toolRotation = com.badlogic.gdx.math.MathUtils.atan2(deltaY, deltaX) * com.badlogic.gdx.math.MathUtils.radiansToDegrees;
            }

            Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();
            spriteBatch.setProjectionMatrix(camera.combined);
            spriteBatch.begin();
            renderMap();
            renderGiantCrops();
            renderAnimals();
            renderMachinePlacement(spriteBatch);

            if (game != null && game.getPlayers() != null && !usernameToReaction.isEmpty()) {
                for (User u : game.getPlayers()) {
                    if (u == null || u.getPlayer() == null || u.getUsername().startsWith("empty_slot_")) continue;
                    ReactionState rs = usernameToReaction.get(u.getUsername());
                    if (rs == null || rs.texture == null || rs.remainingSeconds <= 0f) continue;

                    rs.remainingSeconds -= delta;
                    if (rs.remainingSeconds <= 0f) { usernameToReaction.remove(u.getUsername()); continue; }

                    float worldX = u.getPlayer().currentX() * TILE_SIZE;
                    float worldY = (MAP_HEIGHT - 1 - u.getPlayer().currentY()) * TILE_SIZE;
                    Texture ptex = getPlayerTexture(u.getPlayer());
                    float playerHeight = ptex != null ? ptex.getHeight() * 2f : TILE_SIZE * 2f;
                    float scaledEmojiWidth = rs.texture.getWidth() * 0.5f;
                    float scaledEmojiHeight = rs.texture.getHeight() * 0.5f;
                    float emojiDrawX = worldX + (TILE_SIZE / 2f) - (scaledEmojiWidth / 2f);
                    float emojiDrawY = worldY + playerHeight + 6f;
                    spriteBatch.draw(rs.texture, emojiDrawX, emojiDrawY, scaledEmojiWidth, scaledEmojiHeight);
                }
            }
            spriteBatch.end();
            renderAnimations();
            renderWeather(delta);
            renderDayNightOverlay();
            renderCheatMenu(delta);
            renderPlantingUI(delta);

            if (showMinimap) {
                renderMinimap();
            }
            renderWeather(delta);
            renderHud();
            renderDayNightOverlay();
            renderToolMenu(delta);
            renderInventoryOverlay(delta);

            if (isBuildingPlacementMode && buildingPlacementTexture != null) {
                renderBuildingPlacementPreview();
            }

            renderShepherdModeUI();
            renderCrowAnimations(delta);
            renderCraftingMenu(delta);
            renderCookingMenu(delta);
            renderCraftingCheatMenu(delta);
            renderCookingCheatMenu(delta);
            renderEatMenu(delta);
            renderFriendsMenu(delta);
            renderSellMenu(delta);
            renderBuffs();
            renderFishingMinigame();
        }
        machineUiStage.act(delta);
        machineUiStage.draw();
        stage.act(delta);
        stage.draw();

        if (isTradeMenuOpen && tradeMenuStage != null) {
            tradeMenuStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            tradeMenuStage.act(delta);
            tradeMenuStage.draw();
        }
    }

    private void initializePlayerPosition() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i).getPlayer();
            if (player != null) {
                if (player.originX() == 0 && player.originY() == 0) {
                    if (i < HOUSE_POSITIONS.length) {
                        player.setOriginX(HOUSE_POSITIONS[i][0]);
                        player.setOriginY(HOUSE_POSITIONS[i][1] + 9);
                    }
                }

                player.setCurrentX(player.originX());
                player.setCurrentY(player.originY());
            }
        }

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null) {
            playerTargetX = currentPlayer.originX();
            playerTargetY = currentPlayer.originY();
        }
    }

    private void loadTextures() {
        ground1Texture = textureManager.getTexture("ground1");
        ground2Texture = textureManager.getTexture("ground2");
        grass1Texture = textureManager.getTexture("grass1");
        grass2Texture = textureManager.getTexture("grass2");
        shoveledTexture = textureManager.getTexture("shoveled");
        tree1Texture = textureManager.getTexture("tree1");
        tree2Texture = textureManager.getTexture("tree2");
        tree3Texture = textureManager.getTexture("tree3");
        house1Texture = textureManager.getTexture("player_house1");
        house2Texture = textureManager.getTexture("player_house2");
        house3Texture = textureManager.getTexture("player_house3");
        npcHouse1Texture = textureManager.getTexture("npc_house1");
        npcHouse2Texture = textureManager.getTexture("npc_house2");
        npcHouse3Texture = textureManager.getTexture("npc_house3");
        npcHouse4Texture = textureManager.getTexture("npc_house4");
        npcHouse5Texture = textureManager.getTexture("npc_house5");
        bushTexture = textureManager.getTexture("bush");
        stone1Texture = textureManager.getTexture("stone1");
        stone2Texture = textureManager.getTexture("stone2");
        copperStoneTexture = textureManager.getTexture("copper_stone");
        ironStoneTexture = textureManager.getTexture("iron_stone");
        goldStoneTexture = textureManager.getTexture("gold_stone");
        iridiumStoneTexture = textureManager.getTexture("iridium_stone");
        jewelStoneTexture = textureManager.getTexture("jewel_stone");
        crowTexture = textureManager.getTexture("Crow");
        eatingTexture = textureManager.getTexture("eating");
        maxEnergyBuffTexture = textureManager.getTexture("Max_Energy_Buff");
        miningBuffTexture = textureManager.getTexture("Mining_Skill_Icon");
        farmingBuffTexture = textureManager.getTexture("Farming_Skill_Icon");
        foragingBuffTexture = textureManager.getTexture("Foraging_Skill_Icon");
        fishingBuffTexture = textureManager.getTexture("Fishing_Skill_Icon");
        fishingBarBg = textureManager.getTexture("fishing_bar_background");
        playerBarTexture = textureManager.getTexture("fishing_player_bar");
        fishIconTexture = textureManager.getTexture("fishing_fish_icon");
        legendaryCrownTexture = textureManager.getTexture("fishing_legendary_crown");
        progressBarBg = textureManager.getTexture("fishing_progress_bar_background");
        progressBarFill = textureManager.getTexture("fishing_progress_bar_fill");
        try {
            blacksmithTexture = textureManager.getTexture("blacksmith");
            jojamartTexture = textureManager.getTexture("jojamart");
            pierresShopTexture = textureManager.getTexture("pierres_shop");
            carpentersShopTexture = textureManager.getTexture("carpenters_shop");
            fishShopTexture = textureManager.getTexture("fish_shop");
            ranchTexture = textureManager.getTexture("marines_ranch");
            saloonTexture = textureManager.getTexture("stardrop_saloon");
        } catch (Exception e) {
            blacksmithTexture = ground1Texture;
            jojamartTexture = ground1Texture;
            pierresShopTexture = ground1Texture;
            carpentersShopTexture = ground1Texture;
            fishShopTexture = ground1Texture;
            ranchTexture = ground1Texture;
            saloonTexture = ground1Texture;
        }

        lake1Texture = textureManager.getTexture("lake1");
        lake2Texture = textureManager.getTexture("lake2");
        lake3Texture = textureManager.getTexture("lake3");

        barnTexture = textureManager.getTexture("Barn");
        coopTexture = textureManager.getTexture("Coop");


        trashCanTexture = textureManager.getTexture("Trash_Can");
        copperTrashCanTexture = textureManager.getTexture("Trash_Can");
        steelTrashCanTexture = textureManager.getTexture("Trash_Can");
        goldTrashCanTexture = textureManager.getTexture("Trash_Can");
        iridiumTrashCanTexture = textureManager.getTexture("Trash_Can");

        maleIdleTexture = textureManager.getTexture("male_idle");
        maleDown1Texture = textureManager.getTexture("male_down1");
        maleDown2Texture = textureManager.getTexture("male_down2");
        maleUp1Texture = textureManager.getTexture("male_up1");
        maleUp2Texture = textureManager.getTexture("male_up2");
        maleLeft1Texture = textureManager.getTexture("male_left1");
        maleLeft2Texture = textureManager.getTexture("male_left2");
        maleRight1Texture = textureManager.getTexture("male_right1");
        maleRight2Texture = textureManager.getTexture("male_right2");

        maleHoeIdle1Texture = textureManager.getTexture("male_hoe_idle1");
        maleHoeIdle2Texture = textureManager.getTexture("male_hoe_idle2");
        maleScytheIdle1Texture = textureManager.getTexture("male_scyhte_idle1");
        maleScytheIdle2Texture = textureManager.getTexture("male_scyhte_idle2");
        maleWatercanIdle1Texture = textureManager.getTexture("male_watercan_idle1");
        maleWatercanIdle2Texture = textureManager.getTexture("male_watercan_idle2");
        malePlantingIdleTexture = textureManager.getTexture("male_planting_idle");

        femaleHoeIdle1Texture = textureManager.getTexture("male_hoe_idle1");
        femaleHoeIdle2Texture = textureManager.getTexture("male_hoe_idle2");
        femaleScytheIdle1Texture = textureManager.getTexture("male_scyhte_idle1");
        femaleScytheIdle2Texture = textureManager.getTexture("male_scyhte_idle2");
        femaleWatercanIdle1Texture = textureManager.getTexture("male_watercan_idle1");
        femaleWatercanIdle2Texture = textureManager.getTexture("male_watercan_idle2");
        femalePlantingIdleTexture = textureManager.getTexture("male_planting_idle");

        femaleIdleTexture = textureManager.getTexture("female_idle");
        femaleDown1Texture = textureManager.getTexture("female_down1");
        femaleDown2Texture = textureManager.getTexture("female_down2");
        femaleUp1Texture = textureManager.getTexture("female_up1");
        femaleUp2Texture = textureManager.getTexture("female_up2");
        femaleLeft1Texture = textureManager.getTexture("female_left1");
        femaleLeft2Texture = textureManager.getTexture("female_left2");
        femaleRight1Texture = textureManager.getTexture("female_right1");
        femaleRight2Texture = textureManager.getTexture("female_right2");


        sebastianTexture = textureManager.getTexture("sebastian");
        abigailTexture = textureManager.getTexture("abigail");
        harveyTexture = textureManager.getTexture("harvey");
        liaTexture = textureManager.getTexture("lia");
        robinTexture = textureManager.getTexture("robin");


        dialogBoxTexture = textureManager.getTexture("dialog_box");
        menuBackgroundTexture = textureManager.getTexture("menu_background");
        if (menuBackgroundTexture == null) {

            try {
                menuBackgroundTexture = new Texture(Gdx.files.internal("content/Cut/menu_background.png"));
            } catch (Exception e) {

                Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pm.setColor(Color.WHITE);
                pm.fill();
                menuBackgroundTexture = new Texture(pm);
                pm.dispose();
            }
        }


        loadAnimalTextures();
    }

    private void loadAnimalTextures() {

        for (AnimalType animalType : AnimalType.values()) {
            animalTextures.put(animalType, textureManager.getTexture(animalType.name()));
        }
    }

    private void loadWeatherAssets() {

        rainTexture = textureManager.getTexture("rain1");
        if (rainTexture == null) {
            try {
                rainTexture = new Texture(Gdx.files.internal("content/weather/rain1.png"));
            } catch (Exception ignored) {

                Pixmap pm = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
                pm.setColor(Color.WHITE);
                pm.fill();
                rainTexture = new Texture(pm);
                pm.dispose();
            }
        }

        snowTexture = textureManager.getTexture("snow");
        if (snowTexture == null) {
            try {
                snowTexture = new Texture(Gdx.files.internal("content/weather/snow.png"));
            } catch (Exception ignored) {
                Pixmap pm = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
                pm.setColor(Color.WHITE);
                pm.fill();
                snowTexture = new Texture(pm);
                pm.dispose();
            }
        }


        rainParticles = new Array<>();
        for (int i = 0; i < 200; i++) {
            rainParticles.add(new Rectangle(
                (float) (Math.random() * Gdx.graphics.getWidth()),
                (float) (Math.random() * Gdx.graphics.getHeight()),
                rainTexture.getWidth(),
                rainTexture.getHeight()
            ));
        }


        snowParticles = new Array<>();
        for (int i = 0; i < 100; i++) {
            snowParticles.add(new Rectangle(
                (float) (Math.random() * Gdx.graphics.getWidth()),
                (float) (Math.random() * Gdx.graphics.getHeight()),
                snowTexture.getWidth(),
                snowTexture.getHeight()
            ));
        }
    }

    private void generateRandomMaps() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                baseGroundMap[x][y] = random.nextInt(2);
                grassVariantMap[x][y] = random.nextInt(2);
                treeVariantMap[x][y] = random.nextInt(3);
                stoneVariantMap[x][y] = random.nextInt(2);

                float waterRandom = random.nextFloat();
                if (waterRandom < 0.6f) {
                    waterVariantMap[x][y] = 0;
                } else if (waterRandom < 0.8f) {
                    waterVariantMap[x][y] = 1;
                } else {
                    waterVariantMap[x][y] = 2;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            playerHouseVariants[i] = random.nextInt(3);
        }

        for (int i = 0; i < 5; i++) {
            npcHouseVariants[i] = i;
        }
    }


    private void handleInput(float delta) {
        if (isFishingMinigameActive) {
            return;
        }

        if (actionTimer > 0) {
            return;
        }

        if (showShopMenu) {
            return;
        }

        if (showAnimalMenu) {
            return;
        }

        if (isBuildingPlacementMode) {
            handleBuildingPlacement();
            handleCameraMovement(delta);
            return;
        }

        if (isShepherdMode) {
            handleShepherdMode();
            return;
        }

        if (showShopMenu) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }
            }
            return;
        }

        if (showAnimalMenu) {
            return;
        }


        if (isTradeMenuOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                hideTradeMenu();
                isTradeMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
            return;
        }


        if (showNPCMenu) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showNPCMenu = false;
                selectedNPC = null;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }
            }
            return;
        }


        if (showHousingMenu) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showHousingMenu = false;
                selectedHousing = null;
                if (housingMenuTable != null) {
                    housingMenuTable.remove();
                    housingMenuTable = null;
                }
            }
            return;
        }

        if (showFriendsMenu) {
           if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
            return;
        }

        if (isInventoryOpen || isToolMenuOpen || isCheatMenuOpen || isPlantingSelectionOpen
            || isCraftingMenuOpen || isCookingMenuOpen || isCraftingCheatMenuOpen || isCookingCheatMenuOpen
            || isEatMenuOpen || isInfoMenuOpen || isQuestMenuOpen || isJournalMenuOpen) {
            return;
        }

        com.badlogic.gdx.math.Vector3 mouseInWorld = camera.unproject(new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        int targetTileX = (int) (mouseInWorld.x / TILE_SIZE);
        int targetTileY = MAP_HEIGHT - 1 - (int) (mouseInWorld.y / TILE_SIZE);

        if (showReactionMenu && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            String[] emojiNames = {
                "+1", "face_with_raised_eyebrow", "face_with_symbols_on_mouth", "farm_dialog",
                "Good_Morning_dialog", "grin", "heart_eyes", "heart", "Hi_dialog", "joy",
                "neutral_face", "nice_dialog", "relieved", "sunglasses"
            };


            if (selectedEmojiIndex < emojiNames.length) {
                String emojiName = emojiNames[selectedEmojiIndex];
                selectedEmojiTexture = textureManager.getTexture(emojiName);
                emojiDisplayTime = EMOJI_DISPLAY_DURATION;

                if (game != null && game.getCurrentPlayer() != null && selectedEmojiTexture != null) {
                    ReactionState rs = new ReactionState();
                    rs.texture = selectedEmojiTexture;
                    rs.remainingSeconds = EMOJI_DISPLAY_DURATION;
                    usernameToReaction.put(game.getCurrentPlayer().getUsername(), rs);
                }

                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null && game != null && game.getCurrentPlayer() != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("senderUsername", game.getCurrentPlayer().getUsername());
                    data.put("emojiName", emojiName);
                    data.put("duration", EMOJI_DISPLAY_DURATION);
                    ns.sendPlayerAction("reaction", data);
                }
            }


            showReactionMenu = false;
            emojiScrollPane.setVisible(false);
            leftArrowButton.setVisible(false);
            rightArrowButton.setVisible(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (scoreboardContainer.isVisible()) {
                scoreboardContainer.setVisible(false);
            }
            else if (isInventoryOpen) {
                isInventoryOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
            else if (isPlantingSelectionOpen) {
                closePlantingMenu();
            }
            else {
                isInventoryOpen = true;
                showMainMenuButtons();
                Gdx.input.setInputProcessor(inventoryStage);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            isToolMenuOpen = !isToolMenuOpen;
            if (isToolMenuOpen) {
                playerTools = game.getCurrentPlayer().getTools();
                currentToolIndex = 0;
                updateToolMenuDisplay();
                Gdx.input.setInputProcessor(toolMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            isCheatMenuOpen = !isCheatMenuOpen;
            if(isCheatMenuOpen) {
                Gdx.input.setInputProcessor(cheatMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            isCraftingCheatMenuOpen = !isCraftingCheatMenuOpen;
            if (isCraftingCheatMenuOpen) {
                Gdx.input.setInputProcessor(craftingCheatMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            isTradeMenuOpen = !isTradeMenuOpen;
            if (isTradeMenuOpen) {
                Gdx.app.log("TradeMenu", "Opening trade menu");
                showTradeMainMenu();
                Gdx.input.setInputProcessor(tradeMenuStage);
            } else {
                Gdx.app.log("TradeMenu", "Closing trade menu");
                hideTradeMenu();
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            isCookingCheatMenuOpen = !isCookingCheatMenuOpen;
            if (isCookingCheatMenuOpen) {
                Gdx.input.setInputProcessor(cookingCheatMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            isCraftingMenuOpen = !isCraftingMenuOpen;
            if (isCraftingMenuOpen) {
                showCraftingMenu();
                Gdx.input.setInputProcessor(craftingStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            Player currentPlayer = game.getCurrentPlayer();
            Tile currentTile = gameMap.getTile(currentPlayer.currentX(), currentPlayer.currentY());
            if (currentTile.getType() == TileType.House) {
                isCookingMenuOpen = !isCookingMenuOpen;
                if (isCookingMenuOpen) {
                    showCookingMenu();
                    Gdx.input.setInputProcessor(cookingStage);
                } else {
                    Gdx.input.setInputProcessor(multiplexer);
                }
            } else {
                generalMessageLabel.setText("You can only cook in your house!");
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            isEatMenuOpen = !isEatMenuOpen;
            if (isEatMenuOpen) {
                showEatMenu();
                Gdx.input.setInputProcessor(eatMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

       if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            showFriendsMenu = !showFriendsMenu;
            if (showFriendsMenu) {
               checkForNewMessages();
                checkForNewGifts();
                createFriendsMenuUI();
                Gdx.input.setInputProcessor(stage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (isInventoryOpen || isToolMenuOpen || isCheatMenuOpen || isPlantingSelectionOpen
            || isCraftingMenuOpen || isCookingMenuOpen || isCraftingCheatMenuOpen || isCookingCheatMenuOpen || isEatMenuOpen || showSellMenu) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (targetTileX >= 0 && targetTileX < MAP_WIDTH && targetTileY >= 0 && targetTileY < MAP_HEIGHT) {
                Tile hoveredTile = gameMap.getTiles()[targetTileX][targetTileY];
                if (hoveredTile.getType() == TileType.Shoveled && hoveredTile.getPlant() == null && hoveredTile.getSeed() == null) {
                    openPlantingMenu(hoveredTile);
                    return;
                }
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (controller != null && game.getCurrentPlayer() != null) {
                Tool currentTool = game.getCurrentPlayer().getCurrentTool();
                String message;

                if (currentTool == null) {
                    message = "No tool equipped!";
                    generalMessageLabel.setColor(Color.YELLOW);
                } else {

                    if (currentTool.getToolType().name().contains("Rod")) {
                        Result result = controller.fishing(currentTool.getName());
                        message = result.Message();
                        generalMessageLabel.setColor(Color.CYAN);
                        if (result.isSuccessful()) {
                            startFishingMinigame();

                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                                data.put("senderUsername", game.getCurrentPlayer().getUsername());
                                data.put("toolEnumName", currentTool.getToolType().name());
                                data.put("targetX", targetTileX);
                                data.put("targetY", targetTileY);
                                ns.sendPlayerAction("use_tool", data);
                            }
                        }
                    }

                    else {
                        Result result;
                        if (targetTileX >= 0 && targetTileX < MAP_WIDTH && targetTileY >= 0 && targetTileY < MAP_HEIGHT) {
                            Tile targetTile = gameMap.getTiles()[targetTileX][targetTileY];
                            result = controller.useTool(targetTile);
                            if (result.isSuccessful()) {
                                String toolName = currentTool.getToolType().name().toLowerCase();
                                if (toolName.contains("watering_can")) {
                                    startActionAnimation(PlayerActionState.WATERING);
                                } else if (toolName.contains("hoe")) {
                                    startActionAnimation(PlayerActionState.TILLING);
                                } else if (toolName.contains("scythe")) {
                                    startActionAnimation(PlayerActionState.HARVESTING);
                                }
                                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                if (ns != null) {
                                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                                    data.put("senderUsername", game.getCurrentPlayer().getUsername());
                                    data.put("toolEnumName", currentTool.getToolType().name());
                                    data.put("targetX", targetTileX);
                                    data.put("targetY", targetTileY);
                                    ns.sendPlayerAction("use_tool", data);
                                }
                            }
                        } else {
                            result = new Result(false, "Target is outside the map.");
                        }
                        message = result.Message();
                        generalMessageLabel.setColor(Color.WHITE);
                    }
                }
                generalMessageLabel.setText(message);
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
            }
        }



        handleMinimapToggle();
        handleTurnSwitching();
        handlePlayerMovement(delta);
        handleCameraMovement(delta);
        handleShopInteraction();
        handleMachinePlacement();
        handleMachineInteraction();


        if (showDialog) {
            handleDialogDismiss();
        } else {

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                handleNPCClick(Gdx.input.getX(), Gdx.input.getY());
            }

            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                handleNPCRightClick(Gdx.input.getX(), Gdx.input.getY());
            }
        }


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            if (handleAnimalClick(Gdx.input.getX(), Gdx.input.getY())) {
                return;
            }

            if (handleTrashCanClick(Gdx.input.getX(), Gdx.input.getY())) {
                return;
            }
            handleHousingClick(Gdx.input.getX(), Gdx.input.getY());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (controller != null) {
                Result result = controller.changeTime("1");
                if (result != null && result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("senderUsername", game.getCurrentPlayer().getUsername());
                        data.put("hours", 1);
                        ns.sendPlayerAction("cheat_advance_time", data);
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (controller != null) {
                Result result = controller.changeDate(1);
                if (result != null && result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("senderUsername", game.getCurrentPlayer().getUsername());
                        data.put("days", 1);

                        com.example.main.models.User main = game.getMainPlayer();
                        com.example.main.enums.design.Weather w = game.getTodayWeather();
                        if (main != null && w != null) {
                            data.put("todayWeather", w.name());
                            data.put("tomorrowWeather", game.getTomorrowWeather() != null ? game.getTomorrowWeather().name() : null);
                        }
                        ns.sendPlayerAction("cheat_advance_date", data);
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            if (controller != null && game != null) {
                Player currentPlayer = game.getCurrentPlayer();
                String x = String.valueOf(currentPlayer.currentX());
                String y = String.valueOf(currentPlayer.currentY());
                Result r = controller.cheatLightning(x, y);
                System.out.println(r.Message());
                cheatLightningActive = true;
                lightningDuration = 0.15f;
                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("senderUsername", game.getCurrentPlayer().getUsername());
                    data.put("x", Integer.parseInt(x));
                    data.put("y", Integer.parseInt(y));
                    ns.sendPlayerAction("cheat_lightning", data);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            controller.cheatSetEnergy(200);
        }
    }

    private void handlePlayerMovement(float delta) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        playerAnimationTime += delta;

        if (playerMoving && playerMoveProgress < 1.0f) {
            playerMoveProgress += PLAYER_MOVE_SPEED * delta;
            if (playerMoveProgress >= 1.0f) {
                playerMoveProgress = 1.0f;
                currentPlayer.setCurrentX(playerTargetX);
                currentPlayer.setCurrentY(playerTargetY);
                playerMoving = false;
                currentPlayer.reduceEnergy(10);
            }
            if (localPlayer != null && (localPlayer.currentX() != lastSentX || localPlayer.currentY() != lastSentY)) {
                networkController.sendPlayerMove(localPlayer.currentX(), localPlayer.currentY());
                lastSentX = localPlayer.currentX();
                lastSentY = localPlayer.currentY();
            }
            return;
        }

        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (currentPlayer != null && currentPlayer.isFainted() && (upPressed || downPressed || leftPressed || rightPressed)) {
            generalMessageLabel.setText("I'm too exhausted to move...");
            generalMessageLabel.setColor(Color.RED);
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
            return;
        }

        int newX = currentPlayer.currentX();
        int newY = currentPlayer.currentY();
        PlayerDirection newDirection = playerDirection;

        if (upPressed && leftPressed) {
            newDirection = PlayerDirection.UP_LEFT;
            newX -= 1;
            newY -= 1;
        } else if (upPressed && rightPressed) {
            newDirection = PlayerDirection.UP_RIGHT;
            newX += 1;
            newY -= 1;
        } else if (downPressed && leftPressed) {
            newDirection = PlayerDirection.DOWN_LEFT;
            newX -= 1;
            newY += 1;
        } else if (downPressed && rightPressed) {
            newDirection = PlayerDirection.DOWN_RIGHT;
            newX += 1;
            newY += 1;
        } else if (upPressed) {
            newDirection = PlayerDirection.UP;
            newY -= 1;
        } else if (downPressed) {
            newDirection = PlayerDirection.DOWN;
            newY += 1;
        } else if (leftPressed) {
            newDirection = PlayerDirection.LEFT;
            newX -= 1;
        } else if (rightPressed) {
            newDirection = PlayerDirection.RIGHT;
            newX += 1;
        }

        playerDirection = newDirection;

        if (newX != currentPlayer.currentX() || newY != currentPlayer.currentY()) {
            if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT) {
                if (isPlayerWalkable(newX, newY, currentPlayer)) {
                    playerTargetX = newX;
                    playerTargetY = newY;
                    playerMoving = true;
                    playerActionState = PlayerActionState.WALKING;
                    playerMoveProgress = 0f;
                    playerAnimationTime = 0f;
                }
            }
        } else {
            playerMoving = false;
            if (playerActionState == PlayerActionState.WALKING) {
                playerActionState = PlayerActionState.IDLE;
            }
            playerAnimationTime = 0f;
        }

        if (localPlayer != null && (localPlayer.currentX() != lastSentX || localPlayer.currentY() != lastSentY)) {

            networkController.sendPlayerMove(localPlayer.currentX(), localPlayer.currentY());


            lastSentX = localPlayer.currentX();
            lastSentY = localPlayer.currentY();
        }
    }

    private boolean isPlayerWalkable(int x, int y, Player currentPlayer) {
        Tile tile = gameMap.getTiles()[x][y];
        if (tile == null) {
            return false;
        }

        if (!tile.getType().isReachable()) {
            return false;
        }

        Player tileOwner = tile.getOwner();

        return tileOwner == null ||
               tileOwner.equals(currentPlayer) ||
               (currentPlayer.getSpouse() != null && tileOwner.equals(currentPlayer.getSpouse()));
    }

    private void handleMinimapToggle() {
        boolean mKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.M);

        if (mKeyCurrentlyPressed && !mKeyPressed) {
            showMinimap = !showMinimap;
        }

        mKeyPressed = mKeyCurrentlyPressed;
    }

    private boolean nKeyPressed = false;

    private void handleTurnSwitching() {
        boolean nKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.N);

        if (nKeyCurrentlyPressed && !nKeyPressed) {
            controller.switchTurn();

            cameraFollowsPlayer = true;

            Player newCurrentPlayer = game.getCurrentPlayer();
            if (newCurrentPlayer != null) {
                updateCameraToFollowPlayer(newCurrentPlayer);
            }

            playerMoving = false;
            playerMoveProgress = 0f;
        }

        nKeyPressed = nKeyCurrentlyPressed;
    }

    private void handleBuildingPlacement() {

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();



        buildingPlacementX = mouseX;
        buildingPlacementY = Gdx.graphics.getHeight() - mouseY;


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            Vector3 worldCoords = camera.unproject(new Vector3(mouseX, mouseY, 0));

            int tileX = (int) (worldCoords.x / TILE_SIZE);
            int tileY = (int) ((MAP_HEIGHT * TILE_SIZE - worldCoords.y) / TILE_SIZE);


            Result result = shopController.buildBarnOrCoop(buildingToPlace, String.valueOf(tileX), String.valueOf(tileY));

            if (result.isSuccessful()) {

                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null) {
                    java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                    actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                    actionData.put("buildingKey", buildingToPlace);
                    actionData.put("x", tileX);
                    actionData.put("y", tileY);
                    ns.sendPlayerAction("build_barn_or_coop", actionData);
                }

                generalMessageLabel.setText(result.Message());
                generalMessageLabel.setColor(Color.GREEN);
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;


                isBuildingPlacementMode = false;
                buildingToPlace = null;
                buildingPlacementTexture = null;
            } else {

                generalMessageLabel.setText(result.Message());
                generalMessageLabel.setColor(Color.RED);
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
            }
        }


        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            isBuildingPlacementMode = false;
            buildingToPlace = null;
            buildingPlacementTexture = null;

            generalMessageLabel.setText("Building placement cancelled");
            generalMessageLabel.setColor(Color.YELLOW);
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
        }
    }

    private void handleShepherdMode() {

        handleCameraMovement(Gdx.graphics.getDeltaTime());


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.input.getY();


            Vector3 worldCoords = camera.unproject(new Vector3(mouseX, mouseY, 0));


            int tileX = (int) (worldCoords.x / TILE_SIZE);
            int tileY = (int) ((MAP_HEIGHT * TILE_SIZE - worldCoords.y) / TILE_SIZE);

            if (tileX >= 0 && tileX < MAP_WIDTH && tileY >= 0 && tileY < MAP_HEIGHT) {
                Player currentPlayer = game.getCurrentPlayer();
                if (currentPlayer != null && isAnimalWalkable(tileX, tileY, currentPlayer)) {

                    if (controller != null && animalToShepherd != null) {
                        if (currentPlayer != null) {
                            for (Housing housing : currentPlayer.getHousings()) {
                                for (PurchasedAnimal animal : housing.getOccupants()) {
                                    if (animal.getName().equals(animalToShepherd)) {

                                        animal.setTargetX(tileX);
                                        animal.setTargetY(tileY);
                                        animal.setMoving(true);
                                        animal.setMoveProgress(0f);

                                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                        if (ns != null) {
                                            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                                            data.put("senderUsername", game.getCurrentPlayer().getUsername());
                                            data.put("animalName", animalToShepherd);
                                            data.put("targetX", tileX);
                                            data.put("targetY", tileY);
                                            ns.sendPlayerAction("shepherd_animal", data);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }


                    isShepherdMode = false;
                    animalToShepherd = null;

                    generalMessageLabel.setText("Animal is moving to new location");
                    generalMessageLabel.setColor(Color.GREEN);
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                } else {
                    generalMessageLabel.setText("Cannot shepherd animal to that location");
                    generalMessageLabel.setColor(Color.RED);
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                }
            }
        }


        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            isShepherdMode = false;
            animalToShepherd = null;

            generalMessageLabel.setText("Shepherd mode cancelled");
            generalMessageLabel.setColor(Color.YELLOW);
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isShepherdMode = false;
            animalToShepherd = null;

            generalMessageLabel.setText("Shepherd mode cancelled");
            generalMessageLabel.setColor(Color.YELLOW);
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
        }
    }

    private void renderBuildingPlacementPreview() {
        if (buildingPlacementTexture == null) return;


        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();


        spriteBatch.setColor(1f, 1f, 1f, 0.7f);


        float buildingWidth = buildingPlacementTexture.getWidth() * 2f;
        float buildingHeight = buildingPlacementTexture.getHeight() * 2f;

        spriteBatch.draw(buildingPlacementTexture, buildingPlacementX + 10, buildingPlacementY - buildingHeight + 10, buildingWidth, buildingHeight);


        spriteBatch.setColor(1f, 1f, 1f, 1f);
        spriteBatch.end();


        spriteBatch.setProjectionMatrix(camera.combined);
    }

    private void renderShepherdModeUI() {
        if (!isShepherdMode || animalToShepherd == null) return;


        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();


        hudFont.setColor(Color.YELLOW);
        String shepherdText = "Shepherd Mode: " + animalToShepherd + " - Click to move animal";
        hudFont.draw(spriteBatch, shepherdText, 10, Gdx.graphics.getHeight() - 10);

        spriteBatch.end();


        spriteBatch.setProjectionMatrix(camera.combined);
    }

    private void createAnimation(float worldX, float worldY, String type) {
        activeAnimations.add(new AnimationEffect(worldX, worldY, 2.0f, type));
    }

    private void updateAnimations(float delta) {
        for (int i = activeAnimations.size() - 1; i >= 0; i--) {
            AnimationEffect effect = activeAnimations.get(i);
            effect.elapsed += delta;

            if (effect.type.equals("bounce")) {
                effect.bounceHeight = (float) Math.sin(effect.elapsed * effect.bounceSpeed) * 10f;
                if (effect.elapsed > effect.duration) {
                    effect.bounceHeight = 0f;
                }
            }

            if (effect.elapsed >= effect.duration) {
                activeAnimations.remove(i);
            }
        }
    }

    private void renderAnimations() {
        if (activeAnimations.isEmpty()) return;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (AnimationEffect effect : activeAnimations) {
            float alpha = 1.0f - (effect.elapsed / effect.duration);
            alpha = Math.max(0f, alpha);

            if (effect.type.equals("food")) {

                spriteBatch.setColor(1f, 1f, 1f, alpha);
                for (int i = 0; i < 5; i++) {
                    float offsetX = (float) Math.sin(effect.elapsed * 3f + i) * 20f;
                    float offsetY = effect.elapsed * 30f + i * 10f;
                    float particleX = effect.x + offsetX;
                    float particleY = effect.y + offsetY;


                    spriteBatch.setColor(0.8f, 0.6f, 0.2f, alpha);

                    if (grass1Texture != null) {
                        spriteBatch.draw(grass1Texture, particleX, particleY, 8, 8);
                    }
                }
            } else if (effect.type.equals("hearts")) {

                spriteBatch.setColor(1f, 0.3f, 0.7f, alpha);
                for (int i = 0; i < 3; i++) {
                    float offsetX = (float) Math.sin(effect.elapsed * 2f + i) * 15f;
                    float offsetY = effect.elapsed * 40f + i * 15f;
                    float heartX = effect.x + offsetX;
                    float heartY = effect.y + offsetY;


                    hudFont.setColor(1f, 0.3f, 0.7f, alpha);
                    hudFont.draw(spriteBatch, "♥", heartX, heartY);
                }
            }
        }

        spriteBatch.setColor(1f, 1f, 1f, 1f);
        spriteBatch.end();


        if (isTradeMenuOpen && tradeMenuStage != null) {
            tradeMenuStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            tradeMenuStage.act(Gdx.graphics.getDeltaTime());
            tradeMenuStage.draw();
        }
    }

    private void handleShopInteraction() {

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.input.getY();


            Vector3 worldCoords = camera.unproject(new Vector3(mouseX, mouseY, 0));


            int tileX = (int) (worldCoords.x / TILE_SIZE);
            int tileY = (int) ((MAP_HEIGHT * TILE_SIZE - worldCoords.y) / TILE_SIZE);


            int shopIndex = getShopIndex(tileX, tileY);
            if (shopIndex != -1) {

                ShopType[] shopTypes = ShopType.values();
                if (shopIndex < shopTypes.length) {
                    currentShopType = shopTypes[shopIndex];


                    Player currentPlayer = game.getCurrentPlayer();
                    if (currentPlayer != null) {


                        currentPlayer.setCurrentX(currentShopType.getCornerX() + 1);
                        currentPlayer.setCurrentY(currentShopType.getCornerY() + 1);


                        updateCameraToFollowPlayer(currentPlayer);
                    }

                    showShopMenu = true;
                    createShopMenuUI();
                }
            }
        }
    }

    private void handleCameraMovement(float delta) {
        Player currentPlayer = game.getCurrentPlayer();

        boolean manualControl = Gdx.input.isKeyPressed(Input.Keys.I) ||
                               Gdx.input.isKeyPressed(Input.Keys.K) ||
                               Gdx.input.isKeyPressed(Input.Keys.J) ||
                               Gdx.input.isKeyPressed(Input.Keys.L);

        if (manualControl) {
            cameraFollowsPlayer = false;

            float movement = cameraSpeed * delta;

            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                camera.position.y += movement;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                camera.position.y -= movement;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                camera.position.x -= movement;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                camera.position.x += movement;
            }
        } else if (currentPlayer != null) {
            cameraFollowsPlayer = true;
            updateCameraToFollowPlayer(currentPlayer);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }

        camera.zoom = Math.max(0.1f, Math.min(camera.zoom, 3.0f));

        constrainCameraToMapBounds();
    }

    private void updateCameraToFollowPlayer(Player player) {
        float playerX = player.currentX();
        float playerY = player.currentY();

        if (playerMoving && playerMoveProgress < 1.0f) {
            float startX = player.currentX();
            float startY = player.currentY();
            float endX = playerTargetX;
            float endY = playerTargetY;

            playerX = startX + (endX - startX) * playerMoveProgress;
            playerY = startY + (endY - startY) * playerMoveProgress;
        }

        float worldX = playerX * TILE_SIZE;
        float worldY = (MAP_HEIGHT - 1 - playerY) * TILE_SIZE;

        camera.position.set(worldX, worldY, 0);
    }

    private void constrainCameraToMapBounds() {
        float worldWidth = MAP_WIDTH * TILE_SIZE;
        float worldHeight = MAP_HEIGHT * TILE_SIZE;

        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2f;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2f;

        camera.position.x = Math.max(cameraHalfWidth, Math.min(camera.position.x, worldWidth - cameraHalfWidth));
        camera.position.y = Math.max(cameraHalfHeight, Math.min(camera.position.y, worldHeight - cameraHalfHeight));
    }

    @Override
    public void show() {
    }

    private void renderMap() {
        Tile[][] tiles = gameMap.getTiles();

        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                float worldX = x * TILE_SIZE;
                float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;

                Tile currentTile = tiles[x] != null && tiles[x][y] != null ? tiles[x][y] : null;
                renderBaseGround(x, y, worldX, worldY, currentTile);

                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    TileType tileType = tile.getType();

                    if (tileType != TileType.Tree) {
                        renderTileOverlay(tile, x, y, worldX, worldY);
                    }
                }
            }
        }

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    TileType tileType = tile.getType();

                    if (tileType == TileType.House || tileType == TileType.Wall) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderHouseSprite(x, y, worldX, worldY);

                        if (tileType == TileType.Wall) {
                            int npcIndex = getNPCIndexForHouse(x, y);
                            if (npcIndex != -1) {
                                renderNPCHouseSprite(x, y, worldX, worldY);
                            }

                            int shopIndex = getShopIndex(x, y);
                            if (shopIndex != -1) {
                                renderShopSprite(x, y, worldX, worldY);
                            }

                            int housingIndex = getHousingIndex(x, y);
                            if (housingIndex != -1) {
                                renderHousingSprite(x, y, worldX, worldY);
                            }
                        }
                    } else if (tileType == TileType.NPCHouse) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderNPCHouseSprite(x, y, worldX, worldY);
                    } else if (tileType == TileType.Shop) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderShopSprite(x, y, worldX, worldY);
                    }
                }
            }
        }

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    if (tile.getType() == TileType.Tree) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderTreeSprite(x, y, worldX, worldY);
                    }
                }
            }
        }

        renderPlayer();
        renderNPCs();
        renderTrashCans();

        updateNearbyNPCs();
        renderMeetButtons();
        renderDialogBox();
    }

    private void renderPlayer() {
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player == null || user.getUsername().startsWith("empty_slot_")) {
                continue;
            }

            float playerX = player.currentX();
            float playerY = player.currentY();
            Player currentPlayer = game.getCurrentPlayer();
            if (player.equals(currentPlayer) && playerMoving && playerMoveProgress < 1.0f) {
                float startX = player.currentX();
                float startY = player.currentY();
                float endX = playerTargetX;
                float endY = playerTargetY;

                playerX = startX + (endX - startX) * playerMoveProgress;
                playerY = startY + (endY - startY) * playerMoveProgress;
            }

            if (playerX < 0 || playerX >= MAP_WIDTH || playerY < 0 || playerY >= MAP_HEIGHT) {
                continue;
            }

            float worldX = playerX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - playerY) * TILE_SIZE;

            Texture playerTexture = getPlayerTexture(player);
            float playerWidth = playerTexture.getWidth() * 2;
            float playerHeight = playerTexture.getHeight() * 2;
            float renderX = worldX + (TILE_SIZE - playerWidth) / 2f;
            float renderY = worldY;

            if (!player.equals(localPlayer)) {
                spriteBatch.setColor(1f, 1f, 1f, 0.8f);
            }

            if (!player.equals(game.getCurrentPlayer())) {
                spriteBatch.setColor(1f, 1f, 1f, 0.7f);
            }
            spriteBatch.draw(playerTexture, renderX, renderY, playerWidth, playerHeight);
            if (player.equals(game.getCurrentPlayer()) && eatingAnimationTimer > 0 && eatingTexture != null) {
                float bobOffset = (float) (Math.sin(eatingAnimationTimer * 12) * 4);
                float animX = worldX + (TILE_SIZE / 2f) - (eatingTexture.getWidth() / 2f);
                float animY = worldY + TILE_SIZE + bobOffset;
                spriteBatch.draw(eatingTexture, animX, animY-35);
            }
            spriteBatch.setColor(1f, 1f, 1f, 1f);

            if (player != null && player.isFainted()) {
                hudFont.setColor(Color.WHITE);
                float bobOffset = (float) (Math.sin(weatherStateTime * 4) * 5);
                hudFont.draw(spriteBatch, "Z z Z", worldX + 8, worldY + 48 + bobOffset);
                if(player.getEnergy() > 0) player.setFainted(false);
            }

            if (player.equals(game.getCurrentPlayer()) && player.getCurrentTool() != null && actionTimer <= 0) {
                Tool currentTool = player.getCurrentTool();
                String key = generateTextureKey(currentTool);
                Texture toolTexture = textureManager.getTexture(key);

                if (toolTexture != null) {
                    float toolWidth = toolTexture.getWidth() * 1.0f;
                    float toolHeight = toolTexture.getHeight() * 1.0f;

                    float toolX = (worldX + (TILE_SIZE / 2f)) - (toolWidth / 2f);
                    float toolY = (worldY + (TILE_SIZE / 2f)) - (toolHeight / 4f);

                    spriteBatch.draw(
                        new TextureRegion(toolTexture),
                        toolX, toolY,
                        toolWidth / 2f, toolHeight / 4f,
                        toolWidth, toolHeight,
                        1f, 1f,
                        toolRotation - 90f
                    );
                }
            }
        }
    }

        private Texture getPlayerTexture(Player player) {
            boolean isMale = player.getGender().name().equals("Male");
            Player currentPlayer = game.getCurrentPlayer();


            if (player.equals(currentPlayer) && playerActionState != PlayerActionState.IDLE && playerActionState != PlayerActionState.WALKING) {
                switch (playerActionState) {
                    case WATERING:
                        switch (playerDirection) {
                            case UP: case UP_LEFT: case UP_RIGHT: return isMale ? maleWatercanIdle2Texture : femaleWatercanIdle1Texture;
                            case LEFT: return isMale ? maleWatercanIdle2Texture : femaleWatercanIdle1Texture;
                            case RIGHT: return isMale ? maleWatercanIdle2Texture : femaleWatercanIdle1Texture;
                            default: return isMale ? maleWatercanIdle2Texture : femaleWatercanIdle1Texture;
                        }
                    case TILLING:
                        switch (playerDirection) {
                            case UP: case UP_LEFT: case UP_RIGHT: return isMale ? maleHoeIdle2Texture : femaleHoeIdle2Texture;
                            case LEFT: return isMale ? maleHoeIdle2Texture : femaleHoeIdle2Texture;
                            case RIGHT: return isMale ? maleHoeIdle2Texture : femaleHoeIdle2Texture;
                            default: return isMale ? maleHoeIdle2Texture : femaleHoeIdle2Texture;
                        }
                    case HARVESTING:
                        switch (playerDirection) {
                            case UP: case UP_LEFT: case UP_RIGHT: return isMale ? maleScytheIdle2Texture : femaleScytheIdle2Texture;
                            case LEFT: return isMale ? maleScytheIdle2Texture : femaleScytheIdle2Texture;
                            case RIGHT: return isMale ? maleScytheIdle2Texture : femaleScytheIdle2Texture;
                            default: return isMale ? maleScytheIdle2Texture : femaleScytheIdle2Texture;
                        }
                    case PLANTING:

                        return isMale ? malePlantingIdleTexture : femalePlantingIdleTexture;
                }
            }


            if (!player.equals(currentPlayer) || !playerMoving) {
                return isMale ? maleIdleTexture : femaleIdleTexture;
            }

            int animFrame = ((int) (playerAnimationTime / ANIMATION_SPEED)) % 2;

            switch (playerDirection) {
                case DOWN: case DOWN_LEFT: case DOWN_RIGHT:
                    return isMale ? (animFrame == 0 ? maleDown1Texture : maleDown2Texture) : (animFrame == 0 ? femaleDown1Texture : femaleDown2Texture);
                case UP: case UP_LEFT: case UP_RIGHT:
                    return isMale ? (animFrame == 0 ? maleUp1Texture : maleUp2Texture) : (animFrame == 0 ? femaleUp1Texture : femaleUp2Texture);
                case LEFT:
                    return isMale ? (animFrame == 0 ? maleLeft1Texture : maleLeft2Texture) : (animFrame == 0 ? femaleLeft1Texture : femaleLeft2Texture);
                case RIGHT:
                    return isMale ? (animFrame == 0 ? maleRight1Texture : maleRight2Texture) : (animFrame == 0 ? femaleRight1Texture : femaleRight2Texture);
                default:
                    return isMale ? maleIdleTexture : femaleIdleTexture;
            }
        }

    private void renderNPCs() {
        ArrayList<NPC> npcs = game.getNPCs();
        if (npcs == null) {
            return;
        }

        for (NPC npc : npcs) {
            if (npc == null) {
                continue;
            }

            int npcX = npc.getX();
            int npcY = npc.getY();


            if (npcX < 0 || npcX >= MAP_WIDTH || npcY < 0 || npcY >= MAP_HEIGHT) {
                continue;
            }

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;

            Texture npcTexture = getNPCTexture(npc);
            if (npcTexture == null) {
                continue;
            }

            float npcWidth = npcTexture.getWidth() * 2;
            float npcHeight = npcTexture.getHeight() * 2;
            float renderX = worldX + (TILE_SIZE - npcWidth) / 2f;
            float renderY = worldY;

            spriteBatch.draw(npcTexture, renderX, renderY, npcWidth, npcHeight);
        }
    }

    private void renderTrashCans() {
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player == null) {
                continue;
            }

            int trashCanX = player.getTrashCanX();
            int trashCanY = player.getTrashCanY();

            if (trashCanX < 0 || trashCanX >= MAP_WIDTH || trashCanY < 0 || trashCanY >= MAP_HEIGHT) {
                continue;
            }

            float worldX = trashCanX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - trashCanY) * TILE_SIZE;

            TrashCan trashCan = player.getTrashCan();
            if (trashCan == null) {
                continue;
            }

            Texture trashCanTexture = getTrashCanTexture(trashCan.getTrashCanType());
            if (trashCanTexture == null) {
                continue;
            }

            float trashCanWidth = TILE_SIZE;
            float trashCanHeight = TILE_SIZE;
            float renderX = worldX;
            float renderY = worldY;

            spriteBatch.draw(trashCanTexture, renderX, renderY, trashCanWidth, trashCanHeight);
        }
    }

    private Texture getNPCTexture(NPC npc) {
        NPCType npcType = npc.getType();
        switch (npcType) {
            case Sebastian:
                return sebastianTexture;
            case Abigail:
                return abigailTexture;
            case Harvey:
                return harveyTexture;
            case Lia:
                return liaTexture;
            case Robin:
                return robinTexture;
            default:
                return null;
        }
    }

    private Texture getTrashCanTexture(TrashCanType trashCanType) {
        switch (trashCanType) {
            case Trash_Can:
                return trashCanTexture;
            case Copper_Trash_Can:
                return copperTrashCanTexture;
            case Steel_Trash_Can:
                return steelTrashCanTexture;
            case Gold_Trash_Can:
                return goldTrashCanTexture;
            case Iridium_Trash_Can:
                return iridiumTrashCanTexture;
            default:
                return trashCanTexture;
        }
    }

    private void updateNearbyNPCs() {
        nearbyNPCs.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        int playerX = currentPlayer.currentX();
        int playerY = currentPlayer.currentY();

        ArrayList<NPC> npcs = game.getNPCs();
        if (npcs == null) return;


        ArrayList<NPC> npcsToRemove = new ArrayList<>();

        for (NPC npc : npcs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();


            int distanceX = Math.abs(playerX - npcX);
            int distanceY = Math.abs(playerY - npcY);

            if (distanceX <= 1 && distanceY <= 1) {

                if (!clickedNPCs.contains(npc)) {
                    nearbyNPCs.add(npc);
                }
            } else {

                if (clickedNPCs.contains(npc)) {
                    npcsToRemove.add(npc);
                }
            }
        }


        clickedNPCs.removeAll(npcsToRemove);
    }

    private void renderMeetButtons() {
        for (NPC npc : nearbyNPCs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;


            float buttonWidth = 60f;
            float buttonHeight = 24f;
            float buttonX = worldX + TILE_SIZE + 8f;
            float buttonY = worldY + TILE_SIZE + 8f;


            spriteBatch.setColor(0.8f, 0.8f, 0.8f, 1f);
            spriteBatch.draw(ground1Texture, buttonX, buttonY, buttonWidth, buttonHeight);
            spriteBatch.setColor(1f, 1f, 1f, 1f);


            spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1f);

            float borderThickness = 2f;
            spriteBatch.draw(ground1Texture, buttonX, buttonY, buttonWidth, borderThickness);
            spriteBatch.draw(ground1Texture, buttonX, buttonY + buttonHeight - borderThickness, buttonWidth, borderThickness);
            spriteBatch.draw(ground1Texture, buttonX, buttonY, borderThickness, buttonHeight);
            spriteBatch.draw(ground1Texture, buttonX + buttonWidth - borderThickness, buttonY, borderThickness, buttonHeight);
            spriteBatch.setColor(1f, 1f, 1f, 1f);


            if (hudFont != null) {
                hudFont.setColor(Color.BLACK);
                hudFont.getData().setScale(0.8f);
                hudFont.draw(spriteBatch, "meet", buttonX + 15, buttonY + 15);
                hudFont.getData().setScale(1.0f);
                hudFont.setColor(Color.WHITE);
            }
        }
    }

    private void renderDialogBox() {
        if (!showDialog || currentDialogNPC == null) return;

        int npcX = currentDialogNPC.getX();
        int npcY = currentDialogNPC.getY();

        float worldX = npcX * TILE_SIZE;
        float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;


        float dialogWidth = 200f;
        float dialogHeight = 80f;
        float dialogX = worldX + TILE_SIZE + 10f;
        float dialogY = worldY + TILE_SIZE + 20f;


        spriteBatch.draw(dialogBoxTexture, dialogX, dialogY, dialogWidth, dialogHeight);


        if (hudFont != null && currentDialogMessage != null && !currentDialogMessage.isEmpty()) {
            hudFont.setColor(Color.BLACK);

            hudFont.getData().setScale(1.0f);
            hudFont.draw(spriteBatch, currentDialogMessage, dialogX + 10, dialogY + dialogHeight - 10);

            hudFont.getData().setScale(1.0f);
            hudFont.setColor(Color.WHITE);
        }
    }

    private void handleNPCClick(int screenX, int screenY) {
        if (nearbyNPCs.isEmpty()) return;


        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

        for (NPC npc : nearbyNPCs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;


            float buttonWidth = 60f;
            float buttonHeight = 24f;
            float buttonX = worldX + TILE_SIZE + 8f;
            float buttonY = worldY + TILE_SIZE + 8f;

            if (worldCoords.x >= buttonX && worldCoords.x <= buttonX + buttonWidth &&
                worldCoords.y >= buttonY && worldCoords.y <= buttonY + buttonHeight) {


                Result result = controller.meetNPC(npc.getType().name());


                currentDialogNPC = npc;
                currentDialogMessage = result.Message();
                showDialog = true;


                clickedNPCs.add(npc);
                nearbyNPCs.remove(npc);

                break;
            }
        }
    }

    private void handleDialogDismiss() {
        if (showDialog && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            showDialog = false;
            currentDialogNPC = null;
            currentDialogMessage = "";
        }
    }

    private void handleNPCRightClick(int screenX, int screenY) {

        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

        ArrayList<NPC> npcs = game.getNPCs();
        if (npcs == null) return;

        for (NPC npc : npcs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;


            if (worldCoords.x >= worldX && worldCoords.x <= worldX + TILE_SIZE &&
                worldCoords.y >= worldY && worldCoords.y <= worldY + TILE_SIZE) {

                selectedNPC = npc;
                showNPCMenu = true;
                createNPCMenuUI();
                break;
            }
        }
    }

    private void createNPCMenuUI() {
        if (npcMenuTable != null) {
            npcMenuTable.remove();
        }

        npcMenuTable = new Table();
        npcMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));

        npcMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        npcMenuTable.setPosition(
            (Gdx.graphics.getWidth() - npcMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - npcMenuTable.getHeight()) / 2f
        );

        stage.addActor(npcMenuTable);


        Gdx.input.setInputProcessor(stage);

        switch (currentNPCMenuState) {
            case MAIN_MENU:
                createMainNPCMenu();
                break;
            case FRIENDSHIP:
                createFriendshipMenu();
                break;
            case QUESTS:
                createQuestsMenu();
                break;
            case GIFT:
                createGiftMenu();
                break;
        }
    }

    private void createMainNPCMenu() {
        npcMenuTable.clear();


        Label npcNameLabel = new Label(selectedNPC.getType().name(), skin);
        npcNameLabel.setFontScale(1.2f);
        npcMenuTable.add(npcNameLabel).colspan(2).pad(10).row();


        TextButton friendshipButton = new TextButton("Friendship", skin);
        TextButton questsButton = new TextButton("Quests", skin);
        TextButton giftButton = new TextButton("Gift", skin);
        TextButton closeButton = new TextButton("Close", skin);


        friendshipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.FRIENDSHIP;
                createNPCMenuUI();
            }
        });

        questsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.QUESTS;
                questResultMessage = "";
                questResultSuccess = false;
                createNPCMenuUI();
            }
        });

        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.GIFT;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });


        npcMenuTable.add(friendshipButton).size(200, 50).pad(10);
        npcMenuTable.add(questsButton).size(200, 50).pad(10).row();
        npcMenuTable.add(giftButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createFriendshipMenu() {
        npcMenuTable.clear();


        Label titleLabel = new Label(selectedNPC.getType().name() + " friendship status", skin);
        titleLabel.setFontScale(1.5f);
        npcMenuTable.add(titleLabel).colspan(2).pad(20).row();


        NPCFriendship friendship = selectedNPC.getFriendShipWith(game.getCurrentPlayer());
        if (friendship != null) {
            Label friendshipLabel = new Label(friendship.toString(), skin);
            friendshipLabel.setFontScale(1.0f);
            npcMenuTable.add(friendshipLabel).colspan(2).pad(20).row();
        } else {
            Label noFriendshipLabel = new Label("No friendship data available", skin);
            noFriendshipLabel.setFontScale(1.0f);
            npcMenuTable.add(noFriendshipLabel).colspan(2).pad(20).row();
        }


        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        npcMenuTable.add(backButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createQuestsMenu() {
        npcMenuTable.clear();


        Label titleLabel = new Label(selectedNPC.getType().name() + " Quests", skin);
        titleLabel.setFontScale(1.5f);
        npcMenuTable.add(titleLabel).colspan(2).pad(20).row();


        Label resultMessageLabel = new Label(questResultMessage, skin);
        resultMessageLabel.setFontScale(1.2f);
        resultMessageLabel.setColor(questResultSuccess ? Color.GREEN : Color.RED);

        npcMenuTable.add(resultMessageLabel).colspan(2).pad(10).fillX().center().row();


        Table questListTable = new Table();
        questListTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));


        HashMap<Quest, Boolean> quests = selectedNPC.getQuests();
        if (quests != null && !quests.isEmpty()) {
            int questIndex = 0;
            for (Map.Entry<Quest, Boolean> entry : quests.entrySet()) {
                Quest quest = entry.getKey();
                Boolean isCompleted = entry.getValue();


                Table questEntry = new Table();
                questEntry.setBackground(new TextureRegionDrawable(menuBackgroundTexture));


                Label questInfoLabel = new Label(quest.toString(), skin);
                questInfoLabel.setColor(Color.BLACK);
                questInfoLabel.setFontScale(1.2f);
                questEntry.add(questInfoLabel).pad(5).row();


                String statusText = (isCompleted != null && isCompleted) ? "Status: COMPLETED" : "Status: AVAILABLE";
                Color statusColor = (isCompleted != null && isCompleted) ? Color.GRAY : Color.GREEN;
                Label statusLabel = new Label(statusText, skin);
                statusLabel.setColor(statusColor);
                statusLabel.setFontScale(0.9f);
                questEntry.add(statusLabel).pad(5).row();


                if (isCompleted == null || !isCompleted) {
                    TextButton completeButton = new TextButton("Complete Quest", skin);
                    final int finalQuestIndex = questIndex;
                                            completeButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {

                                Result result = controller.finishQuest(String.valueOf(quest.getId()));


                                questResultMessage = result.Message();
                                if (result.isSuccessful()) {

                                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                    if (ns != null) {
                                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                        actionData.put("questId", quest.getId());
                                        ns.sendPlayerAction("finish_quest", actionData);
                                    }
                                }
                                questResultSuccess = result.isSuccessful();


                                createQuestsMenu();
                            }
                        });
                    questEntry.add(completeButton).size(150, 40).pad(5).row();
                }

                questListTable.add(questEntry).fillX().pad(10).row();
                questIndex++;
            }
        } else {
            Label noQuestsLabel = new Label("No quests available for this NPC", skin);
            noQuestsLabel.setFontScale(1.0f);
            questListTable.add(noQuestsLabel).pad(20).row();
        }


        ScrollPane scrollPane = new ScrollPane(questListTable, skin);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setFadeScrollBars(false);

        npcMenuTable.add(scrollPane).colspan(2).fill().expand().pad(10).row();


        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                questResultMessage = "";
                questResultSuccess = false;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                questResultMessage = "";
                questResultSuccess = false;
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        npcMenuTable.add(backButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createGiftMenu() {
        npcMenuTable.clear();
        Label titleLabel = new Label(selectedNPC.getType().name() + " Gift", skin);
        titleLabel.setFontScale(1.5f);
        npcMenuTable.add(titleLabel).colspan(2).pad(20).row();


        if (!giftResultMessage.isEmpty()) {
            Label resultLabel = new Label(giftResultMessage, skin);
            resultLabel.setFontScale(1.2f);
            resultLabel.setColor(giftResultSuccess ? Color.GREEN : Color.RED);
            npcMenuTable.add(resultLabel).colspan(2).pad(10).fillX().center().row();

            if (giftResultSuccess) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        showNPCMenu = false;
                        selectedNPC = null;
                        currentNPCMenuState = NPCMenuState.MAIN_MENU;
                        giftResultMessage = "";
                        giftResultSuccess = false;
                        if (npcMenuTable != null) {
                            npcMenuTable.remove();
                            npcMenuTable = null;
                        }
                    }
                }, 1.0f);
            }
        }


        Table itemsTable = new Table();
        Player player = game.getCurrentPlayer();
        ArrayList<Item> items = player.getInventory().getItems();
        int column = 0;
        final int ITEMS_PER_ROW = 4;
        for (Item item : items) {
            String key = generateTextureKey(item);
            Texture texture = textureManager.getTexture(key);
            Table itemSlot = new Table(skin);
            itemSlot.setBackground("default-round");
            if (texture != null) {
                itemSlot.add(new Image(texture)).size(40, 40);
            } else {
                itemSlot.add(new Label("?", skin)).size(40, 40);
            }
            itemSlot.row();
            itemSlot.add(new Label(String.valueOf(item.getNumber()), skin));

            itemSlot.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result result = controller.giftNPC(selectedNPC.getType().name(), item.getName());
                    giftResultMessage = result.Message();
                    giftResultSuccess = result.isSuccessful();
                    createGiftMenu();
                }
            });
            itemsTable.add(itemSlot).pad(8);
            column++;
            if (column >= ITEMS_PER_ROW) {
                itemsTable.row();
                column = 0;
            }
        }
        ScrollPane scrollPane = new ScrollPane(itemsTable, skin);
        npcMenuTable.add(scrollPane).colspan(2).expand().fill().pad(10).row();


        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                giftResultMessage = "";
                giftResultSuccess = false;
                createNPCMenuUI();
            }
        });
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                giftResultMessage = "";
                giftResultSuccess = false;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        npcMenuTable.add(backButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void renderBaseGround(int x, int y, float worldX, float worldY, Tile tile) {
        if (tile != null && tile.getType() == TileType.Water) {
            return;
        }

        Texture groundTexture = baseGroundMap[x][y] == 0 ? ground1Texture : ground2Texture;
        spriteBatch.draw(groundTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
    }

    private void renderTileOverlay(Tile tile, int tileX, int tileY, float worldX, float worldY) {
        TileType tileType = tile.getType();

        switch (tileType) {
            case Water:
                Texture waterTexture;
                switch (waterVariantMap[tileX][tileY]) {
                    case 0: waterTexture = lake1Texture; break;
                    case 1: waterTexture = lake2Texture; break;
                    case 2: waterTexture = lake3Texture; break;
                    default: waterTexture = lake1Texture; break;
                }
                spriteBatch.draw(waterTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Grass:
                Texture grassTexture = grassVariantMap[tileX][tileY] == 0 ? grass1Texture : grass2Texture;
                spriteBatch.draw(grassTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Shoveled:
                spriteBatch.draw(shoveledTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Bush:
                spriteBatch.draw(bushTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Stone:
                Texture stoneTexture = stoneVariantMap[tileX][tileY] == 0 ? stone1Texture : stone2Texture;
                spriteBatch.draw(stoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case CopperStone:
                spriteBatch.draw(copperStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case IronStone:
                spriteBatch.draw(ironStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case GoldStone:
                spriteBatch.draw(goldStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case IridiumStone:
                spriteBatch.draw(iridiumStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case JewelStone:
                spriteBatch.draw(jewelStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Tree:
                break;

            case House:
                break;

            case NPCHouse:
                break;

            case Wall:
                break;

            case Shop:
                break;

            default:
                break;
        }

        if (tile.getPlant() != null && tile.getPlant() instanceof Crop) {
            if (tile.isPartOfGiantCrop()) {
                return;
            }
            Crop crop = (Crop) tile.getPlant();
            ItemType itemType = crop.getCropType();
            if (itemType instanceof CropType) {
                CropType cropType = (CropType) itemType;
                String textureKey = cropType.getEnumName() + "_Stage_" + crop.getCurrentStage();
                Texture cropTexture = textureManager.getTexture(textureKey);

                if (cropTexture != null) {
                    float px = worldX + (TILE_SIZE - cropTexture.getWidth()) / 2f;
                    float py = worldY;
                    spriteBatch.draw(cropTexture, px, py, cropTexture.getWidth(), cropTexture.getHeight());
                }
            }
        } else if (tile.getSeed() != null) {
            String textureKey = tile.getSeed().getForagingSeedType().getEnumName();
            Texture seedTexture = textureManager.getTexture(textureKey);

            if (seedTexture != null) {
                float px = worldX + (TILE_SIZE - seedTexture.getWidth()) / 2f;
                float py = worldY;
                spriteBatch.draw(seedTexture, px, py, seedTexture.getWidth(), seedTexture.getHeight());
            }
        }

        if (tile.getPlacedMachine() != null) {
            PlacedMachine machine = tile.getPlacedMachine();
            Texture machineTexture = textureManager.getTexture(machine.getMachineType().getEnumName());
            if (machineTexture != null) {
                spriteBatch.draw(machineTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void renderTreeSprite(int tileX, int tileY, float worldX, float worldY) {
        Tile tile = gameMap.getTiles()[tileX][tileY];
        if (tile.getPlant() instanceof Fruit) {
            Fruit fruitTree = (Fruit) tile.getPlant();
            TreeType treeType = fruitTree.getTreeType();
            if (treeType == null) return;
            String textureKey;
            if (fruitTree.isReadyToHarvest()) {
                textureKey = treeType.getEnumName() + "_Stage_5_Fruit";
            } else {
                textureKey = treeType.getEnumName() + "_Stage_" + fruitTree.getCurrentStage();
            }
            Texture treeTexture = textureManager.getTexture(textureKey);
            if (treeTexture == null) {
                treeTexture = tree1Texture;
            }

            float px = worldX + (TILE_SIZE - treeTexture.getWidth()) / 2f;
            float py = worldY;
            spriteBatch.draw(treeTexture, px, py, treeTexture.getWidth(), treeTexture.getHeight());
            if (fruitTree.isReadyToHarvest()) {
                String fruitKey = fruitTree.getFruitType().getEnumName();
                Texture fruitTexture = textureManager.getTexture(fruitKey);
                if (fruitTexture != null) {
                    Random rand = new Random((long)tileX * tileY);
                    for(int i = 0; i < 3; i++) {
                        float fruitX = px + (rand.nextFloat() * (treeTexture.getWidth() - 8));
                        float fruitY = py + (treeTexture.getHeight() / 2f) + (rand.nextFloat() * (treeTexture.getHeight() / 2f));
                        spriteBatch.draw(fruitTexture, fruitX, fruitY, fruitTexture.getWidth() / 2f, fruitTexture.getHeight() / 2f);
                    }
                }
            }
        } else if (tile.getTree() != null) {
            Texture treeTexture;
            switch (treeVariantMap[tileX][tileY]) {
                case 0: treeTexture = tree1Texture; break;
                case 1: treeTexture = tree2Texture; break;
                case 2: treeTexture = tree3Texture; break;
                default: treeTexture = tree1Texture; break;
            }

            float px = worldX + (TILE_SIZE - treeTexture.getWidth()) / 2f;
            float py = worldY;
            spriteBatch.draw(treeTexture, px, py, treeTexture.getWidth(), treeTexture.getHeight());
        }
    }

    private void renderHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        int playerIndex = getPlayerIndexForHouse(tileX, tileY);
        if (playerIndex == -1) return;

        Texture houseTexture;
        switch (playerHouseVariants[playerIndex]) {
            case 0: houseTexture = house1Texture; break;
            case 1: houseTexture = house2Texture; break;
            case 2: houseTexture = house3Texture; break;
            default: houseTexture = house1Texture; break;
        }

        int[] houseArea = HOUSE_AREAS[playerIndex];
        int houseStartX = houseArea[0];
        int houseStartY = houseArea[2];
        int houseEndY = houseArea[3];


        if (tileX == houseStartX && tileY == houseStartY) {

            float houseWidth = 8 * TILE_SIZE;
            float houseHeight = 8 * TILE_SIZE;




            float houseX = worldX;
            float houseY = worldY - (7 * TILE_SIZE);

            spriteBatch.draw(houseTexture, houseX, houseY, houseWidth, houseHeight);
        }
    }

    private void renderNPCHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        int npcIndex = getNPCIndexForHouse(tileX, tileY);
        if (npcIndex == -1) {
            return;
        }

        Texture npcHouseTexture;
        switch (npcHouseVariants[npcIndex]) {
            case 0: npcHouseTexture = npcHouse1Texture; break;
            case 1: npcHouseTexture = npcHouse2Texture; break;
            case 2: npcHouseTexture = npcHouse3Texture; break;
            case 3: npcHouseTexture = npcHouse4Texture; break;
            case 4: npcHouseTexture = npcHouse5Texture; break;
            default: npcHouseTexture = npcHouse1Texture; break;
        }

        int[] npcHouseArea = NPC_HOUSE_AREAS[npcIndex];
        int houseStartX = npcHouseArea[0];
        int houseStartY = npcHouseArea[2];


        if (tileX == houseStartX && tileY == houseStartY) {

            float npcHouseWidth = 5 * TILE_SIZE;
            float npcHouseHeight = 7 * TILE_SIZE;




            float npcHouseX = worldX;
            float npcHouseY = worldY - (6 * TILE_SIZE);

            spriteBatch.draw(npcHouseTexture, npcHouseX, npcHouseY, npcHouseWidth, npcHouseHeight);
        }
    }

    private void renderShopSprite(int tileX, int tileY, float worldX, float worldY) {
        int shopIndex = getShopIndex(tileX, tileY);
        if (shopIndex == -1) {
            renderSimpleShopSprite(tileX, tileY, worldX, worldY);
            return;
        }

        Texture shopTexture;
        switch (shopIndex) {
            case 0: shopTexture = blacksmithTexture; break;
            case 1: shopTexture = jojamartTexture; break;
            case 2: shopTexture = pierresShopTexture; break;
            case 3: shopTexture = carpentersShopTexture; break;
            case 4: shopTexture = fishShopTexture; break;
            case 5: shopTexture = ranchTexture; break;
            case 6: shopTexture = saloonTexture; break;
            default: shopTexture = ground1Texture; break;
        }

        int[] shopArea = SHOP_AREAS[shopIndex];
        int shopStartX = shopArea[0];
        int shopStartY = shopArea[2];

        if (tileX == shopStartX && tileY == shopStartY) {

            float shopWidth = 8 * TILE_SIZE;
            float shopHeight = 8 * TILE_SIZE;




            float shopX = worldX;
            float shopY = worldY - (7 * TILE_SIZE);

            spriteBatch.draw(shopTexture, shopX, shopY, shopWidth, shopHeight);
        }
    }

    private void renderSimpleShopSprite(int tileX, int tileY, float worldX, float worldY) {
        Texture shopTexture = ground1Texture;

        float shopWidth = TILE_SIZE;
        float shopHeight = TILE_SIZE;

        float shopX = worldX;
        float shopY = worldY;

        spriteBatch.draw(shopTexture, shopX, shopY, shopWidth, shopHeight);
    }

    private void renderHousingSprite(int tileX, int tileY, float worldX, float worldY) {
        if (game == null) return;
        for (User u : game.getPlayers()) {
            Player p = u.getPlayer();
            if (p == null || p.getHousings() == null) continue;
            for (Housing housing : p.getHousings()) {
            if (housing.getX() == tileX && housing.getY() == tileY) {
                CageType cageType = housing.getType();
                String typeName = cageType.getName().toLowerCase();
                Texture housingTexture;
                int buildingWidthTiles, buildingHeightTiles;
                if (typeName.contains("barn")) {
                    housingTexture = barnTexture;
                    buildingWidthTiles = 7;
                    buildingHeightTiles = 8;
                } else if (typeName.contains("coop")) {
                    housingTexture = coopTexture;
                    buildingWidthTiles = 6;
                    buildingHeightTiles = 8;
                } else {
                    housingTexture = barnTexture;
                    buildingWidthTiles = 7;
                    buildingHeightTiles = 8;
                }
                float buildingWidth = buildingWidthTiles * TILE_SIZE;
                float buildingHeight = buildingHeightTiles * TILE_SIZE;
                spriteBatch.draw(housingTexture, worldX, worldY - buildingHeight + TILE_SIZE, buildingWidth, buildingHeight);
                return;
            }
        }
        }
    }

    private int getPlayerIndexForHouse(int x, int y) {
        for (int i = 0; i < HOUSE_AREAS.length; i++) {
            int[] area = HOUSE_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1;
    }

    private int getNPCIndexForHouse(int x, int y) {
        for (int i = 0; i < NPC_HOUSE_AREAS.length; i++) {
            int[] area = NPC_HOUSE_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1;
    }

    private int getShopIndex(int x, int y) {
        for (int i = 0; i < SHOP_AREAS.length; i++) {
            int[] area = SHOP_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1;
    }

    private int getHousingIndex(int x, int y) {
        if (game == null) return -1;
        for (User u : game.getPlayers()) {
            Player p = u.getPlayer();
            if (p == null || p.getHousings() == null) continue;
            for (Housing housing : p.getHousings()) {
                CageType cageType = housing.getType();
                int buildingWidth, buildingHeight;
                if (cageType.getName().toLowerCase().contains("barn")) {
                    buildingWidth = 7;
                    buildingHeight = 4;
                } else {
                    buildingWidth = 6;
                    buildingHeight = 3;
                }
                int housingX = housing.getX();
                int housingY = housing.getY();
                if (x >= housingX && x <= housingX + buildingWidth &&
                    y >= housingY && y <= housingY + buildingHeight) {
                    return cageType.getName().toLowerCase().contains("barn") ? 0 : 1;
                }
            }
        }
        return -1;
    }

    private void renderMinimap() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float minimapSize = Math.min(screenWidth, screenHeight) * 0.25f;

        float minimapX = (screenWidth - minimapSize) / 2f;
        float minimapY = (screenHeight - minimapSize) / 2f;

        renderMinimapBackgroundAndBorder(minimapX, minimapY, minimapSize, minimapSize);


        OrthographicCamera minimapCamera = new OrthographicCamera();
        minimapCamera.setToOrtho(false, minimapSize, minimapSize);
        minimapCamera.position.set(minimapSize / 2f, minimapSize / 2f, 0);
        minimapCamera.update();

        spriteBatch.setProjectionMatrix(minimapCamera.combined);
        spriteBatch.begin();


        float scaleX = minimapSize / (MAP_WIDTH * TILE_SIZE);
        float scaleY = minimapSize / (MAP_HEIGHT * TILE_SIZE);
        float scale = Math.min(scaleX, scaleY) * 0.9f;


        float offsetX = (minimapSize - (MAP_WIDTH * TILE_SIZE * scale)) / 2f;
        float offsetY = (minimapSize - (MAP_HEIGHT * TILE_SIZE * scale)) / 2f;

        renderMinimapContent(scale, offsetX, offsetY);

        spriteBatch.end();


        spriteBatch.setProjectionMatrix(camera.combined);
    }

    private void renderHud() {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.0f, 0.0f, 0.5f, 0.5f);

        float infoBgX = 10;
        float infoBgY = Gdx.graphics.getHeight() - 70;
        float infoBgWidth = 220;
        float infoBgHeight = 60;
        shapeRenderer.rect(infoBgX, infoBgY, infoBgWidth, infoBgHeight);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float clockWidth = clockTexture.getWidth() * 1f;
        float clockHeight = clockTexture.getHeight() * 1f;
        float clockX = screenWidth - clockWidth - 20;
        float clockY = Gdx.graphics.getHeight() - clockHeight - 20;
        spriteBatch.draw(clockTexture, clockX, clockY, clockWidth, clockHeight);

        hudFont.setColor(Color.BLACK);
        Date date = game.getDate();
        String dateString = date.getCurrentWeekday().name().substring(0, 3) + ". " + date.getCurrentDay();
        hudFont.draw(spriteBatch, dateString, clockX + 160, clockY + 210);

        Time time = game.getTime();
        int hour = time.getHour();
        String ampm = hour < 12 || hour >= 24 ? "am" : "pm";
        if (hour == 0) {
            hour = 12;
        } else if (hour > 12) {
            if (hour < 24) {
                hour -= 12;
            } else {
                hour -= 24;
                if (hour == 0) hour = 12;
            }
        }
        String timeString = String.format("%d:%02d %s", hour, time.getMinute(), ampm);
        hudFont.draw(spriteBatch, timeString, clockX + 148, clockY + 120);


        Player player = game.getCurrentPlayer();
        if (player != null) {
            String balanceString = String.valueOf(player.getBankAccount().getBalance());
            balanceString += " | ";
            String energyString = String.valueOf(player.getEnergy());
            hudFont.draw(spriteBatch, balanceString, clockX + 100, clockY + 40);
            hudFont.draw(spriteBatch, energyString,clockX + 180, clockY + 40);
        }

        hudFont.setColor(Color.WHITE);
        String seasonString = "Season: " + controller.showSeason();
        hudFont.draw(spriteBatch, seasonString, 20, Gdx.graphics.getHeight() - 20);

        String weatherString = "Weather: " + game.getTodayWeather().name();
        hudFont.draw(spriteBatch, weatherString, 20, Gdx.graphics.getHeight() - 50);

        float onlineUsersX = 20;
        float onlineUsersY = Gdx.graphics.getHeight() - 100;
        float backgroundWidth = 250;
        float backgroundHeight = 0;

        int userCount = 0;
        if (game != null && game.getPlayers() != null) {
            for (User user : game.getPlayers()) {
                if (user != null && user.getPlayer() != null && !user.getUsername().startsWith("empty_slot_")) {
                    userCount++;
                }
            }
        }
        backgroundHeight = 40 + (userCount * 25);

        spriteBatch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(onlineUsersX - 10, onlineUsersY - backgroundHeight + 20, backgroundWidth, backgroundHeight);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        spriteBatch.begin();


        hudFont.setColor(Color.WHITE);
        float textY = onlineUsersY;
        hudFont.draw(spriteBatch, "Online Users:", onlineUsersX, textY);
        textY -= 30;

        if (game != null && game.getPlayers() != null) {
            for (User user : game.getPlayers()) {
                if (user != null && user.getPlayer() != null && !user.getUsername().startsWith("empty_slot_")) {
                    hudFont.draw(spriteBatch, "- " + user.getUsername(), onlineUsersX, textY);
                    textY -= 25;
                }
            }
        }
        spriteBatch.end();
    }


    private void renderMinimapContent(float scale, float offsetX, float offsetY) {
        Tile[][] tiles = gameMap.getTiles();


        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];

                    if (tile.getType() == TileType.Water) {
                        float worldX = offsetX + (x * TILE_SIZE * scale);
                        float worldY = offsetY + ((MAP_HEIGHT - 1 - y) * TILE_SIZE * scale);


                        spriteBatch.setColor(0.2f, 0.4f, 0.8f, 1f);
                        spriteBatch.draw(ground1Texture, worldX, worldY, TILE_SIZE * scale, TILE_SIZE * scale);
                        spriteBatch.setColor(1f, 1f, 1f, 1f);
                    }
                }
            }
        }


        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    TileType tileType = tile.getType();

                    float worldX = offsetX + (x * TILE_SIZE * scale);
                    float worldY = offsetY + ((MAP_HEIGHT - 1 - y) * TILE_SIZE * scale);

                    if (tileType == TileType.House || tileType == TileType.Wall) {
                        renderMinimapHouseSprite(x, y, worldX, worldY, scale);

                        if (tileType == TileType.Wall) {
                            int npcIndex = getNPCIndexForHouse(x, y);
                            if (npcIndex != -1) {
                                renderMinimapNPCHouseSprite(x, y, worldX, worldY, scale);
                            }

                            int shopIndex = getShopIndex(x, y);
                            if (shopIndex != -1) {
                                renderMinimapShopSprite(x, y, worldX, worldY, scale);
                            }

                            int housingIndex = getHousingIndex(x, y);
                            if (housingIndex != -1) {
                                renderMinimapHousingSprite(x, y, worldX, worldY, scale);
                            }
                        }
                    } else if (tileType == TileType.NPCHouse) {
                        renderMinimapNPCHouseSprite(x, y, worldX, worldY, scale);
                    } else if (tileType == TileType.Shop) {
                        renderMinimapShopSprite(x, y, worldX, worldY, scale);
                    }
                }
            }
        }


        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player == null) continue;

            float playerX = player.currentX();
            float playerY = player.currentY();

            if (playerX < 0 || playerX >= MAP_WIDTH || playerY < 0 || playerY >= MAP_HEIGHT) {
                continue;
            }

            float worldX = offsetX + (playerX * TILE_SIZE * scale);
            float worldY = offsetY + ((MAP_HEIGHT - 1 - playerY) * TILE_SIZE * scale);


            if (player.equals(game.getCurrentPlayer())) {
                spriteBatch.setColor(1f, 1f, 0f, 1f);
            } else {
                spriteBatch.setColor(0f, 1f, 0f, 1f);
            }
            spriteBatch.draw(ground1Texture, worldX + (TILE_SIZE * scale * 0.25f), worldY + (TILE_SIZE * scale * 0.25f),
                           TILE_SIZE * scale * 0.5f, TILE_SIZE * scale * 0.5f);
            spriteBatch.setColor(1f, 1f, 1f, 1f);
        }
    }

    private Texture getMinimapBaseTexture(TileType tileType) {
        switch (tileType) {
            case Water:
                return lake1Texture;
            case Grass:
                return grass1Texture;
            case Earth:
                return ground1Texture;
            case Stone:
                return stone1Texture;
            case Tree:
                return tree1Texture;
            case House:
                return house1Texture;
            case NPCHouse:
                return npcHouse1Texture;
            case Shop:
                return blacksmithTexture;
            default:
                return ground1Texture;
        }
    }

    private void renderMinimapHouseSprite(int tileX, int tileY, float worldX, float worldY, float scale) {
        int playerIndex = getPlayerIndexForHouse(tileX, tileY);
        if (playerIndex != -1 && playerIndex < playerHouseVariants.length) {
            Texture houseTexture = null;
            switch (playerHouseVariants[playerIndex]) {
                case 0:
                    houseTexture = house1Texture;
                    break;
                case 1:
                    houseTexture = house2Texture;
                    break;
                case 2:
                    houseTexture = house3Texture;
                    break;
            }
            if (houseTexture != null) {
                spriteBatch.setColor(0.8f, 0.6f, 0.4f, 1f);
                spriteBatch.draw(houseTexture, worldX, worldY, TILE_SIZE * scale, TILE_SIZE * scale);
                spriteBatch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    private void renderMinimapNPCHouseSprite(int tileX, int tileY, float worldX, float worldY, float scale) {
        int npcIndex = getNPCIndexForHouse(tileX, tileY);
        if (npcIndex != -1 && npcIndex < npcHouseVariants.length) {
            Texture npcHouseTexture = null;
            switch (npcHouseVariants[npcIndex]) {
                case 0:
                    npcHouseTexture = npcHouse1Texture;
                    break;
                case 1:
                    npcHouseTexture = npcHouse2Texture;
                    break;
                case 2:
                    npcHouseTexture = npcHouse3Texture;
                    break;
                case 3:
                    npcHouseTexture = npcHouse4Texture;
                    break;
                case 4:
                    npcHouseTexture = npcHouse5Texture;
                    break;
            }
            if (npcHouseTexture != null) {
                spriteBatch.setColor(0.6f, 0.4f, 0.8f, 1f);
                spriteBatch.draw(npcHouseTexture, worldX, worldY, TILE_SIZE * scale, TILE_SIZE * scale);
                spriteBatch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    private void renderMinimapShopSprite(int tileX, int tileY, float worldX, float worldY, float scale) {
        int shopIndex = getShopIndex(tileX, tileY);
        if (shopIndex != -1) {
            Texture shopTexture = null;
            switch (shopIndex) {
                case 0:
                    shopTexture = blacksmithTexture;
                    break;
                case 1:
                    shopTexture = jojamartTexture;
                    break;
                case 2:
                    shopTexture = pierresShopTexture;
                    break;
                case 3:
                    shopTexture = carpentersShopTexture;
                    break;
                case 4:
                    shopTexture = fishShopTexture;
                    break;
                case 5:
                    shopTexture = ranchTexture;
                    break;
                case 6:
                    shopTexture = saloonTexture;
                    break;
            }
            if (shopTexture != null) {
                spriteBatch.setColor(0.8f, 0.8f, 0.2f, 1f);
                spriteBatch.draw(shopTexture, worldX, worldY, TILE_SIZE * scale, TILE_SIZE * scale);
                spriteBatch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    private void renderMinimapHousingSprite(int tileX, int tileY, float worldX, float worldY, float scale) {
        int housingIndex = getHousingIndex(tileX, tileY);
        if (housingIndex != -1) {
            Texture housingTexture = null;
            if (housingIndex == 0) {
                housingTexture = barnTexture;
            } else if (housingIndex == 1) {
                housingTexture = coopTexture;
            }
            if (housingTexture != null) {
                spriteBatch.setColor(0.4f, 0.2f, 0.1f, 1f);
                spriteBatch.draw(housingTexture, worldX, worldY, TILE_SIZE * scale, TILE_SIZE * scale);
                spriteBatch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    private void renderMinimapTreeSprite(int tileX, int tileY, float worldX, float worldY, float scale) {
        Texture treeTexture = null;
        switch (treeVariantMap[tileX][tileY]) {
            case 0:
                treeTexture = tree1Texture;
                break;
            case 1:
                treeTexture = tree2Texture;
                break;
            case 2:
                treeTexture = tree3Texture;
                break;
        }
        if (treeTexture != null) {
            spriteBatch.setColor(0.2f, 0.6f, 0.2f, 1f);
            spriteBatch.draw(treeTexture, worldX, worldY, TILE_SIZE * scale, TILE_SIZE * scale);
            spriteBatch.setColor(1f, 1f, 1f, 1f);
        }
    }

    private void renderMinimapBackgroundAndBorder(float x, float y, float width, float height) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        OrthographicCamera uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.update();
        shapeRenderer.setProjectionMatrix(uiCamera.combined);

        shapeRenderer.setColor(0, 0, 0, 0.8f);
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.end();

        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    private void renderWeather(float delta) {
        if (game == null || game.getTodayWeather() == null) {
            return;
        }

        weatherStateTime += delta;
        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        switch (game.getTodayWeather()) {
            case Rainy:
            case Stormy:
                for (Rectangle particle : rainParticles) {

                    particle.y -= 300 * delta;

                    if (particle.y < 0) {
                        particle.y = Gdx.graphics.getHeight();
                        particle.x = (float) (Math.random() * Gdx.graphics.getWidth());
                    }
                    spriteBatch.draw(rainTexture, particle.x, particle.y);
                }
                break;

            case Snowy:
                for (Rectangle particle : snowParticles) {

                    particle.y -= 60 * delta;
                    particle.x += (float) (Math.sin(particle.y / 30) * 20 * delta);


                    if (particle.y < 0) {
                        particle.y = Gdx.graphics.getHeight();
                        particle.x = (float) (Math.random() * Gdx.graphics.getWidth());
                    }
                    spriteBatch.draw(snowTexture, particle.x, particle.y, 24, 24);
                }
                break;

            default:

                break;
        }

        spriteBatch.end();


        boolean shouldFlash = false;
        if (game.getTodayWeather() == Weather.Stormy) {
            stormEffectTimer += delta;
            if (stormEffectTimer > nextLightningTime) {
                lightningActive = true;
                lightningDuration = 0.15f;
                nextLightningTime = (float) (stormEffectTimer + 3 + Math.random() * 5);
            }
            if (lightningActive) {
                shouldFlash = true;
            }
        }


        if (cheatLightningActive) {
            shouldFlash = true;
        }


        if (shouldFlash) {
            lightningDuration -= delta;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(hudCamera.combined);
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 0.7f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);


            if (lightningDuration <= 0) {
                lightningActive = false;
                cheatLightningActive = false;
            }
        }
    }

    private void renderDayNightOverlay() {
        if (game == null) return;

        Time time = game.getTime();
        int hour = time.getHour();
        int minute = time.getMinute();


        float startHour = 18f;
        float endHour = 26f;

        float maxOpacity = 0.4f;

        float currentHour = hour + (minute / 60f);
        float opacity = 0f;

        if (currentHour >= startHour) {

            float progress = (currentHour - startHour) / (endHour - startHour);

            progress = Math.max(0, Math.min(1, progress));
            opacity = progress * maxOpacity;
        }


        if (opacity > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(hudCamera.combined);
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);


            shapeRenderer.setColor(0, 0, 0, opacity);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }



    private void renderInventoryOverlay(float delta) {
        if (!isInventoryOpen) {
            return;
        }
        inventoryStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        inventoryStage.act(delta);
        inventoryStage.draw();
    }

    private void renderToolMenu(float delta) {
        if (!isToolMenuOpen) return;

        toolMenuStage.act(delta);
        toolMenuStage.draw();
    }

    private void renderPlantingUI(float delta) {
        if (!isPlantingSelectionOpen) return;
        plantingStage.act(delta);
        plantingStage.draw();
    }

    private void setupCraftingUI() {
        Table mainCraftingContainer = new Table();
        mainCraftingContainer.setFillParent(true);
        mainCraftingContainer.center();
        craftingStage.addActor(mainCraftingContainer);


        Table background = new Table(skin);
        background.setBackground(new TextureRegionDrawable(new TextureRegion(inventoryBackground)));
        mainCraftingContainer.add(background).width(Gdx.graphics.getWidth() * 0.7f).height(Gdx.graphics.getHeight() * 0.8f);


        TextButton craftingBackButton = new TextButton("Back", skin);
        craftingBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isCraftingMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        background.add(craftingBackButton).left().pad(8).row();

        craftingMenuContentTable = new Table();
        ScrollPane scrollPane = new ScrollPane(craftingMenuContentTable, skin);
        scrollPane.setFadeScrollBars(false);

        background.add(scrollPane).expand().fill().pad(20);
    }

    private void showCraftingMenu() {
        craftingMenuContentTable.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        ArrayList<CraftingRecipe> knownRecipes = currentPlayer.getCraftingRecipe();


        for (CraftingRecipes recipeEnum : CraftingRecipes.values()) {
            boolean isKnown = false;

            for (CraftingRecipe known : knownRecipes) {
                if (known.getRecipeType() == recipeEnum) {
                    isKnown = true;
                    break;
                }
            }


            Table recipeRow = new Table();
            recipeRow.pad(5);

            ItemType product = recipeEnum.getProduct();
            Texture productTexture = textureManager.getTexture(product.getEnumName());
            Image productImage = new Image(productTexture);
            if (productTexture != null) {
                productImage = new Image(productTexture);
            } else {

                productImage = new Image(skin.getDrawable("default-round"));
                Gdx.app.log("CraftingMenu", "Missing texture for product: " + product.getEnumName());
            }

            Label recipeLabel = new Label(recipeEnum.getName(), skin);

            if (isKnown) {

                TextButton craftButton = new TextButton("Craft", skin);


                boolean canCraft = true;
                for (Map.Entry<ItemType, Integer> entry : recipeEnum.getIngredients().entrySet()) {
                    Item itemInInventory = currentPlayer.getInventory().findItemByType(entry.getKey());
                    if (itemInInventory == null || itemInInventory.getNumber() < entry.getValue()) {
                        canCraft = false;
                        break;
                    }
                }
                craftButton.setDisabled(!canCraft);

                craftButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Result result = controller.craftItem(recipeEnum.getName());
                        if (result.isSuccessful()) {
                            Item craftedItem = game.getCurrentPlayer().getInventory().findItemByType(recipeEnum.getProduct());
                            if (craftedItem instanceof CraftingMachine) {
                                isMachinePlacementMode = true;
                                machineToPlace = (CraftingMachine) craftedItem;
                                machinePlacementTexture = textureManager.getTexture(machineToPlace.getItemType().getEnumName());
                                isCraftingMenuOpen = false;
                                Gdx.input.setInputProcessor(multiplexer);
                            }

                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                                data.put("senderUsername", game.getCurrentPlayer().getUsername());
                                data.put("recipeName", recipeEnum.getName());
                                ns.sendPlayerAction("craft_item", data);
                            }
                        }
                        showCraftingMenu();
                        generalMessageLabel.setText(result.Message());
                        generalMessageLabel.setVisible(true);
                        generalMessageTimer = GENERAL_MESSAGE_DURATION;
                    }
                });

                recipeRow.add(productImage).size(32, 32).padRight(10);
                recipeRow.add(recipeLabel).width(200).left();
                recipeRow.add(craftButton).width(80);

            } else {

                productImage.setColor(Color.BLACK);
                recipeLabel.setColor(Color.GRAY);
                recipeRow.add(productImage).size(32, 32).padRight(10);
                recipeRow.add(recipeLabel).width(200).left();
                recipeRow.add(new Label("[LOCKED]", skin)).width(80);
            }
            craftingMenuContentTable.add(recipeRow).fillX().row();
        }
    }

    private void renderCraftingMenu(float delta) {
        if (!isCraftingMenuOpen) return;
        craftingStage.act(delta);
        craftingStage.draw();
    }



    private void setupCookingUI() {
        Table mainCookingContainer = new Table();
        mainCookingContainer.setFillParent(true);
        mainCookingContainer.center();
        cookingStage.addActor(mainCookingContainer);

        Table background = new Table(skin);
        background.setBackground(new TextureRegionDrawable(new TextureRegion(inventoryBackground)));
        mainCookingContainer.add(background).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.85f);


        TextButton cookingBackButton = new TextButton("Back", skin);
        cookingBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isCookingMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        background.add(cookingBackButton).left().pad(8).row();

        Table contentSplit = new Table();
        background.add(contentSplit).expand().fill().pad(15);

        Table leftPanel = new Table();
        leftPanel.top();
        leftPanel.add(new Label("Recipes", skin)).padBottom(10).row();
        cookingMenuContentTable = new Table();
        ScrollPane recipeScrollPane = new ScrollPane(cookingMenuContentTable, skin);
        recipeScrollPane.setFadeScrollBars(false);
        leftPanel.add(recipeScrollPane).expand().fill();

        Table rightPanel = new Table();
        rightPanel.top();
        rightPanel.add(new Label("Refrigerator", skin)).padBottom(10).row();
        fridgeContentTable = new Table();
        ScrollPane fridgeScrollPane = new ScrollPane(fridgeContentTable, skin);
        fridgeScrollPane.setFadeScrollBars(false);
        rightPanel.add(fridgeScrollPane).expand().fill().row();

        TextButton moveToFridgeButton = new TextButton("Move Food to Fridge", skin);
        rightPanel.add(moveToFridgeButton).padTop(10).fillX();

        moveToFridgeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.moveRandomFoodToRefrigerator();
                showCookingMenu();


                generalMessageLabel.setText(result.Message());
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
                if (result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("senderUsername", game.getCurrentPlayer().getUsername());
                        ns.sendPlayerAction("move_random_food_to_refrigerator", data);
                    }
                }
            }
        });

        contentSplit.add(leftPanel).expand().fill();
        Image separator = new Image(skin.newDrawable("white", Color.GRAY));
        contentSplit.add(separator).width(2).fillY().pad(0, 10, 0, 10);
        contentSplit.add(rightPanel).width(250);
    }

    private void showCookingMenu() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;


        cookingMenuContentTable.clear();
        cookingMenuContentTable.top();
        ArrayList<CookingRecipe> knownRecipes = currentPlayer.getCookingRecipe();

        for (CookingRecipeType recipeEnum : CookingRecipeType.values()) {
            boolean isKnown = knownRecipes.stream().anyMatch(kr -> kr.getRecipeType() == recipeEnum);

            Table recipeRow = new Table();
            recipeRow.pad(5).left();

            Texture recipeTexture = textureManager.getTexture(recipeEnum.getEnumName());
            Image recipeImage = recipeTexture != null ? new Image(recipeTexture) : new Image(skin.getDrawable("default-round"));
            Label recipeLabel = new Label(recipeEnum.getFoodType().getName(), skin);

            if (isKnown) {
                TextButton cookButton = new TextButton("Cook", skin);


                boolean canCook = true;
                Map<ItemType, Integer> totalIngredients = new HashMap<>();
                currentPlayer.getInventory().getItems().forEach(item -> totalIngredients.merge(item.getItemType(), item.getNumber(), Integer::sum));
                currentPlayer.getHouseRefrigerator().getItems().forEach(item -> totalIngredients.merge(item.getItemType(), item.getNumber(), Integer::sum));

                for (Map.Entry<ItemType, Integer> entry : recipeEnum.getIngredients().entrySet()) {
                    if (totalIngredients.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                        canCook = false;
                        break;
                    }
                }
                cookButton.setDisabled(!canCook);
                cookButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        Result result = controller.cookFood(recipeEnum.getDisplayName());
                        if (result.isSuccessful()) {

                            showPlacementDialog(controller.getJustCookedFood());

                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                                data.put("senderUsername", game.getCurrentPlayer().getUsername());
                                data.put("recipeName", recipeEnum.getDisplayName());
                                data.put("senderX", game.getCurrentPlayer().currentX());
                                data.put("senderY", game.getCurrentPlayer().currentY());
                                ns.sendPlayerAction("cook_food", data);
                            }
                        } else {

                            generalMessageLabel.setText(result.Message());
                            generalMessageLabel.setVisible(true);
                            generalMessageTimer = GENERAL_MESSAGE_DURATION;
                        }

                        showCookingMenu();
                    }
                });

                recipeRow.add(recipeImage).size(32, 32).padRight(10);
                recipeRow.add(recipeLabel).width(150).left();
                recipeRow.add(cookButton).width(80);

            } else {
                recipeImage.setColor(Color.BLACK);
                recipeLabel.setColor(Color.GRAY);
                recipeRow.add(recipeImage).size(32, 32).padRight(10);
                recipeRow.add(recipeLabel).width(150).left();
                recipeRow.add(new Label("[Locked]", skin)).width(80);
            }
            cookingMenuContentTable.add(recipeRow).fillX().row();
        }


        fridgeContentTable.clear();
        fridgeContentTable.top();
        for (Item item : currentPlayer.getHouseRefrigerator().getItems()) {
            Table itemRow = new Table();
            itemRow.left();
            Texture itemTexture = textureManager.getTexture(item.getItemType().getEnumName());
            Image itemImage = itemTexture != null ? new Image(itemTexture) : new Image(skin.getDrawable("default-round"));
            Label itemLabel = new Label(item.getName() + " (" + item.getNumber() + ")", skin);

            itemRow.add(itemImage).size(24, 24).padRight(8);
            itemRow.add(itemLabel).expandX().left();
            fridgeContentTable.add(itemRow).pad(4).fillX().row();
        }
    }

    private void renderCookingMenu(float delta) {
        if (!isCookingMenuOpen) return;
        cookingStage.act(delta);
        cookingStage.draw();
    }

    public void triggerCrowAttackAnimation() {
        crowAnimations.clear();

        int crowCount = 3 + (int)(Math.random() * 3);
        for (int i = 0; i < crowCount; i++) {
            crowAnimations.add(new CrowAnimation());
        }
    }

    private void renderCrowAnimations(float delta) {
        if (crowAnimations.isEmpty() || crowTexture == null) {
            return;
        }


        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        for (java.util.Iterator<CrowAnimation> iterator = crowAnimations.iterator(); iterator.hasNext();) {
            CrowAnimation crow = iterator.next();
            crow.update(delta);

            if (crow.isOffScreen()) {
                iterator.remove();
            } else {

                boolean flip = crow.speedX < 0;
                spriteBatch.draw(crowTexture,
                    crow.x, crow.y,
                    crowTexture.getWidth() * 2, crowTexture.getHeight() * 2,
                    0, 0,
                    crowTexture.getWidth(), crowTexture.getHeight(),
                    flip, false);
            }
        }

        spriteBatch.end();
    }

    private void setupInventoryUI() {
        mainInventoryContainer = new Table();
        inventoryStage.addActor(mainInventoryContainer);
        mainInventoryContainer.setFillParent(true);
        mainInventoryContainer.center();

        menuContentTable = new Table();
        mainInventoryContainer.add(menuContentTable).expand().fill();
    }

    private void showMainMenuButtons() {
        menuContentTable.clear();

        Stack mainStack = new Stack();
        mainStack.add(new Image(inventoryBackground));

        Table buttonTable = new Table();

        TextButton journalButton = new TextButton("Journal", skin);
        TextButton inventoryButton = new TextButton("Inventory", skin);
        TextButton skillsButton = new TextButton("Skills", skin);
        TextButton socialButton = new TextButton("Social", skin);
        TextButton mapButton = new TextButton("Map", skin);
        TextButton craftInfoButton = new TextButton("Craft Info", skin);
        TextButton questsButton = new TextButton("Show Quests", skin);
        TextButton scoreBoard = new TextButton("Score Board", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton closeButton = new TextButton("Close", skin);

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showInventoryDisplay(false);
            }
        });
        questsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuestMenuOpen = true;
                showQuestsMenu();
            }
        });
        skillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSkillsDisplay();
            }
        });

        socialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSocialDisplay();
            }
        });

        mapButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMinimap = !showMinimap;
                isInventoryOpen = false;
            }
        });

        TextButton scoreboardButton = new TextButton("Score Board", skin);
        scoreboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scoreboardContainer.setVisible(true);
                updateScoreboard();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSettingsMenu();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isInventoryOpen = false;
            }
        });
        craftInfoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isInfoMenuOpen = true;
                showInfoMenu();
            }
        });
        journalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isJournalMenuOpen = true;
                showJournalMenu();
            }
        });

        float buttonWidth = 200f;
        float buttonPad = 10f;

        buttonTable.add(journalButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(inventoryButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(skillsButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(socialButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(mapButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(craftInfoButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(questsButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(scoreboardButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(settingsButton).width(buttonWidth).pad(buttonPad).row();
        buttonTable.add(closeButton).width(buttonWidth).pad(buttonPad).row();
        mainStack.add(buttonTable);
        menuContentTable.add(mainStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void showInventoryDisplay(boolean showOnlyPlantables) {
        menuContentTable.clear();

        Stack inventoryStack = new Stack();
        inventoryStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();
        Table itemsTable = new Table();
        updateInventoryGrid(itemsTable, showOnlyPlantables);

        ScrollPane scrollPane = new ScrollPane(itemsTable, skin);
        scrollPane.setFadeScrollBars(false);

        contentTable.add(scrollPane).expand().fill().pad(40).row();

        TextButton trashButton = new TextButton("Trash Item", skin);
        if (isTrashModeActive) {
            trashButton.setText("Cancel Trash");
            trashButton.setColor(Color.RED);
        } else {
            trashButton.setColor(Color.WHITE);
        }

        trashButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isTrashModeActive = !isTrashModeActive;
                showInventoryDisplay(false);
            }
        });

        Table bottomButtons = new Table();
        bottomButtons.add(trashButton).pad(10);
        addBackButtonToMenu(bottomButtons);
        contentTable.add(bottomButtons).bottom().left();

        inventoryStack.add(contentTable);
        menuContentTable.add(inventoryStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void showSkillsDisplay() {
        menuContentTable.clear();

        Stack skillsStack = new Stack();
        skillsStack.add(new Image(inventoryBackground));


        Table contentTable = new Table();

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        Table skillsTable = new Table();
        TooltipManager tooltipManager = new TooltipManager();
        tooltipManager.instant();

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = skin.getDrawable("default-slider");
        barStyle.knobBefore = skin.getDrawable("default-slider-knob");

        for (Skills skill : Skills.values()) {
            Table skillRow = new Table();
            Texture iconTexture = textureManager.getTexture(skill.name() + "_Skill_Icon");
            Image icon;
            if (iconTexture != null) {
                icon = new Image(iconTexture);
                Tooltip<Label> tooltip = new Tooltip<>(new Label(skill.getSkillDescription(), skin));
                icon.addListener(tooltip);
            } else {
                icon = new Image(skin.getDrawable("default-round"));
                Gdx.app.log("Skills", "Missing texture for skill icon: " + skill.name() + "_Skill_Icon.png");
            }
            skillRow.add(icon).size(48, 48).padRight(10);

            skillRow.add(new Label(skill.name(), skin)).width(100);

            ProgressBar progressBar = new ProgressBar(0, 100, 1, false, barStyle);
            int currentExp = currentPlayer.getSkillExperience(skill);
            int expForNextLevel = skill.getExpForNextLevel();
            float progress = (expForNextLevel > 0) ? ((float)currentExp / expForNextLevel) * 100f : 0f;
            progressBar.setValue(progress);

            skillRow.add(progressBar).width(200).padRight(10);
            skillRow.add(new Label("Level " + currentPlayer.getSkillLevel(skill), skin));

            skillsTable.add(skillRow).padBottom(10).row();
        }

        contentTable.add(skillsTable).expand().center().row();

        Table bottomButtonTable = new Table();
        addBackButtonToMenu(bottomButtonTable);
        contentTable.add(bottomButtonTable).bottom().left();

        skillsStack.add(contentTable);
        menuContentTable.add(skillsStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void showSocialDisplay() {
        menuContentTable.clear();

        Stack socialStack = new Stack();
        socialStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();

        Table socialInfoTable = new Table();
        String playerFriendships = controller.showAllFriendShips().Message();
        String npcFriendships = controller.showNPCFriendships().Message();

        socialInfoTable.add(new Label("--- Player Friendships ---", skin)).padBottom(10).row();
        socialInfoTable.add(new Label(playerFriendships, skin)).left().padBottom(20).row();
        socialInfoTable.add(new Label("--- NPC Friendships ---", skin)).padBottom(10).row();
        socialInfoTable.add(new Label(npcFriendships, skin)).left().row();

        ScrollPane scrollPane = new ScrollPane(socialInfoTable, skin);
        scrollPane.setFadeScrollBars(false);

        contentTable.add(scrollPane).expand().fill().pad(40).row();


        Table bottomButtonTable = new Table();
        addBackButtonToMenu(bottomButtonTable);
        contentTable.add(bottomButtonTable).bottom().left();

        socialStack.add(contentTable);
        menuContentTable.add(socialStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void showSettingsMenu() {
        menuContentTable.clear();
        Stack settingsStack = new Stack();
        settingsStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();

        TextButton leaveGameButton = new TextButton("Terminate Game", skin);
        TextButton kickPlayerButton = new TextButton("Kick Player", skin);

        leaveGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null && game != null && game.getCurrentPlayer() != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("initiator", game.getCurrentPlayer().getUsername());
                    ns.sendPlayerAction("vote_terminate_start", data);

                    beginTerminateVoteLocal(game.getCurrentPlayer().getUsername());
                }
            }
        });

        kickPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showKickPlayerMenu();
            }
        });


        Table mainButtons = new Table();
        mainButtons.add(leaveGameButton).width(200).pad(10).row();
        mainButtons.add(kickPlayerButton).width(200).pad(10).row();


        contentTable.add(mainButtons).expand().center().row();


        Table bottomButtonTable = new Table();
        addBackButtonToMenu(bottomButtonTable);
        contentTable.add(bottomButtonTable).bottom().left();

        settingsStack.add(contentTable);
        menuContentTable.add(settingsStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void showKickPlayerMenu() {
        menuContentTable.clear();
        Stack stack = new Stack();
        stack.add(new Image(inventoryBackground));
        Table content = new Table();
        content.add(new Label("Select a player to kick", skin)).pad(12).row();
        Table list = new Table();
        if (game != null && game.getPlayers() != null) {
            for (User u : game.getPlayers()) {
                if (u == null || u.getPlayer() == null) continue;
                if (game.getCurrentPlayer() != null && u.getUsername().equals(game.getCurrentPlayer().getUsername())) continue;
                if (u.getUsername().startsWith("empty_slot_")) continue;
                TextButton b = new TextButton(u.getUsername(), skin);
                b.addListener(new ClickListener(){
                    @Override public void clicked(InputEvent e, float x, float y){
                        startKickVote(u.getUsername());
                    }
                });
                list.add(b).width(240).pad(6).row();
            }
        }
        ScrollPane sp = new ScrollPane(list, skin);
        sp.setFadeScrollBars(false);
        content.add(sp).width(400).height(300).pad(10).row();
        Table bottom = new Table();
        TextButton back = new TextButton("Back", skin);
        TextButton close = new TextButton("Close", skin);
        back.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ showSettingsMenu(); }});
        close.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ isInventoryOpen = false; }});
        bottom.add(back).pad(8);
        bottom.add(close).pad(8);
        content.add(bottom).pad(8).row();
        stack.add(content);
        menuContentTable.add(stack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }


    private Window kickVotePopup;
    private String kickTarget = null;
    private int kickYesVotes = 0;
    private int kickTotalPlayers = 0;

    private void startKickVote(String targetUsername) {
        kickTarget = targetUsername;
        kickTotalPlayers = (game != null && game.getPlayers() != null) ? (int) game.getPlayers().stream().filter(u -> u != null && u.getPlayer() != null && !u.getUsername().startsWith("empty_slot_")).count() : 0;
        kickYesVotes = 1;

        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
        if (ns != null && game != null && game.getCurrentPlayer() != null) {
            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
            data.put("initiator", game.getCurrentPlayer().getUsername());
            data.put("target", targetUsername);
            ns.sendPlayerAction("kick_vote_start", data);

            broadcastKickVoteUpdate(false, false);
        }
    }

    public void onKickVoteStarted(String initiator, String target) {
        kickTarget = target;
        kickTotalPlayers = (game != null && game.getPlayers() != null) ? (int) game.getPlayers().stream().filter(u -> u != null && u.getPlayer() != null && !u.getUsername().startsWith("empty_slot_")).count() : 0;
        kickYesVotes = 1;

        if (game != null && game.getCurrentPlayer() != null && game.getCurrentPlayer().getUsername().equals(initiator)) return;
        showKickVotePopup(initiator, target);
    }

    private void showKickVotePopup(String initiator, String target) {
        if (kickVotePopup != null) kickVotePopup.remove();
        kickVotePopup = new Window("Kick Player?", skin);
        kickVotePopup.setMovable(false);
        kickVotePopup.setModal(true);
        Table content = new Table();
        content.add(new Label(initiator + " requests to kick " + target, skin)).pad(10).row();
        TextButton accept = new TextButton("Accept", skin);
        TextButton reject = new TextButton("Reject", skin);
        Table buttons = new Table();
        buttons.add(accept).pad(6);
        buttons.add(reject).pad(6);
        content.add(buttons).pad(6).row();
        kickVotePopup.add(content).pad(8);
        kickVotePopup.pack();
        float x = Gdx.graphics.getWidth() - kickVotePopup.getWidth() - 20f;
        float y = 20f;
        kickVotePopup.setPosition(x, y);
        stage.addActor(kickVotePopup);
        kickVotePopup.toFront();

        accept.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float px, float py){
                kickVotePopup.remove();
                if (game != null && game.getCurrentPlayer() != null && !game.getCurrentPlayer().getUsername().equals(initiator)) {
                    kickYesVotes++;
                }
                checkKickThreshold();
                broadcastKickVoteUpdate(false, false);
                Gdx.input.setInputProcessor(multiplexer);
                isInventoryOpen = false;
                if (stage != null) stage.unfocusAll();
            }
        });
        reject.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float px, float py){
                kickVotePopup.remove();
                broadcastKickVoteUpdate(false, false);
                Gdx.input.setInputProcessor(multiplexer);
                isInventoryOpen = false;
                if (stage != null) stage.unfocusAll();
            }
        });
    }

    private void checkKickThreshold() {
        if (kickTotalPlayers <= 0) return;
        if (kickYesVotes > kickTotalPlayers / 2) {

            broadcastKickVoteUpdate(true, true);

            if (controller != null && kickTarget != null) controller.removePlayer(kickTarget);
        }
    }

    private void broadcastKickVoteUpdate(boolean finished, boolean passed) {
        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
        if (ns != null) {
            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
            if (kickTarget != null) data.put("target", kickTarget);
            data.put("yes", kickYesVotes);
            data.put("total", kickTotalPlayers);
            data.put("finished", finished);
            data.put("passed", passed);
            ns.sendPlayerAction("kick_vote_update", data);
        }
    }

    public void onKickVoteUpdate(String target, int yes, int total, boolean finished, boolean passed) {
        kickTarget = target;
        kickYesVotes = yes;
        kickTotalPlayers = total;
        if (finished && passed) {
            if (kickVotePopup != null) kickVotePopup.remove();
            if (controller != null && kickTarget != null) controller.removePlayer(kickTarget);
        }
    }


    private Window terminateVotePopup;
    private int terminateYesVotes = 0;
    private int terminateTotalPlayers = 0;
    private String terminateInitiator = null;

    private void beginTerminateVoteLocal(String initiator) {
        terminateInitiator = initiator;
        terminateTotalPlayers = (game != null && game.getPlayers() != null) ? (int) game.getPlayers().stream().filter(u -> u != null && u.getPlayer() != null && !u.getUsername().startsWith("empty_slot_")).count() : 0;
        terminateYesVotes = 1;

        isInventoryOpen = false;
        if (stage != null) stage.unfocusAll();
        broadcastTerminateVoteUpdate(false, false);
    }

    public void onTerminateVoteStarted(String initiator) {

        terminateInitiator = initiator;
        terminateTotalPlayers = (game != null && game.getPlayers() != null) ? (int) game.getPlayers().stream().filter(u -> u != null && u.getPlayer() != null && !u.getUsername().startsWith("empty_slot_")).count() : 0;

        terminateYesVotes = 1;
        if (game != null && game.getCurrentPlayer() != null && !game.getCurrentPlayer().getUsername().equals(initiator)) {
            showTerminateVotePopup(initiator);
        }
    }

    private void showTerminateVotePopup(String initiator) {
        if (terminateVotePopup != null) terminateVotePopup.remove();
        terminateVotePopup = new Window("Terminate Game?", skin);
        terminateVotePopup.setMovable(false);
        terminateVotePopup.setModal(false);
        Table content = new Table();
        content.add(new Label(initiator + " requests to terminate the game.", skin)).pad(10).row();
        TextButton accept = new TextButton("Accept", skin);
        TextButton reject = new TextButton("Reject", skin);
        Table buttons = new Table();
        buttons.add(accept).pad(6);
        buttons.add(reject).pad(6);
        content.add(buttons).pad(6).row();
        terminateVotePopup.add(content).pad(8);
        terminateVotePopup.pack();
        float x = Gdx.graphics.getWidth() - terminateVotePopup.getWidth() - 20f;
        float y = 20f;
        terminateVotePopup.setPosition(x, y);
        stage.addActor(terminateVotePopup);
        terminateVotePopup.toFront();

        accept.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float px, float py){
                if (terminateVotePopup != null) { terminateVotePopup.remove(); terminateVotePopup = null; }

                if (game != null && game.getCurrentPlayer() != null && !game.getCurrentPlayer().getUsername().equals(terminateInitiator)) {
                    terminateYesVotes++;
                }
                checkTerminateThreshold();
                broadcastTerminateVoteUpdate(false, false);
                Gdx.input.setInputProcessor(multiplexer);
                isInventoryOpen = false;
                if (stage != null) stage.unfocusAll();
            }
        });
        reject.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float px, float py){
                if (terminateVotePopup != null) { terminateVotePopup.remove(); terminateVotePopup = null; }
                broadcastTerminateVoteUpdate(false, false);
                Gdx.input.setInputProcessor(multiplexer);
                isInventoryOpen = false;
                if (stage != null) stage.unfocusAll();
            }
        });
    }

    private void checkTerminateThreshold() {

        if (terminateTotalPlayers <= 0) return;
        if (terminateYesVotes > terminateTotalPlayers / 2) {
            broadcastTerminateVoteUpdate(true, true);

            isInventoryOpen = false;
            Main.getInstance().setScreen(new GDXMainMenu(com.example.main.models.App.getInstance().getNetworkService()));
        }
    }

    private void broadcastTerminateVoteUpdate(boolean finished, boolean passed) {
        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
        if (ns != null) {
            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
            data.put("initiator", terminateInitiator);
            data.put("yes", terminateYesVotes);
            data.put("total", terminateTotalPlayers);
            data.put("finished", finished);
            data.put("passed", passed);
            ns.sendPlayerAction("vote_terminate_update", data);
        }
    }

    public void onTerminateVoteUpdate(String initiator, int yes, int total, boolean finished, boolean passed) {

        terminateInitiator = initiator;
        terminateYesVotes = yes;
        terminateTotalPlayers = total;
        if (finished && passed) {
            if (terminateVotePopup != null) terminateVotePopup.remove();
            isInventoryOpen = false;
            Main.getInstance().setScreen(new GDXMainMenu(com.example.main.models.App.getInstance().getNetworkService()));
        }
    }

    private void addBackButtonToMenu(Table buttonTable) {
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isTrashModeActive = false;
                isInfoMenuOpen = false;
                isQuestMenuOpen = false;
                isJournalMenuOpen = false;
                showMainMenuButtons();
            }
        });
        buttonTable.add(backButton).pad(10);
    }

    private void updateInventoryGrid(Table table, boolean showOnlyPlantables) {
        table.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        ArrayList<Item> items = currentPlayer.getInventory().getItems();
        if (showOnlyPlantables) {
            items = items.stream()
                .filter(item -> item instanceof Seed)
                .collect(Collectors.toCollection(ArrayList::new));
        }

        int column = 0;
        final int ITEMS_PER_ROW = 4;

        for (Item item : items) {
            String key = generateTextureKey(item);
            Texture texture = textureManager.getTexture(key);

            Table itemSlot = new Table(skin);
            itemSlot.setBackground("default-round");

            if (texture != null) {
                itemSlot.add(new Image(texture)).size(40, 40);
        } else {
                itemSlot.add(new Label("?", skin)).size(40, 40);
                Gdx.app.log("Inventory", "Missing texture for item: '" + item.getName() + "' (tried key: '" + key + "')");
            }

            itemSlot.row();
            itemSlot.add(new Label(String.valueOf(item.getNumber()), skin));

            if (showOnlyPlantables) {
                itemSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Result result = controller.plantItem(item, plantingTargetTile);
                if (result.isSuccessful()) {
                    startActionAnimation(PlayerActionState.PLANTING);
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null && plantingTargetTile != null) {
                        java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                        data.put("senderUsername", game.getCurrentPlayer().getUsername());
                        if (item instanceof com.example.main.models.item.Seed) {
                            String seedEnumName = ((com.example.main.models.item.Seed) item).getForagingSeedType().name();
                            data.put("seedEnumName", seedEnumName);
                        } else {
                            data.put("seedEnumName", null);
                        }
                        data.put("x", plantingTargetTile.getX());
                        data.put("y", plantingTargetTile.getY());
                        ns.sendPlayerAction("plant_item", data);
                    }
                }
                isPlantingMode = false;
                plantingTargetTile = null;
                isInventoryOpen = false;
                plantingPromptLabel.setVisible(false);
                Gdx.input.setInputProcessor(multiplexer);


                generalMessageLabel.setText(result.Message());
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
                }});
            }
            else if (isTrashModeActive) {
                itemSlot.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        showTrashConfirmationDialog(item);
                    }
                });
            }

            table.add(itemSlot).pad(8);

            column++;
            if (column >= ITEMS_PER_ROW) {
                table.row();
                column = 0;
            }
        }
    }

    private String generateTextureKey(Item item) {
        if (item == null || item.getName() == null) {
            return "Unknown";
        }


        return item.getItemType().getEnumName();
    }

    private void setupToolMenuUI() {
        toolMenuStage = new Stage(new ScreenViewport());

        Table toolTable = new Table();
        toolTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        toolMenuStage.addActor(toolTable);

        currentToolImage = new Image();
        currentToolLabel = new Label("", skin);

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton acceptButton = new TextButton("Equip", skin);
        TextButton unequipButton = new TextButton("Unequip", skin);

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerTools.isEmpty()) {
                    currentToolIndex = (currentToolIndex - 1 + playerTools.size()) % playerTools.size();
                    updateToolMenuDisplay();
                }
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerTools.isEmpty()) {
                    currentToolIndex = (currentToolIndex + 1) % playerTools.size();
                    updateToolMenuDisplay();
                }
            }
        });

        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerTools.isEmpty()) {
                    Tool selectedTool = playerTools.get(currentToolIndex);
                    game.getCurrentPlayer().setCurrentTool(selectedTool);
                    isToolMenuOpen = false;
                }
            }
        });


        unequipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getCurrentPlayer().setCurrentTool(null);
                isToolMenuOpen = false;
            }
        });


        toolTable.add(leftButton).pad(5);
        toolTable.add(currentToolImage).size(64, 64).pad(5);
        toolTable.add(rightButton).pad(5);
        toolTable.row();
        toolTable.add(currentToolLabel).colspan(3).pad(5);
        toolTable.row();


        HorizontalGroup buttonGroup = new HorizontalGroup();
        buttonGroup.space(10);
        buttonGroup.addActor(acceptButton);
        buttonGroup.addActor(unequipButton);

        toolTable.add(buttonGroup).colspan(3).pad(5);

        toolTable.pack();
        toolTable.setPosition(10, 10);
    }

    private void updateToolMenuDisplay() {
        if (playerTools.isEmpty()) {
            currentToolImage.setDrawable(null);
            currentToolLabel.setText("No Tools");
            return;
        }

        Tool tool = playerTools.get(currentToolIndex);
        Texture texture = textureManager.getTexture(generateTextureKey(tool));

        if (texture != null) {
            currentToolImage.setDrawable(new Image(texture).getDrawable());
                } else {

            currentToolImage.setDrawable(skin.getDrawable("default-round"));
        }
        currentToolLabel.setText(tool.getName());
    }

    private void renderCheatMenu(float delta) {
        if (!isCheatMenuOpen) return;
        cheatMenuStage.act(delta);
        cheatMenuStage.draw();
    }

    private void setupCheatMenuUI() {
        cheatMenuStage = new Stage(new ScreenViewport());
        Table cheatTable = new Table(skin);
        cheatTable.setBackground(skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.8f)));
        cheatMenuStage.addActor(cheatTable);

        cheatItemNameField = new TextField("", skin);
        cheatItemNameField.setMessageText("Item Name");
        cheatItemQuantityField = new TextField("", skin);
        cheatItemQuantityField.setMessageText("Quantity");

        TextButton addButton = new TextButton("Add Item", skin);
        TextButton closeButton = new TextButton("Close", skin);
        Label cheatMessageLabel = new Label("", skin);

        addButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                String itemName = cheatItemNameField.getText();
                String quantityStr = cheatItemQuantityField.getText();
                Result result = controller.cheatAddItem(itemName, quantityStr);
                cheatMessageLabel.setText(result.Message());
                if (result.isSuccessful()) {

                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        try {
                            int qty = Integer.parseInt(quantityStr);
                            java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                            actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                            actionData.put("itemName", itemName);
                            actionData.put("quantity", qty);
                            ns.sendPlayerAction("cheat_add_item", actionData);
                        } catch (NumberFormatException ignored) {

                        }
                    }
                }
            }
        });


        closeButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                isCheatMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        cheatTable.add(new Label("Cheat Menu", skin)).colspan(2).pad(10).row();
        cheatTable.add(cheatItemNameField).width(200).pad(5);
        cheatTable.add(cheatItemQuantityField).width(100).pad(5).row();


        Table buttonTable = new Table();
        buttonTable.add(addButton).pad(5);
        buttonTable.add(closeButton).pad(5);
        cheatTable.add(buttonTable).colspan(2).pad(10).row();

        cheatTable.add(cheatMessageLabel).colspan(2).pad(10);
        cheatTable.pack();
        cheatTable.setPosition(Gdx.graphics.getWidth() / 2f - cheatTable.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - cheatTable.getHeight() / 2f);
    }

    private void setupPlantingUI() {
        plantingStage = new Stage(new ScreenViewport());
        Table mainTable = new Table(skin);
        mainTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.7f)));
        mainTable.setPosition(Gdx.graphics.getWidth() / 2f, 80, com.badlogic.gdx.utils.Align.center);

        plantingItemNameLabel = new Label("", skin);
        plantingItemsGroup = new HorizontalGroup();
        plantingItemsGroup.space(10);
        plantingSelectionHighlight = new Image(skin.newDrawable("white", Color.RED));
        plantingSelectionHighlight.setSize(52, 52);

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton selectButton = new TextButton("Plant", skin);
        TextButton closeButton = new TextButton("Close", skin);

        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (!plantableItems.isEmpty()) {
                    selectedPlantableIndex = (selectedPlantableIndex - 1 + plantableItems.size()) % plantableItems.size();
                    updatePlantingSelection();
                }
            }
        });
        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (!plantableItems.isEmpty()) {
                    selectedPlantableIndex = (selectedPlantableIndex + 1) % plantableItems.size();
                    updatePlantingSelection();
                }
            }
        });
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (!plantableItems.isEmpty() && plantingTargetTile != null) {
                    Item selectedItem = plantableItems.get(selectedPlantableIndex);
                    Result result = controller.plantItem(selectedItem, plantingTargetTile);
                    if (result.isSuccessful()) {
                        startActionAnimation(PlayerActionState.PLANTING);
                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                        if (ns != null && plantingTargetTile != null) {
                            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                            data.put("senderUsername", game.getCurrentPlayer().getUsername());
                            if (selectedItem instanceof com.example.main.models.item.Seed) {
                                String seedEnumName = ((com.example.main.models.item.Seed) selectedItem).getForagingSeedType().name();
                                data.put("seedEnumName", seedEnumName);
                            } else {
                                data.put("seedEnumName", null);
                            }
                            data.put("x", plantingTargetTile.getX());
                            data.put("y", plantingTargetTile.getY());
                            ns.sendPlayerAction("plant_item", data);
                        }
                    }
                    closePlantingMenu();
                }
            }
        });
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                closePlantingMenu();
            }
        });

        mainTable.add(plantingItemNameLabel).colspan(4).pad(5).row();
        mainTable.add(leftButton).pad(10);
        mainTable.add(plantingItemsGroup).pad(10);
        mainTable.add(rightButton).pad(10).row();
        mainTable.add(selectButton).colspan(2).pad(5);
        mainTable.add(closeButton).colspan(2).pad(5);
        plantingStage.addActor(mainTable);
    }

    private void updatePlantingSelection() {
        plantingItemsGroup.clear();
        if (plantableItems.isEmpty()) {
            plantingItemNameLabel.setText("No seeds to plant!");
            return;
        }
        Item selectedItem = plantableItems.get(selectedPlantableIndex);
        plantingItemNameLabel.setText(selectedItem.getName());
        Stack itemStack = new Stack();
        itemStack.add(plantingSelectionHighlight);
        Texture itemTexture = textureManager.getTexture(generateTextureKey(selectedItem));
        if (itemTexture != null) {
            itemStack.add(new Image(itemTexture));
        }
        plantingItemsGroup.addActor(itemStack);
    }

    private void openPlantingMenu(Tile target) {
        plantableItems.clear();
        plantableItems.addAll(
            game.getCurrentPlayer().getInventory().getItems().stream()
                .filter(item -> item instanceof Seed)
                .collect(Collectors.toCollection(ArrayList::new))
        );
        if (plantableItems.isEmpty()) {
            generalMessageLabel.setText("You have no seeds to plant.");
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
            return;
        }
        isPlantingSelectionOpen = true;
        plantingTargetTile = target;
        selectedPlantableIndex = 0;
        updatePlantingSelection();
        Gdx.input.setInputProcessor(plantingStage);
    }

    private void closePlantingMenu() {
        isPlantingSelectionOpen = false;
        plantingTargetTile = null;
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setupCraftingCheatMenuUI() {
        craftingCheatMenuStage = new Stage(new ScreenViewport());
        Table cheatTable = new Table(skin);
        cheatTable.setBackground(skin.newDrawable("white", new Color(0.1f, 0.1f, 0.2f, 0.8f)));
        craftingCheatMenuStage.addActor(cheatTable);

        cheatCraftingRecipeField = new TextField("", skin);
        cheatCraftingRecipeField.setMessageText("Crafting Recipe Name");

        TextButton addButton = new TextButton("Add Recipe", skin);
        TextButton closeButton = new TextButton("Close", skin);
        Label messageLabel = new Label("", skin);

        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.cheatAddCraftingRecipe(cheatCraftingRecipeField.getText());
                messageLabel.setText(result.Message());
                if (result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                        actionData.put("recipeName", cheatCraftingRecipeField.getText());
                        ns.sendPlayerAction("cheat_add_crafting_recipe", actionData);
                    }
                }
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isCraftingCheatMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        cheatTable.add(new Label("Cheat: Add Crafting Recipe", skin)).colspan(2).pad(10).row();
        cheatTable.add(cheatCraftingRecipeField).width(250).pad(5).colspan(2).row();
        Table buttonTable = new Table();
        buttonTable.add(addButton).pad(5);
        buttonTable.add(closeButton).pad(5);
        cheatTable.add(buttonTable).colspan(2).pad(10).row();
        cheatTable.add(messageLabel).colspan(2).pad(10);
        cheatTable.pack();
        cheatTable.setPosition(Gdx.graphics.getWidth() / 2f - cheatTable.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - cheatTable.getHeight() / 2f);
    }

    private void setupCookingCheatMenuUI() {
        cookingCheatMenuStage = new Stage(new ScreenViewport());
        Table cheatTable = new Table(skin);
        cheatTable.setBackground(skin.newDrawable("white", new Color(0.2f, 0.1f, 0.1f, 0.8f)));
        cookingCheatMenuStage.addActor(cheatTable);

        cheatCookingRecipeField = new TextField("", skin);
        cheatCookingRecipeField.setMessageText("Cooking Recipe Name");

        TextButton addButton = new TextButton("Add Recipe", skin);
        TextButton closeButton = new TextButton("Close", skin);
        Label messageLabel = new Label("", skin);

        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.cheatAddCookingRecipe(cheatCookingRecipeField.getText());
                messageLabel.setText(result.Message());
                if (result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                        actionData.put("recipeName", cheatCookingRecipeField.getText());
                        ns.sendPlayerAction("cheat_add_cooking_recipe", actionData);
                    }
                }
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isCookingCheatMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        cheatTable.add(new Label("Cheat: Add Cooking Recipe", skin)).colspan(2).pad(10).row();
        cheatTable.add(cheatCookingRecipeField).width(250).pad(5).colspan(2).row();
        Table buttonTable = new Table();
        buttonTable.add(addButton).pad(5);
        buttonTable.add(closeButton).pad(5);
        cheatTable.add(buttonTable).colspan(2).pad(10).row();
        cheatTable.add(messageLabel).colspan(2).pad(10);
        cheatTable.pack();
        cheatTable.setPosition(Gdx.graphics.getWidth() / 2f - cheatTable.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - cheatTable.getHeight() / 2f);
    }

    private void renderCraftingCheatMenu(float delta) {
        if (!isCraftingCheatMenuOpen) return;
        craftingCheatMenuStage.act(delta);
        craftingCheatMenuStage.draw();
    }

    private void renderCookingCheatMenu(float delta) {
        if (!isCookingCheatMenuOpen) return;
        cookingCheatMenuStage.act(delta);
        cookingCheatMenuStage.draw();
    }

    private void setupEatMenuUI() {
        Table eatMenuTable = new Table(skin);
        eatMenuTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.7f)));
        eatMenuTable.pad(10);

        selectedFoodImage = new Image();
        selectedFoodLabel = new Label("No food available", skin);

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton eatButton = new TextButton("Eat", skin);
        TextButton closeButton = new TextButton("Close", skin);

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerFoodItems.isEmpty()) {
                    selectedFoodIndex = (selectedFoodIndex - 1 + playerFoodItems.size()) % playerFoodItems.size();
                    updateEatMenuDisplay();
                }
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerFoodItems.isEmpty()) {
                    selectedFoodIndex = (selectedFoodIndex + 1) % playerFoodItems.size();
                    updateEatMenuDisplay();
                }
            }
        });

        eatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerFoodItems.isEmpty()) {
                    Food foodToEat = playerFoodItems.get(selectedFoodIndex);
                    Result result = controller.eat(foodToEat.getName());


                    eatingAnimationTimer = EATING_ANIMATION_DURATION;
                    generalMessageLabel.setText(result.Message());
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;


                    if (result.isSuccessful()) {
                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                        if (ns != null) {
                            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                            data.put("senderUsername", game.getCurrentPlayer().getUsername());
                            data.put("foodName", foodToEat.getName());
                            ns.sendPlayerAction("eat", data);
                        }
                    }


                    isEatMenuOpen = false;
                    Gdx.input.setInputProcessor(multiplexer);
                }
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isEatMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        Table selectorTable = new Table();
        selectorTable.add(leftButton).pad(10);
        selectorTable.add(selectedFoodImage).size(64, 64).pad(10);
        selectorTable.add(rightButton).pad(10);

        eatMenuTable.add(selectedFoodLabel).colspan(2).pad(10).row();
        eatMenuTable.add(selectorTable).colspan(2).row();
        eatMenuTable.add(eatButton).pad(10);
        eatMenuTable.add(closeButton).pad(10);

        eatMenuTable.pack();
        eatMenuTable.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - eatMenuTable.getHeight() - 20, com.badlogic.gdx.utils.Align.center);
        eatMenuStage.addActor(eatMenuTable);
    }

    private void showEatMenu() {
        playerFoodItems.clear();
        game.getCurrentPlayer().getInventory().getItems().stream()
            .filter(item -> item instanceof Food)
            .forEach(item -> playerFoodItems.add((Food) item));

        selectedFoodIndex = 0;
        updateEatMenuDisplay();
    }

    private void updateEatMenuDisplay() {
        if (playerFoodItems.isEmpty()) {
            selectedFoodLabel.setText("No food in inventory");
            selectedFoodImage.setDrawable(null);
            return;
        }

        Food selectedFood = playerFoodItems.get(selectedFoodIndex);
        selectedFoodLabel.setText(selectedFood.getName() + " (+" + selectedFood.getFoodType().getEnergy() + " Energy)");

        Texture foodTexture = textureManager.getTexture(selectedFood.getItemType().getEnumName());
        if (foodTexture != null) {
            selectedFoodImage.setDrawable(new TextureRegionDrawable(new TextureRegion(foodTexture)));
        } else {
            selectedFoodImage.setDrawable(skin.getDrawable("default-round"));
        }
    }

    private void renderEatMenu(float delta) {
        if (!isEatMenuOpen) return;
        eatMenuStage.act(delta);
        eatMenuStage.draw();
    }

    private void renderBuffs() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null || currentPlayer.getActiveBuffs().isEmpty()) {
            return;
        }

        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        float buffIconX = Gdx.graphics.getWidth() - 60;
        float buffIconY = Gdx.graphics.getHeight() - 250;

        for (ActiveBuff buff : currentPlayer.getActiveBuffs()) {
            Texture buffTexture = null;
            if (buff.getType() == ActiveBuff.BuffType.MAX_ENERGY) {
                buffTexture = maxEnergyBuffTexture;
            } else if (buff.getType() == ActiveBuff.BuffType.SKILL) {
                switch (buff.getSkill()) {
                    case Mining: buffTexture = miningBuffTexture; break;
                    case Farming: buffTexture = farmingBuffTexture; break;
                    case Foraging: buffTexture = foragingBuffTexture; break;
                    case Fishing: buffTexture = fishingBuffTexture; break;
                }
            }

            if (buffTexture != null) {
                spriteBatch.draw(buffTexture, buffIconX - 5, buffIconY + 10, 32, 32);
                buffIconY -= 40;
            }
        }
        spriteBatch.end();
    }

    private void handleMachinePlacement() {
        if (!isMachinePlacementMode) return;


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (worldCoords.x / TILE_SIZE);
            int tileY = MAP_HEIGHT - 1 - (int) (worldCoords.y / TILE_SIZE);

            if (game.getMap() == null) {
                generalMessageLabel.setText("Map is still loading. Please wait.");
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
                return;
            }

            Result result = controller.placeMachine(machineToPlace, tileX, tileY);
            generalMessageLabel.setText(result.Message());
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;

            if (result.isSuccessful()) {

                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null && machineToPlace != null && machineToPlace.getItemType() != null) {
                    java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                    data.put("senderUsername", game.getCurrentPlayer().getUsername());
                    data.put("machineEnumName", machineToPlace.getItemType().getEnumName());
                    data.put("tileX", tileX);
                    data.put("tileY", tileY);
                    ns.sendPlayerAction("place_machine", data);
                }
                isMachinePlacementMode = false;
                machineToPlace = null;
                machinePlacementTexture = null;
            }
        }


        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            isMachinePlacementMode = false;
            machineToPlace = null;
            machinePlacementTexture = null;
        }
    }

    private void handleMachineInteraction() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (worldCoords.x / TILE_SIZE);
            int tileY = MAP_HEIGHT - 1 - (int) (worldCoords.y / TILE_SIZE);

            if (gameMap.inBounds(tileX, tileY)) {
                Tile targetTile = gameMap.getTile(tileX, tileY);
                if (targetTile.getPlacedMachine() != null) {
                    activeMachine = targetTile.getPlacedMachine();
                    activeMachineTileX = tileX;
                    activeMachineTileY = tileY;
                    showMachineUI();
                    Gdx.input.setInputProcessor(machineUiStage);
                }
            }
        }
    }

    private void showMachineUI() {
        machineUiStage.clear();
        Dialog dialog = new Dialog(activeMachine.getMachineType().getName(), skin, "dialog");


        Table content = dialog.getContentTable();
        Image inputImage = new Image();
        Image outputImage = new Image();
        ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
        Table recipeTable = new Table();
        ScrollPane recipePane = new ScrollPane(recipeTable, skin);


        content.add(new Label("Input:", skin));
        content.add(inputImage).size(48, 48).pad(10);
        content.add(new Label("Output:", skin));
        content.add(outputImage).size(48, 48).pad(10).row();
        content.add(progressBar).colspan(4).fillX().pad(5).row();
        content.add(recipePane).colspan(4).height(150).fill().pad(5);


        if (activeMachine.isIdle()) {
            for (ArtisanProductType recipe : ArtisanProductType.values()) {
                if (recipe.getMachine() == activeMachine.getMachineType()) {
                    TextButton recipeButton = new TextButton(recipe.getName(), skin);
                    recipeTable.add(recipeButton).fillX().pad(2).row();

                    recipeButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Result result = controller.startArtisanProcess(activeMachine, recipe);
                            generalMessageLabel.setText(result.Message());
                            generalMessageLabel.setVisible(true);
                            generalMessageTimer = GENERAL_MESSAGE_DURATION;
                            showMachineUI();
                        }
                    });
                }
            }
        }


        if (activeMachine.isIdle()) {
            dialog.getButtonTable().add(new Label("Select a recipe to start.", skin));
        } else if (activeMachine.isProcessing()) {
            inputImage.setDrawable(new TextureRegionDrawable(textureManager.getTexture(activeMachine.getInput().getItemType().getEnumName())));
            float progress = (float)activeMachine.getProgress() / activeMachine.getCurrentRecipe().getProcessingTime() * 100f;
            progressBar.setValue(progress);

            TextButton finishNow = new TextButton("Finish Now (-100 Energy)", skin);
            finishNow.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result result = controller.finishArtisanProcessNow(activeMachine);
                    generalMessageLabel.setText(result.Message());
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                    showMachineUI();
                }
            });
            dialog.button(finishNow);

            TextButton cancel = new TextButton("Cancel", skin);
            cancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result result = controller.cancelArtisanProcess(activeMachine);
                    generalMessageLabel.setText(result.Message());
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                    showMachineUI();
                }
            });
            dialog.button(cancel);

        } else if (activeMachine.isDone()) {
            outputImage.setDrawable(new TextureRegionDrawable(textureManager.getTexture(activeMachine.getOutput().getItemType().getEnumName())));
            progressBar.setValue(100);

            TextButton collect = new TextButton("Collect", skin);
            collect.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result result = controller.collectArtisanProduct(activeMachine);
                    generalMessageLabel.setText(result.Message());
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                    showMachineUI();
                }
            });
            dialog.button(collect);
        }

        dialog.button("Close");
        dialog.show(machineUiStage);
    }

    private void renderMachinePlacement(SpriteBatch batch) {
        if (!isMachinePlacementMode || machinePlacementTexture == null) return;

        Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        int tileX = (int) (worldCoords.x / TILE_SIZE);
        int tileY = MAP_HEIGHT - 1 - (int) (worldCoords.y / TILE_SIZE);

        float renderX = tileX * TILE_SIZE;
        float renderY = (MAP_HEIGHT - 1 - tileY) * TILE_SIZE;

        batch.setColor(1, 1, 1, 0.6f);
        batch.draw(machinePlacementTexture, renderX, renderY, TILE_SIZE, TILE_SIZE);
        batch.setColor(1, 1, 1, 1);
    }

    private void startFishingMinigame() {
        if (controller.getActiveMinigame() == null) return;
        isFishingMinigameActive = true;
        Gdx.input.setInputProcessor(fishingStage);
    }

    private void endFishingMinigame(boolean success) {
        isFishingMinigameActive = false;
        Gdx.input.setInputProcessor(multiplexer);

        FishingMinigame minigame = controller.getActiveMinigame();
        if (minigame == null) return;

        if (success) {
            Fish caughtFish = new Fish(minigame.getFish(), 1);

            if (minigame.isPerfect()) {
                generalMessageLabel.setText("Perfect Catch! " + caughtFish.getName());

            } else {
                generalMessageLabel.setText("You caught a " + caughtFish.getName() + "!");
            }
            game.getCurrentPlayer().getInventory().addItem(caughtFish);
            game.getCurrentPlayer().catchFish();
        } else {
            generalMessageLabel.setText("The fish got away...");
        }

        generalMessageLabel.setVisible(true);
        generalMessageTimer = GENERAL_MESSAGE_DURATION;
    }

    private void updateFishingMinigame(float delta) {
        if (!isFishingMinigameActive) return;

        FishingMinigame minigame = controller.getActiveMinigame();
        if (minigame == null) {
            endFishingMinigame(false);
            return;
        }


        boolean holdingUp = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean holdingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            endFishingMinigame(false);
            return;
        }

        minigame.update(delta, holdingUp, holdingDown);


        if (minigame.getCaptureProgress() >= 1.0f) {
            endFishingMinigame(true);
        } else if (minigame.getCaptureProgress() <= 0f) {
            endFishingMinigame(false);
        }
    }

    private void renderFishingMinigame() {
        if (!isFishingMinigameActive) return;

        FishingMinigame minigame = controller.getActiveMinigame();
        if (minigame == null) return;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();


        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();


        float barWidth = 64;
        float barHeight = screenHeight * 0.7f;
        float barX = (screenWidth - barWidth) / 2f;
        float barY = (screenHeight - barHeight) / 2f;
        spriteBatch.draw(fishingBarBg, barX, barY, barWidth, barHeight);


        float playerBarHeight = barHeight * minigame.getPlayerBarSize();
        float playerBarY = barY + (barHeight - playerBarHeight) * minigame.getPlayerBarPosition();
        spriteBatch.draw(playerBarTexture, barX, playerBarY, barWidth, playerBarHeight);


        float fishY = barY + (barHeight - fishIconTexture.getHeight()) * minigame.getFishPosition();
        spriteBatch.draw(fishIconTexture, barX + (barWidth - fishIconTexture.getWidth()) / 2f, fishY);


        if (minigame.getFish().getType().equals("Legendary")) {
            spriteBatch.draw(legendaryCrownTexture, barX + (barWidth - legendaryCrownTexture.getWidth()) / 2f, fishY + fishIconTexture.getHeight() - 10);
        }


        float progressX = barX + barWidth + 20;
        float progressY = barY;
        spriteBatch.draw(progressBarBg, progressX, progressY, progressBarBg.getWidth(), barHeight);
        spriteBatch.draw(progressBarFill, progressX, progressY, progressBarBg.getWidth(), barHeight * minigame.getCaptureProgress());

        spriteBatch.end();
    }

    private void setupInfoMenuUI() {}

    private void setupReactionMenu() {
        Table reactionMenuContainer = new Table();
        reactionMenuContainer.setFillParent(true);
        reactionMenuContainer.bottom().right();
        stage.addActor(reactionMenuContainer);

        Table emojiTable = new Table();

        emojiScrollPane = new ScrollPane(emojiTable, skin);
        emojiScrollPane.setFadeScrollBars(false);
        emojiScrollPane.setVisible(false);

        emojiImages.clear();
        String[] emojiNames = {
            "+1", "face_with_raised_eyebrow", "face_with_symbols_on_mouth", "farm_dialog",
            "Good_Morning_dialog", "grin", "heart_eyes", "heart", "Hi_dialog", "joy",
            "neutral_face", "nice_dialog", "relieved", "sunglasses"
        };

        for (String name : emojiNames) {
            Texture emojiTexture = textureManager.getTexture(name);
            if (emojiTexture != null) {
                Image image = new Image(emojiTexture);


                emojiImages.add(image);
                emojiTable.add(image).size(image.getPrefWidth() * 0.5f, image.getPrefHeight() * 0.5f).pad(5);
            }
        }


        leftArrowButton = new TextButton("<", skin);
        leftArrowButton.setVisible(false);
        leftArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedEmojiIndex--;
                if (selectedEmojiIndex < 0) {
                    selectedEmojiIndex = emojiImages.size() - 1;
                }
                updateEmojiHighlight();
            }
        });

        rightArrowButton = new TextButton(">", skin);
        rightArrowButton.setVisible(false);
        rightArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedEmojiIndex++;
                if (selectedEmojiIndex >= emojiImages.size()) {
                    selectedEmojiIndex = 0;
                }
                updateEmojiHighlight();
            }
        });

        TextButton reactionToggleButton = new TextButton("React", skin);
        reactionToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showReactionMenu = !showReactionMenu;
                leftArrowButton.setVisible(showReactionMenu);
                emojiScrollPane.setVisible(showReactionMenu);
                rightArrowButton.setVisible(showReactionMenu);
            }
        });

        reactionMenuContainer.add(leftArrowButton).pad(5);
        reactionMenuContainer.add(emojiScrollPane).size(400, 100);
        reactionMenuContainer.add(rightArrowButton).pad(5);
        reactionMenuContainer.add(reactionToggleButton).pad(15);

        updateEmojiHighlight();
    }


    public void applyRemoteReaction(String senderUsername, String emojiName, float duration) {
        if (textureManager == null || game == null) return;
        Texture tex = textureManager.getTexture(emojiName);
        if (tex == null) return;
        ReactionState rs = new ReactionState();
        rs.texture = tex;
        rs.remainingSeconds = duration > 0 ? duration : EMOJI_DISPLAY_DURATION;
        usernameToReaction.put(senderUsername, rs);
    }

    private void showInfoMenu() {
        menuContentTable.clear();
        isInfoMenuOpen = true;

        Stack infoStack = new Stack();
        infoStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();
        contentTable.pad(20);

        TextField searchField = new TextField("", skin);
        searchField.setMessageText("Enter plant or tree name...");

        TextButton searchButton = new TextButton("Get Info", skin);


        Image itemImage = new Image();


        Label infoLabel = new Label("Enter a name and click 'Get Info'.", skin);
        infoLabel.setWrap(true);
        infoLabel.setAlignment(Align.center);

        ScrollPane scrollPane = new ScrollPane(infoLabel, skin);
        scrollPane.setFadeScrollBars(false);

        searchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String plantName = searchField.getText().trim();
                if (plantName.isEmpty()) {
                    infoLabel.setText("Please enter a name.");
                    itemImage.setDrawable(null);
                    return;
                }


                Result result = controller.craftInfo(plantName);
                String textureKey = null;

                if (result.isSuccessful()) {

                    for (CropType type : CropType.values()) {
                        if (type.getName().equalsIgnoreCase(plantName)) {
                            textureKey = type.getEnumName();
                            break;
                        }
                    }
                } else {

                    result = controller.treeInfo(plantName);
                    if (result.isSuccessful()) {

                        for (TreeType type : TreeType.values()) {
                            if (type.getName().equalsIgnoreCase(plantName)) {
                                textureKey = type.getEnumName();
                                break;
                            }
                        }
                    }
                }


                if (result.isSuccessful()) {
                    infoLabel.setText(result.Message());
                } else {
                    infoLabel.setText("Could not find information for '" + plantName + "'.");
                }


                if (textureKey != null) {
                    Texture itemTexture = textureManager.getTexture(textureKey);
                    if (itemTexture != null) {
                        itemImage.setDrawable(new TextureRegionDrawable(new TextureRegion(itemTexture)));
                    } else {
                        itemImage.setDrawable(null);
                    }
                } else {
                    itemImage.setDrawable(null);
                }
            }
        });

        Table searchBar = new Table();
        searchBar.add(searchField).width(300).padRight(10);
        searchBar.add(searchButton);

        contentTable.add(searchBar).padBottom(15).row();
        contentTable.add(itemImage).size(64, 64).padBottom(15).row();
        contentTable.add(scrollPane).expand().fill().padBottom(15).row();

        Table bottomButtonTable = new Table();
        addBackButtonToMenu(bottomButtonTable);
        contentTable.add(bottomButtonTable).bottom().left();

        infoStack.add(contentTable);
        menuContentTable.add(infoStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void renderInfoMenu(float delta) {
        if (!isInfoMenuOpen) return;
    }



    private void showQuestsMenu() {
        menuContentTable.clear();

        Stack questStack = new Stack();
        questStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();
        contentTable.pad(20);

        contentTable.add(new Label("Active Quests", skin)).padBottom(15).row();


        Result result = controller.showQuests();
        String questsText = result.Message();


        if (questsText.trim().isEmpty()) {
            questsText = "No active quests at the moment.";
        }

        Label questsLabel = new Label(questsText, skin);
        questsLabel.setWrap(true);
        questsLabel.setAlignment(Align.topLeft);

        ScrollPane scrollPane = new ScrollPane(questsLabel, skin);
        scrollPane.setFadeScrollBars(false);

        contentTable.add(scrollPane).expand().fill().padBottom(15).row();

        Table bottomButtonTable = new Table();
        addBackButtonToMenu(bottomButtonTable);
        contentTable.add(bottomButtonTable).bottom().left();

        questStack.add(contentTable);
        menuContentTable.add(questStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }



    private void showJournalMenu() {
        menuContentTable.clear();

        Stack journalStack = new Stack();
        journalStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();
        contentTable.pad(20);


        contentTable.add(new Label("Journal", skin)).padBottom(20).row();


        Table infoTable = new Table();
        infoTable.defaults().pad(5).left();
        Date currentDate = game.getDate();
        String dateInfo = "Day " + currentDate.getCurrentDay() + " of " + currentDate.getCurrentSeason().name();
        String weatherInfo = "Weather: " + game.getTodayWeather().name();

        infoTable.add(new Label(dateInfo, skin)).row();
        infoTable.add(new Label(weatherInfo, skin)).row();
        contentTable.add(infoTable).left().padBottom(20).row();


        contentTable.add(new Label("--- Active Quests ---", skin)).padBottom(10).row();
        Result result = controller.showQuests();
        String questsText = result.Message();
        if (questsText.trim().isEmpty()) {
            questsText = "No active quests.";
        }
        Label questsLabel = new Label(questsText, skin);
        questsLabel.setWrap(true);
        questsLabel.setAlignment(Align.topLeft);

        ScrollPane questScrollPane = new ScrollPane(questsLabel, skin);
        questScrollPane.setFadeScrollBars(false);
        contentTable.add(questScrollPane).expand().fillX().height(150).padBottom(20).row();


        contentTable.add(new Label("--- Daily News ---", skin)).padBottom(10).row();
        Label newsLabel = new Label("The news channel is quiet today.", skin);
        newsLabel.setWrap(true);
        newsLabel.setAlignment(Align.topLeft);

        ScrollPane newsScrollPane = new ScrollPane(newsLabel, skin);
        newsScrollPane.setFadeScrollBars(false);
        contentTable.add(newsScrollPane).expand().fillX().height(100).padBottom(20).row();


        Table bottomButtonTable = new Table();
        addBackButtonToMenu(bottomButtonTable);
        contentTable.add(bottomButtonTable).bottom().left().expandY();

        journalStack.add(contentTable);
        menuContentTable.add(journalStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
    }

    private void renderGiantCrops() {
        Tile[][] tiles = gameMap.getTiles();
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                Tile tile = tiles[x][y];

                if (tile != null && tile.isPartOfGiantCrop() && tile.getGiantCropRootX() == x && tile.getGiantCropRootY() == y) {
                    Crop crop = (Crop) tile.getPlant();
                    if (!(crop.getCropType() instanceof CropType)) continue;
                    CropType cropType = (CropType) crop.getCropType();
                    int finalStage = cropType.getStages().size();
                    String textureKey = cropType.getEnumName() + "_Stage_" + finalStage;
                    Texture cropTexture = textureManager.getTexture(textureKey);


                    if (cropTexture != null) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        spriteBatch.draw(cropTexture, worldX, worldY - TILE_SIZE, TILE_SIZE * 2, TILE_SIZE * 2);
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,
            height, true);
        hudCamera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        inventoryStage.dispose();
        inventoryBackground.dispose();
        textureManager.dispose();
        toolMenuStage.dispose();
        cheatMenuStage.dispose();

        clockTexture.dispose();
        hudFont.dispose();

        ground1Texture.dispose();
        ground2Texture.dispose();
        grass1Texture.dispose();
        grass2Texture.dispose();
        shoveledTexture.dispose();
        tree1Texture.dispose();
        tree2Texture.dispose();
        tree3Texture.dispose();
        house1Texture.dispose();
        house2Texture.dispose();
        house3Texture.dispose();
        npcHouse1Texture.dispose();
        npcHouse2Texture.dispose();
        npcHouse3Texture.dispose();
        npcHouse4Texture.dispose();
        npcHouse5Texture.dispose();
        bushTexture.dispose();
        stone1Texture.dispose();
        stone2Texture.dispose();
        copperStoneTexture.dispose();
        ironStoneTexture.dispose();
        goldStoneTexture.dispose();
        iridiumStoneTexture.dispose();
        jewelStoneTexture.dispose();

        blacksmithTexture.dispose();
        jojamartTexture.dispose();
        pierresShopTexture.dispose();
        carpentersShopTexture.dispose();
        fishShopTexture.dispose();
        ranchTexture.dispose();
        saloonTexture.dispose();

        lake1Texture.dispose();
        lake2Texture.dispose();
        lake3Texture.dispose();

        barnTexture.dispose();
        coopTexture.dispose();

        maleIdleTexture.dispose();
        maleDown1Texture.dispose();
        maleDown2Texture.dispose();
        maleUp1Texture.dispose();
        maleUp2Texture.dispose();
        maleLeft1Texture.dispose();
        maleLeft2Texture.dispose();
        maleRight1Texture.dispose();
        maleRight2Texture.dispose();

        femaleIdleTexture.dispose();
        femaleDown1Texture.dispose();
        femaleDown2Texture.dispose();
        femaleUp1Texture.dispose();
        femaleUp2Texture.dispose();
        femaleLeft1Texture.dispose();
        femaleLeft2Texture.dispose();
        femaleRight1Texture.dispose();
        femaleRight2Texture.dispose();


        dialogBoxTexture.dispose();
        menuBackgroundTexture.dispose();


        if (friendsMenuTable != null) {
            friendsMenuTable.remove();
            friendsMenuTable = null;
        }

        textureManager.dispose();
    }

    private static class CrowAnimation {
        float x, y;
        float speedX, speedY;

        public CrowAnimation() {

            if (Math.random() > 0.5) {
                x = -100;
                speedX = (float) (250 + Math.random() * 150);
            } else {
                x = Gdx.graphics.getWidth() + 100;
                speedX = (float) -(250 + Math.random() * 150);
            }

            y = (float) (Math.random() * Gdx.graphics.getHeight());

            speedY = (float) (Math.random() * 100 - 50);
        }

        public void update(float delta) {
            x += speedX * delta;
            y += speedY * delta;
        }

        public boolean isOffScreen() {
            return x < -150 || x > Gdx.graphics.getWidth() + 150;
        }
    }

    private void createShopMenuUI() {
        if (shopMenuTable != null) {
            shopMenuTable.remove();
        }

        shopMenuTable = new Table();

        shopMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        shopMenuTable.setPosition(
            (Gdx.graphics.getWidth() - shopMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - shopMenuTable.getHeight()) / 2f
        );


        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(menuBackgroundTexture);
        shopMenuTable.setBackground(backgroundDrawable);

        stage.addActor(shopMenuTable);


        Gdx.input.setInputProcessor(stage);


        switch (currentShopMenuState) {
            case MAIN_MENU:
                createMainShopMenu();
                break;
            case ALL_PRODUCTS:
                createAllProductsMenu();
                break;
            case AVAILABLE_PRODUCTS:
                createAvailableProductsMenu();
                break;
        }
    }

    private void createMainShopMenu() {
        shopMenuTable.clear();


        Label titleLabel = new Label(currentShopType.getName(), skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();


        TextButton availableProductsButton = new TextButton("Available Products", skin);
        availableProductsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.AVAILABLE_PRODUCTS;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        shopMenuTable.add(availableProductsButton).width(200).pad(10).row();


        TextButton allProductsButton = new TextButton("All Products", skin);
        allProductsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.ALL_PRODUCTS;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        shopMenuTable.add(allProductsButton).width(200).pad(10).row();


        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        shopMenuTable.add(closeButton).width(200).pad(10).row();
    }

    private void createAllProductsMenu() {
        shopMenuTable.clear();

        Label titleLabel = new Label(currentShopType.getName() + " - All Products", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();

        if (!shopResultMessage.isEmpty()) {
            Label resultLabel = new Label(shopResultMessage, skin);
            resultLabel.setColor(shopResultSuccess ? Color.GREEN : Color.RED);
            resultLabel.setFontScale(1.1f);
            shopMenuTable.add(resultLabel).padBottom(15).row();
        }

        Result result = shopController.showAllProducts();
        String productsText = result.isSuccessful() ? result.Message() : "Failed to load products";

        Table productsTable = new Table();
        if (result.isSuccessful() && !productsText.trim().isEmpty()) {
            String[] products = productsText.split("\n");
            for (String product : products) {
                if (product.trim().isEmpty()) continue;


                Table productCard = new Table();
                productCard.setBackground(skin.getDrawable("default-round"));
                productCard.pad(15);


                Label productLabel = new Label(product.trim(), skin);
                productLabel.setColor(Color.WHITE);
                productLabel.setFontScale(1.2f);
                productCard.add(productLabel).colspan(2).padBottom(10).row();

                productsTable.add(productCard).width(500).pad(15).row();
            }
        } else {
            Label noProductsLabel = new Label("No products available", skin);
            noProductsLabel.setFontScale(1.2f);
            noProductsLabel.setColor(Color.GRAY);
            productsTable.add(noProductsLabel).pad(30).row();
        }

        ScrollPane scrollPane = new ScrollPane(productsTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setScrollingDisabled(false, false);

        shopMenuTable.add(scrollPane).width(550).height(400).pad(10).row();

        Table buttonTable = new Table();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.MAIN_MENU;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        buttonTable.add(backButton).width(200).pad(20);
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        buttonTable.add(closeButton).width(200).pad(20);
        shopMenuTable.add(buttonTable).row();
    }

    private void createAvailableProductsMenu() {
        shopMenuTable.clear();

        Label titleLabel = new Label(currentShopType.getName() + " - Available Products", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();

        if (!shopResultMessage.isEmpty()) {
            Label resultLabel = new Label(shopResultMessage, skin);
            resultLabel.setColor(shopResultSuccess ? Color.GREEN : Color.RED);
            resultLabel.setFontScale(1.1f);
            shopMenuTable.add(resultLabel).padBottom(15).row();
        }

        Result result = shopController.showAvailableProducts();
        String productsText = result.isSuccessful() ? result.Message() : "Failed to load products";

        Table productsTable = new Table();
        if (result.isSuccessful() && !productsText.trim().isEmpty()) {
            String[] products = productsText.split("\n");
            for (int i = 0; i < products.length; i += 2) {
                if (i >= products.length) break;
                String product = products[i];
                String stockInfo = (i + 1 < products.length) ? products[i + 1] : "";
                if (product.trim().isEmpty()) continue;


                String productName = extractProductName(product);


                String displayText = product.trim();
                if (!stockInfo.isEmpty()) {
                    displayText += " | " + stockInfo;
                }
                Table productCard = new Table();
                productCard.setBackground(skin.getDrawable("default-round"));
                productCard.pad(15);


                Label productLabel = new Label(displayText, skin);
                productLabel.setColor(Color.WHITE);
                productLabel.setFontScale(1.2f);
                productCard.add(productLabel).colspan(4).padBottom(10).row();

                Label amountLabel = new Label("Amount:", skin);
                amountLabel.setColor(Color.WHITE);
                productCard.add(amountLabel).padRight(5);
                TextField amountField = new TextField("1", skin);
                amountField.setWidth(80);
                amountField.setMaxLength(3);
                amountField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
                productCard.add(amountField).padRight(5);
                TextButton plusButton = new TextButton("+", skin);
                plusButton.setWidth(30);
                plusButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        try {
                            int currentAmount = Integer.parseInt(amountField.getText());
                            amountField.setText(String.valueOf(currentAmount + 1));
                        } catch (NumberFormatException e) {
                            amountField.setText("1");
                        }
                    }
                });
                productCard.add(plusButton).padRight(10);
                TextButton purchaseButton = new TextButton("Purchase", skin);
                purchaseButton.setWidth(100);
                purchaseButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        try {
                            int amount = Integer.parseInt(amountField.getText());
                            if (amount > 0) {

                                if (currentShopType == ShopType.CarpentersShop &&
                                    (productName.contains("Barn") || productName.contains("Coop"))) {

                                    isBuildingPlacementMode = true;
                                    buildingToPlace = productName;


                                    if (productName.contains("Barn")) {
                                        buildingPlacementTexture = barnTexture;
                                    } else if (productName.contains("Coop")) {
                                        buildingPlacementTexture = coopTexture;
                                    }


                                    showShopMenu = false;
                                    if (shopMenuTable != null) {
                                        shopMenuTable.remove();
                                        shopMenuTable = null;
                                    }


                                    generalMessageLabel.setText("Click where you want to place the " + productName + ". Right-click to cancel.");
                                    generalMessageLabel.setColor(Color.CYAN);
                                    generalMessageLabel.setVisible(true);
                                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                                } else if (currentShopType == ShopType.MarniesRanch) {

                                    boolean isAnimal = false;
                                    for (com.example.main.enums.design.Shop.MarniesRanch entry : com.example.main.enums.design.Shop.MarniesRanch.values()) {
                                        if (entry.getDisplayName().equals(productName) && entry.getBuildingRequired() != null) {
                                            isAnimal = true;
                                            break;
                                        }
                                    }
                                    if (isAnimal) {
                                        showAnimalPurchasePage(productName);
                                        return;
                                    }

                                    Result purchaseResult = shopController.purchase(productName, String.valueOf(amount));
                                    shopResultMessage = purchaseResult.Message();
                                    shopResultSuccess = purchaseResult.isSuccessful();
                                    if (shopResultSuccess) {
                                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                        if (ns != null) {
                                            java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                            actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                            actionData.put("name", productName);
                                            actionData.put("amount", amount);
                                            actionData.put("shopX", game.getCurrentPlayer().currentX());
                                            actionData.put("shopY", game.getCurrentPlayer().currentY());
                                            ns.sendPlayerAction("purchase", actionData);
                                        }
                                    }
                                    createShopMenuUI();
                                } else {

                                    Result purchaseResult = shopController.purchase(productName, String.valueOf(amount));
                                    shopResultMessage = purchaseResult.Message();
                                    shopResultSuccess = purchaseResult.isSuccessful();
                                    if (shopResultSuccess) {
                                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                        if (ns != null) {
                                            java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                            actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                            actionData.put("name", productName);
                                            actionData.put("amount", amount);
                                            actionData.put("shopX", game.getCurrentPlayer().currentX());
                                            actionData.put("shopY", game.getCurrentPlayer().currentY());
                                            ns.sendPlayerAction("purchase", actionData);
                                        }
                                    }
                                    createShopMenuUI();
                                }
                            }
                        } catch (NumberFormatException e) {
                            shopResultMessage = "Invalid amount!";
                            shopResultSuccess = false;
                            createShopMenuUI();
                        }
                    }
                });
                productCard.add(purchaseButton).row();
                productsTable.add(productCard).width(500).pad(15).row();
            }
        } else {
            Label noProductsLabel = new Label("No products available", skin);
            noProductsLabel.setFontScale(1.2f);
            noProductsLabel.setColor(Color.GRAY);
            productsTable.add(noProductsLabel).pad(30).row();
        }

        ScrollPane scrollPane = new ScrollPane(productsTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setScrollingDisabled(false, false);

        shopMenuTable.add(scrollPane).width(550).height(400).pad(10).row();

        Table buttonTable = new Table();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.MAIN_MENU;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        buttonTable.add(backButton).width(200).pad(20);
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }

                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        buttonTable.add(closeButton).width(200).pad(20);
        shopMenuTable.add(buttonTable).row();
    }

    private String extractProductName(String productText) {


        if (productText.contains(" - ")) {
            return productText.split(" - ")[0].trim();
        }
        return productText.trim();
    }

    private void showAnimalPurchasePage(String animalKey) {
        if (shopResultSuccess) {

        } else {
            shopResultMessage = "";
        }
        shopResultSuccess = false;
        shopMenuTable.clear();
        Label titleLabel = new Label("Buy " + animalKey, skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();


        Label nameLabel = new Label("Animal Name:", skin);
        nameLabel.setColor(Color.WHITE);
        final TextField nameField = new TextField("", skin);
        nameField.setMessageText("Enter animal name");
        shopMenuTable.add(nameLabel).padRight(10);
        shopMenuTable.add(nameField).width(200).padBottom(10).row();


        Label housingLabel = new Label("Select Housing:", skin);
        housingLabel.setColor(Color.WHITE);
        Player player = game.getCurrentPlayer();
        if (player == null || player.getHousings() == null) return;
        java.util.List<Housing> housings = player.getHousings();
        java.util.List<Housing> validHousings = new java.util.ArrayList<>();

        String requiredBuilding = null;
        int animalCapacity = 1;
        for (com.example.main.enums.design.Shop.MarniesRanch entry : com.example.main.enums.design.Shop.MarniesRanch.values()) {
            if (entry.getDisplayName().equals(animalKey)) {
                requiredBuilding = entry.getBuildingRequired();
                animalCapacity = 1;
                break;
            }
        }
        for (Housing h : housings) {
            boolean typeOk = false;
            if (requiredBuilding != null) {
                String housingTypeName = h.getType().getName().toLowerCase();
                String requiredBuildingLower = requiredBuilding.toLowerCase();


                if (requiredBuildingLower.contains("barn")) {
                    typeOk = housingTypeName.contains("barn");
                } else if (requiredBuildingLower.contains("coop")) {
                    typeOk = housingTypeName.contains("coop");
                }
            }
            boolean hasSpace = h.getOccupants().size() < h.getCapacity();
            if (typeOk && hasSpace) {
                validHousings.add(h);
            }
        }
        if (validHousings.isEmpty()) {
            Label noHousingLabel = new Label("No available housing for this animal. Build or free up space in a barn/coop.", skin);
            noHousingLabel.setColor(Color.RED);
            shopMenuTable.add(noHousingLabel).colspan(2).padBottom(20).row();
            TextButton backButton = new TextButton("Back", skin);
            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    createShopMenuUI();
                }
            });
            shopMenuTable.add(backButton).width(120).pad(10).row();
            if (!shopResultMessage.isEmpty()) {
                Label resultLabel = new Label(shopResultMessage, skin);
                resultLabel.setColor(Color.GREEN);
                resultLabel.setFontScale(1.1f);
                shopMenuTable.add(resultLabel).colspan(2).padBottom(15).row();
            }
            return;
        }
        final com.badlogic.gdx.scenes.scene2d.ui.SelectBox<String> housingSelect = new com.badlogic.gdx.scenes.scene2d.ui.SelectBox<>(skin);
        java.util.List<String> housingOptions = new java.util.ArrayList<>();
        for (Housing h : validHousings) {
            housingOptions.add(h.getType().getName() + " (ID: " + h.getId() + ") - " + h.getOccupants().size() + "/" + h.getCapacity());
        }
        housingSelect.setItems(housingOptions.toArray(new String[0]));
        shopMenuTable.add(housingLabel).padRight(10);
        shopMenuTable.add(housingSelect).width(200).padBottom(10).row();


        Table buttonTable = new Table();
        TextButton confirmButton = new TextButton("Confirm", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);
        buttonTable.add(confirmButton).width(120).pad(10);
        buttonTable.add(cancelButton).width(120).pad(10);
        shopMenuTable.add(buttonTable).colspan(2).row();

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String animalName = nameField.getText().trim();
                int selectedIndex = housingSelect.getSelectedIndex();
                if (animalName.isEmpty()) {
                    shopResultMessage = "Please enter an animal name.";
                    shopResultSuccess = false;
                    showAnimalPurchasePage(animalKey);
                    return;
                }
                if (selectedIndex < 0 || selectedIndex >= validHousings.size()) {
                    shopResultMessage = "Please select a housing.";
                    shopResultSuccess = false;
                    showAnimalPurchasePage(animalKey);
                    return;
                }
                String housingId = String.valueOf(validHousings.get(selectedIndex).getId());
                Result result = shopController.buyAnimal(animalKey, housingId, animalName);
                shopResultMessage = result.Message();
                shopResultSuccess = result.isSuccessful();
                if (shopResultSuccess) {

                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                        actionData.put("animalKey", animalKey);
                        actionData.put("housingId", Integer.parseInt(housingId));
                        actionData.put("givenName", animalName);
                        ns.sendPlayerAction("buy_animal", actionData);
                    }

                    showAnimalPurchasePage(animalKey);
                } else {
                    createShopMenuUI();
                }
            }
        });
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createShopMenuUI();
            }
        });

        if (!shopResultMessage.isEmpty()) {
            Label resultLabel = new Label(shopResultMessage, skin);
            resultLabel.setColor(shopResultSuccess ? Color.GREEN : Color.RED);
            resultLabel.setFontScale(1.1f);
            shopMenuTable.add(resultLabel).colspan(2).padBottom(15).row();
        }
    }

    private void handleHousingClick(int screenX, int screenY) {

        Vector3 mouseInWorld = camera.unproject(new Vector3(screenX, screenY, 0));
        int targetTileX = (int) (mouseInWorld.x / TILE_SIZE);
        int targetTileY = MAP_HEIGHT - 1 - (int) (mouseInWorld.y / TILE_SIZE);


        int housingIndex = getHousingIndex(targetTileX, targetTileY);
        if (housingIndex != -1) {
            Player currentPlayer = game.getCurrentPlayer();
            if (currentPlayer != null && housingIndex < currentPlayer.getHousings().size()) {
                selectedHousing = currentPlayer.getHousings().get(housingIndex);
                showHousingMenu = true;
                createHousingMenuUI();
            }
        }
    }

    private void createHousingMenuUI() {
        if (housingMenuTable != null) {
            housingMenuTable.remove();
        }

        housingMenuTable = new Table();
        housingMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        housingMenuTable.setSize(600, 400);
        housingMenuTable.setPosition(Gdx.graphics.getWidth() / 2f - 300, Gdx.graphics.getHeight() / 2f - 200);


        Label titleLabel = new Label("Housing Management - " + selectedHousing.getType().getName() + " (ID: " + selectedHousing.getId() + ")", skin);
        titleLabel.setFontScale(1.2f);
        titleLabel.setColor(Color.WHITE);
        housingMenuTable.add(titleLabel).colspan(2).padBottom(20).row();


        Table animalListTable = new Table();
        animalListTable.defaults().pad(5);

        if (selectedHousing.getOccupants().isEmpty()) {
            Label noAnimalsLabel = new Label("No animals in this housing.", skin);
            noAnimalsLabel.setColor(Color.GRAY);
            animalListTable.add(noAnimalsLabel).row();
        } else {

            Label nameHeader = new Label("Animal Name", skin);
            Label typeHeader = new Label("Type", skin);
            Label statusHeader = new Label("Status", skin);
            Label actionHeader = new Label("Action", skin);

            nameHeader.setColor(Color.YELLOW);
            typeHeader.setColor(Color.YELLOW);
            statusHeader.setColor(Color.YELLOW);
            actionHeader.setColor(Color.YELLOW);

            animalListTable.add(nameHeader).width(150);
            animalListTable.add(typeHeader).width(100);
            animalListTable.add(statusHeader).width(100);
            animalListTable.add(actionHeader).width(100).row();


            for (PurchasedAnimal animal : selectedHousing.getOccupants()) {
                Label nameLabel = new Label(animal.getName(), skin);
                Label typeLabel = new Label(animal.getType().getName(), skin);
                Label statusLabel = new Label(animal.isInCage() ? "Inside" : "Outside", skin);
                statusLabel.setColor(animal.isInCage() ? Color.GREEN : Color.ORANGE);

                TextButton actionButton;
                if (animal.isInCage()) {
                    actionButton = new TextButton("Bring Out", skin);
                    actionButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            bringAnimalOut(animal);
                        }
                    });
                } else {
                    actionButton = new TextButton("Bring In", skin);
                    actionButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            bringAnimalIn(animal);
                        }
                    });
                }

                animalListTable.add(nameLabel).width(150);
                animalListTable.add(typeLabel).width(100);
                animalListTable.add(statusLabel).width(100);
                animalListTable.add(actionButton).width(100).row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(animalListTable, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollBarPositions(false, true);

        housingMenuTable.add(scrollPane).width(500).height(250).padBottom(20).row();


        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showHousingMenu = false;
                selectedHousing = null;
                if (housingMenuTable != null) {
                    housingMenuTable.remove();
                    housingMenuTable = null;
                }
            }
        });
        housingMenuTable.add(closeButton).width(120).pad(10).row();

        stage.addActor(housingMenuTable);
    }

    private void bringAnimalOut(PurchasedAnimal animal) {

        int[] position = findReachablePositionAroundHousing(selectedHousing);
        animal.setX(position[0]);
        animal.setY(position[1]);
        animal.setInCage(false);

        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
        if (ns != null) {
            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
            data.put("senderUsername", game.getCurrentPlayer().getUsername());
            data.put("animalName", animal.getName());
            data.put("targetX", position[0]);
            data.put("targetY", position[1]);
            ns.sendPlayerAction("bring_animal_out", data);
        }


        animal.setTargetX(position[0]);
        animal.setTargetY(position[1]);
        animal.setMoving(false);
        animal.setMoveProgress(0f);
        animal.setLastMoveTime(0);


        if (housingMenuTable != null) {
            housingMenuTable.remove();
            createHousingMenuUI();
        }
    }

    private void bringAnimalIn(PurchasedAnimal animal) {
        animal.setInCage(true);

        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
        if (ns != null) {
            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
            data.put("senderUsername", game.getCurrentPlayer().getUsername());
            data.put("animalName", animal.getName());
            ns.sendPlayerAction("bring_animal_in", data);
        }


        if (housingMenuTable != null) {
            housingMenuTable.remove();
            createHousingMenuUI();
        }
    }

    private int[] findReachablePositionAroundHousing(Housing housing) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) {
            return new int[]{housing.getX() + 2, housing.getY() + 2};
        }


        List<int[]> potentialPositions = new ArrayList<>();


        for (int y = housing.getY() + 2; y <= housing.getY() + 6; y++) {
            potentialPositions.add(new int[]{housing.getX() + 8, y});
        }


        for (int y = housing.getY() + 2; y <= housing.getY() + 6; y++) {
            potentialPositions.add(new int[]{housing.getX() - 1, y});
        }


        for (int x = housing.getX() + 2; x <= housing.getX() + 6; x++) {
            potentialPositions.add(new int[]{x, housing.getY() + 9});
        }


        for (int x = housing.getX() + 2; x <= housing.getX() + 6; x++) {
            potentialPositions.add(new int[]{x, housing.getY() - 1});
        }


        java.util.Collections.shuffle(potentialPositions, random);


        for (int[] pos : potentialPositions) {
            if (pos[0] >= 0 && pos[0] < MAP_WIDTH && pos[1] >= 0 && pos[1] < MAP_HEIGHT) {
                if (isAnimalWalkable(pos[0], pos[1], currentPlayer)) {
                    return pos;
                }
            }
        }


        return new int[]{housing.getX() + 2, housing.getY() + 2};
    }

    private boolean isAnimalWalkable(int x, int y, Player currentPlayer) {

        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            return false;
        }

        Tile tile = gameMap.getTile(x, y);
        if (tile == null) {
            return false;
        }


        TileType tileType = tile.getType();
        return tileType == TileType.Grass || tileType == TileType.Earth ||
               tileType == TileType.Shoveled || tileType == TileType.Water;
    }

    private void renderAnimals() {
        if (game == null) return;
        for (User u : game.getPlayers()) {
            Player p = u.getPlayer();
            if (p == null || p.getHousings() == null) continue;
            for (Housing housing : p.getHousings()) {
                for (PurchasedAnimal animal : housing.getOccupants()) {
                if (!animal.isInCage()) {

                    Texture animalTexture = animalTextures.get(animal.getType());
                    if (animalTexture != null) {
                        float worldX = animal.getX() * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - animal.getY()) * TILE_SIZE;


                        if (animal.isMoving()) {
                            float startX = animal.getX() * TILE_SIZE;
                            float startY = (MAP_HEIGHT - 1 - animal.getY()) * TILE_SIZE;
                            float endX = animal.getTargetX() * TILE_SIZE;
                            float endY = (MAP_HEIGHT - 1 - animal.getTargetY()) * TILE_SIZE;

                            worldX = startX + (endX - startX) * animal.getMoveProgress();
                            worldY = startY + (endY - startY) * animal.getMoveProgress();
                        }


                        float bounceOffset = 0f;
                        for (AnimationEffect effect : activeAnimations) {
                            if (effect.type.equals("bounce")) {
                                float animalCenterX = worldX + TILE_SIZE / 2f;
                                float animalCenterY = worldY + TILE_SIZE / 2f;
                                if (Math.abs(effect.x - animalCenterX) < TILE_SIZE &&
                                    Math.abs(effect.y - animalCenterY) < TILE_SIZE) {
                                    bounceOffset = effect.bounceHeight;
                                    break;
                                }
                            }
                        }
                        worldY += bounceOffset;


                        boolean movingLeft = animal.isMoving() && animal.getTargetX() < animal.getX();


                        if (movingLeft) {

                            spriteBatch.draw(animalTexture, worldX + TILE_SIZE, worldY, -TILE_SIZE, TILE_SIZE);
                        } else {

                            spriteBatch.draw(animalTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                        }
                    }
                }
            }
        }
    }

    }

    private void updateAnimalMovement(float delta) {
        if (showAnimalMenu) return;
        if (game == null) return;

        animalMovementTimer += delta;

        if (animalMovementTimer >= ANIMAL_MOVEMENT_UPDATE_INTERVAL) {
            animalMovementTimer = 0f;

            for (User u : game.getPlayers()) {
                Player p = u.getPlayer();
                if (p == null || p.getHousings() == null) continue;
                for (Housing housing : p.getHousings()) {
                    for (PurchasedAnimal animal : housing.getOccupants()) {
                        if (!animal.isInCage()) {
                            updateAnimalMovement(animal, housing);
                        }
                    }
                }
            }
        }

        for (User u : game.getPlayers()) {
            Player p = u.getPlayer();
            if (p == null || p.getHousings() == null) continue;
            for (Housing housing : p.getHousings()) {
                for (PurchasedAnimal animal : housing.getOccupants()) {
                    if (!animal.isInCage() && animal.isMoving()) {
                        animal.setMoveProgress(animal.getMoveProgress() + delta * animal.MOVE_SPEED);

                        if (animal.getMoveProgress() >= 1.0f) {
                            animal.setX(animal.getTargetX());
                            animal.setY(animal.getTargetY());
                            animal.setMoving(false);
                            animal.setMoveProgress(0f);
                        }
                    }
                }
            }
        }
    }

    private void updateAnimalMovement(PurchasedAnimal animal, Housing housing) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - animal.getLastMoveTime() < animal.MOVE_INTERVAL) {
            return;
        }

        int currentX = animal.getX();
        int currentY = animal.getY();

        for (int attempts = 0; attempts < 20; attempts++) {
            int offsetX = random.nextInt(11) - 5;
            int offsetY = random.nextInt(11) - 5;

            int targetX = currentX + offsetX;
            int targetY = currentY + offsetY;

            if (targetX < 0 || targetX >= MAP_WIDTH || targetY < 0 || targetY >= MAP_HEIGHT) {
                continue;
            }

            if (isAnimalWalkable(targetX, targetY, game.getCurrentPlayer())) {
                int stepX = currentX;
                int stepY = currentY;

                if (targetX > currentX) stepX++;
                else if (targetX < currentX) stepX--;

                if (targetY > currentY) stepY++;
                else if (targetY < currentY) stepY--;

                if (isAnimalWalkable(stepX, stepY, game.getCurrentPlayer())) {
                    animal.setTargetX(stepX);
                    animal.setTargetY(stepY);
                    animal.setMoving(true);
                    animal.setMoveProgress(0f);
                    animal.setLastMoveTime(currentTime);
                    return;
                }
            }
        }
    }

    private List<int[]> findPathToTarget(int startX, int startY, int targetX, int targetY) {
        PriorityQueue<PathNode> openSet = new PriorityQueue<>();
        Set<String> closedSet = new HashSet<>();
        Map<String, PathNode> allNodes = new HashMap<>();

        PathNode startNode = new PathNode(startX, startY, 0, calculateHeuristic(startX, startY, targetX, targetY));
        openSet.add(startNode);
        allNodes.put(startX + "," + startY, startNode);

        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            closedSet.add(current.x + "," + current.y);

            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
                String key = newX + "," + newY;

                if (closedSet.contains(key)) continue;

                if (newX < 0 || newX >= MAP_WIDTH || newY < 0 || newY >= MAP_HEIGHT) continue;

                if (!isAnimalWalkable(newX, newY, game.getCurrentPlayer())) continue;

                int newG = current.g + 1;
                PathNode neighbor = allNodes.get(key);

                if (neighbor == null) {
                    neighbor = new PathNode(newX, newY, newG, calculateHeuristic(newX, newY, targetX, targetY));
                    neighbor.parent = current;
                    allNodes.put(key, neighbor);
                    openSet.add(neighbor);
                } else if (newG < neighbor.g) {
                    neighbor.g = newG;
                    neighbor.parent = current;
                }
            }
        }

        return null;
    }

    private int calculateHeuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private List<int[]> reconstructPath(PathNode endNode) {
        List<int[]> path = new ArrayList<>();
        PathNode current = endNode;

        while (current != null) {
            path.add(0, new int[]{current.x, current.y});
            current = current.parent;
        }

        return path;
    }

    private static class PathNode implements Comparable<PathNode> {
        int x, y, g, h;
        PathNode parent;

        PathNode(int x, int y, int g, int h) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
        }

        int f() { return g + h; }

        @Override
        public int compareTo(PathNode other) {
            return Integer.compare(this.f(), other.f());
        }
    }

    private boolean handleAnimalClick(int screenX, int screenY) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return false;
        com.badlogic.gdx.math.Vector3 mouseInWorld = camera.unproject(new com.badlogic.gdx.math.Vector3(screenX, screenY, 0));
        float mx = mouseInWorld.x;
        float my = mouseInWorld.y;
        for (Housing housing : currentPlayer.getHousings()) {
            for (PurchasedAnimal animal : housing.getOccupants()) {
                if (!animal.isInCage()) {
                    float ax = animal.getX() * TILE_SIZE;
                    float ay = (MAP_HEIGHT - 1 - animal.getY()) * TILE_SIZE;
                    if (mx >= ax && mx < ax + TILE_SIZE && my >= ay && my < ay + TILE_SIZE) {
                        selectedAnimal = animal;
                        showAnimalMenu = true;
                        createAnimalMenuUI();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean handleTrashCanClick(int screenX, int screenY) {

        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));


        int tileX = (int) (worldCoords.x / TILE_SIZE);
        int tileY = (int) ((MAP_HEIGHT * TILE_SIZE - worldCoords.y) / TILE_SIZE);

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return false;


        if (currentPlayer.getTrashCanX() == tileX && currentPlayer.getTrashCanY() == tileY) {

            showSellMenu = true;
            createSellMenuUI();
            Gdx.input.setInputProcessor(stage);
            return true;
        }

        return false;
    }

    private void createAnimalMenuUI() {
        if (animalMenuTable != null) {
            animalMenuTable.remove();
            animalMenuTable = null;
        }
        animalMenuTable = new Table();
        animalMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        animalMenuTable.setSize(600, 400);
        animalMenuTable.setPosition(Gdx.graphics.getWidth() / 2f - 300, Gdx.graphics.getHeight() / 2f - 200);
        animalMenuTable.pad(20);
        animalMenuTable.defaults().pad(10).width(250);

        Label title = new Label(selectedAnimal != null ? selectedAnimal.getName() : "Animal", skin);
        title.setAlignment(Align.center);
        animalMenuTable.add(title).center().row();

        TextButton infoBtn = new TextButton("Info", skin);
        TextButton feedBtn = new TextButton("Feed", skin);
        TextButton petBtn = new TextButton("Pet", skin);
        TextButton sellBtn = new TextButton("Sell", skin);
        TextButton shepherdBtn = new TextButton("Shepherd", skin);
        TextButton collectBtn = new TextButton("Collect", skin);
        TextButton closeBtn = new TextButton("Close", skin);

        animalMenuTable.add(infoBtn).row();
        animalMenuTable.add(feedBtn).row();
        animalMenuTable.add(petBtn).row();
        animalMenuTable.add(sellBtn).row();
        animalMenuTable.add(shepherdBtn).row();
        animalMenuTable.add(collectBtn).row();
        animalMenuTable.add(closeBtn).row();

        infoBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalInfoPage();
            }
        });

        feedBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller != null && selectedAnimal != null) {
                    Result res = controller.feedHay(selectedAnimal.getName());
                    if (res != null && res.isSuccessful()) {
                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                        if (ns != null) {
                            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                            data.put("senderUsername", game.getCurrentPlayer().getUsername());
                            data.put("animalName", selectedAnimal.getName());
                            data.put("senderX", game.getCurrentPlayer().currentX());
                            data.put("senderY", game.getCurrentPlayer().currentY());
                            ns.sendPlayerAction("feed_hay", data);
                        }
                    }


                    float animalWorldX = selectedAnimal.getX() * TILE_SIZE + TILE_SIZE / 2f;
                    float animalWorldY = (MAP_HEIGHT - 1 - selectedAnimal.getY()) * TILE_SIZE + TILE_SIZE / 2f;
                    createAnimation(animalWorldX, animalWorldY, "food");
                }
                showAnimalMenu = false;
                selectedAnimal = null;
                if (animalMenuTable != null) {
                    animalMenuTable.remove();
                    animalMenuTable = null;
                }
            }
        });

        petBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller != null && selectedAnimal != null) {
                    Result res = controller.petAnimal(selectedAnimal.getName());
                    if (res != null && res.isSuccessful()) {
                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                        if (ns != null) {
                            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                            data.put("senderUsername", game.getCurrentPlayer().getUsername());
                            data.put("animalName", selectedAnimal.getName());
                            data.put("senderX", game.getCurrentPlayer().currentX());
                            data.put("senderY", game.getCurrentPlayer().currentY());
                            ns.sendPlayerAction("pet_animal", data);
                        }
                    }


                    float animalWorldX = selectedAnimal.getX() * TILE_SIZE + TILE_SIZE / 2f;
                    float animalWorldY = (MAP_HEIGHT - 1 - selectedAnimal.getY()) * TILE_SIZE + TILE_SIZE / 2f;
                    createAnimation(animalWorldX, animalWorldY, "hearts");
                    createAnimation(animalWorldX, animalWorldY, "bounce");
                }
                showAnimalMenu = false;
                selectedAnimal = null;
                if (animalMenuTable != null) {
                    animalMenuTable.remove();
                    animalMenuTable = null;
                }
            }
        });

        sellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller != null && selectedAnimal != null) {
                    Result res = controller.sellAnimal(selectedAnimal.getName());
                    if (res != null && res.isSuccessful()) {
                        com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                        if (ns != null) {
                            java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                            data.put("senderUsername", game.getCurrentPlayer().getUsername());
                            data.put("animalName", selectedAnimal.getName());
                            ns.sendPlayerAction("sell_animal", data);
                        }
                    }
                    Player currentPlayer = game.getCurrentPlayer();
                    if (currentPlayer != null) {
                        for (Housing housing : currentPlayer.getHousings()) {
                            for (Iterator<PurchasedAnimal> it = housing.getOccupants().iterator(); it.hasNext(); ) {
                                PurchasedAnimal a = it.next();
                                if (a == selectedAnimal) {
                                    it.remove();
                                    break;
                                }
                            }
                        }
                    }
                }
                showAnimalMenu = false;
                selectedAnimal = null;
                if (animalMenuTable != null) {
                    animalMenuTable.remove();
                    animalMenuTable = null;
                }
            }
        });

        shepherdBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedAnimal != null) {
                    animalToShepherd = selectedAnimal.getName();
                    isShepherdMode = true;
                    showAnimalMenu = false;
                    selectedAnimal = null;
                    if (animalMenuTable != null) {
                        animalMenuTable.remove();
                        animalMenuTable = null;
                    }
                }
            }
        });

        collectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalCollectPage();
            }
        });

        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalMenu = false;
                selectedAnimal = null;
                if (animalMenuTable != null) {
                    animalMenuTable.remove();
                    animalMenuTable = null;
                }
            }
        });

        stage.addActor(animalMenuTable);
        stage.setKeyboardFocus(animalMenuTable);
        stage.setScrollFocus(animalMenuTable);
    }

    private void showAnimalInfoPage() {
        if (animalMenuTable != null) {
            animalMenuTable.remove();
            animalMenuTable = null;
        }
        animalMenuTable = new Table();
        animalMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        animalMenuTable.setSize(600, 400);
        animalMenuTable.setPosition(Gdx.graphics.getWidth() / 2f - 300, Gdx.graphics.getHeight() / 2f - 200);
        animalMenuTable.pad(20);
        animalMenuTable.defaults().pad(10).width(250);

        Label title = new Label(selectedAnimal != null ? selectedAnimal.getName() + " - Info" : "Animal Info", skin);
        title.setAlignment(Align.center);
        animalMenuTable.add(title).center().row();

        String info = selectedAnimal != null ? selectedAnimal.toString() : "No animal selected.";
        Label infoLabel = new Label(info, skin);
        infoLabel.setAlignment(Align.topLeft);
        infoLabel.setWrap(true);
        animalMenuTable.add(infoLabel).expandX().fillX().row();

        Table buttonRow = new Table();
        TextButton backBtn = new TextButton("Back", skin);
        TextButton closeBtn = new TextButton("Close", skin);
        buttonRow.add(backBtn).width(120).padRight(20);
        buttonRow.add(closeBtn).width(120);
        animalMenuTable.add(buttonRow).padTop(20).center().row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createAnimalMenuUI();
            }
        });
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalMenu = false;
                selectedAnimal = null;
                if (animalMenuTable != null) {
                    animalMenuTable.remove();
                    animalMenuTable = null;
                }
            }
        });

        stage.addActor(animalMenuTable);
        stage.setKeyboardFocus(animalMenuTable);
        stage.setScrollFocus(animalMenuTable);
    }

    private void showAnimalCollectPage() {
        if (animalMenuTable != null) {
            animalMenuTable.remove();
            animalMenuTable = null;
        }
        animalMenuTable = new Table();
        animalMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        animalMenuTable.setSize(600, 400);
        animalMenuTable.setPosition(Gdx.graphics.getWidth() / 2f - 300, Gdx.graphics.getHeight() / 2f - 200);
        animalMenuTable.pad(20);
        animalMenuTable.defaults().pad(10).width(250);

        Label title = new Label(selectedAnimal != null ? selectedAnimal.getName() + " - Collect" : "Animal Collect", skin);
        title.setAlignment(Align.center);
        animalMenuTable.add(title).center().row();


        String collectResult = "";
        boolean collectSuccess = false;
        if (controller != null && selectedAnimal != null) {
            Result result = controller.showCollectableProducts(selectedAnimal.getName());
            collectResult = result.Message();
            collectSuccess = result.isSuccessful();
        }


        Label resultLabel = new Label(collectResult, skin);
        resultLabel.setAlignment(Align.topLeft);
        resultLabel.setWrap(true);
        animalMenuTable.add(resultLabel).expandX().fillX().row();


        if (collectSuccess) {
            TextButton collectProductBtn = new TextButton("Collect Product", skin);
            animalMenuTable.add(collectProductBtn).row();

            collectProductBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (controller != null && selectedAnimal != null) {
                        Result res = controller.collectAnimalProduct(selectedAnimal.getName());
                        if (res != null && res.isSuccessful()) {
                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> data = new java.util.HashMap<>();
                                data.put("senderUsername", game.getCurrentPlayer().getUsername());
                                data.put("animalName", selectedAnimal.getName());
                                ns.sendPlayerAction("collect_animal_product", data);
                            }
                        }
                    }
                    showAnimalMenu = false;
                    selectedAnimal = null;
                    if (animalMenuTable != null) {
                        animalMenuTable.remove();
                        animalMenuTable = null;
                    }
                }
            });
        }

        Table buttonRow = new Table();
        TextButton backBtn = new TextButton("Back", skin);
        TextButton closeBtn = new TextButton("Close", skin);
        buttonRow.add(backBtn).width(120).padRight(20);
        buttonRow.add(closeBtn).width(120);
        animalMenuTable.add(buttonRow).padTop(20).center().row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createAnimalMenuUI();
            }
        });
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalMenu = false;
                selectedAnimal = null;
                if (animalMenuTable != null) {
                    animalMenuTable.remove();
                    animalMenuTable = null;
                }
            }
        });

        stage.addActor(animalMenuTable);
        stage.setKeyboardFocus(animalMenuTable);
        stage.setScrollFocus(animalMenuTable);
    }


    private void startActionAnimation(PlayerActionState state) {
        playerActionState = state;
        actionTimer = ACTION_ANIMATION_DURATION;
        playerMoving = false;
    }

    private void showPlacementDialog(Food food) {
        Player player = game.getCurrentPlayer();
        Dialog dialog = new Dialog("Choose Destination", skin, "dialog") {
            protected void result(Object object) {
                if (object instanceof String) {
                    Result placementResult = controller.placeCookedFood((String) object);
                    generalMessageLabel.setText(placementResult.Message());
                    generalMessageLabel.setVisible(true);
                    generalMessageTimer = GENERAL_MESSAGE_DURATION;
                    showCookingMenu();
                }
            }
        };

        dialog.text("Place " + food.getName() + " in:");

        TextButton inventoryButton = new TextButton("Inventory", skin);
        if (player.getInventory().isFull()) {
            inventoryButton.setDisabled(true);
            inventoryButton.setText("Inventory (Full)");
        }
        dialog.button(inventoryButton, "inventory");

        TextButton fridgeButton = new TextButton("Refrigerator", skin);
        if (player.getHouseRefrigerator().isFull()) {
            fridgeButton.setDisabled(true);
            fridgeButton.setText("Refrigerator (Full)");
        }
        dialog.button(fridgeButton, "refrigerator");

        dialog.show(cookingStage);
    }

   private void createFriendsMenuUI() {
        if (friendsMenuTable != null) {
            friendsMenuTable.remove();
        }

        friendsMenuTable = new Table();
        friendsMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        friendsMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        friendsMenuTable.setPosition(
            (Gdx.graphics.getWidth() - friendsMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - friendsMenuTable.getHeight()) / 2f
        );

        stage.addActor(friendsMenuTable);

        switch (currentFriendsMenuState) {
            case MAIN_MENU:
                createMainFriendsMenu();
                break;
            case PLAYER_ACTIONS:
                createPlayerActionsMenu();
                break;
            case FRIENDSHIP_DETAILS:
                createFriendshipDetailsMenu();
                break;
            case CHAT_ROOM:
                createChatRoomMenu();
                break;
            case GIFT_MENU:
                createPlayerGiftMenu();
                break;
            case RECEIVED_GIFTS:
                createReceivedGiftsMenu();
                break;
            case GIFT_HISTORY:
                createGiftHistoryMenu();
                break;
            case SEND_GIFT:
                createSendGiftMenu();
                break;
            case MARRIAGE_MENU:
                createMarriageMenu();
                break;
            case MARRIAGE_RESPOND:
                createMarriageRespondMenu();
                break;
        }
    }

    private void createMainFriendsMenu() {
        friendsMenuTable.clear();

        Label titleLabel = new Label("Friends Menu", skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();

        Player currentPlayer = game.getCurrentPlayer();
        ArrayList<Player> otherPlayers = new ArrayList<>();
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player != null && !player.equals(currentPlayer)) {
                otherPlayers.add(player);
            }
        }

        for (Player player : otherPlayers) {
            String buttonText = player.getUsername();
            if (Boolean.TRUE.equals(unreadTalkNotifications.get(player.getUsername()))) {
                buttonText += " [NEW]";
            }
            TextButton playerButton = new TextButton(buttonText, skin);
            final String playerName = player.getUsername();
            if (Boolean.TRUE.equals(unreadTalkNotifications.get(player.getUsername()))) {
                playerButton.setColor(Color.YELLOW);
            }
            playerButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedPlayerForActions = playerName;
                    currentFriendsMenuState = FriendsMenuState.PLAYER_ACTIONS;

                    unreadTalkNotifications.put(playerName, false);
                    createFriendsMenuUI();
                }
            });
            friendsMenuTable.add(playerButton).size(200, 50).pad(10);
        }

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });
        friendsMenuTable.add(closeButton).size(200, 50).pad(10).row();
    }

    private void createPlayerActionsMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Actions with " + selectedPlayerForActions, skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        if (!friendshipResultMessage.isEmpty()) {
            Label resultLabel = new Label(friendshipResultMessage, skin);
            resultLabel.setColor(friendshipResultSuccess ? Color.GREEN : Color.RED);
            friendsMenuTable.add(resultLabel).colspan(2).pad(10).fillX().center().row();
        }


        String talkButtonText = "Talk";
        if (Boolean.TRUE.equals(unreadTalkNotifications.get(selectedPlayerForActions))) {
            talkButtonText += " [NEW]";
        }
        TextButton talkButton = new TextButton(talkButtonText, skin);

        String giftButtonText = "Gift";
        if (Boolean.TRUE.equals(unreadGiftNotifications.get(selectedPlayerForActions))) {
            giftButtonText += " [NEW]";
        }
        TextButton giftButton = new TextButton(giftButtonText, skin);

        TextButton friendshipButton = new TextButton("Friendship", skin);
        TextButton hugButton = new TextButton("Hug", skin);
        TextButton flowerButton = new TextButton("Flower", skin);
        TextButton marriageButton = new TextButton("Marriage", skin);
        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);


        friendshipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.FRIENDSHIP_DETAILS;
                createFriendsMenuUI();
            }
        });

        talkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.CHAT_ROOM;

                unreadTalkNotifications.put(selectedPlayerForActions, false);
                createFriendsMenuUI();
            }
        });

        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.GIFT_MENU;

                unreadGiftNotifications.put(selectedPlayerForActions, false);
                createFriendsMenuUI();
            }
        });

        hugButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.hug(selectedPlayerForActions);
                if (result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                        actionData.put("receiverUsername", selectedPlayerForActions);
                        actionData.put("senderX", game.getCurrentPlayer().currentX());
                        actionData.put("senderY", game.getCurrentPlayer().currentY());
                        ns.sendPlayerAction("hug", actionData);
                    }

                    showFriendsMenu = false;
                    currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                    selectedPlayerForActions = null;
                    if (friendsMenuTable != null) {
                        friendsMenuTable.remove();
                        friendsMenuTable = null;
                    }
                    Gdx.input.setInputProcessor(multiplexer);
                } else {

                    friendshipResultMessage = result.Message();
                    friendshipResultSuccess = false;
                    createPlayerActionsMenu();
                }
            }
        });

        flowerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.flowerSomeone(selectedPlayerForActions);
                if (result.isSuccessful()) {
                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                        actionData.put("receiverUsername", selectedPlayerForActions);
                        actionData.put("senderX", game.getCurrentPlayer().currentX());
                        actionData.put("senderY", game.getCurrentPlayer().currentY());
                        ns.sendPlayerAction("flower_someone", actionData);
                    }

                    showFriendsMenu = false;
                    currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                    selectedPlayerForActions = null;
                    if (friendsMenuTable != null) {
                        friendsMenuTable.remove();
                        friendsMenuTable = null;
                    }
                    Gdx.input.setInputProcessor(multiplexer);
                } else {

                    friendshipResultMessage = result.Message();
                    friendshipResultSuccess = false;
                    createPlayerActionsMenu();
                }
            }
        });

        marriageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.MARRIAGE_MENU;
                marriageErrorMessage = "";
                showMarriageError = false;
                createFriendsMenuUI();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                createFriendsMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });


        friendsMenuTable.add(friendshipButton).size(200, 50).pad(10);
        friendsMenuTable.add(talkButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(giftButton).size(200, 50).pad(10);
        friendsMenuTable.add(hugButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(flowerButton).size(200, 50).pad(10);
        friendsMenuTable.add(marriageButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(backButton).size(200, 50).pad(10);
        friendsMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void renderFriendsMenu(float delta) {
        if (!showFriendsMenu) {
            return;
        }


        stage.act(delta);
    }

    private void renderSellMenu(float delta) {
        if (!showSellMenu) {
            return;
        }


        stage.act(delta);
    }

    private void createFriendshipDetailsMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Friendship Details with " + selectedPlayerForActions, skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        Player currentPlayer = game.getCurrentPlayer();
        Player targetPlayer = null;


        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player != null && player.getUsername().equals(selectedPlayerForActions)) {
                targetPlayer = player;
                break;
            }
        }

        if (targetPlayer != null) {

            Friendship friendship = game.getFriendshipByPlayers(currentPlayer, targetPlayer);
            if (friendship != null) {
                Label friendshipLabel = new Label(friendship.toString(), skin);
                friendshipLabel.setFontScale(1.0f);
                friendshipLabel.setWrap(true);
                friendshipLabel.setAlignment(Align.center);
                friendsMenuTable.add(friendshipLabel).colspan(2).pad(20).fillX().expandX().center().row();
            } else {
                Label noFriendshipLabel = new Label("No friendship data available", skin);
                noFriendshipLabel.setFontScale(1.0f);
                noFriendshipLabel.setAlignment(Align.center);
                friendsMenuTable.add(noFriendshipLabel).colspan(2).pad(20).center().row();
            }
        } else {
            Label errorLabel = new Label("Player not found", skin);
            errorLabel.setFontScale(1.0f);
            friendsMenuTable.add(errorLabel).colspan(2).pad(20).row();
        }


        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.PLAYER_ACTIONS;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                createFriendsMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        friendsMenuTable.add(backButton).size(200, 50).pad(10);
        friendsMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createChatRoomMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Chat with " + selectedPlayerForActions, skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        chatMessagesTable = new Table();
        chatMessagesTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        chatMessagesTable.left().top();
        chatMessagesTable.defaults().expandX().fillX().pad(2);


        loadChatHistory();


        chatScrollPane = new ScrollPane(chatMessagesTable, skin);
        chatScrollPane.setScrollBarPositions(false, true);
        chatScrollPane.setFadeScrollBars(false);
        chatScrollPane.setScrollPercentY(1.0f);

        friendsMenuTable.add(chatScrollPane).colspan(2).fill().expand().pad(10).row();


        Table inputTable = new Table();

        chatMessageField = new TextField("", skin);
        chatMessageField.setMessageText("Type your message here...");
        chatMessageField.setMaxLength(200);

        TextButton sendButton = new TextButton("Send", skin);

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendChatMessage();
            }
        });


        chatMessageField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    sendChatMessage();
                    return true;
                }
                return false;
            }
        });

        inputTable.add(chatMessageField).expandX().fillX().padRight(10);
        inputTable.add(sendButton).width(80);

        friendsMenuTable.add(inputTable).colspan(2).fillX().pad(10).row();


        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.PLAYER_ACTIONS;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                createFriendsMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        friendsMenuTable.add(backButton).size(200, 50).pad(10);
        friendsMenuTable.add(closeButton).size(200, 50).pad(10);


        stage.setKeyboardFocus(chatMessageField);
    }

    private void loadChatHistory() {
        chatMessagesTable.clear();

        Player currentPlayer = game.getCurrentPlayer();
        Player targetPlayer = null;


        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player != null && player.getUsername().equals(selectedPlayerForActions)) {
                targetPlayer = player;
                break;
            }
        }

        if (targetPlayer != null) {

            Result result = controller.talkHistory(selectedPlayerForActions);
            String history = result.Message();

            if (history.contains("No conversation history")) {
                Label noHistoryLabel = new Label("No previous messages", skin);
                noHistoryLabel.setColor(Color.GRAY);
                chatMessagesTable.add(noHistoryLabel).pad(10).row();
            } else {

                String[] lines = history.split("\n");
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    if (line.contains(":")) {

                        String[] parts = line.split(":", 2);
                        if (parts.length == 2) {
                            String header = parts[0].trim();
                            String message = parts[1].trim();
                            String sender = header;
                            String receiver = "";
                            if (header.contains(" to ")) {
                                String[] headerParts = header.split(" to ", 2);
                                sender = headerParts[0].trim();
                                receiver = headerParts[1].trim();
                            }
                            String formattedMessage = sender + " to " + receiver + ": " + message;
                            Label messageLabel = new Label(formattedMessage, skin);
                            messageLabel.setWrap(false);
                            messageLabel.setFontScale(0.9f);
                            messageLabel.setAlignment(Align.left);
                            if (sender.equals(currentPlayer.getUsername())) {
                                messageLabel.setColor(Color.GREEN);
                            } else if (receiver.equals(currentPlayer.getUsername())) {
                                messageLabel.setColor(Color.BLUE);
                            } else {
                                messageLabel.setColor(Color.BLACK);
                            }
                            chatMessagesTable.add(messageLabel).growX().left().pad(5).row();
                        }
                    } else {
                        Label infoLabel = new Label(line, skin);
                        infoLabel.setColor(Color.GRAY);
                        infoLabel.setFontScale(0.8f);
                        infoLabel.setWrap(false);
                        infoLabel.setAlignment(Align.left);
                        chatMessagesTable.add(infoLabel).growX().left().pad(2).row();
                    }
                }
            }
        }


        if (chatScrollPane != null) {
            chatScrollPane.setScrollPercentY(1.0f);
        }
    }

    private void checkForNewMessages() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player != null && !player.equals(currentPlayer)) {
                Result result = controller.talkHistory(player.getUsername());
                String history = result.Message();
                if (!history.contains("No conversation history")) {
                    String[] lines = history.split("\n");
                    for (int i = lines.length - 1; i >= 0; i--) {
                        String line = lines[i].trim();
                        if (!line.isEmpty() && line.contains(":")) {
                            String[] parts = line.split(":", 2);
                            if (parts.length == 2) {
                                String header = parts[0].trim();
                                String sender = header;
                                String receiver = "";
                                if (header.contains(" to ")) {
                                    String[] headerParts = header.split(" to ", 2);
                                    sender = headerParts[0].trim();
                                    receiver = headerParts[1].trim();
                                }

                                if (sender.equals(player.getUsername()) && receiver.equals(currentPlayer.getUsername())) {
                                    unreadTalkNotifications.put(player.getUsername(), true);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkForNewGifts() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player != null && !player.equals(currentPlayer)) {
                Result result = controller.showGiftHistoryWith(player.getUsername());
                String giftsText = result.Message();

                if (!giftsText.contains("No conversation history") && !giftsText.trim().isEmpty()) {
                    String[] giftBlocks = giftsText.split("------------------------");
                    if (giftBlocks.length > 0) {
                        String lastGiftBlock = giftBlocks[giftBlocks.length - 1].trim();
                        if (!lastGiftBlock.isEmpty()) {

                            String[] lines = lastGiftBlock.split("\n");
                            for (String line : lines) {
                                if (line.startsWith("Gift from ")) {
                                    String senderName = line.substring(10).split(":")[0].trim();
                                    if (senderName.equals(player.getUsername())) {

                                        boolean hasUnratedGift = false;
                                        for (String giftLine : lines) {
                                            if (giftLine.trim().equals("Rate: 0")) {
                                                hasUnratedGift = true;
                                                break;
                                            }
                                        }
                                        if (hasUnratedGift) {
                                            unreadGiftNotifications.put(player.getUsername(), true);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendChatMessage() {
        String message = chatMessageField.getText().trim();
        if (!message.isEmpty()) {
            Result result = controller.talk(selectedPlayerForActions, message);
            if (result.isSuccessful()) {

                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                if (ns != null) {
                    java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                    actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                    actionData.put("receiverUsername", selectedPlayerForActions);
                    actionData.put("message", message);
                    ns.sendPlayerAction("talk", actionData);
                }

                if (!selectedPlayerForActions.equals(game.getCurrentPlayer().getUsername())) {
                    unreadTalkNotifications.put(selectedPlayerForActions, true);
                }
                chatMessageField.setText("");
                loadChatHistory();
                chatScrollPane.setScrollPercentY(1.0f);
            } else {
                friendshipResultMessage = result.Message();
                friendshipResultSuccess = false;
                createChatRoomMenu();
            }
        }
    }

    public void applyRemoteTalk(String senderUsername, String receiverUsername, String message) {
        if (game == null || controller == null) {
            return;
        }


        User previousUser = game.getUrrentUser();
        Player previousPlayer = game.getCurrentPlayer();

        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;

        if (senderUser == null || senderPlayer == null) {
            return;
        }

        try {

            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);

            controller.talkRemote(senderUsername, receiverUsername, message);


            String localUsername = localPlayer != null ? localPlayer.getUsername() : (game.getCurrentPlayer() != null ? game.getCurrentPlayer().getUsername() : null);
            if (localUsername != null && localUsername.equals(receiverUsername)) {
                boolean chatOpenWithSender = (currentFriendsMenuState == FriendsMenuState.CHAT_ROOM)
                        && senderUsername.equals(selectedPlayerForActions);
                if (chatOpenWithSender) {
                    unreadTalkNotifications.put(senderUsername, false);
                    loadChatHistory();
                    if (chatScrollPane != null) {
                        chatScrollPane.setScrollPercentY(1.0f);
                    }
                } else {
                    unreadTalkNotifications.put(senderUsername, true);
                }
            }
        } finally {

            if (previousUser != null) game.setCurrentUser(previousUser);
            if (previousPlayer != null) game.setCurrentPlayer(previousPlayer);
        }
    }

    public void applyRemoteBuildBarnOrCoop(String senderUsername, String buildingKey, int x, int y) {
        if (game == null || shopController == null) return;
        User previousUser = game.getUrrentUser();
        Player previousPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            shopController.buildBarnOrCoop(buildingKey, String.valueOf(x), String.valueOf(y));
        } finally {
            if (previousUser != null) game.setCurrentUser(previousUser);
            if (previousPlayer != null) game.setCurrentPlayer(previousPlayer);
        }
    }

    public void applyRemotePurchase(String senderUsername, String name, int amount, Integer shopX, Integer shopY) {
        if (game == null || shopController == null) return;
        User previousUser = game.getUrrentUser();
        Player previousPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);

            if (shopX != null && shopY != null && senderPlayer != null) {
                senderPlayer.setCurrentX(shopX);
                senderPlayer.setCurrentY(shopY);
            }
            shopController.purchase(name, String.valueOf(amount));
        } finally {
            if (previousUser != null) game.setCurrentUser(previousUser);
            if (previousPlayer != null) game.setCurrentPlayer(previousPlayer);
        }
    }

    public void applyRemoteBuyAnimal(String senderUsername, String animalKey, int housingId, String givenName) {
        if (game == null || shopController == null) return;
        User previousUser = game.getUrrentUser();
        Player previousPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            shopController.buyAnimal(animalKey, String.valueOf(housingId), givenName);
        } finally {
            if (previousUser != null) game.setCurrentUser(previousUser);
            if (previousPlayer != null) game.setCurrentPlayer(previousPlayer);
        }
    }

    public void applyRemoteSell(String senderUsername, String itemName, int amount, Integer x, Integer y) {
        if (game == null || controller == null) return;
        User previousUser = game.getUrrentUser();
        Player previousPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (x != null && y != null) {
                senderPlayer.setCurrentX(x);
                senderPlayer.setCurrentY(y);
            }
            controller.sell(itemName, String.valueOf(amount));
        } finally {
            if (previousUser != null) game.setCurrentUser(previousUser);
            if (previousPlayer != null) game.setCurrentPlayer(previousPlayer);
        }
    }

    public void applyRemoteCheatAddItem(String senderUsername, String itemName, int quantity) {
        if (game == null || controller == null) return;
        User previousUser = game.getUrrentUser();
        Player previousPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.cheatAddItem(itemName, String.valueOf(quantity));
        } finally {
            if (previousUser != null) game.setCurrentUser(previousUser);
            if (previousPlayer != null) game.setCurrentPlayer(previousPlayer);
        }
    }

    public void applyRemoteCheatAddCraftingRecipe(String senderUsername, String recipeName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.cheatAddCraftingRecipe(recipeName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCheatAddCookingRecipe(String senderUsername, String recipeName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.cheatAddCookingRecipe(recipeName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemotePetAnimal(String senderUsername, String animalName, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (senderX != null && senderY != null) { senderPlayer.setCurrentX(senderX); senderPlayer.setCurrentY(senderY); }
            controller.petAnimal(animalName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteFeedHay(String senderUsername, String animalName, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (senderX != null && senderY != null) { senderPlayer.setCurrentX(senderX); senderPlayer.setCurrentY(senderY); }
            controller.feedHay(animalName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteSellAnimal(String senderUsername, String animalName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.sellAnimal(animalName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCollectAnimalProduct(String senderUsername, String animalName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.collectAnimalProduct(animalName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteShepherdAnimal(String senderUsername, String animalName, int targetX, int targetY) {
        if (game == null) return;
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderPlayer == null) return;
        for (Housing housing : senderPlayer.getHousings()) {
            for (PurchasedAnimal animal : housing.getOccupants()) {
                if (animal.getName().equals(animalName)) {
                    animal.setTargetX(targetX);
                    animal.setTargetY(targetY);
                    animal.setMoving(true);
                    animal.setMoveProgress(0f);

                    if (game.getMap() != null) {
                        Tile tile = game.getMap().getTile(targetX, targetY);
                        if (tile != null && tile.getType() == TileType.Housing) {
                            animal.setInCage(true);
                        } else {
                            animal.setInCage(false);
                        }
                    }
                    return;
                }
            }
        }
    }

    public void applyRemoteBringAnimalIn(String senderUsername, String animalName) {
        if (game == null) return;
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderPlayer == null) return;
        for (Housing housing : senderPlayer.getHousings()) {
            for (PurchasedAnimal animal : housing.getOccupants()) {
                if (animal.getName().equals(animalName)) {
                    animal.setInCage(true);
                    animal.setMoving(false);
                    animal.setMoveProgress(0f);
                    return;
                }
            }
        }
    }

    public void applyRemoteBringAnimalOut(String senderUsername, String animalName, int x, int y) {
        if (game == null) return;
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderPlayer == null) return;
        for (Housing housing : senderPlayer.getHousings()) {
            for (PurchasedAnimal animal : housing.getOccupants()) {
                if (animal.getName().equals(animalName)) {
                    animal.setInCage(false);
                    animal.setX(x);
                    animal.setY(y);
                    animal.setTargetX(x);
                    animal.setTargetY(y);
                    animal.setMoving(false);
                    animal.setMoveProgress(0f);
                    return;
                }
            }
        }
    }

    public void applyRemotePlantItem(String senderUsername, String seedEnumName, int x, int y) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (game.getMap() == null || !game.getMap().inBounds(x, y)) return;
            Tile targetTile = game.getMap().getTile(x, y);
            com.example.main.models.item.Seed seedToPlant = null;
            try {
                com.example.main.enums.items.ForagingSeedType type = com.example.main.enums.items.ForagingSeedType.valueOf(seedEnumName);
                seedToPlant = new com.example.main.models.item.Seed(type, 1);
            } catch (Exception ignored) {}
            if (seedToPlant != null) {
                controller.plantItem(seedToPlant, targetTile);

                this.gameMap = game.getMap();
            }
        } catch (Exception ignored) {
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteAdvanceTime(String senderUsername, int hours) {
        if (game == null || controller == null) return;
        com.example.main.models.User prevUser = game.getUrrentUser();
        com.example.main.models.Player prevPlayer = game.getCurrentPlayer();
        com.example.main.models.User senderUser = game.getUserByUsername(senderUsername);
        com.example.main.models.Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.changeTime(String.valueOf(hours));
            this.gameMap = game.getMap();
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteAdvanceDate(String senderUsername, int days) {
        if (game == null || controller == null) return;
        com.example.main.models.User prevUser = game.getUrrentUser();
        com.example.main.models.Player prevPlayer = game.getCurrentPlayer();
        com.example.main.models.User senderUser = game.getUserByUsername(senderUsername);
        com.example.main.models.Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.changeDate(days);
            this.gameMap = game.getMap();

            com.example.main.models.User main = game.getMainPlayer();
            if (main != null) {
                com.example.main.enums.design.Weather w = game.getTodayWeather();
                game.setTodayWeather(w);
            }
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteUseTool(String senderUsername, String toolEnumName, int targetX, int targetY) {
        if (game == null || controller == null || game.getMap() == null) return;
        if (!game.getMap().inBounds(targetX, targetY)) return;
        com.example.main.models.User prevUser = game.getUrrentUser();
        com.example.main.models.Player prevPlayer = game.getCurrentPlayer();
        com.example.main.models.User senderUser = game.getUserByUsername(senderUsername);
        com.example.main.models.Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);

            try {
                com.example.main.enums.items.ToolType t = com.example.main.enums.items.ToolType.valueOf(toolEnumName);

                for (com.example.main.models.item.Item it : senderPlayer.getInventory().getItems()) {
                    if (it instanceof com.example.main.models.item.Tool tool && tool.getToolType() == t) {
                        senderPlayer.setCurrentTool(tool);
                        break;
                    }
                }
                if (senderPlayer.getCurrentTool() == null || senderPlayer.getCurrentTool().getToolType() != t) {

                    com.example.main.models.item.Item tmp = com.example.main.models.item.ItemFactory.createItemOrThrow(t.getName(), 1);
                    if (tmp instanceof com.example.main.models.item.Tool) {
                        senderPlayer.setCurrentTool((com.example.main.models.item.Tool) tmp);
                    }
                }
            } catch (Exception ignored) {}
            com.example.main.models.Tile tile = game.getMap().getTile(targetX, targetY);
            controller.useTool(tile);
            this.gameMap = game.getMap();
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCheatLightning(String senderUsername, int x, int y) {
        if (game == null || controller == null || game.getMap() == null) return;
        if (!game.getMap().inBounds(x, y)) return;
        com.example.main.models.User prevUser = game.getUrrentUser();
        com.example.main.models.Player prevPlayer = game.getCurrentPlayer();
        com.example.main.models.User senderUser = game.getUserByUsername(senderUsername);
        com.example.main.models.Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.cheatLightning(String.valueOf(x), String.valueOf(y));
            cheatLightningActive = true;
            lightningDuration = 0.15f;
            this.gameMap = game.getMap();
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCraftItem(String senderUsername, String recipeName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.craftItem(recipeName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCookFood(String senderUsername, String recipeName, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (senderX != null && senderY != null) { senderPlayer.setCurrentX(senderX); senderPlayer.setCurrentY(senderY); }
            controller.cookFood(recipeName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteEat(String senderUsername, String foodName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.eat(foodName);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    private PlacedMachine getPlacedMachineAt(int x, int y) {
        if (game == null || game.getMap() == null) return null;
        if (!game.getMap().inBounds(x, y)) return null;
        Tile t = game.getMap().getTile(x, y);
        return t != null ? t.getPlacedMachine() : null;
    }

    public void applyRemoteStartArtisanProcess(String senderUsername, int machineX, int machineY, String recipeEnumName) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        PlacedMachine machine = getPlacedMachineAt(machineX, machineY);
        if (machine == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            com.example.main.enums.items.ArtisanProductType recipe = null;
            try { recipe = com.example.main.enums.items.ArtisanProductType.valueOf(recipeEnumName); } catch (Exception ignored) {}
            if (recipe != null) {
                controller.startArtisanProcess(machine, recipe);
            }
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCancelArtisanProcess(String senderUsername, int machineX, int machineY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        PlacedMachine machine = getPlacedMachineAt(machineX, machineY);
        if (machine == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.cancelArtisanProcess(machine);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteCollectArtisanProduct(String senderUsername, int machineX, int machineY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        PlacedMachine machine = getPlacedMachineAt(machineX, machineY);
        if (machine == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.collectArtisanProduct(machine);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteFinishArtisanProcessNow(String senderUsername, int machineX, int machineY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        PlacedMachine machine = getPlacedMachineAt(machineX, machineY);
        if (machine == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.finishArtisanProcessNow(machine);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemotePlaceMachine(String senderUsername, String machineEnumName, int tileX, int tileY) {
        if (game == null || controller == null) return;
        if (game.getMap() == null || !game.getMap().inBounds(tileX, tileY)) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);

            com.example.main.enums.items.ItemType maybeType = null;
            try {
                com.example.main.enums.items.CraftingMachineType t = com.example.main.enums.items.CraftingMachineType.valueOf(machineEnumName);
                maybeType = t;
            } catch (Exception ignored) {}
            if (maybeType == null) return;
            com.example.main.models.item.Item item = com.example.main.models.item.ItemFactory.createItemOrThrow(maybeType.getName(), 1);
            if (!(item instanceof com.example.main.models.item.CraftingMachine)) return;
            com.example.main.models.item.CraftingMachine machineToPlaceRemote = (com.example.main.models.item.CraftingMachine) item;
            controller.placeMachine(machineToPlaceRemote, tileX, tileY);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteMoveRandomFoodToRefrigerator(String senderUsername) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.moveRandomFoodToRefrigerator();
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteHug(String senderUsername, String receiverUsername, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (senderX != null && senderY != null) {
                senderPlayer.setCurrentX(senderX);
                senderPlayer.setCurrentY(senderY);
            }
            controller.hug(receiverUsername);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteFlower(String senderUsername, String receiverUsername, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (senderX != null && senderY != null) {
                senderPlayer.setCurrentX(senderX);
                senderPlayer.setCurrentY(senderY);
            }
            controller.flowerSomeone(receiverUsername);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteGift(String senderUsername, String receiverUsername, String itemName, int amount, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);

            controller.giftPlayerRemote(senderUsername, receiverUsername, itemName, String.valueOf(amount));
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteRateGift(String senderUsername, String giftId, int rate) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.rateGift(giftId, String.valueOf(rate));

            if (currentFriendsMenuState == FriendsMenuState.RECEIVED_GIFTS) {
                createFriendsMenuUI();
            }
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteAskMarriage(String senderUsername, String receiverUsername, Integer senderX, Integer senderY) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            if (senderX != null && senderY != null) {
                senderPlayer.setCurrentX(senderX);
                senderPlayer.setCurrentY(senderY);
            }
            controller.askMarriage(receiverUsername);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteRespondMarriage(String senderUsername, String proposerUsername, String response) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.respondToMarriage(response, proposerUsername);
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    public void applyRemoteFinishQuest(String senderUsername, int questId) {
        if (game == null || controller == null) return;
        User prevUser = game.getUrrentUser();
        Player prevPlayer = game.getCurrentPlayer();
        User senderUser = game.getUserByUsername(senderUsername);
        Player senderPlayer = senderUser != null ? senderUser.getPlayer() : null;
        if (senderUser == null || senderPlayer == null) return;
        try {
            game.setCurrentUser(senderUser);
            game.setCurrentPlayer(senderPlayer);
            controller.finishQuest(String.valueOf(questId));
        } finally {
            if (prevUser != null) game.setCurrentUser(prevUser);
            if (prevPlayer != null) game.setCurrentPlayer(prevPlayer);
        }
    }

    private void createPlayerGiftMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Gift Menu", skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        TextButton sendGiftButton = new TextButton("Send Gift", skin);
        TextButton receivedGiftsButton = new TextButton("Received Gifts", skin);
        TextButton giftHistoryButton = new TextButton("Gift History", skin);
        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);


        sendGiftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.SEND_GIFT;
                giftErrorMessage = "";
                showGiftError = false;
                createFriendsMenuUI();
            }
        });

        receivedGiftsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.RECEIVED_GIFTS;
                createFriendsMenuUI();
            }
        });

        giftHistoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.GIFT_HISTORY;
                createFriendsMenuUI();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.PLAYER_ACTIONS;
                createFriendsMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                friendshipResultMessage = "";
                friendshipResultSuccess = false;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });


        friendsMenuTable.add(sendGiftButton).size(200, 50).pad(10);
        friendsMenuTable.add(receivedGiftsButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(giftHistoryButton).size(200, 50).pad(10);
        friendsMenuTable.add(backButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(closeButton).size(200, 50).pad(10).row();
    }

    private void createReceivedGiftsMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Received Gifts", skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        Result result = controller.showGiftsList();
        String giftsText = result.Message();


        receivedGiftsTable = new Table();
        receivedGiftsTable.defaults().expandX().fillX().pad(2);

        if (giftsText.contains("No conversation history") || giftsText.trim().isEmpty()) {
            Label noGiftsLabel = new Label("No gifts received yet!", skin);
            noGiftsLabel.setColor(Color.GRAY);
            receivedGiftsTable.add(noGiftsLabel).row();
        } else {

            String[] giftBlocks = giftsText.split("------------------------");
            for (String block : giftBlocks) {
                String trimmed = block.trim();
                if (!trimmed.isEmpty()) {

                    String giftId = null;
                    for (String line : trimmed.split("\n")) {
                        if (line.startsWith("ID:")) {
                            giftId = line.substring(3).trim();
                            break;
                        }
                    }
                    addGiftToTable(trimmed, giftId);
                }
            }
        }


        receivedGiftsScrollPane = new ScrollPane(receivedGiftsTable, skin);
        receivedGiftsScrollPane.setFadeScrollBars(false);
        receivedGiftsScrollPane.setScrollBarPositions(false, true);

        friendsMenuTable.add(receivedGiftsScrollPane).size(600, 400).pad(10).row();


        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.GIFT_MENU;
                createFriendsMenuUI();
            }
        });

        friendsMenuTable.add(backButton).size(200, 50).pad(10).row();
    }

    private void addGiftToTable(String giftInfo, String giftId) {

        Label giftLabel = new Label(giftInfo, skin);
        giftLabel.setWrap(true);
        giftLabel.setAlignment(Align.left);


        boolean unrated = false;
        for (String line : giftInfo.split("\n")) {
            if (line.trim().equals("Rate: 0")) {
                unrated = true;
                break;
            }
        }

        Table giftTable = new Table();
        giftTable.defaults().expandX().fillX().pad(2);
        giftTable.add(giftLabel).row();

        if (unrated) {

            final TextField ratingField = new TextField("", skin);
            ratingField.setMessageText("Enter rating (1-5)");
            ratingField.setMaxLength(1);
            TextButton rateButton = new TextButton("Rate Gift", skin);
            final String finalGiftId = giftId;
            rateButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String rating = ratingField.getText().trim();
                    if (!rating.isEmpty()) {
                        Result result = controller.rateGift(finalGiftId, rating);
                        if (result.isSuccessful()) {

                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                actionData.put("giftId", finalGiftId);
                                actionData.put("rate", Integer.parseInt(rating));
                                ns.sendPlayerAction("rate_gift", actionData);
                            }

                            currentFriendsMenuState = FriendsMenuState.RECEIVED_GIFTS;
                            createFriendsMenuUI();
                        } else {

                            generalMessageLabel.setText(result.Message());
                            generalMessageLabel.setVisible(true);
                            generalMessageTimer = GENERAL_MESSAGE_DURATION;
                        }
                    }
                }
            });
            giftTable.add(ratingField).size(150, 30).pad(5);
            giftTable.add(rateButton).size(100, 30).pad(5).row();
        }
        receivedGiftsTable.add(giftTable).padBottom(10).row();
    }

    private void createGiftHistoryMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Gift History with " + selectedPlayerForActions, skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        Result result = controller.showGiftHistoryWith(selectedPlayerForActions);
        String giftsText = result.Message();


        giftHistoryTable = new Table();
        giftHistoryTable.defaults().expandX().fillX().pad(2);

        if (giftsText.contains("No conversation history") || giftsText.trim().isEmpty()) {
            Label noGiftsLabel = new Label("No gift history with " + selectedPlayerForActions + "!", skin);
            noGiftsLabel.setColor(Color.GRAY);
            giftHistoryTable.add(noGiftsLabel).row();
        } else {

            String[] lines = giftsText.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                Label giftLabel = new Label(line, skin);
                giftLabel.setWrap(true);
                giftLabel.setAlignment(Align.left);
                giftHistoryTable.add(giftLabel).row();
            }
        }


        giftHistoryScrollPane = new ScrollPane(giftHistoryTable, skin);
        giftHistoryScrollPane.setFadeScrollBars(false);
        giftHistoryScrollPane.setScrollBarPositions(false, true);

        friendsMenuTable.add(giftHistoryScrollPane).size(600, 400).pad(10).row();


        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.GIFT_MENU;
                createFriendsMenuUI();
            }
        });

        friendsMenuTable.add(backButton).size(200, 50).pad(10).row();
    }

    private void createSendGiftMenu() {
        friendsMenuTable.clear();


        Label titleLabel = new Label("Send Gift to " + selectedPlayerForActions, skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();


        if (showGiftError && !giftErrorMessage.isEmpty()) {
            Label errorLabel = new Label(giftErrorMessage, skin);
            errorLabel.setColor(Color.RED);
            friendsMenuTable.add(errorLabel).colspan(2).pad(10).row();
        }


        Result result = controller.showInventoryItems();
        String inventoryText = result.Message();


        sendGiftTable = new Table();
        sendGiftTable.defaults().expandX().fillX().pad(2);

        if (inventoryText.contains("You have no items") || inventoryText.trim().isEmpty()) {
            Label noItemsLabel = new Label("No items in inventory!", skin);
            noItemsLabel.setColor(Color.GRAY);
            sendGiftTable.add(noItemsLabel).row();
        } else {

            String[] lines = inventoryText.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;


                String[] parts = line.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int maxAmount = Integer.parseInt(parts[1].trim());


                    Table itemRow = new Table();
                    itemRow.defaults().expandX().fillX().pad(2);


                    Label itemLabel = new Label(itemName + " x" + maxAmount, skin);
                    itemRow.add(itemLabel).expandX().left().pad(5);


                    TextField amountField = new TextField("1", skin);
                    amountField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
                    itemRow.add(amountField).size(80, 30).pad(5);


                    TextButton giftButton = new TextButton("Gift", skin);
                    giftButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            String amountText = amountField.getText().trim();
                            if (amountText.isEmpty()) {
                                giftErrorMessage = "Please enter an amount";
                                showGiftError = true;
                                createSendGiftMenu();
                                return;
                            }

                            int amount;
                            try {
                                amount = Integer.parseInt(amountText);
                            } catch (NumberFormatException e) {
                                giftErrorMessage = "Invalid amount";
                                showGiftError = true;
                                createSendGiftMenu();
                                return;
                            }

                            if (amount <= 0) {
                                giftErrorMessage = "Amount must be greater than 0";
                                showGiftError = true;
                                createSendGiftMenu();
                                return;
                            }

                            if (amount > maxAmount) {
                                giftErrorMessage = "You don't have enough " + itemName;
                                showGiftError = true;
                                createSendGiftMenu();
                                return;
                            }


                            Result giftResult = controller.giftPlayer(selectedPlayerForActions, itemName, String.valueOf(amount));
                            if (giftResult.isSuccessful()) {

                                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                if (ns != null) {
                                    java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                    actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                    actionData.put("receiverUsername", selectedPlayerForActions);
                                    actionData.put("itemName", itemName);
                                    actionData.put("amount", amount);
                                    actionData.put("senderX", game.getCurrentPlayer().currentX());
                                    actionData.put("senderY", game.getCurrentPlayer().currentY());
                                    ns.sendPlayerAction("gift_player", actionData);
                                }

                                showFriendsMenu = false;
                                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                                selectedPlayerForActions = null;
                                friendshipResultMessage = "";
                                friendshipResultSuccess = false;
                                giftErrorMessage = "";
                                showGiftError = false;
                                if (friendsMenuTable != null) {
                                    friendsMenuTable.remove();
                                    friendsMenuTable = null;
                                }
                                Gdx.input.setInputProcessor(multiplexer);


                                generalMessageLabel.setText(giftResult.Message());
                                generalMessageLabel.setVisible(true);
                                generalMessageTimer = GENERAL_MESSAGE_DURATION;
                            } else {

                                giftErrorMessage = giftResult.Message();
                                showGiftError = true;
                                createSendGiftMenu();
                            }
                        }
                    });
                    itemRow.add(giftButton).size(80, 30).pad(5);

                    sendGiftTable.add(itemRow).row();
                }
            }
        }


        sendGiftScrollPane = new ScrollPane(sendGiftTable, skin);
        sendGiftScrollPane.setFadeScrollBars(false);
        sendGiftScrollPane.setScrollBarPositions(false, true);


        friendsMenuTable.add(sendGiftScrollPane).size(600, 400).pad(10).row();


        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.GIFT_MENU;
                giftErrorMessage = "";
                showGiftError = false;
                createFriendsMenuUI();
            }
        });

        friendsMenuTable.add(backButton).size(200, 50).pad(10).row();
    }

    private void createSellMenuUI() {
        if (sellMenuTable != null) {
            sellMenuTable.remove();
        }

        sellMenuTable = new Table();
        sellMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        sellMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        sellMenuTable.setPosition(
            (Gdx.graphics.getWidth() - sellMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - sellMenuTable.getHeight()) / 2f
        );

        stage.addActor(sellMenuTable);

        Label titleLabel = new Label("Sell Items", skin);
        titleLabel.setFontScale(1.5f);
        sellMenuTable.add(titleLabel).colspan(2).pad(20).row();

        if (showSellError && !sellErrorMessage.isEmpty()) {
            Label errorLabel = new Label(sellErrorMessage, skin);
            errorLabel.setColor(Color.RED);
            sellMenuTable.add(errorLabel).colspan(2).pad(10).row();
        }

        Result result = controller.showInventoryItems();
        String inventoryText = result.Message();

        Table itemsTable = new Table();
        itemsTable.defaults().expandX().fillX().pad(2);

        if (inventoryText.contains("You have no items") || inventoryText.trim().isEmpty()) {
            Label noItemsLabel = new Label("No items in inventory!", skin);
            noItemsLabel.setColor(Color.GRAY);
            itemsTable.add(noItemsLabel).row();
        } else {
            String[] lines = inventoryText.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int maxAmount = Integer.parseInt(parts[1].trim());

                    Table itemRow = new Table();
                    itemRow.defaults().expandX().fillX().pad(2);

                    Label itemLabel = new Label(itemName + " x" + maxAmount, skin);
                    itemRow.add(itemLabel).expandX().left().pad(5);

                    TextField amountField = new TextField("1", skin);
                    amountField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
                    itemRow.add(amountField).size(80, 30).pad(5);

                    TextButton sellButton = new TextButton("Sell", skin);
                    sellButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            String amountText = amountField.getText().trim();
                            if (amountText.isEmpty()) {
                                sellErrorMessage = "Please enter an amount";
                                showSellError = true;
                                createSellMenuUI();
                                return;
                            }

                            int amount;
                            try {
                                amount = Integer.parseInt(amountText);
                            } catch (NumberFormatException e) {
                                sellErrorMessage = "Invalid amount";
                                showSellError = true;
                                createSellMenuUI();
                                return;
                            }

                            if (amount <= 0) {
                                sellErrorMessage = "Amount must be greater than 0";
                                showSellError = true;
                                createSellMenuUI();
                                return;
                            }

                            if (amount > maxAmount) {
                                sellErrorMessage = "You don't have enough " + itemName;
                                showSellError = true;
                                createSellMenuUI();
                                return;
                            }

                            Result sellResult = controller.sell(itemName, String.valueOf(amount));
                            if (sellResult.isSuccessful()) {

                                com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                                if (ns != null) {
                                    java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                    actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                    actionData.put("itemName", itemName);
                                    actionData.put("amount", amount);
                                    actionData.put("x", game.getCurrentPlayer().currentX());
                                    actionData.put("y", game.getCurrentPlayer().currentY());
                                    ns.sendPlayerAction("sell", actionData);
                                }
                                showSellMenu = false;
                                sellErrorMessage = "";
                                showSellError = false;
                                if (sellMenuTable != null) {
                                    sellMenuTable.remove();
                                    sellMenuTable = null;
                                }
                                Gdx.input.setInputProcessor(multiplexer);

                                generalMessageLabel.setText(sellResult.Message());
                                generalMessageLabel.setVisible(true);
                                generalMessageTimer = GENERAL_MESSAGE_DURATION;
                            } else {
                                sellErrorMessage = sellResult.Message();
                                showSellError = true;
                                createSellMenuUI();
                            }
                        }
                    });
                    itemRow.add(sellButton).size(80, 30).pad(5);

                    itemsTable.add(itemRow).row();
                }
            }
        }

        sellMenuScrollPane = new ScrollPane(itemsTable, skin);
        sellMenuScrollPane.setFadeScrollBars(false);
        sellMenuScrollPane.setScrollBarPositions(false, true);

        sellMenuTable.add(sellMenuScrollPane).size(600, 400).pad(10).row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSellMenu = false;
                sellErrorMessage = "";
                showSellError = false;
                if (sellMenuTable != null) {
                    sellMenuTable.remove();
                    sellMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        sellMenuTable.add(closeButton).size(200, 50).pad(10).row();
    }

    private void createMarriageMenu() {
        friendsMenuTable.clear();

        Label titleLabel = new Label("Marriage Menu", skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();

        if (showMarriageError && !marriageErrorMessage.isEmpty()) {
            Label errorLabel = new Label(marriageErrorMessage, skin);
            errorLabel.setColor(Color.RED);
            friendsMenuTable.add(errorLabel).colspan(2).pad(10).row();
        }

        TextButton askMarriageButton = new TextButton("Ask Marriage", skin);
        TextButton respondButton = new TextButton("Respond", skin);
        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        askMarriageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.askMarriage(selectedPlayerForActions);
                if (result.isSuccessful()) {

                    com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                    if (ns != null) {
                        java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                        actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                        actionData.put("receiverUsername", selectedPlayerForActions);
                        actionData.put("senderX", game.getCurrentPlayer().currentX());
                        actionData.put("senderY", game.getCurrentPlayer().currentY());
                        ns.sendPlayerAction("ask_marriage", actionData);
                    }
                    showFriendsMenu = false;
                    currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                    selectedPlayerForActions = null;
                    if (friendsMenuTable != null) {
                        friendsMenuTable.remove();
                        friendsMenuTable = null;
                    }
                    Gdx.input.setInputProcessor(multiplexer);
                } else {
                    marriageErrorMessage = result.Message();
                    showMarriageError = true;
                    createMarriageMenu();
                }
            }
        });

        respondButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.MARRIAGE_RESPOND;
                createFriendsMenuUI();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.PLAYER_ACTIONS;
                marriageErrorMessage = "";
                showMarriageError = false;
                createFriendsMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                marriageErrorMessage = "";
                showMarriageError = false;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });


        friendsMenuTable.add(askMarriageButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(respondButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(backButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(closeButton).size(200, 50).pad(10).row();
    }

    private void createMarriageRespondMenu() {
        friendsMenuTable.clear();

        Label titleLabel = new Label("Marriage Proposals", skin);
        titleLabel.setFontScale(1.5f);
        friendsMenuTable.add(titleLabel).colspan(2).pad(20).row();

        Player currentPlayer = game.getCurrentPlayer();
        ArrayList<Notification> notifications = currentPlayer.getNotifications();
        boolean hasMarriageProposals = false;

        for (Notification notification : notifications) {
            if (notification.getMessage().contains(" has proposed to you!")) {
                hasMarriageProposals = true;
                String proposerName = notification.sender().getUsername();

                Label proposalLabel = new Label(proposerName + " has proposed to you!", skin);
                proposalLabel.setFontScale(1.2f);
                friendsMenuTable.add(proposalLabel).colspan(2).pad(10).row();

                TextButton acceptButton = new TextButton("Accept", skin);
                TextButton rejectButton = new TextButton("Reject", skin);

                acceptButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Result result = controller.respondToMarriage("accept", proposerName);
                        if (result.isSuccessful()) {

                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                actionData.put("proposerUsername", proposerName);
                                actionData.put("response", "accept");
                                ns.sendPlayerAction("respond_marriage", actionData);
                            }
                            removeMarriageProposalNotification(proposerName);
                            showFriendsMenu = false;
                            currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                            selectedPlayerForActions = null;
                            if (friendsMenuTable != null) {
                                friendsMenuTable.remove();
                                friendsMenuTable = null;
                            }
                            Gdx.input.setInputProcessor(multiplexer);
                        } else {
                            generalMessageLabel.setText(result.Message());
                            generalMessageLabel.setVisible(true);
                            generalMessageTimer = GENERAL_MESSAGE_DURATION;
                        }
                    }
                });

                rejectButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Result result = controller.respondToMarriage("reject", proposerName);
                        if (result.isSuccessful()) {

                            com.example.main.service.NetworkService ns = com.example.main.models.App.getInstance().getNetworkService();
                            if (ns != null) {
                                java.util.HashMap<String, Object> actionData = new java.util.HashMap<>();
                                actionData.put("senderUsername", game.getCurrentPlayer().getUsername());
                                actionData.put("proposerUsername", proposerName);
                                actionData.put("response", "reject");
                                ns.sendPlayerAction("respond_marriage", actionData);
                            }
                            removeMarriageProposalNotification(proposerName);
                            showFriendsMenu = false;
                            currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                            selectedPlayerForActions = null;
                            if (friendsMenuTable != null) {
                                friendsMenuTable.remove();
                                friendsMenuTable = null;
                            }
                            Gdx.input.setInputProcessor(multiplexer);
                        } else {
                            generalMessageLabel.setText(result.Message());
                            generalMessageLabel.setVisible(true);
                            generalMessageTimer = GENERAL_MESSAGE_DURATION;
                        }
                    }
                });

                friendsMenuTable.add(acceptButton).size(150, 40).pad(5);
                friendsMenuTable.add(rejectButton).size(150, 40).pad(5).row();
            }
        }

        if (!hasMarriageProposals) {
            Label messageLabel = new Label("No marriage proposals found.", skin);
            messageLabel.setAlignment(Align.center);
            friendsMenuTable.add(messageLabel).colspan(2).pad(20).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentFriendsMenuState = FriendsMenuState.MARRIAGE_MENU;
                createFriendsMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsMenu = false;
                currentFriendsMenuState = FriendsMenuState.MAIN_MENU;
                selectedPlayerForActions = null;
                if (friendsMenuTable != null) {
                    friendsMenuTable.remove();
                    friendsMenuTable = null;
                }
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        friendsMenuTable.add(backButton).size(200, 50).pad(10).row();
        friendsMenuTable.add(closeButton).size(200, 50).pad(10).row();
    }

    private void removeMarriageProposalNotification(String proposerName) {
        Player currentPlayer = game.getCurrentPlayer();
        ArrayList<Notification> notifications = currentPlayer.getNotifications();
        notifications.removeIf(n -> n.sender() != null && n.sender().getUsername().equals(proposerName) && n.getMessage().contains(" has proposed to you!"));
    }

    private void markGiftNotificationForReceiver(String receiverUsername) {
        if (!receiverUsername.equals(game.getCurrentPlayer().getUsername())) {
            unreadGiftNotifications.put(receiverUsername, true);
        }
    }

    private void showTrashConfirmationDialog(Item item) {
        new Dialog("Confirm Deletion", skin, "dialog") {
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    controller.removeItemFromInventory(item.getName(), item.getNumber());
                    showInventoryDisplay(false);
                }
            }
        }.text("Are you sure you want to permanently delete all " + item.getNumber() + "x " + item.getName() + "?")
            .button("Yes, Delete", true)
            .button("No, Cancel", false)
            .key(Input.Keys.ENTER, true)
            .key(Input.Keys.ESCAPE, false)
            .show(inventoryStage);
   }

    private void onRequestMapSnapshot(RequestMapSnapshotEvent event) {
        Gdx.app.postRunnable(() -> {
            System.out.println("[GAMESCREEN] Event received. Creating and sending map snapshot.");
            GameMapSnapshot snapshot = createMapSnapshot();

            Message mapMessage = new Message(new HashMap<>(), MessageType.SEND_GAME_MAP);
            mapMessage.putInBody("gameMapSnapshot", snapshot);


            App.getInstance().getNetworkService().getClient().sendMessage(mapMessage);
        });
    }


    private void onGameMapSync(GameMapSyncEvent event) {
        Gdx.app.postRunnable(() -> {
            System.out.println("[GAMESCREEN] Event received. Initializing map from snapshot.");
            initializeMapFromSnapshot(event.getGameMapSnapshot());

            if (game != null && game.getMap() != null) {
                controller.setMap(game.getMap());
            }
        });
    }

    private void onHostNeedsToSendMap(RequestMapSnapshotEvent event) {
        Gdx.app.postRunnable(() -> {
            System.out.println("[GAMESCREEN - HOST] Event received! Generating the master map...");


            ArrayList<FarmThemes> themes = new ArrayList<>();
            for (User user : game.getPlayers()) {
                if (!user.getUsername().startsWith("empty_slot_")) themes.add(user.getFarmTheme());
            }
            while (themes.size() < 4) themes.add(FarmThemes.Neutral);
            GameMap newMasterMap = new GameMap(game.getPlayers(), themes);
            game.setGameMap(newMasterMap);


            generateRandomMaps();
            System.out.println("[GAMESCREEN - HOST] Visuals generated.");


            GameMapSnapshot snapshot = createMapSnapshot();
            System.out.println("[GAMESCREEN - HOST] Snapshot created.");


            Message mapMessage = new Message(new HashMap<>(), MessageType.SEND_GAME_MAP);
            mapMessage.putInBody("gameMapSnapshot", snapshot);
            App.getInstance().getNetworkService().getClient().sendMessage(mapMessage);
            System.out.println("[GAMESCREEN - HOST] Snapshot sent to server. Setting local map.");


            this.gameMap = newMasterMap;
            this.loadingLabel.setVisible(false);
        });
    }


    private void onGuestNeedsToBuildMap(GameMapSyncEvent event) {
        Gdx.app.postRunnable(() -> {
            System.out.println("[GAMESCREEN - GUEST] Sync event received! Building map from snapshot...");
            initializeMapFromSnapshot(event.getGameMapSnapshot());
            System.out.println("[GAMESCREEN - GUEST] Map built successfully.");
            this.loadingLabel.setVisible(false);
        });
    }

    public void initializeMapFromSnapshot(GameMapSnapshot snapshot) {

        System.out.println("[GAMESCREEN] Initializing map from host's snapshot.");

        int mapWidth = snapshot.tileTypes.length;
        int mapHeight = snapshot.tileTypes[0].length;
        Tile[][] newTiles = new Tile[mapWidth][mapHeight];
        List<User> players = game.getPlayers();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Player owner = null;
                if (x < mapWidth / 2 && y >= mapHeight / 2) { if (players.size() > 0) owner = players.get(0).getPlayer(); }
                else if (x >= mapWidth / 2 && y >= mapHeight / 2) { if (players.size() > 1) owner = players.get(1).getPlayer(); }
                else if (x < mapWidth / 2 && y < mapHeight / 2) { if (players.size() > 2) owner = players.get(2).getPlayer(); }
                else { if (players.size() > 3) owner = players.get(3).getPlayer(); }

                newTiles[x][y] = new Tile(x, y, snapshot.tileTypes[x][y], owner);
            }
        }

        if (game.getMap() == null) {
            game.setGameMap(new GameMap(game.getPlayers(), new ArrayList<>()));
        }
        game.getMap().setTiles(newTiles);

        this.baseGroundMap = snapshot.baseGroundMap;
        this.grassVariantMap = snapshot.grassVariantMap;
        this.treeVariantMap = snapshot.treeVariantMap;
        this.stoneVariantMap = snapshot.stoneVariantMap;
        this.waterVariantMap = snapshot.waterVariantMap;
        this.playerHouseVariants = snapshot.playerHouseVariants;
        this.npcHouseVariants = snapshot.npcHouseVariants;

        this.gameMap = game.getMap();


        rebuildInteractiveElementsAfterSnapshot();
    }

    private void rebuildInteractiveElementsAfterSnapshot() {
        if (game == null || game.getMap() == null) return;
        Tile[][] tiles = game.getMap().getTiles();

        for (ShopType shopType : ShopType.values()) {
            com.example.main.models.building.Shop shop = new com.example.main.models.building.Shop(shopType);
            for (int i = shopType.getCornerX(); i < shopType.getCornerX() + 7; i++) {
                for (int j = shopType.getCornerY(); j < shopType.getCornerY() + 7; j++) {
                    if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
                        Tile t = tiles[i][j];
                        if (t != null && t.getType() == TileType.Shop) {
                            t.setShop(shop);
                        }
                    }
                }
            }
        }


        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                Tile t = tiles[x][y];
                if (t == null) continue;
                if (t.getType() == TileType.Tree && t.getTree() == null) {
                    int variant = (treeVariantMap != null && x < treeVariantMap.length && y < treeVariantMap[0].length)
                        ? treeVariantMap[x][y] : 0;
                    TreeType treeType = switch (variant) {
                        case 1 -> TreeType.Maple;
                        case 2 -> TreeType.Pine;
                        default -> TreeType.Oak;
                    };
                    t.setTree(new Tree(treeType));

                    Fruit growth = new Fruit(treeType.getProduct(), 1);
                    growth.setCurrentStage(1);
                    t.setPlant(growth);
                }

                if (t.getType() == TileType.Planted && t.getPlant() == null) {
                    com.example.main.enums.items.ForagingCropType[] all = com.example.main.enums.items.ForagingCropType.values();
                    int idx = Math.abs((x * 31 + y * 17)) % all.length;
                    com.example.main.enums.items.ForagingCropType cropType = all[idx];
                    com.example.main.models.item.Crop crop = new com.example.main.models.item.Crop(cropType, 1);
                    crop.setReadyToHarvest(true);
                    t.setPlant(crop);
                }

                if (t.getType() == TileType.Shoveled && t.getSeed() == null) {
                    com.example.main.enums.items.ForagingSeedType[] seeds = com.example.main.enums.items.ForagingSeedType.values();

                    com.example.main.enums.items.ForagingSeedType seedType = seeds[Math.abs((x * 13 + y * 7)) % seeds.length];
                    if (seedType.isForaging()) {
                        t.setSeed(new com.example.main.models.item.Seed(seedType, 1));
                    }
                }

                if (t.getItem() == null) {
                    switch (t.getType()) {
                        case CopperStone -> t.setItem(new com.example.main.models.item.Mineral(com.example.main.enums.items.MineralType.Copper_Ore, 1));
                        case IronStone -> t.setItem(new com.example.main.models.item.Mineral(com.example.main.enums.items.MineralType.Iron_Ore, 1));
                        case GoldStone -> t.setItem(new com.example.main.models.item.Mineral(com.example.main.enums.items.MineralType.Gold_Ore, 1));
                        case IridiumStone -> t.setItem(new com.example.main.models.item.Mineral(com.example.main.enums.items.MineralType.Iridium_Ore, 1));
                        case JewelStone -> t.setItem(new com.example.main.models.item.Mineral(com.example.main.enums.items.MineralType.Amethyst, 1));
                        default -> {}
                    }
                }
            }
        }
    }

    public GameMapSnapshot createMapSnapshot() {

        GameMapSnapshot snapshot = new GameMapSnapshot();
        Tile[][] tiles = game.getMap().getTiles();
        snapshot.tileTypes = new TileType[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                snapshot.tileTypes[i][j] = tiles[i][j] != null ? tiles[i][j].getType() : TileType.Earth;
            }
        }
        snapshot.baseGroundMap = this.baseGroundMap;
        snapshot.grassVariantMap = this.grassVariantMap;
        snapshot.treeVariantMap = this.treeVariantMap;
        snapshot.stoneVariantMap = this.stoneVariantMap;
        snapshot.waterVariantMap = this.waterVariantMap;
        snapshot.playerHouseVariants = this.playerHouseVariants;
        snapshot.npcHouseVariants = this.npcHouseVariants;
        return snapshot;
    }

    private void updateEmojiHighlight() {
        for (int i = 0; i < emojiImages.size(); i++) {
            Image image = emojiImages.get(i);
            if (i == selectedEmojiIndex) {

                image.setColor(Color.WHITE);
                image.setScale(0.6f);
            } else {

                image.setColor(Color.GRAY);
                image.setScale(0.5f);
            }
        }
    }

    private void updateScoreboard() {
        if (game == null || game.getPlayers() == null) return;

        List<PlayerScore> playerScores = new ArrayList<>();
        String scoreLabel = "";


        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player == null) continue;

            double score = 0;
            switch (currentSortMode) {
                case BALANCE:
                    scoreLabel = "g";
                    scoreboardTitle.setText("Scoreboard - Balance");
                    if (player.getBankAccount() != null) {
                        score = player.getBankAccount().getBalance();
                    }
                    break;

                case SKILL_EXPERIENCE:
                    scoreLabel = "XP";
                    scoreboardTitle.setText("Scoreboard - Total Experience");
                    if (player.getSkills() != null) {
                        for (SkillData skillData : player.getSkills().values()) {
                            score += skillData.getExperience();
                        }
                    }
                    break;

                case FINISHED_QUESTS:
                    scoreLabel = "Quests";
                    scoreboardTitle.setText("Scoreboard - Quests Completed");
                    score = player.getFinishedQuests();
                    break;
            }
            playerScores.add(new PlayerScore(player.getUsername(), score, scoreLabel));
        }


        playerScores.sort(Comparator.comparingDouble(PlayerScore::getScore).reversed());


        scoreboardDisplayTable.clearChildren();
        int rank = 1;
        for (PlayerScore playerScore : playerScores) {
            scoreboardDisplayTable.add(new Label(rank + ".", skin)).padRight(10);
            scoreboardDisplayTable.add(new Label(playerScore.getPlayerName(), skin)).expandX().left();
            scoreboardDisplayTable.add(new Label(playerScore.getFormattedScore(), skin)).right();
            scoreboardDisplayTable.row();
            rank++;
        }
    }

    private void setupScoreboardUI() {
        scoreboardContainer = new Table(skin);
        scoreboardContainer.setBackground("default-window");
        scoreboardContainer.setVisible(false);
        scoreboardContainer.setSize(400, 500);

        scoreboardContainer.setPosition(
            (Gdx.graphics.getWidth() - scoreboardContainer.getWidth()) / 2,
            (Gdx.graphics.getHeight() - scoreboardContainer.getHeight()) / 2
        );



        inventoryStage.addActor(scoreboardContainer);


        scoreboardTitle = new Label("Scoreboard", skin);
        scoreboardContainer.add(scoreboardTitle).colspan(3).pad(10).row();


        Table buttonTable = new Table();
        TextButton sortByBalanceBtn = new TextButton("Balance", skin);
        sortByBalanceBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentSortMode = SortMode.BALANCE;
                updateScoreboard();
            }
        });

        TextButton sortBySkillsBtn = new TextButton("Skills", skin);
        sortBySkillsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentSortMode = SortMode.SKILL_EXPERIENCE;
                updateScoreboard();
            }
        });

        TextButton sortByQuestsBtn = new TextButton("Quests", skin);
        sortByQuestsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentSortMode = SortMode.FINISHED_QUESTS;
                updateScoreboard();
            }
        });

        buttonTable.add(sortByBalanceBtn).expandX().fillX();
        buttonTable.add(sortBySkillsBtn).expandX().fillX();
        buttonTable.add(sortByQuestsBtn).expandX().fillX();
        scoreboardContainer.add(buttonTable).colspan(3).fillX().padBottom(10).row();


        scoreboardDisplayTable = new Table();
        ScrollPane scrollPane = new ScrollPane(scoreboardDisplayTable, skin);
        scrollPane.setFadeScrollBars(false);
        scoreboardContainer.add(scrollPane).colspan(3).expand().fill().pad(5).row();


        addScoreboardBackButton(scoreboardContainer);
    }

    private void addScoreboardBackButton(Table container) {
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (scoreboardContainer != null) {
                    scoreboardContainer.setVisible(false);
                }
            }
        });

        container.add(backButton).width(150).pad(10);
    }
}
