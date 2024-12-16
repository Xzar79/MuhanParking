package com.example.muhanparking.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.muhanparking.Mypage_Activity;
import com.example.muhanparking.User_Edit_Activity;

public class PreferenceManager {
    private static final String PREF_NAME = "MuhanParkingPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USERNAME = "username";

    private SharedPreferences sharedPreferences;
    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 토큰 저장
    public static void saveToken(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // 토큰 가져오기
    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(KEY_TOKEN, null);
    }

    // 토큰 삭제 (로그아웃 시 사용)
    public static void clearToken(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    // 사용자 이름 저장
    public static void saveUsername(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // 사용자 이름 가져오기
    public static String getUsername(Context context) {
        return getSharedPreferences(context).getString(KEY_USERNAME, null);
    }

    // 모든 데이터 삭제
    public static void clearAll(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    // 문자열 값 저장
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 문자열 값 가져오기
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void clearUsername(Mypage_Activity mypageActivity) {

    }

}