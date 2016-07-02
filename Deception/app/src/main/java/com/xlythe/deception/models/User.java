package com.xlythe.deception.models;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.Param;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 7/2/16.
 */
@Database(version=1, retainDataOnUpgrade=false)
public class User extends RemoteModel<User> {
    public static class Query extends RemoteModel.Query<User> {
        public Query(Context context) {
            super(User.class, context);
        }

        public User.Query username(String username) {
            where(new Param("username", username));
            return this;
        }

        public User.Query password(String password) {
            where(new Param("password", password));
            return this;
        }
    }

    private String username;
    private String password;
    private String name;
    private String email;

    public User(Context context) {
        super(context);
    }
}