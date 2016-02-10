package com.science.myaccounts.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.science.myaccounts.R;
import com.science.myaccounts.adapter.UserInfoAdapter;
import com.science.myaccounts.utils.ImageUtils;

/**
 * @author 幸运Science-陈土燊
 * @description 用户资料
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/9/24
 */
public class UserInfoActivityTest extends BaseActivity {

    private AppBarLayout mAppBarLayout;

    private ImageView mImageBlurred;
    private ImageView mImageNonBlur;

    private ListView mListView;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user_info);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        setToolbar("我的");

        mImageBlurred = (ImageView) findViewById(R.id.blured_image);
        mImageNonBlur = (ImageView) findViewById(R.id.orginal_image);

    }

    @Override
    public void initData() {
        loadBlurredImage();
        initList();
    }

    @Override
    public void initListener() {
        listenToScroll();
    }

    private void loadBlurredImage() {
        ImageUtils.getBlurredImage(this, R.mipmap.user_info_default_boy,
                ImageUtils.getImageName(R.mipmap.user_info_default_boy), 20,
                new ImageUtils.BlurEffectListener() {
                    @Override
                    public void onDone(Bitmap bitmap) {
                        mImageBlurred.setImageBitmap(bitmap);
                    }
                });
    }

    private void initList() {
        mListView = (ListView) findViewById(R.id.list);
        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        mListView.setAdapter(new UserInfoAdapter(this, bitmap));

        mScrollView = (ScrollView) findViewById(R.id.bgScrollView);
    }

    @SuppressLint("NewApi")
    public void setTitleAlpha(float val) {

        mAppBarLayout.setAlpha(val);
    }

    @SuppressLint("NewApi")
    private void listenToScroll() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                View v = view.findViewById(R.id.id0);
                if (v != null) {
                    int scrollY = -v.getTop();

                    if (scrollY < 512 && scrollY >= 0) {

                        float val = 0;
                        val = scrollY * (1f / 512f);
                        mImageBlurred.setAlpha(val);
                        setTitleAlpha(val);
                    }
                    if (scrollY < 400 && scrollY >= 0) {
                        mScrollView.scrollTo(0, scrollY / 3);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.alter_information:
                Intent intent = new Intent(UserInfoActivityTest.this, AlterInformationActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
