package survivalblock.non_unfiction;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class ShardOfFate extends Item {
    public ShardOfFate(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient()) {
            return TypedActionResult.pass(stack);
        }
        if (user instanceof ServerPlayerEntity player) {
            UUID playerUuid = player.getUuid();
            PacketByteBuf buf = PacketByteBufs.create();
            MinecraftServer server = player.getServer();
            if (user.isSneaking()) {
                if (NonUnfictionUtil.isInPowerList(player)) {
                    NonUnfictionUtil.whoHasThePower.remove(playerUuid);
                    NonUnfictionUtil.sendPowerListPacket(buf, server);
                    player.sendMessage(Text.of("You were removed from the power list"));
                } else {
                    NonUnfictionUtil.whoHasThePower.add(playerUuid);
                    NonUnfictionUtil.sendPowerListPacket(buf, server);
                    player.sendMessage(Text.of("You were added to the power list"));
                }
            } else {
                if (NonUnfictionUtil.equalsTarget(playerUuid)) {
                    NonUnfictionUtil.removeTarget();
                    buf.writeUuid(NonUnfictionUtil.targetUuid);
                    NonUnfictionUtil.sendTargetUuidPacket(buf, server);
                    player.sendMessage(Text.of("You are no longer the target"));
                } else {
                    NonUnfictionUtil.targetUuid = playerUuid;
                    buf.writeUuid(NonUnfictionUtil.targetUuid);
                    NonUnfictionUtil.sendTargetUuidPacket(buf, server);
                    player.sendMessage(Text.of("You were set as the target"));
                }
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }
}
