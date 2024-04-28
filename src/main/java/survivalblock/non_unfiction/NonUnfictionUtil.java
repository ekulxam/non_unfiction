package survivalblock.non_unfiction;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ApiServices;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

// 22 usages lmao
public class NonUnfictionUtil {

    // welcome to this abomination of a util class
    public static UUID targetUuid;
    public static List<UUID> whoHasThePower = new ArrayList<>();
    @Nullable
    private static LoadingCache<String, CompletableFuture<Optional<GameProfile>>> userCache;
    public static Identifier id(String id) {
        return new Identifier(NonUnfiction.MOD_ID, id);
    }
    public static boolean isInPowerList(CommandOutput cmdOutput){
        syncPowerListWithConfig();
        for (UUID playerUuidInList : whoHasThePower) {
            if (cmdOutput instanceof Entity entity && Objects.equals(entity.getUuid(), playerUuidInList)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalsTarget(UUID uuid){
        syncTargetWithConfig();
        return uuid.equals(NonUnfictionUtil.targetUuid);
    }

    public static void syncTargetWithConfig(){
        targetUuid = getUuidFromName(NonUnfictionConfig.targetPlayerName);
    }

    public static void syncPowerListWithConfig(){
        whoHasThePower.clear();
        for (String playerName : NonUnfictionConfig.powerUsernameList) {
            whoHasThePower.add(getUuidFromName(playerName));
        }
    }

    public static void sendTargetUuidPacket(PacketByteBuf buf, MinecraftServer server){
        syncTargetWithConfig();
        if (server == null) {
            return;
        }
        buf.writeString(NonUnfictionConfig.targetPlayerName);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, NonUnfiction.SYNC_TARGET_UUID_PACKET_ID, buf);
        }
    }

    public static void sendPowerListPacket(PacketByteBuf buf, MinecraftServer server){
        syncPowerListWithConfig();
        if (server == null) {
            return;
        }
        buf.writeCollection(NonUnfictionConfig.powerUsernameList, PacketByteBuf::writeString);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, NonUnfiction.SYNC_POWER_LIST_PACKET_ID, buf);
        }
    }

    public static void setServices(final ApiServices apiServices, Executor executor) {
        final BooleanSupplier booleanSupplier = () -> userCache == null;
        userCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(10L)).maximumSize(256L).build(new CacheLoader<String, CompletableFuture<Optional<GameProfile>>>(){

            @Override
            public @NotNull CompletableFuture<Optional<GameProfile>> load(@NotNull String string) {
                if (booleanSupplier.getAsBoolean()) {
                    return CompletableFuture.completedFuture(Optional.empty());
                }
                return fetchProfileByName(string, apiServices, booleanSupplier);
            }
        });
    }
    public static void clearServices() {
        userCache = null;
    }

    protected static CompletableFuture<Optional<GameProfile>> fetchProfileByName(String name, ApiServices apiServices, BooleanSupplier booleanSupplier) {
        return apiServices.userCache().findByNameAsync(name).thenApplyAsync(profile -> {
            if (profile.isPresent() && !booleanSupplier.getAsBoolean()) {
                UUID uUID = profile.get().getId();
                ProfileResult profileResult = apiServices.sessionService().fetchProfile(uUID, true);
                if (profileResult != null) {
                    return Optional.ofNullable(profileResult.profile());
                }
                return profile;
            }
            return Optional.empty();
        }, Util.getMainWorkerExecutor());
    }

    protected static CompletableFuture<Optional<GameProfile>> fetchProfileByName(String name) {
        LoadingCache<String, CompletableFuture<Optional<GameProfile>>> loadingCache = userCache;
        if (loadingCache != null && PlayerEntity.isUsernameValid(name)) {
            return loadingCache.getUnchecked(name);
        }
        return CompletableFuture.completedFuture(Optional.empty());
    }

    public static UUID getUuidFromName(String name){
        AtomicReference<UUID> uuid = new AtomicReference<>(Util.NIL_UUID);
        fetchProfileByName(name).thenAccept(profile -> {
            if (profile.isPresent() && profile.get().getId() != null) {
                uuid.set(profile.get().getId());
            }
        });
        return uuid.get();
    }
}
