package ai.mawdoo3.salma.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.mawdoo3.salma.BuildConfig;

/**
 * Created by Ahmed Adel on 17/01/2019.
 * email : a3adel@hotmail.com
 */
public class TTSStreamHelper {
    private static TTSStreamHelper instance;
    private static WeakReference<Context> context;
    private ExoPlayer player;
    private boolean playWhenReady;

    private final MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private int currentWindow;
    private long playbackPosition;
    private ArrayList<TTSStreamCompletionListener> streamListeners;

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

    public void stopStream() {
        isPlaying = false;
        releasePlayer();
    }

    public void startStreaming(String ttsId, boolean ttsDynamic, String ttsText) {
        if (isPlaying) {
            stopStream();
        }
        isPlaying = true;

        initializePlayer(ttsId, ttsDynamic, ttsText);
    }

    public interface TTSStreamCompletionListener {
        void onCompletedListener();
    }

    public DataSource.Factory headers() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", SecurityUtils.INSTANCE.rsaEncrypt(
                "4Eyugb2qVEYcQOyOCUYqmNY8xO8I7W7xf9kaJYlpI8ER7kOnVo0Z3CustWiO0Phtm5OBqMhOU8WLbajvttLv98s6c1ww0TDUKWvD6Jy6f-bh1o_LrewgKNF6o5qjeXjovkl1vjk0mgAHif88d6RRaYsBRIKbwC53Zi9KspKrF1Y",
                context.get()));
        return new DefaultHttpDataSource.Factory().setDefaultRequestProperties(headersMap);
    }

    private void initializePlayer(String ttsId, boolean ttsDynamic, String ttsText) {
        if (context.get() != null) {
            String ttsParams;
            if (ttsDynamic)
                ttsParams = "text=" + ttsText + "&diacritize_text=true&override_diacritics=false&streaming=true&encoding=mp3&normalize_text=true&transfer=false&tempo=0";
            else {
                ttsParams = "key=" + ttsId + "&diacritize_text=false&override_diacritics=false&streaming=true&encoding=mp3&transfer=false&tempo=0";
            }

            player = new SimpleExoPlayer.Builder(context.get())
                    .setMediaSourceFactory(new DefaultMediaSourceFactory(headers())).build();
            player.setPlayWhenReady(true);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(BuildConfig.TTS_URL + "?" + ttsParams)
                    .build();

            player.setMediaItem(mediaItem);
            player.prepare();
            Log.d("TTS", BuildConfig.TTS_URL + "?" + ttsParams);
            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerError(@NonNull ExoPlaybackException error) {
                    for (int i = 0; i < streamListeners.size(); i++) {
                        streamListeners.get(i).onCompletedListener();
                    }
                }

                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_ENDED) {
                        for (int i = 0; i < streamListeners.size(); i++) {
                            streamListeners.get(i).onCompletedListener();
                        }
                    }
                }
            });


        } else {
            for (int i = 0; i < streamListeners.size(); i++) {
                streamListeners.get(i).onCompletedListener();
            }
        }
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