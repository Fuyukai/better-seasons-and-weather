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

import nerdhub.cardinal.components.api.ComponentRegistry
import nerdhub.cardinal.components.api.ComponentType
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier

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
    }

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
}
