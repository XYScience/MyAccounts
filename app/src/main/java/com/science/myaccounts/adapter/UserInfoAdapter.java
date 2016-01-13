package com.science.myaccounts.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.science.myaccounts.R;
import com.science.myaccounts.ui.LoginActivity;
import com.science.myaccounts.utils.AVService;
import com.science.myaccounts.utils.CommonUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private int screenHeight;
    private Context mContext;
    private Bitmap mBitmap;
    private int[] mCircleImageColor = {R.color.swipe_blue,
            R.color.swipe_red,
            R.color.swipe_yellow,
            R.color.swipe_green,
            R.color.textColor};
    private int mCircleImageCount = 0;

    public UserInfoAdapter(Activity activity, Bitmap bitmap) {
        layoutInflater = LayoutInflater.from(activity);
        screenHeight = CommonUtil.getScreenHeight(activity);
        mContext = activity;
        mBitmap = bitmap;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case 0:
                return getFirstView(position, convertView, parent);
            case 1:
                return getSecondView(position, convertView, parent);
            default:
                return null;
        }
    }

    private View getFirstView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_user_portrait,
                    parent, false);
            ViewGroup.LayoutParams params = convertView.getLayoutParams();
            params.height = screenHeight;
            convertView.setLayoutParams(params);
            convertView.setId(R.id.id0);
            CircleImageView userAvatar = (CircleImageView) convertView.findViewById(R.id.user_avatar);
            userAvatar.setImageBitmap(mBitmap);
            userAvatarClick(userAvatar);
        }

        return convertView;
    }

    private void userAvatarClick(final CircleImageView userAvatar) {
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击头像边框颜色变化
                userAvatar.setBorderColorResource(mCircleImageColor[mCircleImageCount]);
                mCircleImageCount++;
                if (mCircleImageCount >= mCircleImageColor.length) {
                    mCircleImageCount = 0;
                }
            }
        });
        userAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AVService.logout();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                return true;
            }
        });
    }

    private View getSecondView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
//			convertView = layoutInflater.inflate(R.layout.item_user, parent, false);
            convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView txt = (TextView) convertView;
        txt.setText("Position :" + position);

        return convertView;
    }

}
