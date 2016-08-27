package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 8/25/16.
 */

@Database(version=1, retainDataOnUpgrade=false)
public class Action extends RemoteModel<Action> {
    public static class Query extends RemoteModel.Query<Action> {
        public Query(Context context) {
            super(Action.class, context);
            url("https://www.villageoflies.com/action");
        }

        public Action.Query id(int id) {
            where(new Param("id", id));
            return this;
        }

        public Action.Query stage_id(int stageId) {
            where(new Param("stage_id", stageId));
            return this;
        }

        public Action.Query user_player_id(int userPlayerId) {
            where(new Param("user_player_id", userPlayerId));
            return this;
        }

        public Action.Query target_player_id(int targetPlayerId) {
            where(new Param("target_player_id", targetPlayerId));
            return this;
        }

        public Action.Query ability(String ability) {
            where(new Param("ability", ability));
            return this;
        }
    }

    private int id;
    private int stage_id;
    private int user_player_id;
    private int target_player_id;
    private String ability;

    public Action(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public int getStageId() {
        return stage_id;
    }

    public int getUserPlayerId() {
        return user_player_id;
    }

    public int getTargetPlayerId() {
        return target_player_id;
    }

    public String getAbility() {
        return ability;
    }
}