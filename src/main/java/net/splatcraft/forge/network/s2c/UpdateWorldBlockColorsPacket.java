package net.splatcraft.forge.network.s2c;

import net.minecraft.block.PistonBlockStructureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.splatcraft.forge.data.capabilities.blockcolorinfo.ColorInfoCapability;
import net.splatcraft.forge.data.capabilities.worldinfo.IWorldInfo;
import net.splatcraft.forge.data.capabilities.worldinfo.WorldInfoCapability;
import net.splatcraft.forge.network.SplatcraftPacketHandler;
import net.splatcraft.forge.util.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateWorldBlockColorsPacket extends PlayToClientPacket
{
	private final ArrayList<Long> packagedData;

	public UpdateWorldBlockColorsPacket(ArrayList<Long> packagedData) {
		this.packagedData = packagedData;
	}

	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(packagedData.size());
		for(long i : packagedData)
			buffer.writeLong(i);
	}

	public static UpdateWorldBlockColorsPacket decode(PacketBuffer buffer)
	{
		int size = buffer.readInt();

		ArrayList<Long> data = new ArrayList<>();

		for(int i = 0; i < size; i++)
			data.add(buffer.readLong());

		return new UpdateWorldBlockColorsPacket(data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void execute()
	{
		for(int i = 0; i < packagedData.size(); i+= 3)
		{
			ColorInfoCapability.get(Minecraft.getInstance().player.level.getChunk(Math.toIntExact(packagedData.get(i)), Math.toIntExact(packagedData.get(i + 1))))
				.decode(String.format("%012x", packagedData.get(i+2)));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static final HashMap<ChunkPos, CompoundNBT> COLOR_DATA_CACHE = new HashMap<>();

	@Mod.EventBusSubscriber
	public static class Subscriber
	{
		@SubscribeEvent
		public static void onWorldTick(TickEvent.WorldTickEvent event)
		{
			IWorldInfo worldInfo = WorldInfoCapability.get(event.world);
			if(worldInfo.getPendingColorData().size() > 0)
				SplatcraftPacketHandler.sendToDim(new UpdateWorldBlockColorsPacket(worldInfo.getPendingColorData()), event.world);
			worldInfo.getPendingColorData().clear();
		}

		@SubscribeEvent
		public static void onChunkWatch(ChunkWatchEvent event)
		{
			SplatcraftPacketHandler.sendToPlayer(new UpdateChunkBlockColorsPacket(event.getWorld().getChunk(event.getPos().x, event.getPos().z)), event.getPlayer());
		}

		@SubscribeEvent
		public static void onChunkLoad(ChunkEvent.Load event)
		{
			if(event.getChunk() instanceof Chunk && event.getWorld().isClientSide() && COLOR_DATA_CACHE.containsKey(event.getChunk().getPos()))
			{
				ColorInfoCapability.get((Chunk) event.getChunk()).readNBT(COLOR_DATA_CACHE.get(event.getChunk().getPos()));
				COLOR_DATA_CACHE.remove(event.getChunk().getPos());
			}
		}

		private static PistonBlockStructureHelper cachedPushHelper;

		//putting this here bc i don't want to make a block colors handler class
		@SubscribeEvent
		public static void onPistonPush(PistonEvent.Pre event)
		{
			System.out.println("pre!");
			cachedPushHelper = event.getStructureHelper();
			cachedPushHelper.resolve();
		}
	}
}
