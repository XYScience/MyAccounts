package com.science.myaccounts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.science.myaccounts.AppManager;
import com.science.myaccounts.MainActivity;
import com.science.myaccounts.R;
import com.science.myaccounts.utils.AVService;
import com.science.myaccounts.utils.FileUtil;
import com.science.myaccounts.utils.Logger;
import com.science.myaccounts.utils.SnackbarUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 注册界面
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/7
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static String LOG_TAG = RegisterActivity.class.getSimpleName();
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String IMAGE_FILE_NAME = "avatar.jpg";// 头像文件名称

    private AVService mAVService;
    private CircleImageView mImgUserAvatar;
    private MaterialEditText mEditUsername, mEditEmail, mEditPassword;
    private Button mBtnRegister;
    private CoordinatorLayout mCoordinatorLayout;
    private String mAvaterUrl;
    private boolean isTakeAvatar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mImgUserAvatar = (CircleImageView) findViewById(R.id.user_avatar);
        mEditUsername = (MaterialEditText) findViewById(R.id.username);
        mEditEmail = (MaterialEditText) findViewById(R.id.email);
        mEditPassword = (MaterialEditText) findViewById(R.id.password);
        mBtnRegister = (Button) findViewById(R.id.register);
        mImgUserAvatar.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);

        mAVService = AVService.getInstance();
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
            case R.id.user_avatar:
                getAvatarDialog();
                break;
            case R.id.register:
                register();
                break;
        }
    }

    private void register() {
        final String username = mEditUsername.getText().toString();
        final String email = mEditEmail.getText().toString();
        final String password = mEditPassword.getText().toString();

        if (!TextUtils.isEmpty(username)) {
            if (!TextUtils.isEmpty(email)) {
                if (!TextUtils.isEmpty(password)) {
                    if (isTakeAvatar) {
                        // 保存账号和密码
                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();
                        registerToLeanclound(username, email, password);
                    } else {
                        SnackbarUtils.showSnackbar(mCoordinatorLayout, getString(R.string.error_register_take_gender_null));
                    }
                } else {
                    SnackbarUtils.showSnackbar(mCoordinatorLayout, getString(R.string.error_register_password_null));
                }
            } else {
                SnackbarUtils.showSnackbar(mCoordinatorLayout, getString(R.string.error_register_email_address_null));
            }
        } else {
            SnackbarUtils.showSnackbar(mCoordinatorLayout, getString(R.string.error_register_user_name_null));
        }
    }

    private void registerToLeanclound(final String username, final String email, final String password) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .title("正在努力加载中。。")
                .content("请稍后~~")
                .progress(true, 0).cancelable(false)
                .show();

        final SignUpCallback signUpCallback = new SignUpCallback() {
            public void done(AVException e) {
                if (e == null) {
                    upLoadUserAvatar(progressDialog);
                } else {
                    progressDialog.dismiss();
                    switch (e.getCode()) {
                        case 202:
                            Toast.makeText(
                                    RegisterActivity.this,
                                    getString(R.string.error_register_user_name_repeat),
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 203:
                            Toast.makeText(
                                    RegisterActivity.this,
                                    getString(R.string.error_register_email_repeat),
                                    Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this,
                                    getString(R.string.network_not_connected),
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        };

        AVInstallation.getCurrentInstallation().saveInBackground(
                new SaveCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            // 关联 installationId 到用户表等操作……
                            mAVService.signUp(username, password, email,
                                    AVInstallation.getCurrentInstallation().getInstallationId(),
                                    signUpCallback);
                        } else {
                            // 保存失败，输出错误信息
                            Logger.e(LOG_TAG, ">>>>>>>>>>>>>>>" + e.toString());
                        }
                    }
                });
    }

    // 上传头像
    private void upLoadUserAvatar(final MaterialDialog progressDialog) {
        mAVService.upLoadUserAvatar(AVUser.getCurrentUser().getUsername(), mAvaterUrl, new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressDialog.setContent("注册成功!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(RegisterActivity.this,
                                MainActivity.class);
                        startActivity(mainIntent);
                        AppManager.getAppManager().finishActivity(LoginActivity.class);
                        RegisterActivity.this.finish();
                    }
                }, 1000);
            }
        });
    }

    private void getAvatarDialog() {
        new MaterialDialog.Builder(this).title(R.string.avatar_source).items(R.array.get_avatar).
                itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                Intent intentTakePhotos = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intentTakePhotos.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                                startActivityForResult(intentTakePhotos, PHOTOHRAPH);
                                break;
                            case 1:
                                Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
                                intentGallery.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        IMAGE_UNSPECIFIED);
                                startActivityForResult(intentGallery, PHOTOZOOM);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        switch (requestCode) {
            case PHOTOHRAPH:// 拍照
                // 设置文件保存路径这里放在跟目录下
                File picture = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(picture));
                break;
            case PHOTOZOOM:// 读取相册缩放图片
                startPhotoZoom(data.getData());
                break;
            case PHOTORESOULT: // 取得裁剪后的图片
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃 当前功能时，会报NullException
                 */
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap avaterBitmap = extras.getParcelable("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            avaterBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);// (0 - 100)压缩文件
            mImgUserAvatar.setImageBitmap(avaterBitmap);

            mAvaterUrl = FileUtil.saveFile(RegisterActivity.this,
                    IMAGE_FILE_NAME, avaterBitmap);

            isTakeAvatar = true;

            // 压缩图片
            // BitmapFactory.Options option = new BitmapFactory.Options();
            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
            // option.inSampleSize = 2;
            // 根据图片的SDCard路径读出Bitmap
            // genderBitmap = BitmapFactory.decodeFile(avaterUrl, option);

            Toast.makeText(RegisterActivity.this,
                    "头像保存在:" + mAvaterUrl.replaceAll(IMAGE_FILE_NAME, ""),
                    Toast.LENGTH_LONG).show();

            // 新线程后台上传服务端
            // new Thread(uploadImageRunnable).start();
        }
    }
}
