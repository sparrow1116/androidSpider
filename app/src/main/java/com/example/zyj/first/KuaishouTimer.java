package com.example.zyj.first;

import android.util.Log;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by zyj on 2020/11/9.
 */

public class KuaishouTimer extends Thread{


    private boolean isRunning = false;
    private int step = 0;
    private int allSecond = 99999;

    private int nextAdvertismentSecond = 0;

    private static final String TAG  = "KuaishouTimer";


    @Override
    public void run() {
        EventBus.getDefault().register(KuaishouTimer.this);
    }

    private void begin(){
        try{
            generatorNextAdvertiseTime();
            Log.d(TAG,"nextAdvertismentSecond " + String.valueOf(nextAdvertismentSecond));
            isRunning = true;
            while (isRunning){
                Thread.sleep(1000);
                step++;
//                Log.d(TAG,"second::: " + String.valueOf(step));
                if(step >= nextAdvertismentSecond){
                    generatorNextAdvertiseTime();
                    sendMessage("openBox");
                    isRunning = false;
                }
//                Log.d(TAG,"allSecond>>>>> " + String.valueOf(allSecond));
                if(step >= allSecond){
                    Log.d(TAG,"over   over  over");
                    isRunning = false;
                    sendMessage("over");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void MessageEventBus(KSServiceToTimerEvent event){
        Log.d(TAG,">>>>>>>>>>>>time thread got it >>>>>" + event.name);
        Log.d(TAG,">>>> event data>>>"  + event.data);
        try{
            if(event.name.equals("allSecond")){
                Log.d(TAG,">>>allSecond:" + event.data);
                allSecond = Integer.parseInt(event.data);
                begin();
            }else{
                Log.d(TAG,">>>   " + event.name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(KuaishouTimer.this);
    }

    private void sendMessage(String str){
        Log.d(TAG,">>>>>goto sendMessage>>>>>>>");
        EventBus.getDefault().post(new KuaishouTimerEvent(str,""));
    }

    private void generatorNextAdvertiseTime(){
        int radio = Uitls.getRandomInt(110,200);

        nextAdvertismentSecond = nextAdvertismentSecond + radio/100 * 10 * 60;
        nextAdvertismentSecond = 40;
        Log.d(TAG, ">>>>>>>generatorNextAdvertiseTime>>>>" + String.valueOf(nextAdvertismentSecond));
    }
}
