package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 8/25/16.
 */

@Database(version=1, retainDataOnUpgrade=false)
public class Player extends RemoteModel<Player> {
    public static class Query extends RemoteModel.Query<Player> {
        public Query(Context context) {
            super(Player.class, context);
            url("https://www.villageoflies.com/player");
        }

        public Player.Query id(int id) {
            where(new Param("id", id));
            return this;
        }

        public Player.Query userId(int userId) {
            where(new Param("user_id", userId));
            return this;
        }

        public Player.Query gameId(int gameId) {
            where(new Param("game_id", gameId));
            return this;
        }

        public Player.Query name(String name) {
            where(new Param("name", name));
            return this;
        }

        public Player.Query image(String image) {
            where(new Param("image", image));
            return this;
        }

        public Player.Query alive(boolean alive) {
            where(new Param("alive", alive));
            return this;
        }

        public Player.Query role(String role) {
            where(new Param("role", role));
            return this;
        }

        public Player.Query abilities(String abilities) {
            where(new Param("abilities", abilities));
            return this;
        }
    }

    private int id;
    private int user_id;
    private int game_id;
    private String name;
    private String image;
    private boolean alive;
    private String role;
    private String abilities;

    public Player(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getGameId() {
        return game_id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean getAlive() {
        return alive;
    }

    public String getRole() {
        return role;
    }

    public String getAbilities() {
        return abilities;
    }
}