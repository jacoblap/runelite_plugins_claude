package com.prayeralert;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import java.awt.Color;

@ConfigGroup("prayeralert")
public interface PrayerAlertConfig extends Config
{
	@ConfigItem(
		keyName = "prayerThreshold",
		name = "Prayer Threshold",
		description = "Alert when prayer points fall at or below this value",
		position = 0
	)
	@Range(min = 1, max = 99)
	default int prayerThreshold()
	{
		return 20;
	}

	@ConfigItem(
		keyName = "flashColor",
		name = "Flash Color",
		description = "Color of the screen flash when prayer is low",
		position = 1
	)
	default Color flashColor()
	{
		return new Color(255, 0, 0, 70);
	}

	@ConfigItem(
		keyName = "flashSpeed",
		name = "Flash Speed",
		description = "How fast the screen flashes (lower = faster)",
		position = 2
	)
	@Range(min = 5, max = 50)
	default int flashSpeed()
	{
		return 20;
	}

	@ConfigItem(
		keyName = "showPrayerOverlay",
		name = "Show Prayer Warning Text",
		description = "Show a warning text overlay when prayer is low",
		position = 3
	)
	default boolean showPrayerOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "warningText",
		name = "Warning Text",
		description = "Text displayed in the warning overlay",
		position = 4
	)
	default String warningText()
	{
		return "LOW PRAYER!";
	}

	@ConfigItem(
		keyName = "textColor",
		name = "Warning Text Color",
		description = "Color of the warning text",
		position = 5
	)
	default Color textColor()
	{
		return Color.RED;
	}
}
