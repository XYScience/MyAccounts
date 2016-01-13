package com.science.myaccounts.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.science.myaccounts.R;
import com.science.myaccounts.utils.CommonUtil;
import com.science.myaccounts.utils.SnackbarUtils;

/**
 * @author 幸运Science-陈土燊
 * @description 忘记密码
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/25
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private View mViewToolbarShadow;
    private MaterialEditText mEditEmail;
    private Button mBtnSend;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_forget_password);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewToolbarShadow = findViewById(R.id.toolbar_shadow);
        setToolbar(mToolbar, "找回密码", mViewToolbarShadow);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mEditEmail = (MaterialEditText) findViewById(R.id.email);
        mBtnSend = (Button) findViewById(R.id.send);
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                send();
                break;
        }
    }

    private void send() {
        String email = mEditEmail.getText().toString();
        if (!TextUtils.isEmpty(email)) {
            AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        // 已发送一份重置密码的指令到用户的邮箱
                        CommonUtil.hideKeyboard(ForgetPasswordActivity.this, mEditEmail);
                        SnackbarUtils.showSnackbar(mCoordinatorLayout, getResources().
                                getString(R.string.password_reset_success));
                    } else {
                        // 重置密码出错。
                        CommonUtil.hideKeyboard(ForgetPasswordActivity.this, mEditEmail);
                        SnackbarUtils.showSnackbar(mCoordinatorLayout, getResources().
                                getString(R.string.password_reset_error));
                    }
                }
            });
        } else {
            CommonUtil.hideKeyboard(ForgetPasswordActivity.this, mEditEmail);
            SnackbarUtils.showSnackbar(mCoordinatorLayout, getResources().
                    getString(R.string.error_register_email_address_null));
        }
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
