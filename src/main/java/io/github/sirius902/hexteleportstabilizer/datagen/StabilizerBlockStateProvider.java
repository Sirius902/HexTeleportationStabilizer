package io.github.sirius902.hexteleportstabilizer.datagen;

import io.github.sirius902.hexteleportstabilizer.StabilizerMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class StabilizerBlockStateProvider extends BlockStateProvider {
    public StabilizerBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper) {
        super(output, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(StabilizerMod.BLOCK_TELEPORT_STABILIZER.get(), models().getExistingFile(mcLoc("block/lodestone")));
    }
}
