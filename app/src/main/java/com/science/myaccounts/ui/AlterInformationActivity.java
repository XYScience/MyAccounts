package com.science.myaccounts.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.science.myaccounts.R;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/9/28
 */
public class AlterInformationActivity extends BaseActivity {

    private Toolbar mToolbar;
    private View mViewToolbarShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_alter_information);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewToolbarShadow = findViewById(R.id.toolbar_shadow);
        setToolbar(mToolbar, "修改资料", mViewToolbarShadow);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
