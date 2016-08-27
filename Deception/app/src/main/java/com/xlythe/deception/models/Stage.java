package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 8/25/16.
 */

@Database(version=1, retainDataOnUpgrade=false)
public class Stage extends RemoteModel<Stage> {
    public static class Query extends RemoteModel.Query<Stage> {
        public Query(Context context) {
            super(Stage.class, context);
            url("https://www.villageoflies.com/stage");
        }

        public Stage.Query id(int id) {
            where(new Param("id", id));
            return this;
        }

        public Stage.Query gameId(int gameId) {
            where(new Param("game_id", gameId));
            return this;
        }

        public Stage.Query name(String name) {
            where(new Param("name", name));
            return this;
        }

        public Stage.Query position(int position) {
            where(new Param("position", position));
            return this;
        }

        public Stage.Query startTime(int startTime) {
            where(new Param("start_time", startTime));
            return this;
        }

        public Stage.Query length(int length) {
            where(new Param("length", length));
            return this;
        }

        public Stage.Query allowedAbilities(int allowedAbilities) {
            where(new Param("allowed_abilities", allowedAbilities));
            return this;
        }

        public Stage.Query active(boolean active) {
            where(new Param("active", active));
            return this;
        }
    }

    private int id;
    private int game_id;
    private String name;
    private int position;
    private int start_time;
    private int length;
    private String allowed_abilities;
    private boolean active;

    public Stage(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public int getGameId() {
        return game_id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public int getStartTime() {
        return start_time;
    }

    public int getLength() {
        return length;
    }

    public String getAllowedAbilities() {
        return allowed_abilities;
    }

    public boolean getActive() {
        return active;
    }
}
