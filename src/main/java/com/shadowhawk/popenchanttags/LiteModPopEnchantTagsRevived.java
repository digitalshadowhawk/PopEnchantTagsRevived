package com.shadowhawk.popenchanttags;

import java.io.File;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

@ExposableOptions(
		strategy = ConfigStrategy.Unversioned,
		filename = "popenchanttagsrevived.json",
		aggressive = true
)

public class LiteModPopEnchantTagsRevived implements HUDRenderListener, Tickable, Configurable
{

    public PopEnchantTagsRenderer renderer = new PopEnchantTagsRenderer();
	@Expose
	@SerializedName("tags_enabled")
	private boolean enabled = true;
	@Expose
	@SerializedName("books_enabled")
	private boolean showBooks = true;
	public static KeyBinding toggleTags;
	public static KeyBinding toggleBooks;
	public static LiteModPopEnchantTagsRevived instance;
    
    public LiteModPopEnchantTagsRevived()
    {}

    @Override
	public String getName()
    {
        return "Pop Enchant Tags Revived";
    }

    @Override
	public String getVersion()
    {
        return "1.1";
    }

    @Override
	public void init(File configPath) {
    	instance = this;
    	toggleTags = new KeyBinding("Toggle Pop Enchant Tags", 0, "Pop Enchant Tags Revived");
    	LiteLoader.getInput().registerKeyBinding(toggleTags);
    	toggleBooks = new KeyBinding("Toggle Enchanted Book Tags", 0, "Pop Enchant Tags Revived");
    	LiteLoader.getInput().registerKeyBinding(toggleBooks);
    	this.renderer.setVisible(enabled);
    }

    @Override
	public void onPostRenderHUD(int screenWidth, int screenHeight)
    {
        this.renderer.render(screenWidth, screenHeight);
    }

    @Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {}

    @Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        this.renderer.tick(minecraft, partialTicks, inGame, clock);
        if (toggleTags.isPressed())
        {
            toggleTags();            
        }
        if(toggleBooks.isPressed())
        {
        	toggleBooks();
        }
    }

    public void toggleTags() {
		this.renderer.toggleTags();
		enabled = this.renderer.tagsVisible();
        LiteLoader.getInstance().writeConfig(this);
	}
    
    public boolean tagsVisible()
    {
    	return enabled;
    }
    
    public void toggleBooks() {
    	this.renderer.toggleBooks();
    	showBooks = this.renderer.getBooks();
        LiteLoader.getInstance().writeConfig(this);
    }
    
    public boolean getBooks()
    {
    	return this.renderer.getBooks();
    }

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		return ConfigPopEnchantTags.class;
	}
}
