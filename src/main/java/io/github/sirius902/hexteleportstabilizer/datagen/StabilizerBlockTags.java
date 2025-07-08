package io.github.sirius902.hexteleportstabilizer.datagen;

import io.github.sirius902.hexteleportstabilizer.StabilizerMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class StabilizerBlockTags extends BlockTagsProvider {
    public StabilizerBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper exFileHelper) {
        super(output, lookupProvider, modId, exFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(StabilizerMod.BLOCK_TELEPORT_STABILIZER.get());
    }
}
