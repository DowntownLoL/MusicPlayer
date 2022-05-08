package com.example.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;

public class MyMusicService extends Service {
    public MyMusicService() {
    }

    public class Finder extends Binder {
        // 歌曲总长度
        public int getDuration() {
            return mediaPlayer == null ? 0 : mediaPlayer.getDuration();
        }

        // 歌曲当前播放进度
        public int getCurDuration() {
            return mediaPlayer == null ? 0 : mediaPlayer.getCurrentPosition();
        }

        // 改变当前音乐进度
        public void setProgress(int position){
            if(mediaPlayer != null)
                mediaPlayer.seekTo(position);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Finder();
    }

    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //获取意图传递的信息
        String action = intent.getStringExtra("action");

        switch (action)
        {
            case "play":
                if (mediaPlayer == null)
                {
                    mediaPlayer = MediaPlayer.create(this,R.raw.newyear);
                }
                mediaPlayer.start();

                break;
            case "stop":
                if (mediaPlayer !=null)
                {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                break;
            case "pause":
                if (mediaPlayer !=null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                }
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }



}