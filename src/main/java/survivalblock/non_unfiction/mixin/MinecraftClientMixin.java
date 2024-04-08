package survivalblock.non_unfiction.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.non_unfiction.NonUnfictionUtil;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isIntegratedServerRunning()Z"))
    private boolean wreckingBallOne(MinecraftClient instance, Operation<Boolean> original){
        if(this.player != null && NonUnfictionUtil.equalsTarget(this.player.getUuid())){
            return true; // aaaaa how was the player null
        }
        return original.call(instance);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isRemote()Z"))
    private boolean wreckingBallTwo(IntegratedServer instance, Operation<Boolean> original){
        if(this.player != null && NonUnfictionUtil.equalsTarget(this.player.getUuid())){
            return false;
        }
        return original.call(instance);
    }
}
