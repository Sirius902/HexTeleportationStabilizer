package io.github.sirius902.hexteleportstabilizer;

import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.common.casting.actions.spells.great.OpTeleport;
import at.petrak.hexcasting.common.lib.HexAttributes;
import io.github.sirius902.hexteleportstabilizer.block.BlockTeleportStabilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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
            var destination = player.position().add(delta);
            var ambitRadius = player.getAttributeValue(HexAttributes.AMBIT_RADIUS);

            var activeStabilizers = new ArrayList<Tuple<BlockPos, BlockTeleportStabilizer>>();
            var inactiveStabilizers = new ArrayList<Tuple<BlockPos, BlockTeleportStabilizer>>();
            var searchRadius = (int)Math.ceil(ambitRadius);
            for (var dz = -searchRadius; dz < searchRadius; dz++) {
                for (var dy = -searchRadius; dy < searchRadius; dy++) {
                    for (var dx = -searchRadius; dx < searchRadius; dx++) {
                        var x = (int)Math.floor(destination.x()) + dx;
                        var y = (int)Math.floor(destination.y()) + dy;
                        var z = (int)Math.floor(destination.z()) + dz;

                        var blockPos = new BlockPos(x, y, z);
                        var block = env.getWorld().getBlockState(blockPos);
                        var dist = destination.distanceToSqr(x, y, z);

                        if (dist <= ambitRadius * ambitRadius + 0.00000000001 && block.getBlock() instanceof BlockTeleportStabilizer stabilizer) {
                            if (block.getValue(BlockTeleportStabilizer.ACTIVATED)) {
                                activeStabilizers.add(new Tuple<>(blockPos, stabilizer));
                            } else {
                                inactiveStabilizers.add(new Tuple<>(blockPos, stabilizer));
                            }
                        }
                    }
                }
            }

            if (!activeStabilizers.isEmpty() || !inactiveStabilizers.isEmpty()) {
                var selected = inactiveStabilizers.isEmpty() ?
                    activeStabilizers.get(env.getWorld().getRandom().nextInt(activeStabilizers.size())) :
                    inactiveStabilizers.get(env.getWorld().getRandom().nextInt(inactiveStabilizers.size()));

                var blockPos = selected.getA();
                var stabilizer = selected.getB();

                var world = env.getWorld();
                OpTeleport.INSTANCE.teleportRespectSticky(player, delta, world);
                stabilizer.activate(world, player, blockPos, env);
                return;
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
