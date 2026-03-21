package com.prayeralert;

import com.google.inject.Provides;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
	name = "Prayer Alert",
	description = "Visually alerts the player when prayer points fall below a configured threshold",
	tags = {"prayer", "alert", "overlay", "visual", "warning"}
)
public class PrayerAlertPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PrayerAlertConfig config;

	@Inject
	private PrayerAlertOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter
	private int currentPrayer = 0;

	@Getter
	private int maxPrayer = 0;

	@Getter
	private boolean prayerLow = false;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
		updatePrayerValues();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		prayerLow = false;
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		if (event.getSkill() == Skill.PRAYER)
		{
			updatePrayerValues();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			updatePrayerValues();
		}
		else if (event.getGameState() == GameState.LOGIN_SCREEN
			|| event.getGameState() == GameState.HOPPING)
		{
			prayerLow = false;
		}
	}

	private void updatePrayerValues()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
		maxPrayer = client.getRealSkillLevel(Skill.PRAYER);
		prayerLow = currentPrayer <= config.prayerThreshold();
	}

	@Provides
	PrayerAlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PrayerAlertConfig.class);
	}
}
