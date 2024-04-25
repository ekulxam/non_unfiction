package survivalblock.non_unfiction.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.non_unfiction.NonUnfiction;
import survivalblock.non_unfiction.NonUnfictionUtil;

@Debug(export = true)
@Mixin(ServerCommandSource.class)
public class ServerCommandSourceMixin {

	@Shadow @Final private CommandOutput output;

	@WrapWithCondition(method = "sendToOps", at= @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;sendMessage(Lnet/minecraft/text/Text;)V"))
	private boolean removeSendMessage(MinecraftServer instance, Text message) {
        return !NonUnfictionUtil.isInPowerList(this.output);
    }

	@WrapWithCondition(method = "sendToOps", at= @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendMessage(Lnet/minecraft/text/Text;)V"))
	private boolean removeServerLog(ServerPlayerEntity instance, Text message) {
        return !NonUnfictionUtil.isInPowerList(this.output);
    }
}