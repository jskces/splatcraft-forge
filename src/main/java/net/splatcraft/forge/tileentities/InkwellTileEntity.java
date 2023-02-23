package net.splatcraft.forge.tileentities;

import net.splatcraft.forge.handlers.SplatcraftCommonHandler;
import net.splatcraft.forge.items.ColoredBlockItem;
import net.splatcraft.forge.registries.SplatcraftTileEntitites;
import net.splatcraft.forge.util.ColorUtils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.HashMap;

@Deprecated
public class InkwellTileEntity extends InkColorTileEntity implements ITickableTileEntity
{

    public InkwellTileEntity()
    {
        super(SplatcraftTileEntitites.inkwellTileEntity);
    }


    @Override
    public void tick()
    {
        ColorUtils.setInkColor(level, worldPosition, getColor());
        setRemoved();
    }
}
