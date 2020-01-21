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

@file:JvmName("Util")
package green.sailor.mc.bsaw

import green.sailor.mc.bsaw.api.BiomeExtendedInfo
import green.sailor.mc.bsaw.api.Season
import green.sailor.mc.bsaw.component.WorldSeasonComponent.Companion.seasonComponent
import green.sailor.mc.bsaw.impl.BiomeInfoMapImpl
import net.minecraft.block.BlockState
import net.minecraft.block.SnowBlock
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionType

fun <T> Registry<T>.entryIterator() = object : Iterator<Pair<Identifier, T>> {
    val idIterator = ids.iterator()
    override fun hasNext(): Boolean = idIterator.hasNext()
    override fun next(): Pair<Identifier, T> {
        val nextId = idIterator.next()
        return Pair(nextId, get(nextId)!!)
    }
}

/**
 * Performs snow block melting.
 */
fun doSnowblockMelt(state: BlockState, pos: BlockPos, world: World) {
    val biome = world.getBiome(pos)
    if (world.getTemperature(biome) > 0.5) {
        val level = state[SnowBlock.LAYERS]
        val nextLevel = level - 1
        if (nextLevel == 0) {
            SnowBlock.dropStacks(state, world, pos)
            world.removeBlock(pos, false)
        } else {
            world.setBlockState(pos, state.with(SnowBlock.LAYERS, nextLevel))
        }
    }
}

/**
 * Turns a rainfall type into a minecraft type.
 */
fun BiomeExtendedInfo.RainfallType.asMcType(pos: BlockPos): Biome.Precipitation {
    return when (this) {
        BiomeExtendedInfo.RainfallType.RAIN -> {
            if (pos.y > 90) Biome.Precipitation.SNOW else Biome.Precipitation.RAIN
        }
        BiomeExtendedInfo.RainfallType.SNOW -> Biome.Precipitation.SNOW
        BiomeExtendedInfo.RainfallType.NONE -> Biome.Precipitation.NONE
    }
}

/**
 * Gets the season for this world.
 */
val World.season: Season get() = Season.seasonFromTime(time)

/**
 * Gets the temperature for a biome on this world.
 */
fun World.getTemperature(biome: Biome): Double {
    val component = this.seasonComponent
    return component.getBiomeTemp(biome)
}

/**
 * If it is raining in the specified biome.
 */
fun World.isRainingIn(biome: Biome): Boolean {
    if (!isRaining) return false

    val info = biome.extraInfo
    val temperature = world.getTemperature(biome)
    val type = info.rainfallTypeFor(temperature)
    return isRaining && type === BiomeExtendedInfo.RainfallType.RAIN
}

/**
 * If it is snowing in the specified biome.
 */
fun World.isSnowingIn(biome: Biome): Boolean {
    if (!isRaining) return false

    val info = biome.extraInfo
    val temperature = world.getTemperature(biome)
    val type = info.rainfallTypeFor(temperature)
    return isRaining && type === BiomeExtendedInfo.RainfallType.SNOW
}

/**
 * If the world has a form of downpour currently happening.
 */
val World.hasDownpour: Boolean get() = isRaining

val UNKNOWN_IDENTIFIER = Identifier("bsaw:unknown")

/**
 * Gets the extra info for this biome.
 */
val Biome.extraInfo: BiomeExtendedInfo get() {
    val id = Registry.BIOME.getId(this) ?: UNKNOWN_IDENTIFIER
    return BiomeInfoMapImpl.get(id.toString())
}

/** If the specified world is the overworld. */
val World.isOverworld: Boolean get() = world.dimension.type === DimensionType.OVERWORLD
