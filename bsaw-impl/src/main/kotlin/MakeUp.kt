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

package green.sailor.mc.bsaw

import green.sailor.mc.bsaw.api.entry.InfoRegistrator
import green.sailor.mc.bsaw.component.PlayerHeatComponentImpl
import green.sailor.mc.bsaw.component.PlayerHeatComponentImpl.Companion.PLAYER_HEAT
import green.sailor.mc.bsaw.event.SeasonsServerTickCB
import green.sailor.mc.bsaw.impl.BiomeInfoMapImpl
import green.sailor.mc.bsaw.season.WorldSeasonComponentImpl
import green.sailor.mc.bsaw.season.WorldSeasonComponentImpl.Companion.WORLD_SEASONS
import green.sailor.mc.bsaw.status.HypothermiaStatusEffect
import nerdhub.cardinal.components.api.event.EntityComponentCallback
import nerdhub.cardinal.components.api.event.WorldComponentCallback
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.server.ServerStartCallback
import net.fabricmc.fabric.api.event.server.ServerTickCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.dimension.DimensionType

object MakeUp : ModInitializer {
    override fun onInitialize() {
        val loader = FabricLoader.getInstance()
        val entryPoints = loader.getEntrypoints("main", ModInitializer::class.java)
        for (point in entryPoints) {
            val pkg = point.javaClass.`package`
            if (pkg.name.startsWith("io.github.indicode")) {
                error("Refusing to start with a vulnerable mod loaded")
            }
        }

        Registry.register(
            Registry.STATUS_EFFECT,
            Identifier("bsaw:hypothermia"),
            HypothermiaStatusEffect
        )

        // call other people
        val entry = loader.getEntrypoints("bsaw-biomes", InfoRegistrator::class.java)
        for (point in entry) {
            point.register(BiomeInfoMapImpl)
        }

        // CCA callbacks
        WorldComponentCallback.EVENT
            .register(WorldComponentCallback { world, components ->
                if (world.dimension.type === DimensionType.OVERWORLD) {
                    components[WORLD_SEASONS] = WorldSeasonComponentImpl(world)
                }
            })

        EntityComponentCallback
            .event(PlayerEntity::class.java)
            .register(EntityComponentCallback { entity, components ->
                components[PLAYER_HEAT] = PlayerHeatComponentImpl(entity)
            })

        // only purpose of the initialiser is to init the other callback
        ServerStartCallback.EVENT.register(ServerStartCallback {
            val world = it.getWorld(DimensionType.OVERWORLD)

            ServerTickCallback.EVENT.register(SeasonsServerTickCB(world))
        })
    }
}
