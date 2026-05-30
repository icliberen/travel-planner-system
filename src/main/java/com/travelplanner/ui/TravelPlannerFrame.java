package com.travelplanner.ui;

import com.travelplanner.decorator.BasicCityPlan;
import com.travelplanner.decorator.CityCenterVisit;
import com.travelplanner.decorator.MuseumVisit;
import com.travelplanner.decorator.ParkVisit;
import com.travelplanner.decorator.ShoppingMallVisit;
import com.travelplanner.decorator.VisitableCity;
import com.travelplanner.export.TripExporter;
import com.travelplanner.iterator.CityIterator;
import com.travelplanner.iterator.CityIteratorFactory;
import com.travelplanner.iterator.WeatherFilteredCityCollection;
import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;
import com.travelplanner.observer.WeatherHistoryTracker;
import com.travelplanner.observer.WeatherObserver;
import com.travelplanner.observer.WeatherReportProvider;
import com.travelplanner.repository.CityRepository;
import com.travelplanner.sorting.CitySortContext;
import com.travelplanner.sorting.CitySortStrategy;
import com.travelplanner.sorting.SortByAreaStrategy;
import com.travelplanner.sorting.SortByNameStrategy;
import com.travelplanner.sorting.SortByPopulationStrategy;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main GUI — wires all design patterns together.
 * <p>Improvements over the base version:</p>
 * <ul>
 *   <li>City search (real-time filtering by name)</li>
 *   <li>Weather update speed control (slider)</li>
 *   <li>Trip export to file</li>
 *   <li>Weather history tracking</li>
 *   <li>Factory-based iterator creation</li>
 * </ul>
 */
public class TravelPlannerFrame extends JFrame implements WeatherObserver {
    private static final Logger LOGGER = Logger.getLogger(TravelPlannerFrame.class.getName());
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat HOUR_FORMAT = new DecimalFormat("0.0");
    private static final Color LIGHT_BACKGROUND = new Color(250, 252, 255);
    private static final Color LIGHT_PANEL = new Color(255, 255, 255);
    private static final Color LIGHT_FOREGROUND = new Color(32, 40, 54);
    private static final Color DARK_BACKGROUND = new Color(18, 20, 24);
    private static final Color DARK_PANEL = new Color(29, 32, 38);
    private static final Color DARK_FOREGROUND = new Color(236, 239, 244);
    private static final Color DARK_FIELD = new Color(38, 42, 49);

    private final CityRepository repository = CityRepository.getInstance();
    private final CitySortContext sortContext = new CitySortContext(new SortByNameStrategy());
    private final WeatherFilteredCityCollection weatherCollection =
            new WeatherFilteredCityCollection(repository.getCities());
    private final WeatherReportProvider weatherProvider = new WeatherReportProvider(repository.getCities());
    private final WeatherHistoryTracker historyTracker = new WeatherHistoryTracker();

    private final DefaultListModel<City> allCityModel = new DefaultListModel<>();
    private final DefaultListModel<City> filteredCityModel = new DefaultListModel<>();
    private final JList<City> allCityList = new JList<>(allCityModel);
    private final JList<City> filteredCityList = new JList<>(filteredCityModel);
    private final JComboBox<CitySortStrategy> sortComboBox = new JComboBox<>();
    private final JComboBox<WeatherState> weatherComboBox = new JComboBox<>(WeatherState.values());
    private final JTextField searchField = new JTextField(14);
    private final JSlider speedSlider = new JSlider(1, 10, 3);
    private final JCheckBox museumCheckBox = new JCheckBox("Museum Visit ($35, 2.5h)");
    private final JCheckBox shoppingCheckBox = new JCheckBox("Shopping Mall Visit ($60, 3h)");
    private final JCheckBox parkCheckBox = new JCheckBox("Park Visit ($15, 1.5h)");
    private final JCheckBox cityCenterCheckBox = new JCheckBox("Historic City Center Visit ($25, 2h)");
    private final JButton weatherToggleButton = new JButton("Stop");
    private final JCheckBox darkModeCheckBox = new JCheckBox("Dark Mode");
    private final JButton exportButton = new JButton("Export Plan");
    private final JTextArea planDetailsArea = new JTextArea();
    private final TemperatureBarChartPanel temperatureBarChartPanel = new TemperatureBarChartPanel();
    private final WeatherPieChartPanel weatherPieChartPanel = new WeatherPieChartPanel();
    private boolean darkMode;

