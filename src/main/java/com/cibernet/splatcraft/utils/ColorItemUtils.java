package com.cibernet.splatcraft.utils;

import com.cibernet.splatcraft.utils.InkColors;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ColorItemUtils
{
	public static int getInkColor(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color"))
			return InkColors.DYE_WHITE.getColor();
		return stack.getTagCompound().getInteger("color");
	}
	public static boolean isColorLocked(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("colorLocked"))
			return false;
		return stack.getTagCompound().getBoolean("colorLocked");
	}
	
	public static NBTTagCompound checkTagCompound(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		
		return tagCompound;
	}
	
	public static ItemStack setInkColor(ItemStack stack, int color)
	{
		checkTagCompound(stack).setInteger("color", color);
		return stack;
	}
	public static ItemStack setColorLocked(ItemStack stack, boolean colorLocked)
	{
		checkTagCompound(stack).setBoolean("colorLocked", colorLocked);
		return stack;
	}
}