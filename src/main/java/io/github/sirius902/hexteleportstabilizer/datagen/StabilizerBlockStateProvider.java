package io.github.sirius902.hexteleportstabilizer.datagen;

import io.github.sirius902.hexteleportstabilizer.StabilizerMod;
import io.github.sirius902.hexteleportstabilizer.block.BlockTeleportStabilizer;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class StabilizerBlockStateProvider extends BlockStateProvider {
    public StabilizerBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper) {
        super(output, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        var unactivatedModel = models().cubeColumn(
            "teleport_stabilizer",
            modLoc("block/teleport_stabilizer_side"),
            modLoc("block/teleport_stabilizer_top")
        );

        var activatedModel = models().cubeColumn(
            "teleport_stabilizer_activated",
            modLoc("block/teleport_stabilizer_side_activated"),
            modLoc("block/teleport_stabilizer_top")
        );

        getVariantBuilder(StabilizerMod.BLOCK_TELEPORT_STABILIZER.get())
            .forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.getValue(BlockTeleportStabilizer.ACTIVATED) ? activatedModel : unactivatedModel)
                .build());

        itemModels().getBuilder("teleport_stabilizer")
            .parent(unactivatedModel);
    }
}
