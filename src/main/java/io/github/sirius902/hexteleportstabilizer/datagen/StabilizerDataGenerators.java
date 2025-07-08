package io.github.sirius902.hexteleportstabilizer.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class StabilizerDataGenerators {
    // In some event handler class
    @SubscribeEvent // on the mod event bus
    public static void gatherData(GatherDataEvent event) {
        // Data generators may require some of these as constructor parameters.
        // See below for more details on each of these.
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();

        // Register the provider.
        generator.addProvider(
            // A boolean that determines whether the data should actually be generated.
            // The event provides methods that determine this:
            // event.includeClient(), event.includeServer(),
            // event.includeDev() and event.includeReports().
            // Since recipes are server data, we only run them in a server datagen.
            event.includeServer(),
            // Our provider.
            new StabilizerRecipes(output, lookupProvider)
        );
        // Other data providers here.
    }
}
