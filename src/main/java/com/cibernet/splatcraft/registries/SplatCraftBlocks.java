package com.cibernet.splatcraft.registries;

import com.cibernet.splatcraft.blocks.*;
import com.cibernet.splatcraft.utils.TabSplatCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SplatCraftBlocks
{

    public static Block inkedBlock = new BlockInked();

    public static Block emptyInkwell = new BlockInkwellEmpty();
    public static Block inkwell = new BlockInkwell();
    public static Block canvas = new BlockCanvas();

    public static Block sunkenCrate = new BlockSunkenCrate();
    public static Block oreSardinium = new BlockOre(1, "oreSardinium", "sardinium_ore");

    public static Block PowerEggBlock = new BlockPowerEggStorage();
    
    public static Block inkwellVat = new BlockInkwellVat();
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();
        
        registerBlock(registry, inkedBlock);
        registerBlock(registry, emptyInkwell, true);
        registerBlock(registry, inkwell);
        registerBlock(registry, canvas, true);
        registerBlock(registry, sunkenCrate, true);
        registerBlock(registry, oreSardinium, true);
        registerBlock(registry, PowerEggBlock, true);
        
        registerBlock(registry,inkwellVat, true);

    }


    private static Block registerBlock(IForgeRegistry<Block> registry, Block block, boolean... hasItem)
    {
        registry.register(block);
        SplatCraftModelManager.blocks.add(block);

        if(hasItem.length > 0)
            SplatCraftItems.itemBlocks.add(block);

        return block;
    }
}
