package ai.mawdoo3.salma.utils.asr;

/**
 * Created by Devlomi on 24/08/2017.
 */

public interface OnRecordListener {
    void onStart();

    void onCancel();

    void onFinish(long recordTime, Boolean onLongClickPress);

    void onLessThanSecond();

    void onLongPressRecord(boolean mLongClickTrigger);

    void onSingleTabRecord(boolean mLongClickTrigger);
}
