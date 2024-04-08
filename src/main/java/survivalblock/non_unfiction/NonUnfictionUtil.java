package survivalblock.non_unfiction;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NonUnfictionUtil {
    public static UUID targetUuid;
    public static List<UUID> whoHasThePower = new ArrayList<>();

    public static Identifier id(String id) {
        return new Identifier(NonUnfiction.MOD_ID, id);
    }
    public static boolean isInPowerList(ServerPlayerEntity serverPlayerEntity){
        for (UUID playerUuidInList : whoHasThePower) {
            if (Objects.equals(serverPlayerEntity.getUuid(), playerUuidInList)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isInPowerList(CommandOutput cmdOutput){
        for (UUID playerUuidInList : whoHasThePower) {
            if (cmdOutput instanceof PlayerEntity player && Objects.equals(player.getUuid(), playerUuidInList)) {
                return true;
            }
        }
        return false;
    }

    public static void removeTarget(){
        NonUnfictionUtil.targetUuid = UUID.fromString("");
    }

    public static boolean equalsTarget(UUID uuid){
        return uuid.equals(NonUnfictionUtil.targetUuid);
    }

    public static void sendTargetUuidPacket(PacketByteBuf buf, MinecraftServer server){
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, NonUnfiction.SYNC_TARGET_UUID_PACKET_ID, buf);
        }
    }

    public static void sendPowerListPacket(PacketByteBuf buf, MinecraftServer server){
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, NonUnfiction.SYNC_POWER_LIST_PACKET_ID, buf);
        }
    }
}
