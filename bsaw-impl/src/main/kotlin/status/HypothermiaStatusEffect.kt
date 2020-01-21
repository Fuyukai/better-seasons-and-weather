package green.sailor.mc.bsaw.status

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType

/**
 * Represents the status effect for hypothermia.
 */
object HypothermiaStatusEffect : StatusEffect(StatusEffectType.HARMFUL, 0x0000ff00) {
    @JvmStatic val HYPOTHERMIA_SOURCE = object : DamageSource("hypothermia") {}

    @JvmStatic fun get(): StatusEffectInstance {
        return StatusEffectInstance(this, 20 * 90)
    }

    override fun canApplyUpdateEffect(duration: Int, i: Int): Boolean {
        return duration.rem(20 * 5) == 0
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.damage(HYPOTHERMIA_SOURCE, 0.5f)
    }
}
