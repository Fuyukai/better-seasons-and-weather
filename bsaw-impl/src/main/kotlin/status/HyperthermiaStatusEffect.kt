package green.sailor.mc.bsaw.status

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType

object HyperthermiaStatusEffect : StatusEffect(StatusEffectType.HARMFUL, 0x00FF0000) {
    @JvmStatic val HYPERTHERMIA_SOURCE = object : DamageSource("hyperthermia") {}

    @JvmStatic fun get(): StatusEffectInstance {
        return StatusEffectInstance(this, 20 * 90)
    }

    override fun canApplyUpdateEffect(duration: Int, i: Int): Boolean {
        return duration.rem(20 * 3) == 0
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.damage(HYPERTHERMIA_SOURCE, 1f)
    }
}
