package green.sailor.mc.bsaw.mixin;

import green.sailor.mc.bsaw.Util;
import green.sailor.mc.bsaw.api.BiomeExtendedInfo;
import green.sailor.mc.bsaw.component.WorldSeasonComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
    /*
     * Fixes the hasRain method to use our own methods.
     */
    @Inject(
        method = "hasRain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;"
        ),
        cancellable = true
    )
    void fixHasRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        World thisRef = (World)(Object)this;
        Biome biome = thisRef.getBiome(pos);
        WorldSeasonComponent component = WorldSeasonComponent.getSeasonComponent(thisRef);
        double temp = component.getBiomeTemp(biome);
        BiomeExtendedInfo info = Util.getExtraInfo(biome);
        BiomeExtendedInfo.RainfallType type = info.rainfallTypeFor(temp);
        boolean isRaining = Util.asMcType(type, pos) == Biome.Precipitation.RAIN;
        cir.setReturnValue(isRaining);
    }
}
