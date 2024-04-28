package survivalblock.non_unfiction.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.non_unfiction.NonUnfiction;
import survivalblock.non_unfiction.NonUnfictionUtil;

import java.util.Objects;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    /**
     * removes the invisibility particles when drinking an invis pot
     */
    @WrapOperation(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
    private boolean noInvisParticles(LivingEntity user, StatusEffectInstance statusEffectInstance, Operation<Boolean> original){
        StatusEffect effect = statusEffectInstance.getEffectType();
        if (Objects.equals(effect, StatusEffects.INVISIBILITY) && NonUnfictionUtil.isInPowerList(user)) {
            int duration = statusEffectInstance.getDuration();
            int amplifier = statusEffectInstance.getAmplifier();
            boolean ambient =statusEffectInstance.isAmbient();
            return user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, duration, amplifier, ambient, false, true));
        }
        return original.call(user, statusEffectInstance);
    }
}
