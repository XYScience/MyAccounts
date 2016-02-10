package com.science.myaccounts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.science.myaccounts.adapter.MenuPagerAdapter;
import com.science.myaccounts.fragment.CollectFragment;
import com.science.myaccounts.fragment.DiscoverFragment;
import com.science.myaccounts.fragment.FocusFragment;
import com.science.myaccounts.interfaces.OnGetAvatarListener;
import com.science.myaccounts.ui.BaseActivity;
import com.science.myaccounts.ui.UserInfoActivity;
import com.science.myaccounts.utils.AVOSNetUtils;
import com.science.myaccounts.utils.GlideUtils;
import com.science.myaccounts.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnGetAvatarListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private View mHeaderView;
    private CircleImageView mImgUser;
    private TextView mTextSignature;
    private AVOSNetUtils mAVOSNetUtils;
    private TabLayout mTabMenu;
    private ViewPager mViewPagerMenu;
    private FloatingActionButton mFabButton;
    private String[] mTitles; // TabLayout中的tab标题
    private List<Fragment> mFragments; // 填充到ViewPager中的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        AVAnalytics.trackAppOpened(getIntent());// 跟踪统计应用的打开情况

        mToolbar = setToolbar("MyAccounts");

        mAVOSNetUtils = AVOSNetUtils.getInstance();
        mAVOSNetUtils.setOnGetAvatarListener(this);

        initDrawerLayout();
        initNavigationView();
        initFab();
    }

    @Override
    public void initData() {
        setUpUser();
        mTitles = getResources().getStringArray(R.array.tab_titles_main);
        mFragments = new ArrayList<>();
        DiscoverFragment fragmentDiscover = new DiscoverFragment();
        mFragments.add(0, fragmentDiscover);
        FocusFragment fragmentFocus = new FocusFragment();
        mFragments.add(1, fragmentFocus);
        CollectFragment fragmentCollect = new CollectFragment();
        mFragments.add(2, fragmentCollect);
        initViewPager();
    }

    private void initViewPager() {
        mTabMenu = (TabLayout) findViewById(R.id.tab_layout);
        mViewPagerMenu = (ViewPager) findViewById(R.id.view_pager);
        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPagerMenu.setAdapter(pagerAdapter);
        mViewPagerMenu.setOffscreenPageLimit(3);
        mTabMenu.setupWithViewPager(mViewPagerMenu);
        mTabMenu.setTabsFromPagerAdapter(pagerAdapter); // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
    }

    private void initFab() {
        mFabButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void initListener() {
        mViewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        startAlphaAnimation(mFabButton, 200, View.VISIBLE);
                        break;
                    case 1:
                        startAlphaAnimation(mFabButton, 200, View.INVISIBLE);
                        break;
                    case 2:
                        startAlphaAnimation(mFabButton, 200, View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 设置渐变的动画
    public void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
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
//            switchToDiscover();
        } else if (id == R.id.nav_collect) {
//            switchToCollect();
        } else if (id == R.id.nav_focus) {
//            switchToFocus();
        } else if (id == R.id.nav_communication) {
//            switchToCommunication();
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
