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
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

/**
 * Implementation of the biome info map.
 */
object BiomeInfoMapImpl : BiomeInfoMap {
    private val LOGGER = LogManager.getLogger()
    private val warningIdsUnseen = mutableSetOf<Identifier>()

    /** The actual map of biome infos. */
    val biomeInfo: MutableMap<Identifier, BiomeExtendedInfo> = mutableMapOf(
        Identifier("bsaw:unknown") to BiomeInfoMap.DEFAULT
    )

    override fun add(identifier: String, info: BiomeExtendedInfo) {
        biomeInfo[Identifier(identifier)] = info
    }

    override fun add(info: BiomeExtendedInfo): Unit = add(info.identifier, info)

    override fun get(identifier: String): BiomeExtendedInfo {
        val id = Identifier(identifier)
        val info = biomeInfo[id]
        if (info == null) {
            // this ensures we only warn once rather than every time
            if (id !in warningIdsUnseen) {
                LOGGER.warn("No extra biome info registered for $identifier!")
                warningIdsUnseen.add(id)
            }
            return BiomeInfoMap.DEFAULT
        }
        return info
    }
}
