package survivalblock.non_unfiction;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonUnfictionClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(NonUnfiction.SYNC_TARGET_UUID_PACKET_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				NonUnfictionUtil.targetUuid = buf.readUuid();
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(NonUnfiction.SYNC_POWER_LIST_PACKET_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				NonUnfictionUtil.whoHasThePower = buf.readList(PacketByteBuf::readUuid);
			});
		});
	}
}