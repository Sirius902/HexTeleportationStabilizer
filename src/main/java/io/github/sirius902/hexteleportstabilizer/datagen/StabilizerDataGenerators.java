package io.github.sirius902.hexteleportstabilizer.datagen;

import io.github.sirius902.hexteleportstabilizer.StabilizerMod;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class StabilizerDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var exFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(
            event.includeServer(),
            new StabilizerRecipes(output, lookupProvider)
        );

        generator.addProvider(
            event.includeServer(),
            new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(StabilizerBlockLoot::new, LootContextParamSets.BLOCK)
            ), lookupProvider)
        );

        generator.addProvider(
            event.includeServer(),
            new StabilizerBlockTags(output, lookupProvider, StabilizerMod.MOD_ID, exFileHelper)
        );

        generator.addProvider(
            event.includeClient(),
            new StabilizerBlockStateProvider(output, StabilizerMod.MOD_ID, exFileHelper)
        );
    }
}
