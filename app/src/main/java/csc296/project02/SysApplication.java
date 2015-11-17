package csc296.project02;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yugeyang on 15-11-14.
 */
public class SysApplication extends Application{
    private List<AppCompatActivity> mList = new LinkedList<AppCompatActivity>();
    private static  SysApplication instance;
    private SysApplication(){

    }
    public synchronized static SysApplication getInstance() {
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }

    public void addActivity(AppCompatActivity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (AppCompatActivity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void  onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
