package com.example.routeapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Router跳转实现
 * Created by mj on 2021/2/9 20:01
 */
public class Routers {

    public static final String RAW_URL = "raw_url";

    private static final List<RouterInfo> list = new ArrayList<>();
    private static List<String> stringList = new ArrayList<>();
    private static final Routers instance = new Routers();
    private static Context context;

    private Routers() {
    }

    public static void init(Context ctx) {
        context = ctx;
        invokeAdd();
    }

    public static void invokeAdd() {
        try {
            Class<?> clazz = Class.forName("com.example.route.RouterInit");
            Method add = clazz.getDeclaredMethod("init");
            add.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRoute(String path, Class<? extends Activity> activityClass) {
        if (TextUtils.isEmpty(path)) return;
        Uri uri = Uri.parse(path);
        String host = uri.getHost();
        for (RouterInfo routerInfo : list) {
            Uri uriList = Uri.parse(routerInfo.getPath());
            String hostList = uriList.getHost();
            if (hostList.equals(host)) {
                throw new IllegalStateException("The host(" + host + ") has been declared on activity("
                        + routerInfo.getActivity().getCanonicalName() + ")");
            }
        }
        list.add(new RouterInfo(path, activityClass));
        stringList.add(path);
        Log.println(Log.ERROR, "activitySize", list.size() + "");
    }

    public static Routers getInstance() {
        return instance;
    }

    public void navigation(Context context, String path) {
        for (RouterInfo routerInfo : list) {
            if (routerInfo.getPath().equals(path)) {
                Intent intent = new Intent(context, routerInfo.getActivity());
                intent.putExtra(RAW_URL, path);
                context.startActivity(intent);
                break;
            }
        }
    }

}
