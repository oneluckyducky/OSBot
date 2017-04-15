package com.polycoding.ui;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.osbot.rs07.api.ui.EquipmentSlot;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.util.DynamicArea.PrincipalWind;
import com.polycoding.nmz.util.enums.HealthDegrade;
import com.polycoding.nmz.util.enums.PotionData;
import com.polycoding.nmz.util.enums.RegularPotionData;
import com.polycoding.nmz.util.enums.SpecialWeapon;

public class Gui extends JFrame {
	private JCheckBox chkOverload;
	private JSpinner numOverload;
	private JCheckBox chkAbsorption;
	private JSpinner numAbsorption;
	private JCheckBox chkSRange;
	private JSpinner numSRange;
	private JCheckBox chkSMage;
	private JSpinner numSMage;
	private JCheckBox chkOnlyOnce;
	private JLabel lblNewLabel_1;
	private JComboBox cmbAfkLocation;
	private JButton btnStart;

	private PolyNMZ s;
	private JComboBox cmbHealthDegrade;
	private JCheckBox chkHpAt1;
	private JComboBox cmbAttackOrder;
	private JCheckBox chkAttackNpcs;
	private JCheckBox chkSpecialAttack;
	private JCheckBox chkSStrength;
	private JSpinner numSStrength;
	private JCheckBox chkSDefence;
	private JSpinner numSDefence;
	private JCheckBox chkPrayerPot;
	private JSpinner numPrayerPot;
	private JCheckBox chkSRestore;
	private JSpinner numSRestore;
	private JCheckBox chkSAttack;
	private JSpinner numSAttack;
	private JCheckBox chkPrayMelee;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JComboBox cmbSpecial;
	private JCheckBox chkBlowpipe;
	private JLabel lblRechargeAt;
	private JLabel lblOrAt;
	private JSpinner numBlowpipeRechargePercent;
	private JTextField txtBlowpipeDartNum;
	private JSpinner numMaxDistAfk;

