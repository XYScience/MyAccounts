package com.science.myaccounts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.science.myaccounts.R;
import com.science.myaccounts.adapter.DiscoverAdapter;
import com.science.myaccounts.utils.SnackbarUtils;
import com.science.myaccounts.widget.HidingScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chenyc on 15/7/1.
 */
public class DiscoverFragment extends Fragment {

    private View mRootView;
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private DiscoverAdapter mAdapterDiscover;
    private FloatingActionButton mFabButton;

    private static final int ANIM_DURATION_FAB = 500;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discover, null);

        initView();
        floatingBtnClick();
        initListener();
        return mRootView;
    }

    private void initView() {
        mCoordinatorLayout = (CoordinatorLayout) mRootView.findViewById(R.id.coordinatorLayout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mFabButton = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapterDiscover = new DiscoverAdapter(createItemList());
        mRecyclerView.setAdapter(mAdapterDiscover);
    }

    private void floatingBtnClick() {
        FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SnackbarUtils.showSnackbar(mCoordinatorLayout, "Replace with your own action");
            }
        });
    }

    private List<String> createItemList() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add("item" + i);
        }
        return list;
    }

    private void initListener() {
        mRecyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

}
