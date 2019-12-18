package com.hyd.htalker.factory.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘显示与隐藏
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class KeyBoardUtil {
    /**
     * 强制显示
     */
    public static void showKeyBoard(final EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);//强制显示键盘
            }
        }, 0);

    }
    /**
     * 强制隐藏
     */
    public static void hideKeyboard(final EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
            }
        }, 0);
    }
}
