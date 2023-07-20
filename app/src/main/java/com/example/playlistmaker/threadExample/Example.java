package com.example.playlistmaker.threadExample;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Example {
    private int count;
    private Context context;
    private String TAG = Example.class.getCanonicalName();

    public Example(int count, Context context) {
        this.count = count;
        this.context = context;
    }

    public void doWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    final long threadId = Thread.currentThread().getId();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Successfully done, thread " + threadId, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Toast displayed in thread: " + threadId);
                        }
                    });
                } catch (InterruptedException e) {
                    Log.e(TAG, "Error message " + e.getLocalizedMessage());
                }
            }
        }).start();
    }
}
