package com.science.myaccounts.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.IOException;

/**
 * @author 幸运Science-陈土燊
 * @description AVOS云服务管理类
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/7
 */

public class AVService {

    private AVService() {
    }

    private static AVService mAVService;

    public static synchronized AVService getInstance() {
        if (mAVService == null) {
            return new AVService();
        } else {
            return mAVService;
        }
    }

    // 注册
    public static void signUp(String username, String password, String email, String installationId,
                              SignUpCallback signUpCallback) {

        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("installationId", installationId);
        user.signUpInBackground(signUpCallback);
    }

    // 保持用户头像
    public static void upLoadUserAvatar(String username, String avatarUrl, SaveCallback saveCallback) {
        AVFile imageFile = null;

        try {
            imageFile = AVFile.withAbsoluteLocalPath(username
                    + "_avatar.jpg", avatarUrl);
            imageFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AVException e) {
            e.printStackTrace();
        }

        AVObject userInformation = new AVObject("UserInfo");
        userInformation.put("username", username);
        userInformation.put("avatar", imageFile);
        userInformation.saveInBackground(saveCallback);
    }

    // 退出登录
    public static void logout() {
        AVUser.logOut();
    }

    // 消息列表
    public static void messageList(String friend, String urlAvater,
                                   String currentUser, String sendTime, String messsage) {
        AVObject avObject = new AVObject("MessageList");
        avObject.put("friend", friend);
        avObject.put("urlAvater", urlAvater);
        avObject.put("currentUser", currentUser);
        avObject.put("sendTime", sendTime);
        avObject.put("messsage", messsage);
        avObject.saveInBackground();
    }

    // 更新消息列表
    public static void updateMessageList(String objID, String sendTime,
                                         String messsage) {

        AVObject messageList = new AVObject("MessageList");
        AVQuery<AVObject> query = new AVQuery<AVObject>("MessageList");
        try {
            messageList = query.get(objID);
        } catch (AVException e) {
            e.printStackTrace();
        }
        messageList.put("messsage", messsage);
        messageList.put("sendTime", sendTime);
        messageList.saveInBackground();
    }

    // 删除消息列表
    public static void removeMessage(String objectId) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("MessageList");
        AVObject avObj = null;
        try {
            avObj = query.get(objectId);
            avObj.delete();
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    // 好友通讯录列表
    public static void addressList(String friend, String currentUser,
                                   String avaterUrl, String email, String gender, String sendTime) {
        AVObject avObject = new AVObject("AddressList");
        avObject.put("currentUser", currentUser);
        avObject.put("friend", friend);
        avObject.put("friendAvaterUrl", avaterUrl);
        avObject.put("friendEmail", email);
        avObject.put("friendGender", gender);
        avObject.put("sendTime", sendTime);
        avObject.saveInBackground();
    }

    // 删除好友
    public static void removeFriends(String objectId) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("AddressList");
        AVObject avObj = null;
        try {
            avObj = query.get(objectId);
            avObj.delete();
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static void requestPasswordReset(String email,
                                            RequestPasswordResetCallback callback) {
        AVUser.requestPasswordResetInBackground(email, callback);
    }


    public static void myLocation(String userEmail, String username,
                                  String gender, double latitude, double longititude, String location) {

        AVGeoPoint point = new AVGeoPoint(latitude, longititude);
        AVObject myPlace = new AVObject("MyLocation");
        myPlace.put("locationPoint", point);
        myPlace.put("userEmail", userEmail);
        myPlace.put("username", username);
        myPlace.put("gender", gender);
        myPlace.put("location", location);
        myPlace.saveInBackground();

        // AVObject myLocation = new AVObject("MyLocation");
        // myLocation.put("userObjectId", userObjectId);
        // myLocation.put("username", username);
        // myLocation.put("gender", gender);
        // myLocation.put("latitude", latitude);
        // myLocation.put("longtitude", longititude);
        // myLocation.saveInBackground();
    }

    // APP每天签到
    public static void dailySign(String username, int signTimes,
                                 String signDate, String signPosition, SaveCallback saveCallback) {
        AVObject userInformation = new AVObject("Sign");
        userInformation.put("username", username);
        userInformation.put("signTimes", signTimes);
        userInformation.put("signTime", signDate);
        userInformation.put("signPosition", signPosition);
        userInformation.saveInBackground(saveCallback);
    }

}
