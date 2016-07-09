package com.xlythe.deception;

import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xlythe.swap.SwapEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private SwapEditText mEditText;
    private ImageView mSendButton;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (SwapEditText) findViewById(R.id.edit_text);
        mSendButton = (ImageView) findViewById(R.id.send);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        setSupportActionBar(mToolbar);
        initToolbar();
        initKeyboard();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i< 30; i++) {
            messages.add(new Message());
        }

        mAdapter = new MessageAdapter(this, messages);
        mRecyclerView.setAdapter(mAdapter);

        // Dismiss keyboard when scrolling
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState > 0) {
                    mEditText.hideKeyboard();
                }
            }
        });
    }

    public void initKeyboard() {
        setSendable(false);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mSendButton.isEnabled()) {
                        send();
                    }
                    return true;
                }
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSendable(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void initToolbar() {
        int statusHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        int width = mProgressBar.getLayoutParams().width;
        int height = mProgressBar.getLayoutParams().height + statusHeight;
        mProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        mToolbar.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        mToolbar.setPadding(0, statusHeight, 0, 0);

        mProgressBar.setMax(10000);
        ProgressBarAnimator animator = new ProgressBarAnimator(mProgressBar);
        animator.setDuration(10000).start();

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
    }

    /**
     * Animator for controlling toolbar progress animation
     */
    public class ProgressBarAnimator extends ValueAnimator {
        public ProgressBarAnimator(final ProgressBar progressBar) {
            setInterpolator(new LinearInterpolator());
            setFloatValues(0f, 1f);
            addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    progressBar.setProgress((int) ((1 - percent) * progressBar.getMax()));
                }
            });
        }
    }

    /**
     * Updates the send button ui
     * TODO: Should switch out setting button as disabled with dagger icon
     */
    public void setSendable(boolean sendable){
        mSendButton.setEnabled(sendable);
        if (sendable) {
            mSendButton.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        } else {
            mSendButton.clearColorFilter();
        }
    }

    public void send(){
        //TODO: finish this method
        mEditText.showFragment(VoteFragment.newInstance("hi1", "bye2"));
    }

    @Override
    public void onBackPressed() {
        if (mEditText.getFragmentVisibility()) {
            mEditText.hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}