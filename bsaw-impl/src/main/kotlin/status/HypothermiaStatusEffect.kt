/*
 * This file is part of BSAW.
 *
 * BSAW is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BSAW is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BSAW.  If not, see <https://www.gnu.org/licenses/>.
 */

package green.sailor.mc.bsaw.status

import net.minecraft.entity.LivingEntity
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
