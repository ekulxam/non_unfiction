package survivalblock.non_unfiction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

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
            if (user.isSneaking()) {
                if (NonUnfiction.isInPowerList(player)) {
                    NonUnfiction.whoHasThePower.remove(player);
                    player.sendMessage(Text.of("You were removed from the power list"));
                } else {
                    NonUnfiction.whoHasThePower.add(player);
                    player.sendMessage(Text.of("You were added to the power list"));
                }
            } else {
                if (NonUnfiction.equalsTargetUuidString(player.getUuidAsString())) {
                    NonUnfiction.removeTargetPlayer();
                    player.sendMessage(Text.of("You are no longer the target"));
                } else {
                    NonUnfiction.setTargetPlayer(player);
                    player.sendMessage(Text.of("You were set as the target"));
                }
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }
}
