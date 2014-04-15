package edu.cmu.cs.cs214.hw5.framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cmu.cs.cs214.hw5.plugin.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.plugin.DataPlugin;

@SuppressWarnings("serial")
public class FrameworkGui extends JFrame {
	private Framework framework;
	private JPanel content;
	private GridLayout layout;
	private JMenuBar menu;
	private JMenu dataPluginMenu;
	private JMenu analysisPluginMenu;

	public FrameworkGui(final Framework framework) {
		this.framework = framework;
	}

	/**
	 * actually sets up the frame
	 */
	public void setup() {
		// Setup window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 600);
		this.setPreferredSize(new Dimension(800, 600));
		this.setTitle("BZ/JY Framework");

		// Setup content panel
		content = new JPanel();
		content.setBackground(Color.white);
		setContentPane(content);

		// Create layout
		layout = new GridLayout(1, 3);
		content.setLayout(layout);

		createMenu();
	}

	private void createMenu() {

		menu = new JMenuBar();
		JMenu file = new JMenu("Quit");
		JMenuItem exit = new JMenuItem("Exit");
		dataPluginMenu = new JMenu("DataPlugins");
		analysisPluginMenu = new JMenu("AnalysisPlugins");

		menu.add(file);
		menu.add(dataPluginMenu);
		menu.add(analysisPluginMenu);

		final JTextField inputUser = new JTextField("Enter the username here",
				15);
		JButton submitButton = new JButton("submit");
		add(submitButton);

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				framework.setCurrentUser(inputUser.getText());
				framework.notifyDataPlugin();
				framework.notifyAnalysisPlugins();
				notifyAP();

				updateGui();
			}
		});

		menu.add(inputUser);
		menu.add(submitButton);

		file.add(exit);

		for (final DataPlugin dp : framework.getAllDataPlugins()) {
			final JMenuItem plugin = new JMenuItem(dp.getName());
			dataPluginMenu.add(plugin);
			plugin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					framework.setCurrentDataPlugin(dp);

					// does the update look
					updateGui();
				}
			});
		}

		for (final AnalysisPlugin ap : framework.getAllAnalysisPlugin()) {
			final JMenuItem plugin = new JMenuItem(ap.getName());
			analysisPluginMenu.add(plugin);
			plugin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					framework.changeDisplay(ap);
					notifyAP();

					// does the update look
					updateGui();
				}
			});
		}

		this.setJMenuBar(menu);

		// menu bar
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				framework.stopApplication();
			}
		});

	}

	private void notifyAP() {
		content.removeAll();
		content.setLayout(layout);
		content.revalidate();
		for (JPanel ap : framework.getDisplays()) {
			if (ap == null) {
				System.out.println("ap is null");
				break;
			}
			content.add(ap);
		}
		content.revalidate();
		content.repaint();
	}

	/**
	 * pack and repaint the frame
	 */
	public void updateGui() {
		this.pack();
		this.repaint();
	}

}