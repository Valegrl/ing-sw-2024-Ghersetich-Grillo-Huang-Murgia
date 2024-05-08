package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Token;

public abstract class AbstractViewPlayer {
    private final String username;
    private Token token;
    private final boolean online;

    public AbstractViewPlayer(Player player) {
        this.username = player.getUsername();
        this.token = player.getToken();
        this.online = player.isOnline();
    }

    public String getUsername() {
        return username;
    }

    public Token getToken() {
        return token;
    }

    public boolean isOnline() {
        return online;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
