package com.science.myaccounts.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.science.myaccounts.interfaces.OnGetAvatarListener;

import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/19
 */
public class AVOSNetUtils {

    private AVOSNetUtils() {
    }

    private static AVOSNetUtils mAVOSNetUtils;

    public static synchronized AVOSNetUtils getInstance() {
        if (mAVOSNetUtils == null) {
            return new AVOSNetUtils();
        } else {
            return mAVOSNetUtils;
        }
    }

    private OnGetAvatarListener mOnGetAvatarListener = null;

    public void setOnGetAvatarListener(OnGetAvatarListener onGetAvatarListener) {
        this.mOnGetAvatarListener = onGetAvatarListener;
    }

    // 获取用户头像
    public void getUserAvatar(String username) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null && list.size() != 0) {
                    String objectId = list.get(list.size() - 1).getObjectId();
                    getAvatarFile(objectId);
                }
            }
        });
    }

    // 得到头像文件
    private void getAvatarFile(final String objectId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
                AVObject gender = null;
                try {
                    gender = query.get(objectId);
                } catch (AVException e) {
                    e.printStackTrace();
                }
                // Retrieving the file
                AVFile imageFile = (AVFile) gender.get("avatar");

                if (mOnGetAvatarListener != null) {
                    mOnGetAvatarListener.getAvaterListener(imageFile.getUrl());
                }
            }

        }).start();
    }

    // 通过email查询AVUser表
    public void getUserEmail(String email) {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException arg1) {
                if (list != null && list.size() != 0) {
                    String username = list.get(list.size() - 1).getUsername();
                    getUserAvatar(username);
                }
            }
        });
    }
}
