package com.fartsplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("fartsplugin")
public interface FartsPluginConfig extends Config
{
	@ConfigItem(
		keyName = "enableChatFart",
		name = "Chat Fart",
		description = "Play a fart sound when someone says fart-related words"
	)
	default boolean enableChatFart()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableRaspberryFart",
		name = "Raspberry Fart",
		description = "Play a fart sound when someone does the raspberry emote"
	)
	default boolean enableRaspberryFart()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableIBSMode",
		name = "IBS Mode",
		description = "Enable IBS mode that triggers farts after eating pizza"
	)
	default boolean enableIBSMode()
	{
		return true;
	}

	@ConfigItem(
		keyName = "fartVolume",
		name = "Fart Volume",
		description = "Volume level for fart sounds (0-100)"
	)
	default int fartVolume()
	{
		return 80;
	}


}
