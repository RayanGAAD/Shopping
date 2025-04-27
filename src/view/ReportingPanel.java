// view/ReportingPanel.java
package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import service.CommandeService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Panel affichant un camembert "ventes par marque"
 * et un histogramme "commandes par jour".
 */
public class ReportingPanel extends JPanel {
    private final CommandeService commandeService;

    public ReportingPanel(CommandeService commandeService) {
        this.commandeService = commandeService;
        setLayout(new GridLayout(1, 2, 10, 10));
        add(createSalesByBrandChart());
        add(createOrdersPerDayChart());
    }

    /**
     * Camembert "Ventes par marque" avec tooltips et styles.
     */
    private ChartPanel createSalesByBrandChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> data = commandeService.getSalesByBrand();
        data.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart(
                "Ventes par marque",    // titre
                dataset,                // dataset
                true,                   // légende
                true,                   // tooltips
                false                   // URLs
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("Pas de données");
        plot.setCircular(true);
        plot.setLabelGap(0.02);

        return new ChartPanel(chart);
    }

    /**
     * Histogramme "Commandes par jour" avec labels formatés et rotatifs.
     */
    private ChartPanel createOrdersPerDayChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<LocalDate, Integer> data = commandeService.getOrdersPerDay();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        data.forEach((day, count) ->
                dataset.addValue(count, "Commandes", day.format(fmt))
        );

        JFreeChart chart = ChartFactory.createBarChart(
                "Commandes par jour",   // titre
                "Date",                 // axe X
                "Nombre",               // axe Y
                dataset,                // dataset
                PlotOrientation.VERTICAL,
                false,                  // légende
                true,                   // tooltips
                false                   // URLs
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.UP_45  // rotation 45°
        );

        return new ChartPanel(chart);
    }
}
