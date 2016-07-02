package com.xlythe.deception;

import android.content.Context;

import com.xlythe.dao.Database;
import com.xlythe.dao.RemoteModel;

/**
 * Created by Niko on 7/2/16.
 */
@Database(version=1, retainDataOnUpgrade=false)
public class User extends RemoteModel<User> {
    private String mUsername;
    private String mPassword;
    private String mName;
    private String mEmail;

    public User(Context context) {
        super(context);
    }
}