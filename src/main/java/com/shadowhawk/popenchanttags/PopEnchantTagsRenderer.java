package com.shadowhawk.popenchanttags;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
//keep for 1.8.9 updates
//import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.text.TextFormatting;

public class PopEnchantTagsRenderer {

	private boolean enabled = true;
	private boolean showBooks = true;
	public static float remainingHighlightTicks;
    public static ItemStack highlightingItemStack;
    public static long ticks;
    
	public PopEnchantTagsRenderer()
    {
        remainingHighlightTicks = -1.0F;
        highlightingItemStack = null;
        ticks = 0L;
    }
	
	public void toggleTags()
	{
		this.enabled = !this.enabled;
	}
	
	public boolean tagsVisible()
	{
		return this.enabled;
	}
	
	public void setVisible(boolean visible)
	{
		this.enabled = visible;
	}
	
	public void toggleBooks()
	{
		this.showBooks = !this.showBooks;
	}
	
	public boolean getBooks()
	{
		return this.showBooks;
	}
	
	public void setBooks(boolean visible)
	{
		this.showBooks = visible;
	}
	
	public void render(int screenWidth, int screenHeight)
	{
		Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null && (enabled || showBooks))
        {
            ItemStack items = mc.thePlayer.inventory.getCurrentItem();

            if (items != null && items.hasTagCompound())
            {
            	int transparency = (int)(remainingHighlightTicks * 256.0F / 10.0F);

                if (transparency > 255)
                {
                    transparency = 255;
                }

                if (transparency > 0)
                {
                    String testString = "";
                    String[] displayStrings = {"","","",""};
                    TextFormatting color = TextFormatting.AQUA;
                    //keep for 1.8.9 updates
                    //EnumChatFormatting color = EnumChatFormatting.AQUA;
                    boolean hidden = false;
                    int lines = 1;
                    int enchantCount = 0;
                    int entriesPerLine = 0;

                    if (items.getTagCompound().hasKey("HideFlags", 99))
                    {
                        hidden = (items.getTagCompound().getInteger("HideFlags") & 1) != 0;
                    }

                    NBTTagList tagList = null;
                    if(enabled)
                    {
                    	tagList = items.getEnchantmentTagList();
                    }
                    if((items.getItem() instanceof ItemEnchantedBook) && showBooks)
                    {
                    	tagList = ((ItemEnchantedBook)items.getItem()).getEnchantments(items);
                    	color = TextFormatting.YELLOW;
                    	//keep for 1.8.9 updates
                    	//color = EnumChatFormatting.YELLOW;
                    }
                    

                    if (!hidden && tagList != null)
                    {
                    	FontRenderer var13 = mc.fontRendererObj;
                    	for (int fontRenderer = 0; fontRenderer < tagList.tagCount(); ++fontRenderer)
                    	{
                    		if(var13.getStringWidth(testString) > screenWidth - 120)
                    		{
                    			lines++;
                    			testString = "";
                    		}
                    		short enchantmentId = tagList.getCompoundTagAt(fontRenderer).getShort("id");
                    		short enchantmentLevel = tagList.getCompoundTagAt(fontRenderer).getShort("lvl");
                    		testString = testString + (testString != "" ? " " : "");
                    		//testString = testString + Enchantment.getEnchantmentByID(enchantmentId).getTranslatedName(enchantmentLevel);
                    		testString = testString + Enchantment.getEnchantmentById(enchantmentId).getTranslatedName(enchantmentLevel);
                    	}
                    	entriesPerLine = (int)Math.ceil(tagList.tagCount()/lines);
                    	
                    	for (int fontRenderer = 0; fontRenderer < tagList.tagCount(); ++fontRenderer)
                        {
                            short enchantmentId = tagList.getCompoundTagAt(fontRenderer).getShort("id");
                            short enchantmentLevel = tagList.getCompoundTagAt(fontRenderer).getShort("lvl");

                            
                            int currentLine = 0;
                            if(enchantCount == entriesPerLine)
                            {
                            	if(displayStrings[3] != "" && var13.getStringWidth(displayStrings[3]) < screenWidth - 120)
                            	{
                            		currentLine = 3;
                            	} else if(displayStrings[2] != "" && var13.getStringWidth(displayStrings[2]) < screenWidth - 120)
                            	{
                            		currentLine = 2;
                            	} else if(displayStrings[1] != "" && var13.getStringWidth(displayStrings[1]) < screenWidth - 120)
                            	{
                            		currentLine = 1;
                            	} else
                            	{
                            		displayStrings[3] = displayStrings[2];
                            		displayStrings[2] = displayStrings[1];
                            		displayStrings[1] = displayStrings[0];
                            		displayStrings[0] = "";
                            		enchantCount = 0;
                            	}
                        	}
                            displayStrings[currentLine] = displayStrings[currentLine] + (displayStrings[currentLine] != "" ? " " : "");
                            displayStrings[currentLine] = displayStrings[currentLine] + Enchantment.getEnchantmentById(enchantmentId).getTranslatedName(enchantmentLevel);
                            enchantCount++;
                            currentLine = 0;
                        }	

                    	int[] x = {0, 0, 0, 0};
                    	int[] y = {0, 0, 0, 0};
                    	for(int i = 0; i <= 3; i++){
                        	displayStrings[i] = color + displayStrings[i];
                        	x[i] = (screenWidth - var13.getStringWidth(displayStrings[i])) / 2;
                        	y[i] = screenHeight - 59 - (14 * i);
                        }

                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        for(int i = 0; i <= 3; i++){
                        	var13.drawStringWithShadow(displayStrings[i], (float)x[i], (float)y[i], 16777215 + (transparency << 24));
                        }
                        GlStateManager.disableBlend();
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
	}
	
	public void tick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
	{
		if (inGame && minecraft.thePlayer != null && clock)
        {
            ItemStack items = minecraft.thePlayer.inventory.getCurrentItem();

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
	
}
