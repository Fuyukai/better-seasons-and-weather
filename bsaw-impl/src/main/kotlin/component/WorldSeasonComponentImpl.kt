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

package green.sailor.mc.bsaw.season

import green.sailor.mc.bsaw.api.Season
import green.sailor.mc.bsaw.api.Time
import green.sailor.mc.bsaw.component.WorldSeasonComponent
import green.sailor.mc.bsaw.entryIterator
import green.sailor.mc.bsaw.impl.BiomeInfoMapImpl
import nerdhub.cardinal.components.api.ComponentRegistry
import nerdhub.cardinal.components.api.ComponentType
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

/**
 * Implements the world component component.
 */
class WorldSeasonComponentImpl(@JvmField private val _world: World) : WorldSeasonComponent {
    companion object {
        @JvmStatic val WORLD_SEASONS =
            ComponentRegistry.INSTANCE.registerIfAbsent(
                Identifier("bsaw:seasons"),
                WorldSeasonComponent::class.java
            )
    }

    val biomeTemperatures: MutableMap<Identifier, Double> = mutableMapOf()

    override fun getComponentType(): ComponentType<*> = WORLD_SEASONS
    override fun getWorld(): World = _world

    override fun fromTag(tag: CompoundTag) {
        biomeTemperatures.clear()
        val temps = tag.getList("Temperatures", NbtType.COMPOUND)
        for (subtag in temps) {
            if (subtag !is CompoundTag) continue
            val id = Identifier(subtag.getString("BiomeID"))
            val temp = subtag.getDouble("BiomeTemperature")
            biomeTemperatures[id] = temp
        }
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        val tempsTag = ListTag()
        for ((id, temp) in biomeTemperatures.entries) {
            val subtag = CompoundTag().apply {
                putString("BiomeID", id.toString())
                putDouble("BiomeTemperature", temp)
            }
            tempsTag.add(subtag)
        }
        tag.put("Temperatures", tempsTag)
        return tag
    }

    override fun getBiomeTemp(identifier: String): Double {
        val realIdentifier = Identifier(identifier)
        return biomeTemperatures[realIdentifier] ?: 22.0
    }

    /**
     * Gets the temperature from the specified biome.
     */
    override fun getBiomeTemp(biome: Biome): Double {
        val id = Registry.BIOME.getId(biome)!!
        return biomeTemperatures[id] ?: 22.0
    }

    /**
     * Updates all biome temperatures and synchronises to the client.
     */
    override fun updateAllBiomeTemps(season: Season, at: Time) {
        require(!_world.isClient) { "Don't recalculate temperatures on the client!" }
        val isNight = at == Time.SUNSET || at == Time.MIDNIGHT
        biomeTemperatures.clear()

        for ((id, biome) in Registry.BIOME.entryIterator()) {
            val info = BiomeInfoMapImpl.get(id.toString())
            val temp = info.nextRandomTemperature(season, isNight)
            biomeTemperatures[id] = temp
        }
        sync()
    }
}
