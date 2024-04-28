package survivalblock.non_unfiction;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.tick.TickManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NonUnfiction implements ModInitializer {
	public static final String MOD_ID = "non_unfiction"; // prime examp	le of zero creativity
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier SYNC_POWER_LIST_PACKET_ID = NonUnfictionUtil.id("sync_power_list_packet");
	public static final Identifier SYNC_TARGET_UUID_PACKET_ID = NonUnfictionUtil.id("sync_target_uuid_packet");
	public static final Identifier SYNC_TICK_WITH_SERVER = NonUnfictionUtil.id("sync_tick_with_server");

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, NonUnfictionConfig.class);
	}
}