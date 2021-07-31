package ai.mawdoo3.salma.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Ahmed Adel on 17/01/2019.
 * email : a3adel@hotmail.com
 */
public class TTSStreamHelper {
    ExoPlayer player;
    boolean playWhenReady;
    int currentWindow;
    long playbackPosition;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    static TTSStreamHelper instance;
    static WeakReference<Context> context;
    ArrayList<TTSStreamCompletionListener> streamListeners;

    public void setTtsStreamCompletionListener(TTSStreamCompletionListener ttsStreamCompletionListener) {
        streamListeners.add(ttsStreamCompletionListener);
    }


    public static TTSStreamHelper getInstance(Context context) {
        TTSStreamHelper.context = new WeakReference<>(context);
        if (instance == null) {
            instance = new TTSStreamHelper();
        }

        return instance;
    }

    private TTSStreamHelper() {
        mediaPlayer = new MediaPlayer();
        streamListeners = new ArrayList<>();

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stopStream() {
        isPlaying = false;
        releasePlayer();
    }

    public void startStreaming(String url) {
        if (isPlaying)
            stopStream();
        isPlaying = true;

        initializePlayer(url);
    }

    public boolean isMediaPlayerPlaying() {
        return isPlaying;
    }


    public interface TTSStreamCompletionListener {
        void onCompletedListener();
    }

    private void initializePlayer(String ttsId) {
        if (context.get() != null) {


            player =
                    new SimpleExoPlayer.Builder(context.get())
                            .build();


            player.setPlayWhenReady(true);
            MediaItem mediaItem = MediaItem.fromUri("https://demo.salma-app.com/tts-api-v4-1-3/tts?key="+ttsId+"&diacritize_text=false&override_diacritics=false&streaming=true&as_attachment=false&tempo=0&encoding=mp3&bitrate=16000");
            player.setMediaItem(mediaItem);
            player.prepare();

            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    for (int i = 0; i < streamListeners.size(); i++)
                        streamListeners.get(i).onCompletedListener();
                }

                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_ENDED) {
                        for (int i = 0; i < streamListeners.size(); i++)
                            streamListeners.get(i).onCompletedListener();
                    }
                }
            });




        } else
            for (int i = 0; i < streamListeners.size(); i++)
                streamListeners.get(i).onCompletedListener();
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory source = () -> {
            DefaultHttpDataSource source1 = new DefaultHttpDataSource("exoplayer-codelab");
            source1.setRequestProperty("x-api-key", "ssBIeKRaH615KFMAERPoF3GGYMk0CdjL8f3FHRmj");
            source1.setRequestProperty("Accept", "audio/mp3");
            return source1;
        };
        MediaSource mediaSource = new ExtractorMediaSource.Factory(source).createMediaSource(uri);
        return mediaSource;
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}