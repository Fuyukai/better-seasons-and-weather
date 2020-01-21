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

import green.sailor.mc.bsaw.api.component.PlayerTemperatureProvider
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent
import net.minecraft.entity.player.PlayerEntity

/**
 * Dummy interface for the player heat component.
 */
interface PlayerHeatComponent : EntitySyncedComponent, PlayerTemperatureProvider {
    companion object {
        @JvmStatic
        val PlayerEntity.heatComponent: PlayerHeatComponent get() =
            PlayerHeatComponentImpl.PLAYER_HEAT.get(this)
    }
}
