package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;
import com.xlythe.dao.Unique;

/**
 * Created by Niko on 8/23/16.
 */

@Database(version=2, retainDataOnUpgrade=false)
public class Game extends RemoteModel<Game> {
    public static class Query extends RemoteModel.Query<Game> {
        public Query(Context context) {
            super(Game.class, context);
            url("https://www.villageoflies.com/game");
        }

        public Game.Query id(int id) {
            where(new Param("id", id));
            return this;
        }

        public Game.Query name(String name) {
            where(new Param("name", name));
            return this;
        }

        public Game.Query active(boolean active) {
            where(new Param("active", active));
            return this;
        }

        public Game.Query minPlayers(int minPlayers) {
            where(new Param("min_players", minPlayers));
            return this;
        }

        public Game.Query maxPlayers(int maxPlayers) {
            where(new Param("max_players", maxPlayers));
            return this;
        }

        public Game.Query type(String type) {
            where(new Param("type", type));
            return this;
        }
    }

    private int id;
    private String name;
    private boolean active;
    private int min_players;
    private int max_players;
    private String type;

    public Game(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getActive() {
        return active;
    }

    public int getMinPlayers() {
        return min_players;
    }

    public int getMaxPlayer() {
        return max_players;
    }

    public String getType() {
        return type;
    }
}