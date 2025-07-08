package io.github.sirius902.hexteleportstabilizer.datagen;

import io.github.sirius902.hexteleportstabilizer.StabilizerMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class StabilizerBlockLoot extends BlockLootSubProvider {
    protected StabilizerBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return StabilizerMod.BLOCKS.getEntries()
            .stream()
            .map(e -> (Block)e.value())
            .toList();
    }

    @Override
    protected void generate() {
        dropSelf(StabilizerMod.BLOCK_TELEPORT_STABILIZER.get());
    }
}
