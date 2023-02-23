package net.splatcraft.forge.data.capabilities.blockcolorinfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class BlockColorInfo implements IBlockColorInfo
{
    private final HashMap<BlockPos, ColorData> data = new HashMap<>();

    @Override
    public HashMap<BlockPos, ColorData> getColorData()
    {
        return data;
    }

    @Override
    public void setColor(BlockPos pos, int color)
    {
        pos = new BlockPos(pos.getX() % 16, pos.getY(), pos.getZ() % 16);

        if(color <= -1)
            data.remove(pos);
        else
        {
            if(!data.containsKey(pos))
                data.put(pos, new ColorData());
            data.get(pos).color = color;
        }
    }

    @Override
    public int getColor(BlockPos pos)
    {
        pos = new BlockPos(pos.getX() % 16, pos.getY(), pos.getZ() % 16);

        ColorData cd = data.get(pos);

        return cd == null ? -1 : cd.color;
    }

    public String encode(BlockPos pos)
    {
        ColorData data = this.data.get(pos);

        return String.format("%02d", pos.getX() % 16) + String.format("%02X", pos.getY()) + String.format("%02d", pos.getZ() % 16) + String.format("%06x", data.color);
    }

    public ColorData decode(String str)
    {
        try
        {
            BlockPos pos = new BlockPos(Integer.parseInt(str.substring(0, 2)), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6)));
            ColorData cd = new ColorData();
            cd.color = Integer.parseInt(str.substring(6, 12), 16);

            data.put(pos, cd);

            return cd;
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return null;
    }


    @Override
    public CompoundNBT writeNBT(CompoundNBT nbt)
    {
        ListNBT list = new ListNBT();

        for(BlockPos pos : data.keySet())
            list.add(StringNBT.valueOf(encode(pos)));

        nbt.put("Data", list);

        return nbt;
    }

    @Override
    public void readNBT(CompoundNBT nbt)
    {
        data.clear();

        ListNBT arr = nbt.getList("Data", 8);

        for(int i = 0; i < arr.size(); i++)
            decode(arr.getString(i));
    }

    public static class ColorData
    {
        int color;

        @Override
        public String toString() {
            return "ColorData{" +
                    "color=" + color +
                    '}';
        }
    }
}
