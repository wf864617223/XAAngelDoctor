package com.rimi.xaangel.angeldoctor.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.rimi.xaangel.angeldoctor.bean.UpdateInfo;
import com.rimi.xaangel.angeldoctor.http.HttpUrls;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Android on 2016/6/30.
 */
public class CheckVersionUtils  {
    //获取版本号
    public static String getVersion(Context context) {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "暂无";
        }
    }
    public static UpdateInfo isUpdate() throws Exception {
        UpdateInfo info = new UpdateInfo();
        URL url = new URL(HttpUrls.CHECK_VERSION);
        HttpURLConnection contect = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(contect.getInputStream());
        String result = readInStream(in);
        JSONObject response = new JSONObject(result);
        JSONObject doctor = response.getJSONObject("android").getJSONObject("doctor");
        info.setTitle(doctor.getString("title"));
        info.setUrl(doctor.getString("url"));
        info.setVersion(doctor.getString("version"));
        info.setNote(doctor.getString("note"));
        return info;
    }
    private static String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
