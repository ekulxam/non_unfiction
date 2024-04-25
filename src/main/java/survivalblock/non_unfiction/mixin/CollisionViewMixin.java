package survivalblock.non_unfiction.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CollisionView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CollisionView.class)
public interface CollisionViewMixin {
    @Inject(at = @At("HEAD"), method = "canPlace", cancellable = true)
    default void canPlace(BlockState state, BlockPos pos, ShapeContext context, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
