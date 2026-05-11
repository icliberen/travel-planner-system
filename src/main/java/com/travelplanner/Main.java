package com.travelplanner;

import com.travelplanner.ui.TravelPlannerFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TravelPlannerFrame frame = new TravelPlannerFrame();
            frame.setVisible(true);
        });
    }
}
