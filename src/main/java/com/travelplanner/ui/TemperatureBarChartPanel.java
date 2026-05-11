package com.travelplanner.ui;

import com.travelplanner.model.City;
import com.travelplanner.observer.WeatherObserver;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * Observer Pattern - ConcreteObserver role
 * EN: The bar chart repaints itself whenever the weather provider publishes new temperatures.
 * TR: Weather provider yeni sicakliklari yayinladiginda bar chart kendini yeniden cizer.
 */
public class TemperatureBarChartPanel extends JPanel implements WeatherObserver {
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private volatile List<City> cities = new ArrayList<>();

    public TemperatureBarChartPanel() {
        setPreferredSize(new Dimension(520, 260));
        setBackground(Color.WHITE);
    }

    @Override
    public void updateWeather(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        if (SwingUtilities.isEventDispatchThread()) {
            repaint();
        } else {
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int left = 52;
        int right = 24;
        int top = 34;
        int bottom = 54;
        int chartWidth = width - left - right;
        int chartHeight = height - top - bottom;

        g2.setColor(new Color(32, 40, 54));
        g2.drawString("Temperature Bar Chart", left, 20);

        if (cities.isEmpty()) {
            g2.drawString("No city data", left, top + 24);
            g2.dispose();
            return;
        }

        double min = cities.stream().mapToDouble(City::getCurrentTemperature).min().orElse(0);
        double max = cities.stream().mapToDouble(City::getCurrentTemperature).max().orElse(1);
        double baseline = Math.min(0, min);
        double range = Math.max(1, max - baseline);

        g2.setColor(new Color(219, 225, 232));
        g2.drawLine(left, top, left, top + chartHeight);
        g2.drawLine(left, top + chartHeight, left + chartWidth, top + chartHeight);

        int gap = 6;
        int barWidth = Math.max(8, (chartWidth - gap * (cities.size() + 1)) / cities.size());
        FontMetrics metrics = g2.getFontMetrics();

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            double temperature = city.getCurrentTemperature();
            int barHeight = (int) ((temperature - baseline) / range * chartHeight);
            int x = left + gap + i * (barWidth + gap);
            int y = top + chartHeight - barHeight;

            g2.setColor(colorForTemperature(temperature));
            g2.fillRoundRect(x, y, barWidth, barHeight, 6, 6);

            g2.setColor(new Color(32, 40, 54));
            String value = FORMAT.format(temperature);
            g2.drawString(value, x + (barWidth - metrics.stringWidth(value)) / 2, Math.max(top + 12, y - 5));

            String shortName = city.getName().length() > 4 ? city.getName().substring(0, 4) : city.getName();
            g2.drawString(shortName, x + (barWidth - metrics.stringWidth(shortName)) / 2, top + chartHeight + 18);
        }

        g2.dispose();
    }

    private Color colorForTemperature(double temperature) {
        if (temperature < 0) {
            return new Color(83, 156, 214);
        }
        if (temperature < 18) {
            return new Color(78, 176, 137);
        }
        if (temperature < 30) {
            return new Color(238, 174, 75);
        }
        return new Color(218, 91, 74);
    }
}
