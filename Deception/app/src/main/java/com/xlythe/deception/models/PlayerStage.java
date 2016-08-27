package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 8/25/16.
 */

@Database(version=1, retainDataOnUpgrade=false)
public class PlayerStage extends RemoteModel<PlayerStage> {
    public static class Query extends RemoteModel.Query<PlayerStage> {
        public Query(Context context) {
            super(PlayerStage.class, context);
            url("https://www.villageoflies.com/player_stage");
        }

        public PlayerStage.Query id(int id) {
            where(new Param("id", id));
            return this;
        }

        public PlayerStage.Query playerId(int playerId) {
            where(new Param("player_id", playerId));
            return this;
        }

        public PlayerStage.Query stageId(int stageId) {
            where(new Param("stage_id", stageId));
            return this;
        }

        public PlayerStage.Query activeAbilities(String activeAbilities) {
            where(new Param("active_abilities", activeAbilities));
            return this;
        }

        public PlayerStage.Query maxAbilityUses(String maxAbilityUses) {
            where(new Param("max_ability_uses", maxAbilityUses));
            return this;
        }
    }

    private int id;
    private int player_id;
    private int stage_id;
    private String active_abilities;
    private String max_ability_uses;

    public PlayerStage(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return player_id;
    }

    public int getStageId() {
        return stage_id;
    }

    public String getActiveAbilities() {
        return active_abilities;
    }

    public String getMaxAbilityUses() {
        return max_ability_uses;
    }
}