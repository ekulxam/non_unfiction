package survivalblock.non_unfiction;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.tick.TickManager;

public class NonUnfictionServer implements DedicatedServerModInitializer {

    public static int stopwatch = 0;
    private static boolean wasTickFrozenAlready = false;
    @Override
    public void onInitializeServer() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ++stopwatch;
            if (stopwatch < 1800 && stopwatch > -1) {
                return;
            }
            NonUnfictionUtil.sendTargetUuidPacket(PacketByteBufs.create(), server);
            NonUnfictionUtil.sendPowerListPacket(PacketByteBufs.create(), server);
            stopwatch = 0;
        });
        ServerPlayNetworking.registerGlobalReceiver(NonUnfiction.SYNC_TICK_WITH_SERVER, (server, player, handler, buf, responseSender) -> {
            boolean shouldFreezeTicks = buf.readBoolean();
            TickManager tickManager = server.getTickManager();
            if (tickManager.isFrozen()) {
                if (!shouldFreezeTicks) {
                    tickManager.setFrozen(wasTickFrozenAlready);
                } else {
                    wasTickFrozenAlready = true;
                }
            } else {
                if (shouldFreezeTicks) {
                    tickManager.setFrozen(true);
                } else if (!wasTickFrozenAlready) {
                    tickManager.setFrozen(false);
                }
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (!NonUnfictionUtil.equalsTarget(handler.getPlayer().getUuid())) {
                return;
            }
            boolean bl = wasTickFrozenAlready;
            server.getTickManager().setFrozen(true);
            wasTickFrozenAlready = bl;
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            boolean bl = wasTickFrozenAlready;
            if (!NonUnfictionUtil.equalsTarget(handler.getPlayer().getUuid())) {
                server.getTickManager().setFrozen(true);
                wasTickFrozenAlready = bl;
                return;
            }
            server.getTickManager().setFrozen(false);
            wasTickFrozenAlready = bl;
        });
    }
}
