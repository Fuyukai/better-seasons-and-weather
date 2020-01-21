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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

/**
 * Mixin to fix the snow block class.
 */
@SuppressWarnings("deprecation")
@Mixin(SnowBlock.class)
public abstract class MixinSnowBlock extends Block {
    // stub
    private MixinSnowBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Util.doSnowblockMelt(state, pos, world);
    }

    // disabled in favour of our own function
    /*
     * Makes it so that snow melts progressively, instead of instantly.
     */
    /*@Inject(method = "scheduledTick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/block/SnowBlock;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"
    ), cancellable = true)
    void overrideSnowBlockMeltBehaviour(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        int level = state.get(SnowBlock.LAYERS);
        // if the level is 1, continue with normal behaviour
        if (level == 1) {
            return;
        }
        BlockState newState = state.with(SnowBlock.LAYERS, level - 1);
        world.setBlockState(pos, newState);
        ci.cancel();
    }*/

}
