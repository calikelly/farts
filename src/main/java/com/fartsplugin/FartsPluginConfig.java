package com.fartsplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("fartsplugin")
public interface FartsPluginConfig extends Config
{
	@ConfigItem(
		keyName = "greeting",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}

	@ConfigItem(
		keyName = "enableDeathFart",
		name = "Death Fart",
		description = "Play a fart sound when you die"
	)
	default boolean enableDeathFart()
	{
		return true;
	}

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
		return 50;
	}


}
