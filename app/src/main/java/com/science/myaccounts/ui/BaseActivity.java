package com.science.myaccounts.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.science.myaccounts.AppManager;
import com.science.myaccounts.utils.CommonUtil;
import com.science.myaccounts.utils.StatusBarCompat;

/**
 * @author 幸运Science-陈土燊
 * @description 基类
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/8/27
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);
        // 设置全屏模式SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        initView();
        // 状态栏着色 4.4之后加入windowTranslucentStatus的属性之后，也就是我们可以用到状态栏的区域了
        StatusBarCompat.compat(this);
        initData();
        initListener();
    }

    public void setToolbar(Toolbar toolbar, String title, View toolbarShadow) {
        toolbar.setTitle(title); // 标题的文字需在setSupportActionBar之前，不然会无效;getSupportActionBar().setTitle("标题");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回键
        setToolbarShadow(toolbarShadow); // toolbar阴影
    }

    // toolbar阴影
    public void setToolbarShadow(View viewToolbarShadow) {
        if (!CommonUtil.isAndroid5()) {
            viewToolbarShadow.setVisibility(View.VISIBLE);
        }
    }

    // 去除toolbar阴影
    private void wipeToolbarShadow(View viewToolbarShadow) {
        if (!CommonUtil.isAndroid5()) {
            viewToolbarShadow.setVisibility(View.GONE);
        }
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();
}