	/**
	 * Create the frame.
	 */
	public Gui(PolyNMZ s) {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				s.stopWithMessage("GUI closed, stopping script.");
			}
		});
		this.s = s;
		setTitle("Poly NMZ");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 434, 262);
		getContentPane().add(tabbedPane);

		panel = new JPanel();
		tabbedPane.addTab("Main", null, panel, null);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel(
				"<html>This script is aimed for powering out exp in NMZ.<br><br>\r\nPlease preset the dream you want to perform repeatedly. If you have a practice mode preset, then it will do that over and over. You've been warned</html>\r\n");
		lblNewLabel.setBounds(10, 0, 409, 70);
		panel.add(lblNewLabel);

		chkOnlyOnce = new JCheckBox("Only 1 round");
		chkOnlyOnce.setBounds(10, 77, 114, 23);
		panel.add(chkOnlyOnce);

		btnStart = new JButton("Start");
		btnStart.addActionListener(a -> btnStart());
		btnStart.setBounds(320, 193, 99, 30);
		panel.add(btnStart);

		lblNewLabel_1 = new JLabel("Choose how to keep HP at 1");
		lblNewLabel_1.setBounds(10, 163, 150, 23);
		panel.add(lblNewLabel_1);

		cmbAfkLocation = new JComboBox();
		cmbAfkLocation.setModel(new DefaultComboBoxModel(PrincipalWind.values()));
		cmbAfkLocation.setBounds(297, 94, 122, 23);
		panel.add(cmbAfkLocation);

		JLabel lblNewLabel_2 = new JLabel("AFK Location");
		lblNewLabel_2.setBounds(297, 81, 114, 14);
		panel.add(lblNewLabel_2);

		cmbHealthDegrade = new JComboBox();
		cmbHealthDegrade.setEnabled(false);
		cmbHealthDegrade.setModel(new DefaultComboBoxModel(HealthDegrade.values()));
		cmbHealthDegrade.setBounds(10, 197, 137, 23);
		panel.add(cmbHealthDegrade);

		chkHpAt1 = new JCheckBox("Bring HP to 1");
		chkHpAt1.addActionListener(e -> {
			if (chkHpAt1.isSelected()) {
				cmbHealthDegrade.setEnabled(chkHpAt1.isSelected());
				chkPrayMelee.setSelected(false);
			}
		});
		chkHpAt1.setBounds(10, 133, 137, 23);
		panel.add(chkHpAt1);

		chkPrayMelee = new JCheckBox("Pray melee");
		chkPrayMelee.addActionListener(e -> {
			if (chkPrayMelee.isSelected()) {
				chkHpAt1.setSelected(false);
				cmbHealthDegrade.setEnabled(false);
			}
		});
		chkPrayMelee.setBounds(10, 107, 114, 23);
		panel.add(chkPrayMelee);

		numMaxDistAfk = new JSpinner();
		numMaxDistAfk.setBounds(362, 128, 49, 23);
		panel.add(numMaxDistAfk);

		JLabel lblNewLabel_4 = new JLabel("Max dist:");
		lblNewLabel_4.setBounds(286, 137, 67, 14);
		panel.add(lblNewLabel_4);

		panel_1 = new JPanel();
		tabbedPane.addTab("Potions", null, panel_1, null);
		panel_1.setLayout(null);

		chkOverload = new JCheckBox("Overload");
		chkOverload.addActionListener(e -> {
			numOverload.setEnabled(chkOverload.isSelected());
			if (chkOverload.isSelected()) {
				chkSAttack.setSelected(false);
				chkSStrength.setSelected(false);
				chkSDefence.setSelected(false);
			}
		});
		chkOverload.setBounds(6, 26, 105, 21);
		panel_1.add(chkOverload);

		numOverload = new JSpinner();
		numOverload.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		numOverload.setEnabled(false);
		numOverload.setBounds(117, 26, 38, 21);
		panel_1.add(numOverload);

		chkAbsorption = new JCheckBox("Absorption");
		chkAbsorption.addActionListener(e -> numAbsorption.setEnabled(chkAbsorption.isSelected()));
		chkAbsorption.setBounds(6, 54, 105, 21);
		panel_1.add(chkAbsorption);

		numAbsorption = new JSpinner();
		numAbsorption.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		numAbsorption.setEnabled(false);
		numAbsorption.setBounds(117, 54, 38, 21);
		panel_1.add(numAbsorption);

		chkSRange = new JCheckBox("S. Range");
		chkSRange.addActionListener(e -> numSRange.setEnabled(chkSRange.isSelected()));
		chkSRange.setBounds(6, 82, 105, 21);
		panel_1.add(chkSRange);

		numSRange = new JSpinner();
		numSRange.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		numSRange.setEnabled(false);
		numSRange.setBounds(117, 82, 38, 21);
		panel_1.add(numSRange);

		chkSMage = new JCheckBox("S. Mage");
		chkSMage.addActionListener(e -> numSMage.setEnabled(chkSMage.isSelected()));
		chkSMage.setBounds(6, 110, 105, 21);
		panel_1.add(chkSMage);

		numSMage = new JSpinner();
		numSMage.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		numSMage.setEnabled(false);
		numSMage.setBounds(117, 110, 38, 21);
		panel_1.add(numSMage);

		JLabel lblDosesPerEntry = new JLabel("Doses per entry");
		lblDosesPerEntry.setHorizontalAlignment(SwingConstants.CENTER);
		lblDosesPerEntry.setBounds(71, 5, 149, 14);
		panel_1.add(lblDosesPerEntry);

		JLabel lblPotsPerEntry = new JLabel("Pots per entry");
		lblPotsPerEntry.setHorizontalAlignment(SwingConstants.CENTER);
		lblPotsPerEntry.setBounds(270, 5, 149, 14);
		panel_1.add(lblPotsPerEntry);

		chkSAttack = new JCheckBox("Super Attack");
		chkSAttack.setBounds(205, 26, 105, 21);
		chkSAttack.addActionListener(e -> {
			numSAttack.setEnabled(chkSAttack.isSelected());
			if (chkSAttack.isSelected()) {
				numOverload.setEnabled(chkOverload.isSelected());
				chkOverload.setSelected(false);
			}

		});
		panel_1.add(chkSAttack);

		numSAttack = new JSpinner();
		numSAttack.setEnabled(false);
		numSAttack.setBounds(316, 26, 38, 21);
		panel_1.add(numSAttack);

		chkSStrength = new JCheckBox("Super Strength");
		chkSStrength.setBounds(205, 54, 105, 21);
		chkSStrength.addActionListener(e -> {
			numSStrength.setEnabled(chkSStrength.isSelected());
			if (chkSStrength.isSelected()) {
				numOverload.setEnabled(chkOverload.isSelected());
				chkOverload.setSelected(false);
			}

		});
		panel_1.add(chkSStrength);

		numSStrength = new JSpinner();
		numSStrength.setEnabled(false);
		numSStrength.setBounds(316, 54, 38, 21);
		panel_1.add(numSStrength);

		chkSDefence = new JCheckBox("Super Defence");
		chkSDefence.setBounds(205, 82, 105, 21);
		chkSDefence.addActionListener(e -> {
			numSDefence.setEnabled(chkSDefence.isSelected());
			if (chkSDefence.isSelected()) {
				numOverload.setEnabled(chkOverload.isSelected());
				chkOverload.setSelected(false);
			}

		});
		panel_1.add(chkSDefence);

		numSDefence = new JSpinner();
		numSDefence.setEnabled(false);
		numSDefence.setBounds(316, 82, 38, 21);
		panel_1.add(numSDefence);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Prayer restoration", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_3.setBounds(188, 110, 231, 113);
		panel_1.add(panel_3);
		panel_3.setLayout(null);

		chkPrayerPot = new JCheckBox("Prayer potion");
		chkPrayerPot.setBounds(6, 53, 105, 21);
		panel_3.add(chkPrayerPot);

		chkSRestore = new JCheckBox("Super restore");
		chkSRestore.setBounds(6, 81, 105, 21);
		panel_3.add(chkSRestore);

		numPrayerPot = new JSpinner();
		numPrayerPot.setBounds(117, 53, 38, 21);
		panel_3.add(numPrayerPot);
		numPrayerPot.setEnabled(false);

		numSRestore = new JSpinner();
		numSRestore.setBounds(117, 81, 38, 21);
		panel_3.add(numSRestore);
		numSRestore.setEnabled(false);

		JLabel lblNewLabel_3 = new JLabel(
				"<html><center>Potions will be drank when prayer is between 2-14</center></html>");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setBounds(10, 16, 215, 30);
		panel_3.add(lblNewLabel_3);
		chkSRestore.addActionListener(e -> {
			numSRestore.setEnabled(chkSRestore.isSelected());
			if (chkSRestore.isSelected()) {
				chkPrayerPot.setSelected(false);
			}
		});
		chkPrayerPot.addActionListener(e -> {
			numPrayerPot.setEnabled(chkPrayerPot.isSelected());
			if (chkPrayerPot.isSelected()) {
				chkSRestore.setSelected(false);
			}
		});

		panel_2 = new JPanel();
		tabbedPane.addTab("Combat", null, panel_2, null);
		panel_2.setLayout(null);

		chkSpecialAttack = new JCheckBox("Special attack");
		chkSpecialAttack
				.addActionListener(e -> cmbSpecial.setEnabled(chkSpecialAttack.isSelected()));
		chkSpecialAttack.setBounds(6, 20, 97, 23);
		panel_2.add(chkSpecialAttack);

		chkAttackNpcs = new JCheckBox("Attack NPCs");
		chkAttackNpcs.setBounds(6, 78, 97, 23);
		panel_2.add(chkAttackNpcs);

		cmbAttackOrder = new JComboBox<String>();
		cmbAttackOrder.setEnabled(false);
		cmbAttackOrder.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Highest lvl", "Lowest lvl", "Closest npc" }));
		cmbAttackOrder.setBounds(16, 106, 111, 23);
		panel_2.add(cmbAttackOrder);

		cmbSpecial = new JComboBox();
		cmbSpecial.setEnabled(false);
		cmbSpecial.setModel(new DefaultComboBoxModel(SpecialWeapon.values()));
		cmbSpecial.setBounds(6, 44, 145, 23);
		panel_2.add(cmbSpecial);

		chkBlowpipe = new JCheckBox("Toxic blowpipe");
		chkBlowpipe.addActionListener(e -> {
			numBlowpipeRechargePercent.setEnabled(chkBlowpipe.isSelected());
			txtBlowpipeDartNum.setEnabled(chkBlowpipe.isSelected());
		});
		chkBlowpipe.setBounds(6, 136, 121, 23);
		panel_2.add(chkBlowpipe);

		lblRechargeAt = new JLabel("Recharge at:");
		lblRechargeAt.setBounds(16, 159, 82, 23);
		panel_2.add(lblRechargeAt);

		lblOrAt = new JLabel("% or at # darts:");
		lblOrAt.setBounds(57, 181, 94, 14);
		panel_2.add(lblOrAt);

		numBlowpipeRechargePercent = new JSpinner();
		numBlowpipeRechargePercent.setEnabled(false);
		numBlowpipeRechargePercent.setBounds(6, 178, 41, 17);
		panel_2.add(numBlowpipeRechargePercent);

		txtBlowpipeDartNum = new JTextField();
		txtBlowpipeDartNum.setEnabled(false);
		txtBlowpipeDartNum.setText("1000");
		txtBlowpipeDartNum.setBounds(143, 178, 86, 20);
		panel_2.add(txtBlowpipeDartNum);
		txtBlowpipeDartNum.setColumns(10);
	}

	private void btnStart() {
		s.ss.only1Round = chkOnlyOnce.isSelected();

		if (chkSAttack.isSelected()) {
			s.ss.bankPotionData.put(RegularPotionData.SUPER_ATTACK, (int) numSAttack.getValue());
		}
		if (chkSStrength.isSelected()) {
			s.ss.bankPotionData.put(RegularPotionData.SUPER_STRENGTH,
					(int) numSStrength.getValue());
		}
		if (chkSDefence.isSelected()) {
			s.ss.bankPotionData.put(RegularPotionData.SUPER_DEFENCE, (int) numSDefence.getValue());
		}
		if (chkPrayerPot.isSelected()) {
			s.ss.bankPotionData.put(RegularPotionData.PRAYER, (int) numPrayerPot.getValue());
		}
		if (chkSRestore.isSelected()) {
			s.ss.bankPotionData.put(RegularPotionData.SUPER_RESTORE, (int) numPrayerPot.getValue());
		}

		if (chkBlowpipe.isSelected()) {
			s.ss.blowpipeRechargePercent = (int) numBlowpipeRechargePercent.getValue();
			s.ss.blowRechargeDartNum = Integer.parseInt(txtBlowpipeDartNum.getText());
		}

		if (chkSpecialAttack.isSelected()) {
			s.ss.specialWeapon = SpecialWeapon.values()[cmbSpecial.getSelectedIndex()];
			s.ss.mainWeapon = s.equipment.getItemInSlot(EquipmentSlot.WEAPON.slot).getName();
			s.log("Special weapon:" + s.ss.specialWeapon.getName());
			s.log("Main weapon: " + s.ss.mainWeapon);
		}

		if (chkHpAt1.isSelected()) {
			s.ss.hp1 = true;
			s.ss.healthDegrade = HealthDegrade.values()[cmbHealthDegrade.getSelectedIndex()];
		}
		if (chkPrayMelee.isSelected()) {
			s.ss.prayMelee = true;
		}
		s.ss.afkLocation = PrincipalWind.values()[cmbAfkLocation.getSelectedIndex()];
		s.ss.maxDistFromAfk = (int) numMaxDistAfk.getValue();
		if (chkAbsorption.isSelected()) {
			s.ss.potionsData.put(PotionData.ABSORPTION, (int) numAbsorption.getValue());
		}
		if (chkOverload.isSelected()) {
			s.ss.potionsData.put(PotionData.OVERLOAD, (int) numOverload.getValue());
		}
		if (chkSMage.isSelected()) {
			s.ss.potionsData.put(PotionData.SUPER_MAGIC, (int) numSMage.getValue());
		}
		if (chkSRange.isSelected()) {
			s.ss.potionsData.put(PotionData.SUPER_RANGING, (int) numSRange.getValue());
		}
		s.uiDone = true;
		setVisible(false);
	}
}
