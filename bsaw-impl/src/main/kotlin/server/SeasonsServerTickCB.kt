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

package green.sailor.mc.bsaw.server

import green.sailor.mc.bsaw.api.Season
import green.sailor.mc.bsaw.api.Time
import green.sailor.mc.bsaw.component.WorldSeasonComponent.Companion.seasonComponent
import green.sailor.mc.bsaw.season.WorldSeasonComponentImpl
import net.fabricmc.fabric.api.event.server.ServerTickCallback
import net.minecraft.server.MinecraftServer
import net.minecraft.world.World

/**
 * The tick callback handling seasons.
 */
class SeasonsServerTickCB(val overworld: World) : ServerTickCallback {
    override fun tick(server: MinecraftServer) {
        val component = overworld.seasonComponent as WorldSeasonComponentImpl

        // Branch A: Sunrise, forcibly update all temperatures.
        if (overworld.timeOfDay == 1L) {
            val season = Season.seasonFromTime(overworld.time)
            component.updateAllBiomeTemps(season = season, at = Time.SUNRISE)
        }

        // Branch B: Sunset, forcibly update all temperatures.
        if (overworld.timeOfDay == 12000L) {
            val season = Season.seasonFromTime(overworld.time)
            component.updateAllBiomeTemps(season = season, at = Time.SUNSET)
        }

        // Branch C: Not one of those times; but our component is empty (first run).
        if (component.biomeTemperatures.isEmpty()) {
            val season = Season.seasonFromTime(overworld.time)
            component.updateAllBiomeTemps(season = season, at = Time.SUNRISE)
        }
    }
}
