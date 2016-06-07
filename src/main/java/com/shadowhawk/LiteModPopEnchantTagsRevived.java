package com.shadowhawk;

import java.io.File;

import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.transformers.event.EventInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;

public class LiteModPopEnchantTagsRevived implements HUDRenderListener, Tickable
{
    //private static LiteModPopEnchantTagsRevived instance;
    public static float remainingHighlightTicks;
    public static ItemStack highlightingItemStack;
    public static long ticks;

    public static void onToolHighlight(EventInfo<GuiIngame> e, ScaledResolution var1) {}

    public LiteModPopEnchantTagsRevived()
    {
        //instance = this;
        remainingHighlightTicks = -1.0F;
        highlightingItemStack = null;
        ticks = 0L;
    }

    @Override
	public String getName()
    {
        return "Pop Enchant Tags";
    }

    @Override
	public String getVersion()
    {
        return "1.0.2";
    }

    @Override
	public void init(File configPath) {}

    @Override
	public void onPostRenderHUD(int screenWidth, int screenHeight)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null)
        {
            //LiteModPopEnchantTagsRevived _this = instance;
            ItemStack items = mc.thePlayer.inventory.getCurrentItem();

            if (items != null && items.hasTagCompound())
            {
                int var5 = (int)(remainingHighlightTicks * 256.0F / 10.0F);

                if (var5 > 255)
                {
                    var5 = 255;
                }

                if (var5 > 0)
                {
                    String var2 = "";
                    boolean hidden = false;

                    if (items.getTagCompound().hasKey("HideFlags", 99))
                    {
                        hidden = (items.getTagCompound().getInteger("HideFlags") & 1) != 0;
                    }

                    NBTTagList var15 = items.getEnchantmentTagList();

                    if (!hidden && var15 != null)
                    {
                        for (int fontRenderer = 0; fontRenderer < var15.tagCount(); ++fontRenderer)
                        {
                            short var3 = var15.getCompoundTagAt(fontRenderer).getShort("id");
                            short var4 = var15.getCompoundTagAt(fontRenderer).getShort("lvl");

                            if (Enchantment.getEnchantmentByID(var3) != null)
                            {
                                var2 = var2 + (var2 != "" ? " " : "");
                                var2 = var2 + Enchantment.getEnchantmentByID(var3).getTranslatedName(var4);
                            }
                        }

                        var2 = TextFormatting.AQUA + var2;
                        FontRenderer var13 = mc.fontRendererObj;
                        int var14 = (screenWidth - var13.getStringWidth(var2)) / 2;
                        int var151 = screenHeight - 59 - 14;

                        if (!mc.playerController.shouldDrawHUD())
                        {
                            var151 += 14;
                        }

                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        var13.drawStringWithShadow(var2, (float)var14, (float)var151, 16777215 + (var5 << 24));
                        GlStateManager.disableBlend();
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }

    @Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {}

    @Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        if (inGame && minecraft.thePlayer != null && clock)
        {
            ItemStack items = minecraft.thePlayer.inventory.getCurrentItem();
            //LiteModPopEnchantTagsRevived _this = instance;

            if (items == null)
            {
                remainingHighlightTicks = 0.0F;
            }
            else if (highlightingItemStack != null && items.getItem() == highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(items, highlightingItemStack) && (items.isItemStackDamageable() || items.getMetadata() == highlightingItemStack.getMetadata()))
            {
                if (remainingHighlightTicks > 0.0F)
                {
                    remainingHighlightTicks = (float)(40L - (System.currentTimeMillis() - ticks) / 50L);
                }
            }
            else
            {
                remainingHighlightTicks = 40.0F;
                ticks = System.currentTimeMillis();
            }

            highlightingItemStack = items;
        }
    }

    @Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
}
