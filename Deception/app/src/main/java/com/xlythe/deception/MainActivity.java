package com.xlythe.deception;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Keyboard hack
    private int mScreenSize;
    private int mKeyboardSize;
    private boolean mAdjustNothing;
    private boolean mKeyboardOpen;

    private Toolbar mToolbar;
    private ExtendedEditText mEditText;
    private ImageView mSendButton;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private ProgressBar mProgressBar;
    private View mAttachView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (ExtendedEditText) findViewById(R.id.edit_text);
        mSendButton = (ImageView) findViewById(R.id.send);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAttachView = findViewById(R.id.fragment_container);
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
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    mEditText.clearFocus();
                }
            }
        });
    }

    public void initKeyboard() {
        final Window rootWindow = getWindow();
        final View root = rootWindow.getDecorView().findViewById(android.R.id.content);

        // Seems redundant to set as ADJUST_NOTHING in manifest and then immediately to ADJUST_RESIZE
        // but it seems that the input gets reset to a default on keyboard dismissal if not set otherwise.
        rootWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect r = new Rect();
                View view = rootWindow.getDecorView();
                view.getWindowVisibleDisplayFrame(r);
                if (mScreenSize != 0 && mScreenSize > r.bottom) {
                    mKeyboardSize = mScreenSize - r.bottom;
                    mAttachView.getLayoutParams().height = mKeyboardSize;
                    rootWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    if (mKeyboardOpen) {
                        mAttachView.setVisibility(View.VISIBLE);
                    }
                    mAdjustNothing = true;
                } else {
                    mScreenSize = r.bottom;
                }
            }
        });

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

        mEditText.setOnDismissKeyboardListener(new ExtendedEditText.OnDismissKeyboardListener() {
            @Override
            public void onDismissed() {
                mEditText.clearFocus();
                onAttachmentHidden();
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

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mKeyboardOpen = hasFocus;
                if (hasFocus) {
                    if (!mAdjustNothing) {
                        onAttachmentHidden();
                    } else {
                        // Just because it has focus doesnt mean the keyboard actually opened
                        // This seems like an easier fix than not showing the attachview
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(mEditText, 0);
                        mAttachView.setVisibility(View.VISIBLE);
                    }
                }
            }
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

    public void onAttachmentClicked(View view){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

        mEditText.clearFocus();
        mAttachView.setVisibility(View.VISIBLE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, VoteFragment.newInstance("hi1", "bye2")).commit();
    }

    public void onAttachmentHidden(){
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (activeFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(activeFragment).commit();
        }
        mAttachView.setVisibility(View.GONE);
    }

    public void send(){
        //TODO: finish this method
    }
}