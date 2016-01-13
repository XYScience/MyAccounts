package com.science.myaccounts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.science.myaccounts.fragment.CollectFragment;
import com.science.myaccounts.fragment.CommunicationFragment;
import com.science.myaccounts.fragment.DiscoverFragment;
import com.science.myaccounts.fragment.FocusFragment;
import com.science.myaccounts.interfaces.OnGetAvatarListener;
import com.science.myaccounts.ui.BaseActivity;
import com.science.myaccounts.ui.UserInfoActivity;
import com.science.myaccounts.utils.AVOSNetUtils;
import com.science.myaccounts.utils.GlideUtils;
import com.science.myaccounts.utils.ToastUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnGetAvatarListener {

    private Toolbar mToolbar;
    private View mViewToolbarShadow;
    private DrawerLayout mDrawerLayout;
    private View mHeaderView;
    private CircleImageView mImgUser;
    private TextView mTextSignature;
    private AVOSNetUtils mAVOSNetUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        AVAnalytics.trackAppOpened(getIntent());// 跟踪统计应用的打开情况

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewToolbarShadow = findViewById(R.id.toolbar_shadow);
        setToolbar(mToolbar, "美食笔记", mViewToolbarShadow);

        mAVOSNetUtils = AVOSNetUtils.getInstance();
        mAVOSNetUtils.setOnGetAvatarListener(this);

        initDrawerLayout();
        initNavigationView();
    }

    @Override
    public void initData() {
        setUpUser();
        switchToDiscover();
    }

    @Override
    public void initListener() {
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
    }

    private void setUpUser() {
        TextView textView = (TextView) mHeaderView.findViewById(R.id.username);
        textView.setText(AVUser.getCurrentUser().getUsername());
        mImgUser = (CircleImageView) mHeaderView.findViewById(R.id.useravatar);
        mImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                mImgUser.setDrawingCacheEnabled(true);// 传递图片、
                intent.putExtra("bitmap", mImgUser.getDrawingCache());
                startActivity(intent);
                mImgUser.setDrawingCacheEnabled(false);
                mDrawerLayout.closeDrawers();
            }
        });
        mAVOSNetUtils.getUserAvatar(AVUser.getCurrentUser().getUsername());
    }

    private void switchToDiscover() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new DiscoverFragment()).commit();
        mToolbar.setTitle("发现");
    }

    private void switchToFocus() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FocusFragment()).commit();
        mToolbar.setTitle("关注");
    }

    private void switchToCollect() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new CollectFragment()).commit();
        mToolbar.setTitle("收藏");
    }

    private void switchToCommunication() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new CommunicationFragment()).commit();
        mToolbar.setTitle("交流");
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
                    GlideUtils.getInstance(MainActivity.this).setImage((String) msg.obj, mImgUser);
            }
        }

        ;
    };

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧边栏菜单
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_discover) {
            switchToDiscover();
        } else if (id == R.id.nav_collect) {
            switchToCollect();
        } else if (id == R.id.nav_focus) {
            switchToFocus();
        } else if (id == R.id.nav_communication) {
            switchToCommunication();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "nav_settings", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "nav_about", Toast.LENGTH_LONG).show();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private long exitTime = 0;

    private void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showMessage(this, "再按一次退出程序~~");
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit(this);
        }
    }
}
