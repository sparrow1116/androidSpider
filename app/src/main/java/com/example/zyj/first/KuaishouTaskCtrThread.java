package com.example.zyj.first;

import android.util.Log;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by zyj on 2020/11/10.
 */

public class KuaishouTaskCtrThread extends Thread{
    private static final String TAG  = "KuaishouTaskCtrThread";

    @Override
    public void run() {
        EventBus.getDefault().register(KuaishouTaskCtrThread.this);
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void MessageEventBus(KSServiceToTaskCtrEvent event){
        Log.d(TAG,">>>>>>>>>>>>time thread got it >>>>>" + event.name);
        try{
            if(event.name.equals("comeinTask")){
                Thread.currentThread().sleep(Integer.parseInt(event.data));
                EventBus.getDefault().post(new KuaishouTaskCtrEvent("comeinTask",""));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(KuaishouTaskCtrThread.this);
        super.destroy();

    }
}
