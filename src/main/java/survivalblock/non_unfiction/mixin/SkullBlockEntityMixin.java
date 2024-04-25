package survivalblock.non_unfiction.mixin;

import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.ApiServices;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.non_unfiction.NonUnfictionUtil;

import java.util.concurrent.Executor;

@Mixin(SkullBlockEntity.class)
public class SkullBlockEntityMixin {

    @Inject(method = "setServices", at = @At("RETURN"))
    private static void setServicesForUtil(ApiServices apiServices, Executor executor, CallbackInfo ci){
        NonUnfictionUtil.setServices(apiServices, executor);
    }

    @Inject(method = "clearServices", at = @At("RETURN"))
    private static void clearServicesForUtil(CallbackInfo ci){
        NonUnfictionUtil.clearServices();
    }
}
