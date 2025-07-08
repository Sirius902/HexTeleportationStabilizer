package io.github.sirius902.hexteleportstabilizer;

import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.common.casting.actions.spells.great.OpTeleport;
import at.petrak.hexcasting.common.lib.HexAttributes;
import io.github.sirius902.hexteleportstabilizer.block.BlockTeleportStabilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StabilizerTeleportSpell implements RenderedSpell {
    private final Entity teleportee;
    private final Vec3 delta;
    private final RenderedSpell originalTeleport;

    public StabilizerTeleportSpell(Entity teleportee, Vec3 delta, RenderedSpell originalTeleport) {
        this.teleportee = teleportee;
        this.delta = delta;
        this.originalTeleport = originalTeleport;
    }

    @Override
    public void cast(@NotNull CastingEnvironment env) {
        if (teleportee instanceof ServerPlayer player) {
            var destination = teleportee.position().add(delta);
            var ambitRadius = player.getAttributeValue(HexAttributes.AMBIT_RADIUS);

            var world = env.getWorld();
            var searchRadius = (int)Math.ceil(ambitRadius);
            for (var dz = -searchRadius; dz < searchRadius; dz++) {
                for (var dy = -searchRadius; dy < searchRadius; dy++) {
                    for (var dx = -searchRadius; dx < searchRadius; dx++) {
                        var x = (int)Math.floor(destination.x()) + dx;
                        var y = (int)Math.floor(destination.y()) + dy;
                        var z = (int)Math.floor(destination.z()) + dz;

                        var blockPos = new BlockPos(x, y, z);
                        var block = world.getBlockState(blockPos);
                        var dist = destination.distanceToSqr(x, y, z);

                        if (dist <= ambitRadius * ambitRadius + 0.00000000001 && block.getBlock() instanceof BlockTeleportStabilizer stabilizer) {
                            OpTeleport.INSTANCE.teleportRespectSticky(teleportee, delta, world);
                            stabilizer.activate(world, blockPos, env);
                            return;
                        }
                    }
                }
            }
        }

        this.originalTeleport.cast(env);
    }

    // FUTURE(Sirius902) If OpTeleport Spell actually overrides this it will have to be changed.
    @Override
    public @Nullable CastingImage cast(@NotNull CastingEnvironment env, @NotNull CastingImage image) {
        cast(env);
        return null;
    }
}
