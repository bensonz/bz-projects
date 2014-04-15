package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5.data.Data;
import edu.cmu.edu.cs.cs214.hw5.charts.PieChart;

public class TestAnalysisPlugin implements AnalysisPlugin {
	private JPanel show;

	@Override
	public void onRegister() {
		show = new JPanel();
	}

	@Override
	public void onUnregister() {
		show = null;
	}

	@Override
	public JPanel getDisplay() {
		return show;
	}

	@Override
	public void update(Data data) {
		ArrayList<Double> values = new ArrayList<Double>();
		values.add(new Double(15));
		values.add(new Double(60));
		values.add(new Double(8));
		values.add(new Double(10));
		values.add(new Double(7));

		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.cyan);
		colors.add(Color.GREEN);
		colors.add(Color.red);
		colors.add(Color.pink);
		colors.add(Color.yellow);

		PieChart on = new PieChart(values, colors);
		show = on;
	}

	@Override
	public int getWidth() {
		return 400;
	}

	@Override
	public int getHeight() {
		return 600;
	}

	@Override
	public String getName() {
		return "Test Analysis Plugin";
	}

}
