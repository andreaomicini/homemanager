package it.unibo.homemanager.userinterfaces.agents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.colorchooser.ColorSelectionModel;

import it.unibo.homemanager.agents.wardrobe.ContentChangeListener;
import it.unibo.homemanager.agents.wardrobe.Dress;
import it.unibo.homemanager.agents.wardrobe.Outfit;
import it.unibo.homemanager.agents.wardrobe.WardrobeAgent;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;

public class WardrobePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WardrobeAgent wardrobeAgent;
	private ViewManageAgentPanel butlerPanel;

	private HashMap<String, Color> colorsMap;

	private JLabel name;
	private JButton back;
	private JButton menu;
	private JLabel content;
	private JLabel image;
	private JTextArea wardrobeContent;
	private JScrollPane scrollContent;
	private JLabel yourOutfit;

	private JTextArea outputOutfit;
	private JScrollPane scrollOutfits;

	private JLabel colorLabel;
	private JComboBox<String> colorList;
	private JTextArea colorExample;

	private JButton generateOutfit;
	private JButton cleanOutfit;

	public WardrobePanel(ViewManageAgentPanel butlerPanel, WardrobeAgent wardrobeAgent, TracePanel tracePanel) {
		this.wardrobeAgent = wardrobeAgent;
		this.butlerPanel = butlerPanel;
		initColorsMap();
		initComponents();

		getWardrobeAgent().addStateListener(new ContentChangeListener() {

			@Override
			public void onContentChanged() {
				onContentChange();

			}
		});
	//	onContentChange();

	}

	private void initColorsMap() {
		this.colorsMap = new HashMap<>();
		colorsMap.put("Black", Color.black);
		colorsMap.put("White", Color.white);
		colorsMap.put("Red", Color.red);
		colorsMap.put("Magenta", Color.magenta);
		colorsMap.put("Green", Color.green);
		colorsMap.put("Yellow", Color.yellow);
		colorsMap.put("Blue", Color.blue);
		colorsMap.put("Orange", Color.orange);
		colorsMap.put("Pink", Color.pink);
		colorsMap.put("Grey", Color.gray);

	}

	private void initComponents() {

		this.setBackground(Color.WHITE);

		name = new JLabel("WARDROBE");
		name.setFont(new Font("Courier New", Font.BOLD, 24));

		back = new JButton("Back");

		menu = new JButton("Menu");

		content = new JLabel("Wardrobe content");
		content.setFont(new Font("Courier New", Font.BOLD, 16));

		image = new JLabel(new ImageIcon(getClass().getResource("/wardrobe.jpg")));
		// image = new JLabel(new ImageIcon("/wardrobe.jpg"));

		wardrobeContent = new JTextArea();

		wardrobeContent.setFont(new Font("Courier New", Font.PLAIN, 14));
		wardrobeContent.setEditable(false);
		wardrobeContent.setBackground(new Color(225, 225, 225));
		wardrobeContent.setLineWrap(true);

		scrollContent = new JScrollPane(wardrobeContent);
		// scrollContent.setPreferredSize(new Dimension(200, 200));

		yourOutfit = new JLabel("Your outfits");
		yourOutfit.setFont(new Font("Courier New", Font.BOLD, 16));

		outputOutfit = new JTextArea();
		outputOutfit.setFont(new Font("Courier New", Font.PLAIN, 14));
		outputOutfit.append("Choose a color and press Generate outfit!");
		outputOutfit.setEditable(false);
		outputOutfit.setBackground(new Color(225, 225, 225));
		outputOutfit.setLineWrap(true);

		scrollOutfits = new JScrollPane(outputOutfit);

		colorLabel = new JLabel("Choose a color: ");
		colorLabel.setFont(new Font("Courier New", Font.BOLD, 16));

		Vector<String> colors = new Vector<String>(getColorsMap().keySet());
		colors.add("NO COLOR");
		colorList = new JComboBox<String>(colors);
		colorList.setSelectedItem("NO COLOR");
		colorList.setFont(new Font("Courier New", Font.BOLD, 12));
		colorList.setBorder(BorderFactory.createMatteBorder(15, 0, 15, 0, Color.WHITE));

		colorExample = new JTextArea(3, 3);
		colorExample.setEditable(false);
		colorExample.setText("");
		colorExample.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(7, 0, 7, 0, Color.WHITE), BorderFactory.createLineBorder(Color.black, 1)));

		cleanOutfit = new JButton("Clean outfit");

		generateOutfit = new JButton("Generate outfit");

		BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(bl);

		Box upBox = Box.createHorizontalBox();
		upBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));

		// Box boxName = Box.createHorizontalBox();
		// Box boxBack = Box.createHorizontalBox();
		// Box boxMenu = Box.createHorizontalBox();
		//
		// boxName.add(name);
		// boxBack.add(back);
		// boxMenu.add(menu);

		// upBox.add(boxName);
		// upBox.add(boxBack);
		// upBox.add(boxMenu);

		upBox.add(Box.createRigidArea(new Dimension(50, 0)));
		upBox.add(name);
		upBox.add(Box.createHorizontalGlue());
		// upBox.add(Box.createRigidArea(new Dimension(50, 0)));
		upBox.add(back);
		// upBox.add(Box.createHorizontalGlue());
		upBox.add(Box.createRigidArea(new Dimension(50, 0)));
		upBox.add(menu);
		upBox.add(Box.createRigidArea(new Dimension(50, 0)));

		Box labelBox = Box.createHorizontalBox();
		labelBox.add(Box.createRigidArea(new Dimension(30, 0)));
		labelBox.add(content);
		labelBox.add(Box.createHorizontalGlue());
		labelBox.add(yourOutfit);
		labelBox.add(Box.createRigidArea(new Dimension(50, 0)));

		// Box leftBox = Box.createVerticalBox();
		// leftBox.add(content);
		// leftBox.add(scrollContent);

		// Box rightBox = Box.createVerticalBox();
		// rightBox.add(yourOutfit);
		// rightBox.add(outputOutfit);

		Box centerBox = Box.createHorizontalBox();
		centerBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));

		// centerBox.add(leftBox);
		centerBox.add(scrollContent);
		centerBox.add(image);
		centerBox.add(scrollOutfits);
		// centerBox.add(rightBox);

		Box bottomBox = Box.createHorizontalBox();
		bottomBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
		bottomBox.setBackground(Color.WHITE);

		bottomBox.add(colorLabel);
		bottomBox.add(colorList);
		bottomBox.add(Box.createRigidArea(new Dimension(10, 0)));

		bottomBox.add(colorExample);

		bottomBox.add(Box.createRigidArea(new Dimension(10, 0)));
		bottomBox.add(generateOutfit);
		bottomBox.add(Box.createRigidArea(new Dimension(10, 0)));
		bottomBox.add(cleanOutfit);

		Box containerBox = Box.createVerticalBox();
		containerBox.setBackground(Color.WHITE);
		containerBox.add(upBox);
		containerBox.add(labelBox);
		containerBox.add(centerBox);
		containerBox.add(bottomBox);

		this.add(containerBox);

		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClickButtonMenu(evt);
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClickButtonBack(evt);
			}
		});

		generateOutfit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onGenerateOutfit(evt);
			}

		});

		cleanOutfit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onCleanOutfit();
			}

		});

		colorList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String colore = (String) colorList.getSelectedItem();

				getWardrobeAgent().setColor(colore.toLowerCase());
				colorExample.setBackground(getColorsMap().get(colore));
			}
		});
	}

	public HashMap<String, Color> getColorsMap() {
		return colorsMap;
	}

	public void init() {
		setVisible(true);
	}

	public void hidePanel() {
		setVisible(false);
	}

	public WardrobeAgent getWardrobeAgent() {
		return wardrobeAgent;
	}

	public ViewManageAgentPanel getButlerPanel() {
		return butlerPanel;
	}

	private void onClickButtonBack(ActionEvent event) {
		hidePanel();
		getButlerPanel().showPanel();
	}

	private void onClickButtonMenu(ActionEvent evt) {

		hidePanel();
		getButlerPanel().menu();
	}

	private void onContentChange() {

		List<Dress> content = getWardrobeAgent().getContent();

		wardrobeContent.setText("");

		for (Dress c : content) {
			wardrobeContent.append(c + "\n\n");
		}
	}

	private void onGenerateOutfit(ActionEvent evt) {

		Vector<Outfit> outfits = getWardrobeAgent().generateOutfit();

		StringBuilder sb = new StringBuilder();

		for (Outfit o : outfits) {

			// sb.append(o.getTop().getName() + " " + o.getTop().getColor() +
			// "\n");
			// sb.append(o.getBot().getName() + " " + o.getBot().getColor() +
			// "\n");

			sb.append(o.getTop() + "\n");
			sb.append(o.getBot() + "\n");

			if (!o.getAccessories().isEmpty()) {

				sb.append("\nAccessori:\n");

				for (String accessory : o.getAccessories()) {

					sb.append(accessory + "\n");

				}

			}

			sb.append("\n--------------------\n");

		}

		outputOutfit.setText(sb.toString());

	}

	private void onCleanOutfit() {
		outputOutfit.setText("");
	}
}
