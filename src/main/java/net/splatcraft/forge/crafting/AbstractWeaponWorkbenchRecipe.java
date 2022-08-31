package net.splatcraft.forge.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;


public abstract class AbstractWeaponWorkbenchRecipe implements IRecipe<IInventory>
{
    protected final ResourceLocation id;
    protected final ItemStack recipeOutput;
    protected final NonNullList<StackedIngredient> recipeItems;
    protected final String name;

    public AbstractWeaponWorkbenchRecipe(ResourceLocation id, String name, ItemStack recipeOutput, NonNullList<StackedIngredient> recipeItems)
    {
        this.id = id;
        this.recipeOutput = recipeOutput;
        this.recipeItems = recipeItems;
        this.name = name;
    }


    @Override
    public boolean matches(IInventory inv, World levelIn)
    {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < inv.getContainerSize(); ++j)
        {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty())
            {
                ++i;
                inputs.add(itemstack);
            }
        }

        return i == this.recipeItems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.recipeItems) != null;
    }

    public TextComponent getName()
    {
        return new TranslationTextComponent(name);
    }

    @Override
    public ItemStack assemble(IInventory inv)
    {
        return recipeOutput;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return false;
    }

    @Override
    public ItemStack getResultItem()
    {
        return recipeOutput;
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return SplatcraftRecipeTypes.WEAPON_STATION;
    }

    @Override
    public IRecipeType<?> getType()
    {
        return SplatcraftRecipeTypes.WEAPON_STATION_TYPE;
    }

    public ItemStack getOutput()
    {
        return recipeOutput;
    }

    public NonNullList<StackedIngredient> getInput()
    {
        return recipeItems;
    }


    protected static NonNullList<StackedIngredient> readIngredients(JsonArray p_199568_0_)
    {
        NonNullList<StackedIngredient> nonnulllist = NonNullList.create();

        for (int i = 0; i < p_199568_0_.size(); ++i)
        {
            StackedIngredient ingredient = StackedIngredient.deserialize(p_199568_0_.get(i));
            if (!ingredient.getIngredient().isEmpty() && ingredient.getCount() > 0)
            {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }


    public static class Serializer<T extends AbstractWeaponWorkbenchRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
    {

        public Serializer(String name)
        {
            super();
            setRegistryName(name);
        }

        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject json)
        {
            return null;
        }

        @Nullable
        @Override
        public T fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
        {
            return null;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, T recipe)
        {

        }
    }
}