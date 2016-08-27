package com.xlythe.deception;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.xlythe.dao.RemoteModel;
import com.xlythe.deception.models.Game;
import com.xlythe.deception.models.User;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Game.Query(getApplicationContext()).all(new RemoteModel.Callback<List<Game>>() {
            @Override
            public void onSuccess(List<Game> games) {
                mRecyclerView.setAdapter(new GameAdapter(getApplicationContext(), games));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new Game.Query(getApplicationContext()).name("Nicks a b3ast").minPlayers(3).maxPlayers(10).type("MAFIA").insert(new RemoteModel.Callback<Game>() {
                    @Override
                    public void onSuccess(Game game) {
                        Snackbar.make(view, "New game created", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });
    }

}
