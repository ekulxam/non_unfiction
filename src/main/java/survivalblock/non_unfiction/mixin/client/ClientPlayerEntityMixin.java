package survivalblock.non_unfiction.mixin.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.non_unfiction.NonUnfiction;
import survivalblock.non_unfiction.NonUnfictionUtil;
import survivalblock.non_unfiction.access.ClientPlayerTickAccess;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ClientPlayerTickAccess {
    @Shadow @Final protected MinecraftClient client;
    @Unique
    private boolean previousTickValue = false;
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void freezeTime(CallbackInfo ci){
        if (!NonUnfictionUtil.equalsTarget(this.getUuid())) {
            return;
        }
        Screen screen = this.client.currentScreen;
        PacketByteBuf buf = PacketByteBufs.create();
        boolean shouldFreeze = screen != null && screen.shouldPause();
        if (previousTickValue == shouldFreeze) {
            return;
        }
        previousTickValue = shouldFreeze;
        buf.writeBoolean(shouldFreeze);
        ClientPlayNetworking.send(NonUnfiction.SYNC_TICK_WITH_SERVER, buf);
    }

    @Override
    public boolean non_unfiction$getPreviousTickValue() {
        return this.previousTickValue;
    }
    @Override
    public void non_unfiction$setPreviousTickValue(boolean newValue) {
        this.previousTickValue = newValue;
    }
}
