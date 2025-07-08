package io.github.sirius902.hexteleportstabilizer.datagen;

import at.petrak.hexcasting.common.lib.HexBlocks;
import io.github.sirius902.hexteleportstabilizer.StabilizerMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StabilizerRecipes extends RecipeProvider {
    public StabilizerRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike p_125978_) {
        return paucalInventoryTrigger(ItemPredicate.Builder.item().of(p_125978_).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(TagKey<Item> p_206407_) {
        return paucalInventoryTrigger(ItemPredicate.Builder.item().of(p_206407_).build());
    }

    /**
     * Prefixed with {@code paucal} to avoid collisions when Forge ATs {@link RecipeProvider#inventoryTrigger}.
     */
    protected static Criterion<InventoryChangeTrigger.TriggerInstance> paucalInventoryTrigger(ItemPredicate... $$0) {
        return new Criterion<>(
            CriteriaTriggers.INVENTORY_CHANGED,
            new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of($$0))
        );
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipes) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, StabilizerMod.ITEM_TELEPORT_STABILIZER)
            .define('L', Items.LODESTONE)
            .define('C', Items.CHORUS_FRUIT)
            .define('S', HexBlocks.SLATE_BLOCK.asItem())
            .pattern("SCS")
            .pattern("CLC")
            .pattern("SCS")
            .unlockedBy("has_item", hasItem(Items.LODESTONE)).save(recipes);
    }
}
