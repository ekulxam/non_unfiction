package survivalblock.non_unfiction;

import net.fabricmc.api.ModInitializer;

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

public class NonUnfiction implements ModInitializer {
	public static final String MOD_ID = "non_unfiction"; // prime example of zero creativity
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static PlayerEntity targetPlayer;
	private static String targetUuid;
	public static List<ServerPlayerEntity> whoHasThePower = new ArrayList<>();

	public static final Item SHARD_OF_FATE = new ShardOfFate(new FabricItemSettings().maxCount(1));

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, id("shard_of_fate"), SHARD_OF_FATE);
	}

	// why is there no overload annotation (prob because not neccessary but would still be kinda cool)
	public static boolean isInPowerList(ServerPlayerEntity serverPlayerEntity){
		for (ServerPlayerEntity playerInList : whoHasThePower) {
			if (Objects.equals(serverPlayerEntity, playerInList)) {
				return true;
			}
		}
		return false;
	}
	public static boolean isInPowerList(CommandOutput cmdOutput){
		for (ServerPlayerEntity playerInList : whoHasThePower) {
			if (playerInList == cmdOutput) {
				return true;
			}
		}
		return false;
	}

	public static void setTargetPlayer(PlayerEntity targetPlayer){
		NonUnfiction.targetPlayer = targetPlayer;
		NonUnfiction.targetUuid = NonUnfiction.targetPlayer.getUuidAsString();
	}

	public static void removeTargetPlayer(){
		NonUnfiction.targetPlayer = null;
		NonUnfiction.targetUuid = "";
	}

	public static String getTargetUuidAsString(){
		return NonUnfiction.targetUuid;
	}

	public static boolean equalsTargetUuidString(String uuid){
		return uuid.equals(getTargetUuidAsString());
	}

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}
}