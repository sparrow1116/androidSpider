package com.example.zyj.first;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.content.res.Resources;

import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class KuaishouService extends AccessibilityService {

    private static final String TAG  = "KuaishouService";
    private Boolean IsStart = false;

    private int advertisementCount = 0;// 广告前后2次 + 广告 3次出现
    private int scrollTimer = 0; //每次触发2次scroll;

    private String clickItem = ""; //center
    private String state = ""; // frontPage taskPage
    private int step = 0;

    private int allTimeSecond;

    private AccessibilityNodeInfo rootNode;
    private KuaishouTimer timeThread;
    private KuaishouCtrThread ctlThread;
    private KuaishouTaskCtrThread taskCtrThread;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if(eventType != 2048){
//            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            Log.d(TAG,"eventType:: " + String.valueOf(eventType));
        }

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                Log.d(TAG,">>>>>>>>>>step>>>>"  + String.valueOf(step));
//                if(state.equals("frontPage") && step == 0){
//                    rootNode = getRootInActiveWindow();
//                    isAdvertisment();
//                }
//                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                scrollTimer++;
                if(scrollTimer == 2){
                    scrollTimer = 0;
                    return;
                }
                Log.d(TAG,"come in >>>>>>>>>>scroll>>>>>>>");
                if(state.equals("frontPage") && clickItem.equals("")){
                    rootNode = getRootInActiveWindow();
                    isAdvertisment();
                }
                break;
            default:
                break;
        }
    }

    private void isAdvertisment(){

        Log.d(TAG,">>>>isAdvertisment>>>>>");
        EventBus.getDefault().post(new KSServiceToCtlEvent("sleep","2"));
    }

    private void swipe(String derection){
        if(derection.equals("up")){
            Resources res = getResources();
            GestureDescription.Builder gestureBuilder = Uitls.wripe(res,"up");


            dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                }
            },null);
        }
    }
    private void getAdAndRead(){
        List<AccessibilityNodeInfo> advertisementNodeList  = rootNode.findAccessibilityNodeInfosByViewId("com.kuaishou.nebula:id/thanos_ad_caption_tv");
//        if(advertisementNodeList != null && advertisementNodeList.size() > 0){ // 反扒机制，此标签在广告的前后各一个都是有出现
//            Log.d(TAG, "go to scroll for advertisementNode>>>>>>>>>");
//            if(advertisementCount == 0){
//                Log.d(TAG,"view video");
//                int randomTime = Uitls.getRandomInt(5,30);
//                Log.d(TAG,"view video time" + randomTime);
//                EventBus.getDefault().post(new KSServiceToCtlEvent("advertisment", String.valueOf(randomTime)));
//            }else if(advertisementCount == 1){
//                EventBus.getDefault().post(new KSServiceToCtlEvent("advertisment", "1"));
//            }else if(advertisementCount == 2){
//                Log.d(TAG,"view video");
//                int randomTime = Uitls.getRandomInt(5,30);
//                Log.d(TAG,"view video time" + randomTime);
//
//                EventBus.getDefault().post(new KSServiceToCtlEvent("advertisment", String.valueOf(randomTime)));
//            }
//
//        }else{
            Log.d(TAG,"view video");
            int randomTime = Uitls.getRandomInt(3,30);
            Log.d(TAG,"view video time" + randomTime);
//            Thread.currentThread().sleep(randomTime * 1000);
            EventBus.getDefault().post(new KSServiceToCtlEvent("video", String.valueOf(randomTime)));
//            swipe("up");

//        }
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void taskEventBus(KuaishouTaskCtrEvent event){
        Log.d(TAG,">>>>>>>>>>>>service KuaishouTimerEvent got data >>>>>" + event.name);
        if(event.name.equals("comeinTask")){
            List<AccessibilityNodeInfo> nodeList = rootNode.findAccessibilityNodeInfosByViewId("com.kuaishou.nebula:id/redFloat");
            Rect rect = new Rect();
            nodeList.get(0).getBoundsInScreen(rect);
            state = "taskPage";
            Uitls.click(rect);
            EventBus.getDefault().post(new KSServiceToTaskCtrEvent("comeinTask","10"));
        }else if(event.name.equals("over")){

        }

    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void controlEventBus(KuaishouCtrEvent event){
        Log.d(TAG,">>>>>>>>>>>>service controlEventBus got data >>>>>" + event.name);
        if(state.equals("frontPage")){
            if(event.name.equals("canGetAd")){
                getAdAndRead();
            }
            if(event.name.equals("advertisment")){
                step++;
                advertisementCount++;
                if(advertisementCount == 2){
                    advertisementCount = 0;
                }
                swipe("up");
            }
            if(event.name.equals("video")){
                step++;
                swipe("up");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void timerEventBus(KuaishouTimerEvent event){
        Log.d(TAG,">>>>>>>>>>>>service KuaishouTimerEvent got data >>>>>" + event.name);
        if(event.name.equals("openBox")){
            List<AccessibilityNodeInfo> nodeList = rootNode.findAccessibilityNodeInfosByViewId("com.kuaishou.nebula:id/redFloat");
            Rect rect = new Rect();
            nodeList.get(0).getBoundsInScreen(rect);
            state = "taskPage";
            Uitls.click(rect);
            EventBus.getDefault().post(new KSServiceToTaskCtrEvent("comeinTask","10"));
        }else if(event.name.equals("over")){

        }

    }

    private void generatorSchedule(){
        Log.d(TAG,">>>>>>>>>>>>>generatorSchedule>>>>>>>");
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        if(hour <= 22){
            int min = Integer.parseInt(String.valueOf(calendar.get(Calendar.MINUTE)));
            allTimeSecond = ((24 - 22) * 60 + (60 - min)) * 60;
        }else{
            allTimeSecond = Uitls.getRandomInt(100,140) * 60;
        }

        EventBus.getDefault().register(KuaishouService.this);

//        allTimeSecond = 10;
        Log.d(TAG,">>>>>>>>>go to new Thread");
        timeThread = new KuaishouTimer();
        timeThread.start();
        ctlThread = new KuaishouCtrThread();
        ctlThread.start();
        taskCtrThread = new KuaishouTaskCtrThread();
        taskCtrThread.start();
        try{
            Thread.currentThread().sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }


        EventBus.getDefault().post(new KSServiceToTimerEvent("allSecond",String.valueOf(allTimeSecond)));
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        try{
            IsStart = intent.getBooleanExtra("action", false);
            Log.d(TAG,"onStartCommand  " + IsStart);
            if(IsStart){
                Intent ksIntent = getPackageManager().getLaunchIntentForPackage("com.kuaishou.nebula");

                if(ksIntent != null){
                    ksIntent.putExtra("type","110");
                    ksIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(ksIntent);

                    ksIntent.putExtra("type","110");
                    ksIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ksIntent);
                    state = "frontPage";
                    step = 0;
                }
                Thread.currentThread().sleep(5000);
                generatorSchedule();

            }
        }catch (Exception e){
            Log.e(TAG,"start Kuai shou error");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(KuaishouService.this);
    }
}
