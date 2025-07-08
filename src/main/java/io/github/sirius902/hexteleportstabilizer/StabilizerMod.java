package io.github.sirius902.hexteleportstabilizer;

import io.github.sirius902.hexteleportstabilizer.datagen.StabilizerDataGenerators;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(StabilizerMod.MOD_ID)
public class StabilizerMod {
    public static final String MOD_ID = "hex_teleport_stabilizer";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredBlock<Block> BLOCK_TELEPORT_STABILIZER = BLOCKS.registerSimpleBlock("teleport_stabilizer", slateish().strength(2f, 4f));
    public static final DeferredItem<BlockItem> ITEM_TELEPORT_STABILIZER = ITEMS.registerSimpleBlockItem("teleport_stabilizer", BLOCK_TELEPORT_STABILIZER, new Item.Properties());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HEX_TELEPORT_STABILIZER_TAB = CREATIVE_MODE_TABS.register("hex_teleport_stabilizer_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.hex_teleport_stabilizer"))
        .icon(() -> ITEM_TELEPORT_STABILIZER.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(BLOCK_TELEPORT_STABILIZER.get());
        }).build());

    private static BlockBehaviour.Properties slateish() {
        return BlockBehaviour.Properties
            .ofFullCopy(Blocks.DEEPSLATE_TILES)
            .strength(4f, 4f);
    }

    public StabilizerMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.register(StabilizerDataGenerators.class);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
