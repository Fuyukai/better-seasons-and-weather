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

package green.sailor.mc.bsaw.event

import green.sailor.mc.bsaw.api.Season
import green.sailor.mc.bsaw.api.Time
import green.sailor.mc.bsaw.component.PlayerHeatComponent.Companion.heatComponent
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

        @Suppress("CascadeIf")
        // Branch A: Empty temps, force an update.
        if (component.biomeTemperatures.isEmpty()) {
            val season = Season.seasonFromTime(overworld.time)
            component.updateAllBiomeTemps(season = season, at = Time.SUNRISE)
        }

        // Branch B: Sunrise, update all temperatures for daytime.
        else if (overworld.timeOfDay == 1L) {
            val season = Season.seasonFromTime(overworld.time)
            component.updateAllBiomeTemps(season = season, at = Time.SUNRISE)
        }

        // Branch C: Sunset, update all temperatures for nighttime.
        else if (overworld.timeOfDay == 12000L) {
            val season = Season.seasonFromTime(overworld.time)
            component.updateAllBiomeTemps(season = season, at = Time.SUNSET)
        }

        // Update player heats every minute.
        if (overworld.time.rem(Time.MINUTE_LENGTH) == 0L) {
            for (player in overworld.players) {
                val heatComponent = player.heatComponent
                heatComponent.update()
            }
        }
    }
}
