package com.rtmap.gm.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rtmap.game.AndroidLauncher;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, AndroidLauncher.class));
                    }
                });
            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
