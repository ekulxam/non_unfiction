package survivalblock.non_unfiction;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.UUID;

public class NonUnfictionClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(NonUnfiction.SYNC_TARGET_UUID_PACKET_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				NonUnfictionConfig.targetPlayerName = buf.readString();
				NonUnfictionUtil.syncTargetWithConfig();
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(NonUnfiction.SYNC_POWER_LIST_PACKET_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				NonUnfictionConfig.powerUsernameList = buf.readCollection(ArrayList::new, PacketByteBuf::readString);
				NonUnfictionUtil.syncPowerListWithConfig();
			});
		});
	}
}