package com.prayeralert;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PrayerAlertOverlay extends Overlay
{
	private final Client client;
	private final PrayerAlertPlugin plugin;
	private final PrayerAlertConfig config;

	private int flashTick = 0;
	private boolean flashVisible = false;

	@Inject
	public PrayerAlertOverlay(Client client, PrayerAlertPlugin plugin, PrayerAlertConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isPrayerLow())
		{
			flashTick = 0;
			flashVisible = false;
			return null;
		}

		// Animate flash
		flashTick++;
		if (flashTick >= config.flashSpeed())
		{
			flashTick = 0;
			flashVisible = !flashVisible;
		}

		int canvasWidth = client.getCanvasWidth();
		int canvasHeight = client.getCanvasHeight();

		// Draw flashing screen border/overlay
		if (flashVisible)
		{
			graphics.setColor(config.flashColor());
			graphics.fillRect(0, 0, canvasWidth, canvasHeight);

			// Draw a thick red border around the screen
			Color borderColor = config.flashColor().darker();
			graphics.setColor(new Color(
				borderColor.getRed(),
				borderColor.getGreen(),
				borderColor.getBlue(),
				180
			));
			graphics.setStroke(new BasicStroke(12));
			graphics.drawRect(6, 6, canvasWidth - 12, canvasHeight - 12);
		}

		// Draw warning text
		if (config.showPrayerOverlay())
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Font warningFont = new Font("Arial", Font.BOLD, 32);
			graphics.setFont(warningFont);
			FontMetrics fm = graphics.getFontMetrics();

			String text = config.warningText();
			int textWidth = fm.stringWidth(text);
			int textX = (canvasWidth - textWidth) / 2;
			int textY = canvasHeight / 4;

			// Shadow
			graphics.setColor(Color.BLACK);
			graphics.drawString(text, textX + 2, textY + 2);

			// Main text
			graphics.setColor(config.textColor());
			graphics.drawString(text, textX, textY);

			// Sub-text showing current prayer points
			Font subFont = new Font("Arial", Font.BOLD, 18);
			graphics.setFont(subFont);
			fm = graphics.getFontMetrics();
			String subText = "Prayer: " + plugin.getCurrentPrayer() + " / " + plugin.getMaxPrayer();
			int subTextWidth = fm.stringWidth(subText);
			int subX = (canvasWidth - subTextWidth) / 2;
			int subY = textY + fm.getHeight() + 4;

			graphics.setColor(Color.BLACK);
			graphics.drawString(subText, subX + 1, subY + 1);

			graphics.setColor(Color.ORANGE);
			graphics.drawString(subText, subX, subY);
		}

		return null;
	}
}
