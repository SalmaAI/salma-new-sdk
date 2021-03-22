package ai.mawdoo3.salma.utils.asr;

import android.view.MotionEvent;
import android.widget.Button;

public class RecordUtils {

    private OnRecordListener recordListener;
    private boolean isSwiped;
    private long elapsedTime;
    private long startTime;

    public RecordUtils() {
    }

    public void onActionDown(Button recordBtn, MotionEvent motionEvent) {
        if (recordListener != null)
            recordListener.onStart();
        isSwiped = false;
    }

    public void onActionUp(Button recordBtn, Boolean mLongClickTrigger) {
        elapsedTime = System.currentTimeMillis() - startTime;
        if (recordListener != null && !isSwiped)
            recordListener.onFinish(elapsedTime, mLongClickTrigger);
    }

    public void setOnRecordListener(OnRecordListener recrodListener) {
        this.recordListener = recrodListener;
    }

    public void onLongPressRecord(boolean mLongClickTrigger) {
        //if (recordListener != null && !isSwiped)
            //recordListener.onLongPressRecord(mLongClickTrigger);
    }

    public void onSingleTabRecord(boolean mLongClickTrigger) {
        if (recordListener != null)
            recordListener.onSingleTabRecord(false);
    }
}
