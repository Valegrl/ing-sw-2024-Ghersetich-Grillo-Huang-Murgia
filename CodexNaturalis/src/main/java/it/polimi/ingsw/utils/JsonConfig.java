package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * This class is a singleton that provides access to the configuration parameters of the application.
 * The configuration parameters are loaded from a JSON file.
 */
public class JsonConfig {
    private static JsonConfig instance;
    private static final String DEFAULT_CONFIG_PATH = "it/polimi/ingsw/json/config.json";
    private int serverSocketPort;
    private int rmiRegistryPort;
    private String rmiRegistryName;
    private int pingTimeoutMs;
    private int maxUsernameLength;
    private int maxLobbySize;
    private int maxLobbyIdLength;
    private int maxGlobalChatSize;
    private int maxPrivateChatSize;
    private int setupCardsTimerMs;
    private int setupTokensTimerMs;
    private int playerActionTimerMs;
    private int waitingForPlayersTimerMs;
    private int handSize;
    private int handResourceCards;
    private int handGoldCards;

    /**
     * Private constructor to prevent instantiation.
     */
    private JsonConfig() { }

    /**
     * Returns the singleton instance of this class.
     * If the instance does not exist, it is created.
     *
     * @return The singleton instance of this class.
     */
    public static synchronized JsonConfig getInstance() {
        if (instance == null) {
            instance = new JsonConfig();
        }
        return instance;
    }

    /**
     * Loads the configuration parameters from a JSON file.
     * If the file is not found, an IllegalArgumentException is thrown.
     */
    public static synchronized void loadConfig() {
        InputStream resource = JsonConfig.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found");
        }
        try (Reader reader = new InputStreamReader(resource)) {
            Gson gson = new GsonBuilder().create();
            instance = gson.fromJson(reader, JsonConfig.class);
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
        }
    }

    public int getServerSocketPort() {
        return serverSocketPort;
    }

    public int getRmiRegistryPort() {
        return rmiRegistryPort;
    }

    public String getRmiRegistryName() {
        return rmiRegistryName;
    }

    public int getPingTimeoutMs() {
        return pingTimeoutMs;
    }

    public int getMaxUsernameLength() {
        return maxUsernameLength;
    }

    public int getMaxLobbySize() {
        return maxLobbySize;
    }

    public int getMaxLobbyIdLength() {
        return maxLobbyIdLength;
    }

    public int getMaxGlobalChatSize() {
        return maxGlobalChatSize;
    }

    public int getMaxPrivateChatSize() {
        return maxPrivateChatSize;
    }

    public int getSetupCardsTimerMs() {
        return setupCardsTimerMs;
    }

    public int getSetupTokensTimerMs() {
        return setupTokensTimerMs;
    }

    public int getPlayerActionTimerMs() {
        return playerActionTimerMs;
    }

    public int getWaitingForPlayersTimerMs() {
        return waitingForPlayersTimerMs;
    }

    public int getHandSize() {
        return handSize;
    }

    public int getHandResourceCards() {
        return handResourceCards;
    }

    public int getHandGoldCards() {
        return handGoldCards;
    }
}

