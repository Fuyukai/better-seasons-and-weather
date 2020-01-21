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

package green.sailor.mc.bsaw.impl

import green.sailor.mc.bsaw.api.BiomeExtendedInfo
import green.sailor.mc.bsaw.api.BiomeInfoMap
import green.sailor.mc.bsaw.api.entry.InfoRegistrator

/**
 * Registers vanilla biomes.
 */
object VanillaBiomeRegistration : InfoRegistrator {
    override fun register(infoMap: BiomeInfoMap) = infoMap.run {
        println("Adding standard biomees")
        // "standard" biomes
        add(BiomeExtendedInfo("minecraft:plains", 18.0, 25.0))
        // live happily!
        add(BiomeExtendedInfo("minecraft:sunflower_plains", 18.0, 25.0))

        // hot biomes
        add(BiomeExtendedInfo.VeryHotBiomeExtendedInfo("minecraft:desert"))
        add(BiomeExtendedInfo.HotBiomeExtendedInfo("minecraft:beach"))

        // cold biomes
        add(BiomeExtendedInfo.VeryColdBiomeExtendedInfo("minecraft:mountains"))
        add(BiomeExtendedInfo.VeryColdBiomeExtendedInfo("minecraft:gravelly_mountains"))
        add(BiomeExtendedInfo.VeryColdBiomeExtendedInfo("minecraft:wooded_mountains"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:mountain_edge"))

        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:stone_shore"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:snowy_beach"))

        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:taiga"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:taiga_hills"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:taiga_mountains"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:giant_tree_taiga"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:giant_tree_taiga_hills"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:giant_spruce_taiga"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:giant_spruce_taiga_hills"))

        // oceans
        add(BiomeExtendedInfo("minecraft:ocean", 10.0, 15.0))
        add(BiomeExtendedInfo("minecraft:deep_ocean", 10.0, 15.0))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:frozen_ocean"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:cold_ocean"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:deep_frozen_ocean"))
        add(BiomeExtendedInfo.ColdBiomeExtendedInfo("minecraft:deep_cold_ocean"))
    }
}
