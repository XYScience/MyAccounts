package com.science.myaccounts.utils;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/12
 */
public class SnackbarUtils {

    public static void showSnackbar(CoordinatorLayout coordinatorLayout, String msg) {

        final Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static void showSnackbar(CoordinatorLayout coordinatorLayout, String msg, int length) {

        final Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, length);

        snackbar.show();
    }
}
