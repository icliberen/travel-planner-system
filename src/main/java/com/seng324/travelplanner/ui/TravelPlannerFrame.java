package com.seng324.travelplanner.ui;

import com.seng324.travelplanner.decorator.BasicCityPlan;
import com.seng324.travelplanner.decorator.CityCenterVisit;
import com.seng324.travelplanner.decorator.MuseumVisit;
import com.seng324.travelplanner.decorator.ParkVisit;
import com.seng324.travelplanner.decorator.ShoppingMallVisit;
import com.seng324.travelplanner.decorator.VisitableCity;
import com.seng324.travelplanner.iterator.CityIterator;
import com.seng324.travelplanner.iterator.WeatherFilteredCityCollection;
import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;
import com.seng324.travelplanner.observer.WeatherObserver;
import com.seng324.travelplanner.observer.WeatherReportProvider;
import com.seng324.travelplanner.repository.CityRepository;
import com.seng324.travelplanner.sorting.CitySortContext;
import com.seng324.travelplanner.sorting.CitySortStrategy;
import com.seng324.travelplanner.sorting.SortByAreaStrategy;
import com.seng324.travelplanner.sorting.SortByNameStrategy;
import com.seng324.travelplanner.sorting.SortByPopulationStrategy;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * GUI Client
 * EN: This class wires all required patterns together in one runnable application.
 * TR: Bu sinif zorunlu tum patternlari calisan tek bir uygulamada birbirine baglar.
 */
public class TravelPlannerFrame extends JFrame implements WeatherObserver {
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat HOUR_FORMAT = new DecimalFormat("0.0");

    private final CityRepository repository = CityRepository.getInstance();
    private final CitySortContext sortContext = new CitySortContext(new SortByNameStrategy());
    private final WeatherFilteredCityCollection weatherCollection =
            new WeatherFilteredCityCollection(repository.getCities());
    private final WeatherReportProvider weatherProvider = new WeatherReportProvider(repository.getCities());

    private final DefaultListModel<City> allCityModel = new DefaultListModel<>();
    private final DefaultListModel<City> filteredCityModel = new DefaultListModel<>();
    private final JList<City> allCityList = new JList<>(allCityModel);
    private final JList<City> filteredCityList = new JList<>(filteredCityModel);
    private final JComboBox<CitySortStrategy> sortComboBox = new JComboBox<>();
    private final JComboBox<WeatherState> weatherComboBox = new JComboBox<>(WeatherState.values());
    private final JCheckBox museumCheckBox = new JCheckBox("Museum Visit ($35, 2.5h)");
    private final JCheckBox shoppingCheckBox = new JCheckBox("Shopping Mall Visit ($60, 3h)");
    private final JCheckBox parkCheckBox = new JCheckBox("Park Visit ($15, 1.5h)");
    private final JCheckBox cityCenterCheckBox = new JCheckBox("Historic City Center Visit ($25, 2h)");
    private final JTextArea planDetailsArea = new JTextArea();
    private final TemperatureBarChartPanel temperatureBarChartPanel = new TemperatureBarChartPanel();
    private final WeatherPieChartPanel weatherPieChartPanel = new WeatherPieChartPanel();

    public TravelPlannerFrame() {
        super("SENG 324 Travel Planner System");
        configureWindow();
        configureControls();
        buildLayout();
        connectEvents();
        startWeatherUpdates();
    }

    private void configureWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
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
        planDetailsArea.setBackground(new Color(250, 252, 255));
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));
        panel.add(new JLabel("Sorting criteria:"));
        panel.add(sortComboBox);
        panel.add(new JLabel("Weather filter:"));
        panel.add(weatherComboBox);
        panel.add(new JLabel("Weather updates every 3 seconds"));
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

        JPanel checks = new JPanel(new GridLayout(4, 1, 4, 4));
        checks.add(museumCheckBox);
        checks.add(shoppingCheckBox);
        checks.add(parkCheckBox);
        checks.add(cityCenterCheckBox);

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
                 * EN: The selected combo box item becomes the active sorting algorithm.
                 * TR: Combo box'ta secilen oge aktif siralama algoritmasi olur.
                 */
                sortContext.setStrategy(selectedStrategy);
                refreshSortedCityList();
            }
        });

        weatherComboBox.addActionListener(event -> refreshFilteredCityList());
        allCityList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                updatePlannerDetails();
            }
        });

        museumCheckBox.addActionListener(event -> updatePlannerDetails());
        shoppingCheckBox.addActionListener(event -> updatePlannerDetails());
        parkCheckBox.addActionListener(event -> updatePlannerDetails());
        cityCenterCheckBox.addActionListener(event -> updatePlannerDetails());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                weatherProvider.stop();
            }
        });
    }

    private void startWeatherUpdates() {
        /*
         * Observer Pattern - subscriptions
         * EN: The frame refreshes changed views; chart panels repaint themselves after notifications.
         * TR: Frame degisen gorunumleri yeniler; chart paneller bildirimden sonra kendilerini yeniden cizer.
         */
        weatherProvider.attach(this);
        weatherProvider.attach(temperatureBarChartPanel);
        weatherProvider.attach(weatherPieChartPanel);
        weatherProvider.notifyObservers();
        weatherProvider.start();
    }

    private void refreshCityLists() {
        refreshSortedCityList();
        refreshFilteredCityList();
        updatePlannerDetails();
    }

    private void refreshSortedCityList() {
        City selected = allCityList.getSelectedValue();
        List<City> sortedCities = sortContext.sort(repository.getCities());
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
         * Iterator Pattern - client usage
         * EN: The GUI asks the collection for an iterator, then walks through matching cities only.
         * TR: GUI collection'dan iterator ister ve sadece eslesen sehirler uzerinde gezer.
         */
        City selected = filteredCityList.getSelectedValue();
        List<City> filteredCities = new ArrayList<>();
        CityIterator iterator = weatherCollection.createIterator(selectedState);
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

    private void updatePlannerDetails() {
        City selectedCity = allCityList.getSelectedValue();
        if (selectedCity == null) {
            planDetailsArea.setText("Select a city from the sorted city list.");
            return;
        }

        /*
         * Decorator Pattern - client usage
         * EN: Each checked activity wraps the previous plan and adds cost/time.
         * TR: Isaretlenen her aktivite onceki plani sarar ve maliyet/sure ekler.
         */
        VisitableCity plan = new BasicCityPlan(selectedCity);
        if (museumCheckBox.isSelected()) {
            plan = new MuseumVisit(plan);
        }
        if (shoppingCheckBox.isSelected()) {
            plan = new ShoppingMallVisit(plan);
        }
        if (parkCheckBox.isSelected()) {
            plan = new ParkVisit(plan);
        }
        if (cityCenterCheckBox.isSelected()) {
            plan = new CityCenterVisit(plan);
        }

        String details = "Selected city: " + plan.getCityName()
                + "\nCurrent weather: " + selectedCity.getCurrentWeatherState()
                + "\nCurrent temperature: " + HOUR_FORMAT.format(selectedCity.getCurrentTemperature()) + " C"
                + "\n\nPlan: " + plan.getPlanDescription()
                + "\nTotal budget: $" + MONEY_FORMAT.format(plan.getCost())
                + "\nTotal duration: " + HOUR_FORMAT.format(plan.getRequiredHours()) + " hours";
        planDetailsArea.setText(details);
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
