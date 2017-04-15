package com.polycoding.powertraining.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import org.osbot.rs07.api.ui.EquipmentSlot;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.util.Presets;
import com.polycoding.powertraining.util.SpecialWeapon;

public class Gui extends JFrame {

	private JPanel contentPane;
	private PolycodingPowerTrain s;
	private JTextField txtNpcs;
	private JTextField txtItems;
	private JCheckBox chkRanging;
	private JCheckBox chkLootBuryBones;
	private JSpinner numHp;
	private JButton btnStart;
	private JSpinner numMaxDistance;
	private JSpinner numBuryAt;
	private JComboBox cmbPreset;
	private JTextField txtLevelExclusions;
	private JCheckBox chkDropNotif;
	private JCheckBox chkLogoutFood;
	private JComboBox cmbBonesType;
	private JTextField txtStopLoot;
	private JComboBox cmbSpecialWeapons;
	private JCheckBox chkSpecialAttack;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public Gui(PolycodingPowerTrain s) {

		setTitle("Poly PowerTrainer v" + s.getVersion());
		this.s = s;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 478, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEatAtX = new JLabel("Eat at % hp: ");
		lblEatAtX.setBounds(341, 228, 90, 14);
		contentPane.add(lblEatAtX);

		numHp = new JSpinner();
		numHp.setModel(new SpinnerNumberModel(50, 10, 90, 1));
		numHp.setBounds(341, 249, 53, 20);
		contentPane.add(numHp);

		txtNpcs = new JTextField();
		txtNpcs.setBounds(10, 51, 449, 20);
		contentPane.add(txtNpcs);
		txtNpcs.setColumns(10);

		JLabel lblNewLabel = new JLabel("NPC(s) NAMES");
		lblNewLabel.setBounds(10, 35, 121, 14);
		contentPane.add(lblNewLabel);

		JLabel lblItemsNames = new JLabel("ITEM(s) NAMES");
		lblItemsNames.setBounds(10, 87, 121, 14);
		contentPane.add(lblItemsNames);

		txtItems = new JTextField();
		txtItems.setColumns(10);
		txtItems.setBounds(10, 103, 449, 20);
		contentPane.add(txtItems);

		chkLootBuryBones = new JCheckBox("Loot & Bury Bones");
		chkLootBuryBones.setBounds(8, 172, 134, 23);
		contentPane.add(chkLootBuryBones);

		chkRanging = new JCheckBox("Ranging (Arrows/bolts only)");
		chkRanging.setBounds(146, 228, 173, 20);
		contentPane.add(chkRanging);

		btnStart = new JButton("DEESTROOYY!!!!!");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStart();
			}
		});
		btnStart.setBounds(318, 5, 134, 35);
		contentPane.add(btnStart);

		JLabel lblNewLabel_1 = new JLabel("Distance safety limit");
		lblNewLabel_1.setBounds(341, 172, 128, 14);
		contentPane.add(lblNewLabel_1);

		numMaxDistance = new JSpinner();
		numMaxDistance.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		numMaxDistance.setBounds(341, 198, 53, 20);
		contentPane.add(numMaxDistance);

		JLabel lblBuryBonesAt = new JLabel("Bury bones at ? many");
		lblBuryBonesAt.setBounds(8, 230, 128, 14);
		contentPane.add(lblBuryBonesAt);

		numBuryAt = new JSpinner();
		numBuryAt.setModel(new SpinnerNumberModel(4, 1, 24, 1));
		numBuryAt.setBounds(8, 256, 53, 20);
		contentPane.add(numBuryAt);

		JLabel lblPresets = new JLabel("Presets");
		lblPresets.setBounds(10, 10, 46, 14);
		contentPane.add(lblPresets);

		cmbPreset = new JComboBox<>();
		cmbPreset.setModel(new DefaultComboBoxModel(Presets.values()));
		cmbPreset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmbPreset();
			}
		});
		cmbPreset.setBounds(66, 7, 150, 20);
		contentPane.add(cmbPreset);

		JLabel lblNpcsLevelExclusions = new JLabel("NPC(s) LEVEL EXCLUSIONS");
		lblNpcsLevelExclusions.setBounds(10, 129, 226, 14);
		contentPane.add(lblNpcsLevelExclusions);

		txtLevelExclusions = new JTextField();
		txtLevelExclusions.setColumns(10);
		txtLevelExclusions.setBounds(10, 145, 449, 20);
		contentPane.add(txtLevelExclusions);

		chkDropNotif = new JCheckBox("Listen to drop notifications");
		chkDropNotif.setBounds(146, 172, 173, 23);
		contentPane.add(chkDropNotif);

		chkLogoutFood = new JCheckBox("Stop when out of food");
		chkLogoutFood.setSelected(true);
		chkLogoutFood.setBounds(146, 201, 173, 23);
		contentPane.add(chkLogoutFood);

		cmbBonesType = new JComboBox();
		cmbBonesType.setModel(new DefaultComboBoxModel(Bones.values()));
		cmbBonesType.setBounds(8, 199, 105, 20);
		contentPane.add(cmbBonesType);

		JLabel lblStopAfterLooting = new JLabel("Stop after looting item:");
		lblStopAfterLooting.setBounds(11, 287, 132, 14);
		contentPane.add(lblStopAfterLooting);

		txtStopLoot = new JTextField();
		txtStopLoot.setBounds(10, 307, 134, 23);
		contentPane.add(txtStopLoot);
		txtStopLoot.setColumns(10);

		cmbSpecialWeapons = new JComboBox();
		cmbSpecialWeapons.setModel(new DefaultComboBoxModel(SpecialWeapon.values()));
		cmbSpecialWeapons.setBounds(146, 284, 173, 20);
		contentPane.add(cmbSpecialWeapons);

		chkSpecialAttack = new JCheckBox("Special Attack");
		chkSpecialAttack.setBounds(146, 259, 134, 14);
		contentPane.add(chkSpecialAttack);
	}

	private LinkedList<String> parseTextToList(String text) {
		if (text.isEmpty())
			return null;
		LinkedList<String> tmpList = new LinkedList<String>();
		text = text.trim().replace(", ", ",");
		if (text.substring(text.length() - 1).equalsIgnoreCase(",")) {
			text = text.substring(0, text.length() - 1);
		}
		for (String s : text.split(",")) {
			tmpList.add(s.toUpperCase());
		}
		return tmpList;
	}

	public void log(Object o) {
		System.out.println(o.toString());
	}

	protected void btnStart() {
		if (chkRanging.isSelected()) {
			if (s.equipment.isWearingItem(EquipmentSlot.ARROWS))
				s.is.ammoName = s.equipment.getItemInSlot(EquipmentSlot.ARROWS.slot).getName().trim().toUpperCase();
			else
				JOptionPane.showMessageDialog(null, "No ammo found! Arrows/Bolts only");

		}
		if (!txtStopLoot.getText().isEmpty()) {
			s.is.stopLootName = txtStopLoot.getText().trim().toUpperCase();
		}
		if (!txtItems.getText().isEmpty())
			s.is.itemList = parseTextToList(txtItems.getText());
		if (!txtNpcs.getText().isEmpty())
			s.is.npcList = parseTextToList(txtNpcs.getText());
		if (!txtLevelExclusions.getText().isEmpty())
			s.is.lvlList = parseTextToList(txtLevelExclusions.getText());
		if (chkSpecialAttack.isSelected()) {
			s.is.specialWeapon = SpecialWeapon.values()[cmbSpecialWeapons.getSelectedIndex()];
			if (!s.equipment.isWearingItem(EquipmentSlot.WEAPON)) {
				JOptionPane.showMessageDialog(null, "You must be wielding a weapon for specials to work...");
				return;
			}
			s.is.mainWeapon = s.equipment.getItemInSlot(EquipmentSlot.WEAPON.slot).getName();
		}

		s.is.eatPercent = (Integer) numHp.getValue();
		s.is.isRanging = (chkRanging.isSelected() && !s.is.ammoName.isEmpty());
		if (chkLootBuryBones.isSelected()) {
			s.is.lootBuryBones = true;
			s.is.bonesName = Bones.values()[cmbBonesType.getSelectedIndex()].toString().replace("_", " ");
			s.is.buryAt = (Integer) numBuryAt.getValue();
		}
		s.is.maxDistance = (Integer) numMaxDistance.getValue();
		s.is.lootValuables = chkDropNotif.isSelected();
		s.is.stopOutOfFood = chkLogoutFood.isSelected();
		s.startTime = System.currentTimeMillis();
		s.uiDone = true;

		this.setVisible(false);
	}

	protected void cmbPreset() {
		txtNpcs.setText(Presets.values()[cmbPreset.getSelectedIndex()].getNpc());
		txtItems.setText(Presets.values()[cmbPreset.getSelectedIndex()].getLoot());
	}

	enum Bones {
		BONES, BIG_BONES, BAT_BONES, BABYDRAGON_BONES, DRAGON_BONES, WOLF_BONES, DAGANNOTH_BONES;
	}
}
