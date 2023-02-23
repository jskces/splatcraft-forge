package net.splatcraft.forge.data.capabilities.blockcolorinfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorInfoCapability implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IBlockColorInfo.class)
    public static final Capability<IBlockColorInfo> CAPABILITY = null;
    private final LazyOptional<IBlockColorInfo> instance = LazyOptional.of(CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IBlockColorInfo.class, new ColorInfoStorage(), () -> new BlockColorInfo());
    }

    public static IBlockColorInfo get(Chunk chunk) throws NullPointerException
    {
        return chunk.getCapability(CAPABILITY).orElseThrow(() -> new NullPointerException("Couldn't find ColorInfo capability!"));
    }

    public static IBlockColorInfo get(World level, BlockPos pos)
    {
        return get(level.getChunkAt(pos));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        return (CompoundNBT) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }

}
