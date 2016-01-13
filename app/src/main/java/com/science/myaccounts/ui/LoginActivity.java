package com.science.myaccounts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.science.myaccounts.AppManager;
import com.science.myaccounts.MainActivity;
import com.science.myaccounts.R;
import com.science.myaccounts.interfaces.OnGetAvatarListener;
import com.science.myaccounts.utils.AVOSNetUtils;
import com.science.myaccounts.utils.CommonUtil;
import com.science.myaccounts.utils.GlideUtils;
import com.science.myaccounts.utils.Logger;
import com.science.myaccounts.utils.SnackbarUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 登陆界面
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/6
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, OnGetAvatarListener {

    private static String LOG_TAG = LoginActivity.class.getSimpleName();

    private CircleImageView mCircleImageView;
    private MaterialEditText mEditUsername, mEditPassword;
    private TextView mTextRegister, mTextForgetPassword;
    private Button mBtnLogin;
    private CoordinatorLayout mCoordinatorLayout;
    private AVOSNetUtils mAVOSNetUtils;
    private String mStrUsername;

    private int[] mCircleImageColor = {R.color.swipe_blue,
            R.color.swipe_red,
            R.color.swipe_yellow,
            R.color.swipe_green,
            R.color.textColor};
    private int mCircleImageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 允许用户使用应用
            Intent mainIntent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(mainIntent);
            LoginActivity.this.finish();
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            setContentView(R.layout.activity_login);

            mCircleImageView = (CircleImageView) findViewById(R.id.user_gender);
            mEditUsername = (MaterialEditText) findViewById(R.id.username);
            mEditPassword = (MaterialEditText) findViewById(R.id.password);
            mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            mTextRegister = (TextView) findViewById(R.id.quick_register);
            mTextForgetPassword = (TextView) findViewById(R.id.forget_password);
            mBtnLogin = (Button) findViewById(R.id.login);
            mCircleImageView.setOnClickListener(this);
            mTextRegister.setOnClickListener(this);
            mTextForgetPassword.setOnClickListener(this);
            mBtnLogin.setOnClickListener(this);
            mAVOSNetUtils = AVOSNetUtils.getInstance();
            mAVOSNetUtils.setOnGetAvatarListener(this);
            getUserInfo();
            initEditListener();
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {

    }

    private void getUserInfo() {
        //显示已保存的用户名和密码
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        mEditUsername.setText(sharedPreferences.getString("username", null));
        mEditPassword.setText(sharedPreferences.getString("password", null));
        if (!TextUtils.isEmpty(mEditUsername.getText().toString())) {
            getAvatarType(mEditUsername.getText().toString());
        }
    }

    private void initEditListener() {
        mEditUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getAvatarType(mEditUsername.getText().toString());
                }
            }
        });
        mEditUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mCircleImageView.setImageResource(R.mipmap.ic_launcher);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // 用户名还是email登陆
    private void getAvatarType(String username) {
        if (CommonUtil.isEmail(username)) {
            mAVOSNetUtils.getUserEmail(username);
        } else {
            mAVOSNetUtils.getUserAvatar(username);
        }
    }

    @Override
    public void getAvaterListener(String avatarUrl) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = avatarUrl;
        mHandlerLoad.sendMessage(msg);
    }


    // 子线程Handler刷新UI界面
    private Handler mHandlerLoad = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    GlideUtils.getInstance(LoginActivity.this).setImage((String) msg.obj, mCircleImageView);
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;

            case R.id.quick_register:
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;

            case R.id.forget_password:
                Intent intentForget = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intentForget);
                break;

            case R.id.user_gender:
                // 点击头像边框颜色变化
                mCircleImageView.setBorderColorResource(mCircleImageColor[mCircleImageCount]);
                mCircleImageCount++;
                if (mCircleImageCount >= mCircleImageColor.length) {
                    mCircleImageCount = 0;
                }
                break;
        }
    }

    private void login() {
        CommonUtil.hideKeyboard(this, mEditUsername); // SnackBar.show()的时候,要注意先把Keyboard.hide()
        CommonUtil.hideKeyboard(this, mEditPassword); // SnackBar.show()的时候,要注意先把Keyboard.hide()
        mStrUsername = mEditUsername.getText().toString();
        String password = mEditPassword.getText().toString();
        getAvatarType(mStrUsername);
        // 保存账号和密码
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", mStrUsername);
        editor.putString("password", password);
        editor.commit();
        if (!TextUtils.isEmpty(mStrUsername)) {
            if (!TextUtils.isEmpty(password)) {
                loginAVOS(password);
            } else {
                SnackbarUtils.showSnackbar(mCoordinatorLayout, "你还没输入密码哦！");
            }
        } else {
            SnackbarUtils.showSnackbar(mCoordinatorLayout, "你还没输入账号哦！");
        }
    }

    private void loginAVOS(String password) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .title("正在努力登陆中。。")
                .content("请稍后~~")
                .progress(true, 0).cancelable(false)
                .show();
        // 登陆查询
        AVUser.logInInBackground(mStrUsername, password, new LogInCallback() {
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    progressDialog.setContent("登陆成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(mainIntent);
                            LoginActivity.this.finish();
                        }
                    }, 1000);
                } else {
                    progressDialog.setContent("登陆失败，请重新登陆！");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                    Logger.e(LOG_TAG, ">>>>>>>>>>>" + e.toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AppManager.getAppManager().finishAllActivity();
    }
}
