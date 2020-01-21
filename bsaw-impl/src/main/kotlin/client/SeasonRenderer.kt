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

import green.sailor.mc.bsaw.api.Season
import green.sailor.mc.bsaw.component.PlayerHeatComponent.Companion.heatComponent
import green.sailor.mc.bsaw.component.WorldSeasonComponent.Companion.seasonComponent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.language.I18n
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType

/**
 * Represents the component info renderer.
 */
@Environment(EnvType.CLIENT)
class SeasonRenderer : HudRenderCallback {
    companion object {
        @JvmStatic val NICE_COLOURS = mapOf(
            Season.SPRING to Formatting.GREEN.colorValue!!,
            Season.SUMMER to Formatting.GOLD.colorValue!!,
            Season.AUTUMN to 0x00954535,
            Season.WINTER to Formatting.AQUA.colorValue!!
        )
    }

    val SHARED_BP = BlockPos.Mutable()

    override fun onHudRender(tickDelta: Float) {
        if (MinecraftClient.getInstance().options.debugEnabled) return

        // == common client vars == //
        val instance = MinecraftClient.getInstance()
        val world = instance.world ?: return
        val hud = instance.inGameHud
        val fontRenderer = hud.fontRenderer

        // == rendering info == //
        val player = instance.player ?: error("Callback called without a player?")
        val seasonC = world.seasonComponent
        val heatC = player.heatComponent
        val bp = SHARED_BP.set(player)
        val biome = player.world.getBiome(bp)

        fontRenderer.run {
            val fmtTemp = "%.2f °c".format(heatC.temperature)
            draw("Body temperature: $fmtTemp", 10f, 10f, 0x00FFFFFF)
        }

        // fallback if we're not in the overworld
        if (world.dimension.type !== DimensionType.OVERWORLD) {
            fontRenderer.draw("This world has no weather patterns.", 10f, 20f, 0x00FFFFF)
            return
        }

        // renderSeasonInfo()
        fontRenderer.run {
            val season = Season.seasonFromTime(world.time)
            val seasonName = season.name.toLowerCase().capitalize()
            val colour = NICE_COLOURS[season] ?: error("???")

            val width = fontRenderer.getStringWidth("Season: ")
            draw("Season: ", 10f, 25f, 0x00FFFFFF)
            draw(seasonName, 10f + width, 25f, colour)
        }

        // renderTempInfo()
        fontRenderer.run {
            val temp = seasonC.getBiomeTemp(biome)
            val fmtTemp = "%.2f °c".format(temp)

            draw("Ambient temperature: $fmtTemp", 10f, 40f, 0x00FFFFFF)
        }

        // renderBiomeInfo()
        fontRenderer.run {
            val translatedName = I18n.translate(biome.translationKey)
            draw("Biome: $translatedName", 10f, 50f, 0x00FFFFFF)
        }
    }
}
