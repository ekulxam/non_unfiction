package survivalblock.non_unfiction.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.non_unfiction.NonUnfiction;
import survivalblock.non_unfiction.NonUnfictionUtil;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Unique
    private boolean shouldFreezeTicks = false;
    @Unique
    private boolean wasTickFrozenAlready = false;
    @Shadow @Nullable public ClientPlayerEntity player;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isIntegratedServerRunning()Z"))
    private boolean wreckingBallOne(MinecraftClient instance, Operation<Boolean> original){
        if(this.player != null && this.player.getServer() != null && NonUnfictionUtil.equalsTarget(this.player.getUuid())){
            TickManager tickManager = this.player.getServer().getTickManager();
            if (!tickManager.isFrozen()) {
                if (this.shouldFreezeTicks) {
                    tickManager.setFrozen(true);
                } else {
                    if (!this.wasTickFrozenAlready) {
                        tickManager.setFrozen(false);
                    }
                }
            } else {
                this.wasTickFrozenAlready = true;
            }
        }
        return original.call(instance);
    }
}
