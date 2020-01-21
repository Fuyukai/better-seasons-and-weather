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

package green.sailor.mc.bsaw.client

import green.sailor.mc.bsaw.api.BiomeExtendedInfo
import green.sailor.mc.bsaw.getExtraInfo
import green.sailor.mc.bsaw.getSeason
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

/**
 * A fake biome wrapper used in MixinWorldWrapper.
 */
@Environment(EnvType.CLIENT)
class BiomeWrapper(val wrapped: Biome, val world: World, settings: Biome.Settings) :
    Biome(settings) {
    companion object {
        // cache to avoid allocating hundreds of objects a second
        private val wrapperCache: MutableMap<Biome, BiomeWrapper> = mutableMapOf()

        @JvmStatic fun wrap(other: Biome, world: World): BiomeWrapper {
            return wrapperCache.computeIfAbsent(other) {
                val settings = Biome.Settings().apply {
                    surfaceBuilder(other.surfaceBuilder)
                    precipitation(other.precipitation)
                    category(other.category)
                    depth(other.depth)
                    scale(other.scale)
                    temperature(other.temperature)
                    downfall(other.rainfall)
                    waterColor(other.waterColor)
                    waterFogColor(other.waterFogColor)
                }
                BiomeWrapper(other, world, settings)
            }
        }
    }

    override fun getPrecipitation(): Precipitation {
        val season = world.getSeason()
        val info = wrapped.getExtraInfo()
        return when (info.rainfallTypeFor(season)) {
            BiomeExtendedInfo.RainfallType.NONE -> Precipitation.NONE
            BiomeExtendedInfo.RainfallType.RAIN -> Precipitation.RAIN
            BiomeExtendedInfo.RainfallType.SNOW -> Precipitation.SNOW
        }
    }

    // this is only used for WorldRenderer
    override fun computeTemperature(blockPos: BlockPos): Float {
        val season = world.getSeason()
        val info = wrapped.getExtraInfo()
        val fakeTemp = when (info.rainfallTypeFor(season)) {
            BiomeExtendedInfo.RainfallType.SNOW -> 0.0f
            else -> getTemperature()
        }

        return if (blockPos.y > 64) {
            val f = (TEMPERATURE_NOISE.sample((blockPos.x.toFloat() / 8.0f).toDouble(),
                (blockPos.z.toFloat() / 8.0f).toDouble(), false) * 4.0).toFloat()
            fakeTemp - (f + blockPos.y.toFloat() - 64.0f) * 0.05f / 30.0f
        } else {
            fakeTemp
        }
    }
}
