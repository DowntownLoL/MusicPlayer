package com.example.musicservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv_1;
    SeekBar seekBar;
    ServiceConnection serviceConnection;
    MyMusicService.Finder finder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_1 = (TextView)findViewById(R.id.tv_1);

        tv_1.setText("播放状态11：停止播放。。。");

        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    public void play_onclick(View view)
    {
        Intent intent = new Intent(this,MyMusicService.class);

        intent.putExtra("action","play");

        startService(intent);

        tv_1.setText("播放状态11：正在播放。。。");

        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    finder = (MyMusicService.Finder) service;
                    //设置进度条的最大长度
                    int max = finder.getDuration();

                    seekBar.setMax(max);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser) // 判断是否来自用户操作，防止卡顿
                                finder.setProgress(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    //连接之后启动子线程设置当前进度
                    new Thread()
                    {
                        public void run()
                        {
                            //改变当前进度条的值
                            //设置当前进度
                            while (true) {
                                seekBar.setProgress(finder.getCurDuration());
                                try {
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }.start();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            //以绑定方式连接服务
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void stop_onclick(View view)
    {
        Intent intent = new Intent(this,MyMusicService.class);

        intent.putExtra("action","stop");

        startService(intent);

        tv_1.setText("播放状态11：停止播放。。。");
    }
    public void pause_onclick(View view)
    {
        Intent intent = new Intent(this,MyMusicService.class);

        intent.putExtra("action","pause");

        startService(intent);

        tv_1.setText("播放状态11：暂停播放。。。");
    }
    public void exit_onclick(View view)
    {
        stop_onclick(view);
        finish();
    }

}