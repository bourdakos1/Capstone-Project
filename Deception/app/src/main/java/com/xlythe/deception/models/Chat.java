package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 8/25/16.
 */

@Database(version=1, retainDataOnUpgrade=false)
public class Chat extends RemoteModel<Chat> {
    public static class Query extends RemoteModel.Query<Chat> {
        public Query(Context context) {
            super(Chat.class, context);
            url("https://www.villageoflies.com/chat");
        }

        public Chat.Query id(int id) {
            where(new Param("id", id));
            return this;
        }

        public Chat.Query stageId(int stageId) {
            where(new Param("stage_id", stageId));
            return this;
        }

        public Chat.Query playerId(int playerId) {
            where(new Param("player_id", playerId));
            return this;
        }

        public Chat.Query message(String message) {
            where(new Param("message", message));
            return this;
        }

        public Chat.Query timestamp(int timestamp) {
            where(new Param("timestamp", timestamp));
            return this;
        }

        public Chat.Query audioUrl(String audioUrl) {
            where(new Param("audio_url", audioUrl));
            return this;
        }
    }

    private int id;
    private int stage_id;
    private int player_id;
    private String message;
    private int timestamp;
    private String audio_url;

    public Chat(Context context) {
        super(context);
    }

    public int getId() {
        return id;
    }

    public int getStageId() {
        return stage_id;
    }

    public int getPlayerId() {
        return player_id;
    }

    public String getMessage() {
        return message;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getAudio_url() {
        return audio_url;
    }
}
