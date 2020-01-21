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

package green.sailor.mc.bsaw.mixin;

import green.sailor.mc.bsaw.Util;
import green.sailor.mc.bsaw.api.BiomeExtendedInfo;
import green.sailor.mc.bsaw.api.Season;
import green.sailor.mc.bsaw.component.WorldSeasonComponent;
import green.sailor.mc.bsaw.impl.BiomeInfoMapImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
    /*
     * Mixin that edits tickChunk to use our own biome methods for checking if snow should be
     * placed.
     */
    @Redirect(method = "tickChunk", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/biome/Biome;" +
            "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"
    ))
    public boolean fixCanSetIce(Biome biome, WorldView worldView, BlockPos blockPos) {
        ServerWorld thisRef = (ServerWorld)(Object)this;

        Identifier id = Registry.BIOME.getId(biome);
        assert id != null;
        BiomeExtendedInfo info = BiomeInfoMapImpl.INSTANCE.get(id.toString());
        double temp = Util.getTemperatureAt(thisRef, blockPos);
        BiomeExtendedInfo.RainfallType type = info.rainfallTypeFor(temp);
        if (type != BiomeExtendedInfo.RainfallType.SNOW) return false;

        // default impl
        if (blockPos.getY() >= 0
            && blockPos.getY() < 256
            && worldView.getLightLevel(LightType.BLOCK, blockPos) < 10) {
            BlockState blockState = worldView.getBlockState(blockPos);
            return blockState.isAir()
                && Blocks.SNOW.getDefaultState().canPlaceAt(worldView, blockPos);
        }
        return false;
    }

    // disabled for now; this looks super dumb.
    /*
     * Mixin that edits the setBlockState call to change the snow level.
     */
    /*@Redirect(method = "tickChunk", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/server/world/ServerWorld;" +
            "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z",
        ordinal = 1
    ))
    boolean redirectSetBS(ServerWorld serverWorld, BlockPos pos, BlockState blockState) {
        int size = serverWorld.random.nextInt(6) + 1;
        BlockState state = blockState.with(SnowBlock.LAYERS, size);
        return serverWorld.setBlockState(pos, state);
    }*/

}
