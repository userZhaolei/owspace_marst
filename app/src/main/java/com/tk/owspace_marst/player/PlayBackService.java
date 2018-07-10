package com.tk.owspace_marst.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.tk.owspace_marst.R;
import com.tk.owspace_marst.view.activity.MainActivity;

/**
 * Zhaolei
 * 时间:2018/7/3
 */

public class PlayBackService extends Service implements IPlayback, IPlayback.Callback {

    private static final String ACTION_PLAY_TOGGLE = "com.tk.owspace_marst.ACTION.PLAY_TOGGLE";
    private static final String ACTION_PLAY_LAST = "com.tk.owspace_marst.ACTION.PLAY_LAST";
    private static final String ACTION_PLAY_NEXT = "com.tk.owspace_marst.ACTION.PLAY_NEXT";
    private static final String ACTION_STOP_SERVICE = "com.tk.owspace_marst.ACTION.STOP_SERVICE";

    private Player mPlayer;
    private static final int NOTIFICATION_ID = 1;

    private final Binder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public PlayBackService getService() {
            return PlayBackService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = Player.getInstance();
        mPlayer.registerCallback(this);//注册播放回调
        Logger.d("onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand");
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY_TOGGLE.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (isPlaying()) {
                    pause();
                }
                stopForeground(true);
                unregisterCallback(this);
            }
        }
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        Logger.d("stopService");
        stopForeground(true);
        unregisterCallback(this);
        return super.stopService(name);
    }

    @Override
    public boolean play() {
        return mPlayer.play();
    }

    @Override
    public boolean play(String song) {
        return mPlayer.play(song);
    }

    @Override
    public boolean pause() {
        return mPlayer.pause();
    }

    public void setPause(boolean pause) {
        mPlayer.setPaused(pause);
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mPlayer.getProgress();
    }

    public String getSong() {
        return mPlayer.getSong();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean seekTo(int progress) {
        return mPlayer.seekTo(progress);
    }

    @Override
    public void registerCallback(Callback callback) {
        mPlayer.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mPlayer.unregisterCallback(callback);
    }

    @Override
    public void removeCallbacks() {
        mPlayer.removeCallbacks();
    }

    @Override
    public void releasePlayer() {
        mPlayer.releasePlayer();
    }


    @Override
    public void onComplete(PlayState state) {
        showNotification(state);
    }

    @Override
    public void onPlayStatusChanged(PlayState status) {
        showNotification(status);
    }

    @Override
    public void onPosition(int position) {
    }

    private void showNotification(PlayState state) {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(getSmallContentView()).setPriority(NotificationCompat.PRIORITY_MAX).setOngoing(true).build();

        // Send the notification.
        startForeground(NOTIFICATION_ID, notification);
    }

    private RemoteViews mContentViewBig, mContentViewSmall;

    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private void updateRemoteViews(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlaying() ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close); //给对应位置对应的图标
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);

        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
    }


    // PendingIntent

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);   //跳转到服务，对应状态
    }

}
