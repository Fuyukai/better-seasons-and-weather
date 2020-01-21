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

package green.sailor.mc.bsaw.component

import green.sailor.mc.bsaw.*
import green.sailor.mc.bsaw.status.HyperthermiaStatusEffect
import green.sailor.mc.bsaw.status.HypothermiaStatusEffect
import nerdhub.cardinal.components.api.ComponentRegistry
import nerdhub.cardinal.components.api.ComponentType
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import kotlin.math.max
import kotlin.math.min

/**
 * Implementation of the player heat component.
 */
class PlayerHeatComponentImpl(val player: PlayerEntity) : PlayerHeatComponent {
    companion object {
        @JvmStatic val PLAYER_HEAT =
            ComponentRegistry.INSTANCE.registerIfAbsent(
                Identifier("bsaw:player_heat"),
                PlayerHeatComponent::class.java
            )

        /** The temperature to scale heat loss with. */
        const val LOW_EFFECT_TEMP = -40.0

        /** The temperature to scale heat gain with. */
        const val HIGH_EFFECT_TEMP = 48.0
    }
    val SHARED_POS = BlockPos.Mutable(player)

    /** The actual temperature. */
    var _temperature: Double = idealTemperature

    override fun getComponentType(): ComponentType<*> = PLAYER_HEAT
    override fun getEntity(): Entity = player

    override fun getTemperature(): Double = _temperature

    override fun fromTag(tag: CompoundTag) {
        val heat = tag.getDouble("Heat")
        _temperature = heat
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putDouble("Heat", _temperature)
        return tag
    }

    override fun update() {
        SHARED_POS.set(player)
        // ???
        if (!player.world.isOverworld) return

        val biome = player.world.getBiome(SHARED_POS)
        val ambient = player.world.getTemperature(biome)
        if (ambient < sweatDissipiationTemperature) {
            // scale down heat loss
            // this gets a range from 0 (no loss) to 1
            val ambientOffset = max(-40.0, ambient - LOW_EFFECT_TEMP)
            var heatLoss = 1 - ambientOffset / (sweatDissipiationTemperature - LOW_EFFECT_TEMP)
            // if there's downpour...
            val playerIsCovered = player.world.isSkyVisible(SHARED_POS)
            if (playerIsCovered) {
                if (player.world.isRainingIn(biome)) {
                    heatLoss += 0.3
                } else if (player.world.isSnowingIn(biome)) {
                    heatLoss += 0.4
                }
            }

            _temperature -= heatLoss

        } else if (ambient > sweatDissipiationTemperature) {
            // scale down heat gain
            val ambientOffset = min(48.0, HIGH_EFFECT_TEMP - ambient)
            var heatGain = 1 - ambientOffset / (HIGH_EFFECT_TEMP - sweatDissipiationTemperature)

            val playerIsCovered = player.world.isSkyVisible(SHARED_POS)
            if (playerIsCovered) {
                if (player.world.isRainingIn(biome)) {
                    heatGain -= 0.1
                } else if (player.world.isSnowingIn(biome)) {
                    heatGain -= 0.2
                }
            }

            _temperature += heatGain

        }

        if (isHypothermic) {
            player.addStatusEffect(HypothermiaStatusEffect.get())
        } else if (isHyperthermic) {
            player.addStatusEffect(HyperthermiaStatusEffect.get())
        }

        sync()
    }
}
