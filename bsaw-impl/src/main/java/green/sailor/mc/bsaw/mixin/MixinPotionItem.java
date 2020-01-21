package green.sailor.mc.bsaw.mixin;

import green.sailor.mc.bsaw.component.PlayerHeatComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class MixinPotionItem {
    @Inject(
        method = "finishUsing",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/potion/PotionUtil;getPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;"
        ),
        cancellable = false
    )
    void onPotionFinishUsing(ItemStack stack, World world, LivingEntity user,
                             CallbackInfoReturnable<ItemStack> cir) {
        if (!(user instanceof PlayerEntity)) return;
        if (world.isClient) return;

        Potion potion = PotionUtil.getPotion(stack);
        if (potion == null) return;
        if (potion != Potions.WATER) return;
        PlayerHeatComponent component = PlayerHeatComponent.getHeatComponent((PlayerEntity)user);
        component.coolOff(1.0D);
    }
}
