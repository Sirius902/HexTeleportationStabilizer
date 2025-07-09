package io.github.sirius902.hexteleportstabilizer.block;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BlockTeleportStabilizer extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    private static final List<Vec3> PARTICLE_FACE_OFFSETS = List.of(
        new Vec3(-0.5, 0.0, 0.0),
        new Vec3(0.5, 0.0, 0.0),
        new Vec3(0.0, 0.0, -0.5),
        new Vec3(0.0, 0.0, 0.5)
    );

    private static void makeParticleLine(Vec3 from, Vec3 to, CastingEnvironment env) {
        var count = 2 * (int)Math.round(from.distanceTo(to));
        for (var i = 0; i < count; i++) {
            var delta = (double)i / count;
            var pos = from.lerp(to, delta);
            ParticleSpray.cloud(pos, 0.5, 10).sprayParticles(env.getWorld(), env.getPigment());
        }
    }

    public BlockTeleportStabilizer(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVATED, false));
    }

    public void activate(ServerLevel world, Entity teleportee, BlockPos pos, CastingEnvironment env) {
        world.setBlockAndUpdate(pos, world.getBlockState(pos)
            .setValue(BlockTeleportStabilizer.ACTIVATED, true));

        assert HexEvalSounds.NORMAL_EXECUTE.sound() != null;
        world.playSound(null, pos, HexEvalSounds.NORMAL_EXECUTE.sound(), SoundSource.BLOCKS, 1f, 1f);

        var posCenter = pos.getCenter();
        var teleporteeCenterPos = teleportee.getPosition(0f).add(0.0, 0.5 * teleportee.getBbHeight(), 0.0);
        Vec3 closestExposedFace = null;
        for (var offset : PARTICLE_FACE_OFFSETS) {
            var offsetPos = posCenter.add(offset);
            var neighborPos = BlockPos.containing(posCenter.add(offset.scale(2)));
            var neighborState = world.getBlockState(neighborPos);

            if (!neighborState.isSolidRender(world, neighborPos) && (closestExposedFace == null || offsetPos.distanceToSqr(teleporteeCenterPos) <= closestExposedFace.distanceToSqr(teleporteeCenterPos))) {
                closestExposedFace = offsetPos;
            }

            ParticleSpray.burst(offsetPos, 1.0, 20).sprayParticles(world, env.getPigment());
        }

        makeParticleLine(closestExposedFace != null ? closestExposedFace : posCenter, teleporteeCenterPos, env);

        world.scheduleTick(pos, this, 20);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVATED)) {
            level.setBlockAndUpdate(pos, state.setValue(ACTIVATED, false));
        }
    }
}
