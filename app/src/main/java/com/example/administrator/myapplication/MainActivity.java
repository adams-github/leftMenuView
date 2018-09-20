package com.example.administrator.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.menu_Layout)
    LeftSideMenuView menuLayout;
    @BindView(R.id.transparentProgressView)
    TransparentProgressView progressView;
    int percent = 0;

    private Timer timer = null;
    private TimerTask timerTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startTimer(1000, 1000);
    }


    //下载失败时，启动定时器查询网络状态，有网时继续下载
    public void startTimer(long startTime, long interval){
        if (timerTask == null){
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
        }
        if (timer == null){
            timer = new Timer();
            timer.schedule(timerTask, startTime, interval);
        }
    }


    private void stopTimer(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        handler.removeMessages(1);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (percent < 100){
                    progressView.setPercent(percent);
                    percent++;
                }else{
                    stopTimer();
                }
            }
            super.handleMessage(msg);
        };
    };

}
