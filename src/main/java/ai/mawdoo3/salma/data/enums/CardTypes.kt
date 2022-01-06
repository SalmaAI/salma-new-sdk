package ai.mawdoo3.salma.data.enums

import ai.mawdoo3.salma.R
import android.graphics.drawable.Drawable

enum class CardTypes(val id: Int, val value: String, val drawableID: Int) {
    CreditMCRevolvingClassic(1, "CreditMCRevolvingClassic", R.drawable.credit_mc_revolving_classic),
    CreditMCRevolvingTitanium(2, "CreditMCRevolvingTitanium", R.drawable.ic_chatbot_analytics),
    CreditMCRevolvingPlatinum(3, "CreditMCRevolvingPlatinum", R.drawable.ic_chatbot_analytics),
    CreditMCRevolvingWorld(4, "CreditMCRevolvingWorld", R.drawable.ic_chatbot_analytics),
    CreditWorldElite(5, "CreditMCRevolvingWorldElite", R.drawable.ic_chatbot_analytics),
    CreditRJMCRevolvingPlatinum(6, "CreditRJMCRevolvingPlatinum", R.drawable.ic_chatbot_analytics),
    CreditRJMCRevolvingWorld(7, "CreditRJMCRevolvingWorld", R.drawable.ic_chatbot_analytics),
    CreditRJMCRevolvingWorldElite(8, "CreditRJMCRevolvingWorldElite", R.drawable.ic_chatbot_analytics),
    DebitMCClassic(9, "DebitMCClassic", R.drawable.ic_chatbot_analytics),
    DebitMCTitanium(10, "DebitMCTitanium", R.drawable.ic_chatbot_analytics),
    PrepaidJKB(11, "PrepaidJKB", R.drawable.ic_chatbot_analytics),
    PrepaidNoneJKB(12, "PrepaidNoneJKB", R.drawable.ic_chatbot_analytics),
    PrepaidWearableCard(13, "PrepaidWearableCard", R.drawable.ic_chatbot_analytics),
    PrepaidDinarakCard(14, "PrepaidDinarakCard", R.drawable.ic_chatbot_analytics),
    PrepaidAMMUCard(15, "PrepaidAMMUCard", R.drawable.ic_chatbot_analytics),
    PrepaidPUNVCard(16, "PrepaidPUNVCard", R.drawable.ic_chatbot_analytics);


    companion object {
        fun from(type: String?): CardTypes? = values().find { it.value == type } ?: null
    }

}
