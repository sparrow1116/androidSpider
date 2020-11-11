package com.example.zyj.first;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view){
        Intent intent = new Intent(this, KuaishouService.class);
        intent.putExtra("action",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
//        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        startActivity(intent);
    }
    public void end(View view){
        Intent intent = new Intent(this, KuaishouService.class);
        intent.putExtra("action",false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }

    public static boolean isAccessibilityEnabled(Context context) throws RuntimeException{
        if (context == null) {
            return false;
        }

        // 检查AccessibilityService是否开启
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(android.content.Context.ACCESSIBILITY_SERVICE);
        boolean isAccessibilityEnabled_flag = am.isEnabled();

        boolean isExploreByTouchEnabled_flag = false;

        // 检查无障碍服务是否以语音播报的方式开启
//        isExploreByTouchEnabled_flag = isScreenReaderActive(context);

        return isAccessibilityEnabled_flag;//(isAccessibilityEnabled_flag && isExploreByTouchEnabled_flag);

    }
}
