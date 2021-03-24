package ai.mawdoo3.salma

import ai.mawdoo3.salma.ui.BotMainActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * created by Omar Qadomi on 3/24/21
 */
class MasaSDK {
    companion object {

        fun initialize(botId: String, botChannelId: String, key: String): SdkInstance {
            SdkInstance.botId = botId
            SdkInstance.botChannelId = botChannelId
            SdkInstance.key = key
            return SdkInstance
        }
    }


    object SdkInstance {
        var botId: String = "1"
        var botChannelId: String = "1"
        var key: String = "1"
        fun launchSDK(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, BotMainActivity::class.java))
        }
    }
}
