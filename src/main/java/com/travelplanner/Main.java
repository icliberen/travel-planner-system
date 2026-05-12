package com.travelplanner;

import com.formdev.flatlaf.FlatDarkLaf;
import com.travelplanner.ui.TravelPlannerFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            LOGGER.info("FlatLaf Dark theme applied");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "FlatLaf not available, using system L&F", e);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
        }

        SwingUtilities.invokeLater(() -> {
            TravelPlannerFrame frame = new TravelPlannerFrame();
            frame.setVisible(true);
            LOGGER.info("Travel Planner System started");
        });
    }
}
