package com.xlythe.extendedkeyboard;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Niko on 7/6/16.
 */
public class EditText extends android.widget.EditText {
    private FragmentActivity mContext;
    private View mAttachView;
    private int mScreenSize;
    private int mKeyboardSize;
    private boolean mAdjustNothing;
    private boolean mKeyboardOpen;
    private int mFragmentContainer;

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initKeyboard(context);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initKeyboard(context);
    }

    public EditText(Context context) {
        super(context);
        initKeyboard(context);
    }

    private void initKeyboard(Context context) {
        mContext = ((FragmentActivity) context);
        // TODO: doesn't work
//        mFragmentContainer = mContext.getResources().getIdentifier("fragment_container", "id", mContext.getPackageName());
//        mAttachView = mContext.findViewById(mFragmentContainer);
        final Window rootWindow = mContext.getWindow();
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
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            super.clearFocus();
            hideFragment();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        mKeyboardOpen = focused;
        if (focused) {
            if (!mAdjustNothing) {
                hideFragment();
            } else {
                // Just because it has focus doesnt mean the keyboard actually opened
                // This seems like an easier fix than not showing the attachview
                InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(this, 0);
                mAttachView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showFragment(Fragment fragment){
        clearFocus();

        mAttachView.setVisibility(View.VISIBLE);

        FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
        transaction.replace(mAttachView.getId(), fragment).commit();
    }

    public void hideFragment() {
        Fragment activeFragment = mContext.getSupportFragmentManager().findFragmentById(mAttachView.getId());
        if (activeFragment != null) {
            mContext.getSupportFragmentManager().beginTransaction().remove(activeFragment).commit();
        }
        mAttachView.setVisibility(View.GONE);
    }

    public void setContainer(int id){
        mFragmentContainer = id;
        mAttachView = mContext.findViewById(mFragmentContainer);
    }

    @Override
    public void clearFocus() {
        InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(getWindowToken(), 0);
        super.clearFocus();
    }
}