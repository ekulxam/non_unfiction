package survivalblock.non_unfiction;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NonUnfiction implements ModInitializer {
	public static final String MOD_ID = "non_unfiction"; // prime example of zero creativity
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Item SHARD_OF_FATE = new ShardOfFate(new FabricItemSettings().maxCount(1));
	public static final Identifier SYNC_POWER_LIST_PACKET_ID = NonUnfictionUtil.id("sync_power_list_packet");
	public static final Identifier SYNC_TARGET_UUID_PACKET_ID = NonUnfictionUtil.id("sync_target_uuid_packet");

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, NonUnfictionUtil.id("shard_of_fate"), SHARD_OF_FATE);

	}
}