package ai.mawdoo3.salma.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ai.mawdoo3.salma.BuildConfig;

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

    public void startStreaming(String ttsId, boolean ttsDynamic) {
        if (isPlaying)
            stopStream();
        isPlaying = true;

        initializePlayer(ttsId, ttsDynamic);
    }

    public boolean isMediaPlayerPlaying() {
        return isPlaying;
    }


    public interface TTSStreamCompletionListener {
        void onCompletedListener();
    }

    private void initializePlayer(String ttsId, boolean ttsDynamic) {
        if (context.get() != null) {
            String ttsParams;
            if (ttsDynamic)
                ttsParams = "&diacritize_text=true&override_diacritics=false&streaming=true&encoding=wav&normalize_text=true&transfer=false&tempo=0";
            else {
                ttsParams = "&diacritize_text=false&override_diacritics=false&streaming=true&encoding=wav&transfer=false&tempo=0";
            }

            player = new SimpleExoPlayer.Builder(context.get()).build();

            player.setPlayWhenReady(true);
            MediaItem mediaItem = MediaItem.fromUri(BuildConfig.TTS_URL + "?key=" + ttsId + ttsParams);
            player.setMediaItem(mediaItem);
            player.prepare();
            Log.d("TTS", BuildConfig.TTS_URL + "?key=" + ttsId + ttsParams);
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