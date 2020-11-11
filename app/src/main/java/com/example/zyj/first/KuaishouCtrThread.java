package com.example.zyj.first;

import android.util.Log;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by zyj on 2020/11/10.
 */

public class KuaishouCtrThread extends Thread{

    private static final String TAG = "KuaishouCtrThread";

    @Override
    public void run() {
        EventBus.getDefault().register(KuaishouCtrThread.this);
    }
    @Subscribe(threadMode = ThreadMode.Async)
    public void MessageEventBus(KSServiceToCtlEvent event){
        Log.d(TAG,">>>>>>>>>>>>time thread got it >>>>>" + event.name);
//        Log.d(TAG,">>>> event data>>>"  + event.data);
        try{
            if(event.name.equals("sleep")){
                int time = Integer.parseInt(event.data);
                Log.d(TAG,"sleep time>>>> " + String.valueOf(time));
                Thread.currentThread().sleep(time * 1000);

                if(time == 2){
                    EventBus.getDefault().post(new KuaishouCtrEvent("canGetAd",""));
                }else{
//                    EventBus.getDefault().post(new KuaishouCtrEvent("canSwipe",""));
                }
            }
            if(event.name.equals("advertisment")){
                int time = Integer.parseInt(event.data);
                Log.d(TAG," advertisment time>>>> " + String.valueOf(time));
                Thread.currentThread().sleep(time * 1000);
                EventBus.getDefault().post(new KuaishouCtrEvent("advertisment",""));
            }
            if(event.name.equals("video")){
                int time = Integer.parseInt(event.data);
                Log.d(TAG," video time>>>> " + String.valueOf(time));
                Thread.currentThread().sleep(time * 1000);
                EventBus.getDefault().post(new KuaishouCtrEvent("video",""));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(KuaishouCtrThread.this);
    }
}
