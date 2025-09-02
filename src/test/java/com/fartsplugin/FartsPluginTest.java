package com.fartsplugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FartsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		// If run with --test-audio argument, just test audio without RuneLite
		if (args.length > 0 && "--test-audio".equals(args[0])) {
			testAudioDirectly();
			return;
		}

		ExternalPluginManager.loadBuiltin(FartsPlugin.class);
		RuneLite.main(args);
	}

	private static void testAudioDirectly()
	{
		System.out.println("=== DIRECT AUDIO TEST ===");
		System.out.println("Audio testing functionality has been removed.");
		System.out.println("Use the plugin in RuneLite for actual testing.");
		System.out.println("=== AUDIO TEST COMPLETE ===");
	}
}
