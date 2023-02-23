package net.splatcraft.forge.data.capabilities.worldinfo;

import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;

public class WorldInfo implements IWorldInfo
{
    private final ArrayList<Long> pendingColorData = new ArrayList<>();

    @Override
    public ArrayList<Long> getPendingColorData()
    {
        return pendingColorData;
    }

    @Override
    public CompoundNBT writeNBT(CompoundNBT nbt)
    {
        return nbt;
    }

    @Override
    public void readNBT(CompoundNBT nbt)
    {

    }
}
