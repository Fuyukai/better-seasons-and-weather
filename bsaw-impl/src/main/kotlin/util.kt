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
    if (world.getTemperature(biome) < 0.0) {
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
 * Gets the component for this world.
 */
fun World.getSeason(): Season = Season.seasonFromTime(time)

/**
 * Gets the temperature for a biome on this world.
 */
fun World.getTemperature(biome: Biome): Double {
    val component = this.seasonComponent
    return component.getBiomeTemp(biome)
}

val UNKNOWN_IDENTIFIER = Identifier("bsaw:unknown")

/**
 * Gets the extra info for this biome.
 */
fun Biome.getExtraInfo(): BiomeExtendedInfo {
    val id = Registry.BIOME.getId(this) ?: UNKNOWN_IDENTIFIER
    return BiomeInfoMapImpl.get(id.toString())
}
