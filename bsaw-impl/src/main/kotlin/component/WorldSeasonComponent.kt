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

import green.sailor.mc.bsaw.api.component.WorldSeasonProvider
import green.sailor.mc.bsaw.season.WorldSeasonComponentImpl
import nerdhub.cardinal.components.api.util.sync.WorldSyncedComponent
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

interface WorldSeasonComponent : WorldSyncedComponent, WorldSeasonProvider {
    companion object {
        @JvmStatic val World.seasonComponent: WorldSeasonComponent get() =
            WorldSeasonComponentImpl.WORLD_SEASONS.get(this)
    }

    /**
     * Gets the current temperature for the specified [Biome].
     */
    fun getBiomeTemp(biome: Biome): Double
}
