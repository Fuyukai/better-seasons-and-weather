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
import green.sailor.mc.bsaw.extraInfo
import green.sailor.mc.bsaw.getTemperature
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

/**
 * A fake biome wrapper used in MixinWorldWrapper.
 */
@Environment(EnvType.CLIENT)
class BiomeWrapper(val wrapped: Biome, val world: World, settings: Settings) :
    Biome(settings) {
    companion object {
        // cache to avoid allocating hundreds of objects a second
        private val wrapperCache: MutableMap<Pair<Biome, World>, BiomeWrapper> = mutableMapOf()

        @JvmStatic fun wrap(other: Biome, world: World): BiomeWrapper {
            return wrapperCache.computeIfAbsent(Pair(other, world)) {
                val settings = Settings().apply {
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
        val info = wrapped.extraInfo
        val temp = world.getTemperature(wrapped)
        return when (info.rainfallTypeFor(temp)) {
            BiomeExtendedInfo.RainfallType.NONE -> Precipitation.NONE
            BiomeExtendedInfo.RainfallType.RAIN -> Precipitation.RAIN
            BiomeExtendedInfo.RainfallType.SNOW -> Precipitation.SNOW
        }
    }

    // this is only used for WorldRenderer
    override fun computeTemperature(blockPos: BlockPos): Float {
        val info = wrapped.extraInfo
        val temp = world.getTemperature(wrapped)
        val type = info.rainfallTypeFor(temp)
        return if (type == BiomeExtendedInfo.RainfallType.SNOW) {
            // < 0.15 is snow
            0.0f
        } else if (type == BiomeExtendedInfo.RainfallType.RAIN) {
            // high Y coords always cause snow if it can rain
            if (blockPos.y < 90) {
                0.20f
            } else {
                0.00f
            }
        } else {
            1.0f
        }
    }
}
