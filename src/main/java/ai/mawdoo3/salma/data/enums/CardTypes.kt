package ai.mawdoo3.salma.data.enums

import ai.mawdoo3.salma.R
import android.graphics.drawable.Drawable

enum class CardTypes(val id: Int, val value: String, val drawableID: Int,val nameID :Int) {
    CreditMCRevolvingClassic(1, "CreditMCRevolvingClassic", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    CreditMCRevolvingTitanium(2, "CreditMCRevolvingTitanium", R.drawable.credit_titanium,R.string.credit_mc_resolving_titanium),
    CreditMCRevolvingPlatinum(3, "CreditMCRevolvingPlatinum", R.drawable.credit_platinum,R.string.credit_mc_resolving_platinum),
    CreditMCRevolvingWorld(4, "CreditMCRevolvingWorld", R.drawable.credit_world,R.string.credit_mc_resolving_world),
    CreditWorldElite(5, "CreditMCRevolvingWorldElite", R.drawable.credit_world_elite,R.string.credit_mc_resolving_world_elite),
    CreditRJMCRevolvingPlatinum(6, "CreditRJMCRevolvingPlatinum", R.drawable.credit_rj_platinum,R.string.credit_rjmc_resolving_platinum),
    CreditRJMCRevolvingWorld(7, "CreditRJMCRevolvingWorld", R.drawable.credit_rj_world,R.string.credit_rjmc_resolving_world),
    CreditRJMCRevolvingWorldElite(8, "CreditRJMCRevolvingWorldElite", R.drawable.credit_rj_world_elite,R.string.credit_rjmc_resolving_world_elite),
    DebitMCClassic(9, "DebitMCClassic", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    DebitMCTitanium(10, "DebitMCTitanium", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    PrepaidJKB(11, "PrepaidJKB", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    PrepaidNoneJKB(12, "PrepaidNoneJKB", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    PrepaidWearableCard(13, "PrepaidWearableCard", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    PrepaidDinarakCard(14, "PrepaidDinarakCard", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    PrepaidAMMUCard(15, "PrepaidAMMUCard", R.drawable.credit_classic,R.string.credit_mc_resolving_classic),
    PrepaidPUNVCard(16, "PrepaidPUNVCard", R.drawable.credit_classic,R.string.credit_mc_resolving_classic);


    companion object {
        fun from(id: Int?): CardTypes = values().find { it.id == id } ?: CreditMCRevolvingClassic
    }

}
