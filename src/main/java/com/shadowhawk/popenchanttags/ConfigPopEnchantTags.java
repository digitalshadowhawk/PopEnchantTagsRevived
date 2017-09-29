package com.shadowhawk.popenchanttags;

import org.lwjgl.input.Keyboard;

import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import com.shadowhawk.popenchanttags.LiteModPopEnchantTagsRevived;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ConfigPopEnchantTags implements ConfigPanel{
	
	/** Line spacing, in points. */
	private final static int SPACING = 24;
	  
	private GuiButton activeButton;
	private GuiButton toggleTags;
	private GuiButton toggleBooks;
	//private GuiButton toggleApplicable;
	private Minecraft minecraft;
	private LiteModPopEnchantTagsRevived shell = LiteModPopEnchantTagsRevived.instance;
	

	@Override
	public String getPanelTitle() {
		
		return "Pop Enchant Tags Settings";
	}

	@Override
	public int getContentHeight() {
		return SPACING * 3;
	}

	@Override
	public void onPanelShown(ConfigPanelHost host) {
		minecraft = Minecraft.getMinecraft();
	    int id = 0;
	    int line = 0;
	    toggleTags = new GuiButton(id++, 10, SPACING * line++, "Tags Enabled: " + shell.tagsVisible());
	    toggleBooks = new GuiButton(id++, 10, SPACING * line++, "Book Enchants: " + shell.getBooks());
	    //toggleApplicable = new GuiButton(id++, 10, SPACING * line++, "Super Secret Settings");
	}

	@Override
	public void onPanelResize(ConfigPanelHost host) {}

	@Override
	public void onPanelHidden()
	{
		LiteLoader.getInstance().writeConfig(shell);
	}

	@Override
	public void onTick(ConfigPanelHost host) {}

	@Override
	public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks) {
		toggleTags.drawButton(minecraft, mouseX, mouseY, partialTicks);
		toggleBooks.drawButton(minecraft, mouseX, mouseY, partialTicks);
		//toggleApplicable.drawButton(minecraft, mouseX, mouseY);
	}

	@Override
	public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
		if (toggleTags.mousePressed(minecraft, mouseX, mouseY))
		{
			activeButton = toggleTags;
			shell.toggleTags();
			toggleTags.displayString = ("Tags Enabled: "+ shell.tagsVisible());
			toggleTags.playPressSound(minecraft.getSoundHandler());
		} else if (toggleBooks.mousePressed(minecraft, mouseX, mouseY))
		{
			activeButton = toggleBooks;
			shell.toggleBooks();
			toggleBooks.displayString = ("Book Enchants: " + shell.getBooks());
			toggleBooks.playPressSound(minecraft.getSoundHandler());
		} /*else if (toggleApplicable.mousePressed(minecraft, mouseX, mouseY))
		{
			activeButton = toggleApplicable;
			//shell.toggleApplicable();
			toggleApplicable.displayString = ("Super Secret Settings");
			toggleApplicable.playPressSound(minecraft.getSoundHandler());
		}*/
	}

	@Override
	public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton)
	{
		if (activeButton != null) {
		      activeButton.mouseReleased(mouseX, mouseY);
		      activeButton = null;
		    }
	}

	@Override
	public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY) {}

	@Override
	public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode)
	{
		if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
		      host.close();
		}
	}

}
