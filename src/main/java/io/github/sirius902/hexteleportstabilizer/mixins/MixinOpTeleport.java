package io.github.sirius902.hexteleportstabilizer.mixins;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.common.casting.actions.spells.great.OpTeleport;
import io.github.sirius902.hexteleportstabilizer.StabilizerTeleportSpell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OpTeleport.class)
public abstract class MixinOpTeleport {
    @Shadow
    public abstract int getArgc();

    @Inject(method = "execute", at = @At("RETURN"), cancellable = true)
    public void execute(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<SpellAction.Result> cir) {
        var argc = this.getArgc();
        var teleportee = OperatorUtils.getEntity(args, env.getWorld(), 0, argc);
        var delta = OperatorUtils.getVec3(args, 1, argc);

        var result = cir.getReturnValue();
        cir.setReturnValue(new SpellAction.Result(
            new StabilizerTeleportSpell(teleportee, delta, result.component1()), result.component2(), result.component3(), result.component4()));
    }
}
