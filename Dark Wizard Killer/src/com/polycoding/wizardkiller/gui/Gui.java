package com.polycoding.wizardkiller.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.Script;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.util.enums.Food;

public class Gui extends JFrame implements ChangeListener, WindowListener {

	private static final long serialVersionUID = 1L;

	private Script script;
	private MyProvider methods;

	public Gui(Script script, MyProvider methods) {
		this.script = script;
		this.methods = methods;
		initComponents();
	}

	private void button1ActionPerformed(ActionEvent e) {
		int input = cmbFood.getSelectedIndex();

		methods.attr.set("food", Food.values()[input].getId());
		methods.attr.set("bones", chkRanging.isSelected());
		methods.attr.set("eatAt", sldEatAt.getValue());
		methods.attr.set("banking", chkBanking.isSelected());
		if (chkBanking.isSelected()) {
			script.log("Banking is true!");
			methods.attr.set(
					"foodAmount",
					Integer.parseInt(JOptionPane.showInputDialog("Amount of "
							+ Food.values()[input].toString()
							+ " to withdraw at bank: ")));
		}
		if (chkRanging.isSelected()) {
			final Item item = script.equipment
					.getItemInSlot(EquipmentSlot.ARROWS.slot);
			if (item != null) {
				script.log(String.format("Now looting %s due to ranging!",
						item.getName()));
				methods.attr.set("arrow", item.getName());
				Collections.addAll(methods.itemsList, item.getName());
			} else {
				JOptionPane.showMessageDialog(null,
						"Equip arrows if you plan on ranging!");
				return;
			}
		}
		this.setVisible(false);
		this.dispose();
	}

	private void initComponents() {
		chkBanking = new JCheckBox();
		chkRanging = new JCheckBox();
		cmbFood = new JComboBox<String>(methods.FOOD_NAMES);
		btnStart = new JButton();
		label2 = new JLabel();
		sldEatAt = new JSlider(JSlider.HORIZONTAL, 10, 99, 30);

		sldEatAt.addChangeListener(this);
		sldEatAt.setMajorTickSpacing(20);
		sldEatAt.setMinorTickSpacing(5);
		sldEatAt.setPaintTicks(true);
		sldEatAt.setPaintLabels(true);

		// ======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Dark Wizard Demolisher");
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		// ---- chkBanking ----
		chkBanking.setText("Banking");
		chkBanking.setFont(new Font("Arial", Font.PLAIN, 12));
		chkBanking.setBounds(15, 5, 210, 35);
		contentPane.add(chkBanking);

		// ---- checkbox ----
		chkRanging.setText("Ranging?");
		chkRanging.setFont(new Font("Arial", Font.PLAIN, 12));
		contentPane.add(chkRanging);
		chkRanging.setBounds(150, 43, chkRanging.getPreferredSize().width,
				chkRanging.getPreferredSize().height);

		// ---- comboBox1 ----

		cmbFood.setFont(cmbFood.getFont().deriveFont(
				cmbFood.getFont().getStyle()));
		contentPane.add(cmbFood);
		cmbFood.setBounds(15, 45, 120, cmbFood.getPreferredSize().height);

		// ---- button1 ----
		btnStart.setText("Start!");
		btnStart.setFont(btnStart.getFont().deriveFont(
				btnStart.getFont().getStyle()));
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		contentPane.add(btnStart);
		btnStart.setBounds(60, 145, 111, btnStart.getPreferredSize().height);

		// ---- label2 ----
		label2.setText("Health to eat at: 30");
		label2.setFont(label2.getFont().deriveFont(label2.getFont().getStyle()));
		label2.setBounds(new Rectangle(new Point(50, 75), label2
				.getPreferredSize()));
		contentPane.add(label2);
		sldEatAt.setBounds(new Rectangle(new Point(20, 100), sldEatAt
				.getPreferredSize()));
		contentPane.add(sldEatAt);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for (int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width,
						preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height,
						preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		setSize(255, 200);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// GEN-END:initComponents
	}

	private JComboBox<String> cmbFood;
	private JButton btnStart;
	private JLabel label2;
	private JSlider sldEatAt;
	private JCheckBox chkRanging;
	private JCheckBox chkBanking;

	@Override
	public void stateChanged(ChangeEvent e) {
		label2.setText("Health to eat at: " + sldEatAt.getValue());

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		script.log("GUI Closed");

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		script.log("GUI Closing");

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		script.log(e);

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
