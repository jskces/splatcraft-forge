package net.splatcraft.forge.network.s2c;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.splatcraft.forge.data.capabilities.blockcolorinfo.ColorInfoCapability;

public class UpdateChunkBlockColorsPacket extends PlayToClientPacket
{
    int chunkX;
    int chunkZ;
    CompoundNBT nbt;

    public UpdateChunkBlockColorsPacket(Chunk chunk)
    {
        this(chunk.getPos().x, chunk.getPos().z, ColorInfoCapability.get(chunk).writeNBT(new CompoundNBT()));
    }

    public UpdateChunkBlockColorsPacket(int chunkX, int chunkZ, CompoundNBT info)
    {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.nbt = info;
    }

    public static UpdateChunkBlockColorsPacket decode(PacketBuffer buffer)
    {
        return new UpdateChunkBlockColorsPacket(buffer.readInt(), buffer.readInt(), buffer.readNbt());
    }

    @Override
    public void execute()
    {
		UpdateWorldBlockColorsPacket.COLOR_DATA_CACHE.put(new ChunkPos(chunkX, chunkZ), nbt); //caching bc chunk caps get reset right after ChunkWatchEvent.Watch
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeInt(chunkX);
        buffer.writeInt(chunkZ);
        buffer.writeNbt(nbt);
    }
}
