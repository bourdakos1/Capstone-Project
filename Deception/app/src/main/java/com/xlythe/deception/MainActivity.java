package com.xlythe.deception;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MessageAdapter.MessageViewHolder.ClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private AppBarLayout mAppbar;
    private ExtendedEditText mEditText;
    private ImageView mSendButton;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;

    private ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAppbar = (AppBarLayout) findViewById(R.id.appbar);
        mEditText = (ExtendedEditText) findViewById(R.id.edit_text);
        mSendButton = (ImageView) findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(null);
                setSendable(false);
            }
        });

        mEditText.setOnDismissKeyboardListener(new ExtendedEditText.OnDismissKeyboardListener() {
            @Override
            public void onDismissed() {
                mEditText.clearFocus();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSendable(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        // maybe add transcript mode, and show a notification of new messages
        mRecyclerView.setLayoutManager(layoutManager);
        
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i< 30; i++) {
            messages.add(new Message());
        }

        mAdapter = new MessageAdapter(this, messages);
        mAdapter.setOnClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState > 0) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    mEditText.clearFocus();
                }
            }
        });
    }

    public void setSendable(boolean sendable){
        mSendButton.setEnabled(sendable);
        if (sendable) {
            mSendButton.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        } else {
            mSendButton.clearColorFilter();
        }
    }


    @Override
    public void onItemClicked(Message message) {
        if (mActionMode != null) {
            toggleSelection(message);
        }
    }

    @Override
    public boolean onItemLongClicked(Message message) {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(mActionModeCallback);
        }
        toggleSelection(message);
        return true;
    }

    private void toggleSelection(Message message) {
        mAdapter.toggleSelection(message);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(String.valueOf(count)+" selected");
            mActionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.menu_main, menu);
            menu.findItem(R.id.menu_remove).setVisible(true);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mRecyclerView.setNestedScrollingEnabled(false);
            mAppbar.setExpanded(true);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Set<Message> texts = mAdapter.getSelectedItems();
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    // TODO: delete
                    mAdapter.clearSelection();
                    mode.finish();
                    return true;
                case R.id.menu_copy:
                    String copy = "";
                    for (Message message : texts) {
                        if (!copy.isEmpty()) {
                            copy += "\n";
                        }
                        copy += message.getBody();
                    }
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple message", copy);
                    clipboard.setPrimaryClip(clip);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode.finish();
            mAdapter.clearSelection();
            mActionMode = null;
            mRecyclerView.setNestedScrollingEnabled(true);
        }
    }
}