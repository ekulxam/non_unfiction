package survivalblock.non_unfiction.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.non_unfiction.NonUnfiction;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Shadow @Final private MinecraftClient client;

	@WrapWithCondition(method = "render", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
	private boolean removeSendMessage(PlayerListHud instance, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective) {
        // TODO: Find the null check in said method, but if this is null then minecraft has some serious problems
        return !NonUnfiction.equalsTargetUuidString(this.client.player.getUuidAsString());
    }
}