    public TravelPlannerFrame() {
        super("Travel Planner System");
        configureWindow();
        configureControls();
        buildLayout();
        connectEvents();
        startWeatherUpdates();
    }

    private void configureWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 780));
        setLocationByPlatform(true);
    }

    private void configureControls() {
        sortComboBox.addItem(new SortByNameStrategy());
        sortComboBox.addItem(new SortByPopulationStrategy());
        sortComboBox.addItem(new SortByAreaStrategy());

        allCityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        filteredCityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        planDetailsArea.setEditable(false);
        planDetailsArea.setLineWrap(true);
        planDetailsArea.setWrapStyleWord(true);
        planDetailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        searchField.setToolTipText("Type to search cities by name");
        speedSlider.setMajorTickSpacing(3);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setToolTipText("Weather update interval (seconds)");

        exportButton.setToolTipText("Export current trip plan to a text file");
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createControlPanel(), BorderLayout.NORTH);
        add(createMainSplitPane(), BorderLayout.CENTER);
        add(createChartPanel(), BorderLayout.SOUTH);

        refreshCityLists();
        pack();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));
        panel.add(new JLabel("Sort:"));
        panel.add(sortComboBox);
        panel.add(new JLabel("Weather:"));
        panel.add(weatherComboBox);
        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(new JLabel("Update speed (s):"));
        panel.add(speedSlider);
        panel.add(weatherToggleButton);
        panel.add(darkModeCheckBox);
        return panel;
    }

    private JSplitPane createMainSplitPane() {
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createListsPanel(),
                createPlannerPanel()
        );
        splitPane.setResizeWeight(0.72);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        return splitPane;
    }

    private JPanel createListsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(wrapList("All Cities", allCityList));
        panel.add(wrapList("Weather Filtered Cities", filteredCityList));
        return panel;
    }

    private JScrollPane wrapList(String title, JList<City> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        return scrollPane;
    }

    private JPanel createPlannerPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new Dimension(340, 400));
        panel.setBorder(BorderFactory.createTitledBorder("City Activity Planner"));

        JPanel checks = new JPanel(new GridLayout(5, 1, 4, 4));
        checks.add(museumCheckBox);
        checks.add(shoppingCheckBox);
        checks.add(parkCheckBox);
        checks.add(cityCenterCheckBox);
        checks.add(exportButton);

        panel.add(checks, BorderLayout.NORTH);
        panel.add(new JScrollPane(planDetailsArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(withTitle("Temperatures", temperatureBarChartPanel));
        panel.add(withTitle("Weather Percentages", weatherPieChartPanel));
        return panel;
    }

    private JPanel withTitle(String title, JPanel child) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(child, BorderLayout.CENTER);
        return panel;
    }

    private void connectEvents() {
        sortComboBox.addActionListener(event -> {
            CitySortStrategy selectedStrategy = (CitySortStrategy) sortComboBox.getSelectedItem();
            if (selectedStrategy != null) {
                /*
                 * Strategy Pattern - client usage
                 * The selected combo box item becomes the active sorting algorithm.
                 */
                sortContext.setStrategy(selectedStrategy);
                refreshSortedCityList();
            }
        });

        weatherComboBox.addActionListener(event -> refreshFilteredCityList());
        weatherToggleButton.addActionListener(event -> toggleWeatherUpdates());
        darkModeCheckBox.addActionListener(event -> {
            darkMode = darkModeCheckBox.isSelected();
            applyTheme();
        });

        allCityList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                updatePlannerDetails();
            }
        });

        museumCheckBox.addActionListener(event -> updatePlannerDetails());
        shoppingCheckBox.addActionListener(event -> updatePlannerDetails());
        parkCheckBox.addActionListener(event -> updatePlannerDetails());
        cityCenterCheckBox.addActionListener(event -> updatePlannerDetails());

        // City search — filters the All Cities list in real time
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { refreshSortedCityList(); }
            @Override public void removeUpdate(DocumentEvent e)  { refreshSortedCityList(); }
            @Override public void changedUpdate(DocumentEvent e) { refreshSortedCityList(); }
        });

        // Weather speed slider
        speedSlider.addChangeListener(event -> {
            if (!speedSlider.getValueIsAdjusting()) {
                int seconds = speedSlider.getValue();
                weatherProvider.setUpdateDelayMillis(seconds * 1000);
                LOGGER.info("Weather update interval set to " + seconds + "s");
            }
        });

        // Export button
        exportButton.addActionListener(event -> exportTripPlan());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                weatherProvider.stop();
                LOGGER.info("Application closed. Weather snapshots recorded: "
                        + historyTracker.getSnapshotCount());
            }
        });
    }

    private void startWeatherUpdates() {
        /*
         * Observer Pattern - subscriptions
         * The frame refreshes changed views; chart panels repaint themselves after notifications.
         */
        weatherProvider.attach(this);
        weatherProvider.attach(temperatureBarChartPanel);
        weatherProvider.attach(weatherPieChartPanel);
        weatherProvider.attach(historyTracker);
        weatherProvider.notifyObservers();
        weatherProvider.start();
    }

    private void toggleWeatherUpdates() {
        if (weatherProvider.isRunning()) {
            weatherProvider.stop();
            weatherToggleButton.setText("Start");
        } else {
            weatherProvider.start();
            weatherToggleButton.setText("Stop");
        }
    }

    private void applyTheme() {
        Color background = darkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
        Color panel = darkMode ? DARK_PANEL : LIGHT_PANEL;
        Color field = darkMode ? DARK_FIELD : LIGHT_PANEL;
        Color foreground = darkMode ? DARK_FOREGROUND : LIGHT_FOREGROUND;

        getContentPane().setBackground(background);
        applyThemeToChildren(getContentPane(), panel, field, foreground);
        repaint();
    }

    private void applyThemeToChildren(Container container, Color panel, Color field, Color foreground) {
        for (Component component : container.getComponents()) {
            component.setForeground(foreground);
            if (component instanceof JTextArea || component instanceof JTextField
                    || component instanceof JList<?> || component instanceof JComboBox<?>) {
                component.setBackground(field);
            } else if (component instanceof JButton) {
                component.setBackground(field);
                component.setForeground(foreground);
            } else {
                component.setBackground(panel);
            }

            if (component instanceof JPanel childPanel && childPanel.getBorder() instanceof TitledBorder titledBorder) {
                titledBorder.setTitleColor(foreground);
            }

            if (component instanceof JScrollPane scrollPane) {
                scrollPane.getViewport().setBackground(field);
                if (scrollPane.getBorder() instanceof TitledBorder titledBorder) {
                    titledBorder.setTitleColor(foreground);
                }
            }

            if (component instanceof Container child) {
                applyThemeToChildren(child, panel, field, foreground);
            }
        }
    }

    private void refreshCityLists() {
        refreshSortedCityList();
        refreshFilteredCityList();
        updatePlannerDetails();
    }

    private void refreshSortedCityList() {
        City selected = allCityList.getSelectedValue();
        List<City> sortedCities = sortContext.sort(repository.getCities());

        // Apply search filter
        String query = searchField.getText().trim().toLowerCase();
        if (!query.isEmpty()) {
            sortedCities = sortedCities.stream()
                    .filter(c -> c.getName().toLowerCase().contains(query))
                    .toList();
        }

        replaceModel(allCityModel, sortedCities);

        if (selected != null && sortedCities.contains(selected)) {
            allCityList.setSelectedValue(selected, true);
        } else if (!sortedCities.isEmpty()) {
            allCityList.setSelectedIndex(0);
        }
    }

    private void refreshFilteredCityList() {
        WeatherState selectedState = (WeatherState) weatherComboBox.getSelectedItem();
        if (selectedState == null) {
            return;
        }

        /*
         * Iterator Pattern + Factory Pattern
         * Uses CityIteratorFactory to create the correct iterator.
         */
        City selected = filteredCityList.getSelectedValue();
        List<City> filteredCities = new ArrayList<>();
        CityIterator iterator = CityIteratorFactory.create(repository.getCities(), selectedState);
        while (iterator.hasNext()) {
            filteredCities.add(iterator.next());
        }

        replaceModel(filteredCityModel, filteredCities);
        if (selected != null && filteredCities.contains(selected)) {
            filteredCityList.setSelectedValue(selected, true);
        }
    }

    private void replaceModel(DefaultListModel<City> model, List<City> cities) {
        model.clear();
        for (City city : cities) {
            model.addElement(city);
        }
    }

    private VisitableCity buildCurrentPlan(City selectedCity) {
        /*
         * Decorator Pattern - client usage
         * Each checked activity wraps the previous plan and adds cost/time.
         */
        VisitableCity plan = new BasicCityPlan(selectedCity);
        if (museumCheckBox.isSelected()) plan = new MuseumVisit(plan);
        if (shoppingCheckBox.isSelected()) plan = new ShoppingMallVisit(plan);
        if (parkCheckBox.isSelected()) plan = new ParkVisit(plan);
        if (cityCenterCheckBox.isSelected()) plan = new CityCenterVisit(plan);
        return plan;
    }

    private void updatePlannerDetails() {
        City selectedCity = allCityList.getSelectedValue();
        if (selectedCity == null) {
            planDetailsArea.setText("Select a city from the sorted city list.");
            exportButton.setEnabled(false);
            return;
        }

        VisitableCity plan = buildCurrentPlan(selectedCity);

        String details = "Selected city: " + plan.getCityName()
                + "\nCurrent weather: " + selectedCity.getCurrentWeatherState()
                + "\nCurrent temperature: " + HOUR_FORMAT.format(selectedCity.getCurrentTemperature()) + " °C"
                + "\n\nPlan: " + plan.getPlanDescription()
                + "\nTotal budget: $" + MONEY_FORMAT.format(plan.getCost())
                + "\nTotal duration: " + HOUR_FORMAT.format(plan.getRequiredHours()) + " hours"
                + "\n\n── Weather History ──"
                + "\nSnapshots recorded: " + historyTracker.getSnapshotCount();

        // Show last 5 weather snapshots for this city
        var cityHistory = historyTracker.getHistoryForCity(selectedCity.getName());
        int start = Math.max(0, cityHistory.size() - 5);
        for (int i = start; i < cityHistory.size(); i++) {
            details += "\n  " + cityHistory.get(i);
        }

        planDetailsArea.setText(details);
        exportButton.setEnabled(true);
    }

    private void exportTripPlan() {
        City city = allCityList.getSelectedValue();
        if (city == null) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(city.getName() + "_trip_plan.txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                VisitableCity plan = buildCurrentPlan(city);
                TripExporter.export(city, plan, chooser.getSelectedFile().toPath());
                JOptionPane.showMessageDialog(this,
                        "Trip plan exported successfully!", "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Export failed", ex);
                JOptionPane.showMessageDialog(this,
                        "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void updateWeather(List<City> cities) {
        SwingUtilities.invokeLater(() -> {
            allCityList.repaint();
            refreshFilteredCityList();
            updatePlannerDetails();
        });
    }
}
