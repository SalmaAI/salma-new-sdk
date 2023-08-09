package ai.mawdoo3.salma

import ai.mawdoo3.salma.data.enums.ChatBarType
import ai.mawdoo3.salma.ui.BotMainActivity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity

/**
 * created by Omar Qadomi on 3/24/21
 */
object SalmaSdkInstance {
    var botId: String = "1"
    var botChannelId: String = "1"
    var key: String = "1"
    var jwtToken: String = ""
    var username: String? = ""
    var chatBarType: ChatBarType = ChatBarType.TEXT_AND_AUDIO

    fun launchSDK(activity: FragmentActivity?) {
        activity?.startActivity(Intent(activity, BotMainActivity::class.java))
    }

    fun launchSDKForResult(
        activity: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>
    ) {
        val intent = Intent(activity, BotMainActivity::class.java)
        launcher.launch(intent)
    }

    fun setChatType(chatBarType: ChatBarType): SalmaSdkInstance {
        this.chatBarType = chatBarType
        return this
    }

    fun initialize(
        botId: String,
        botChannelId: String,
        key: String,
        username: String = ""
    ) {

        this.botId = botId
        this.botChannelId = botChannelId
        this.key = key
        this.username = username
    }
}
