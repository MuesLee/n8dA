package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.util.ItemUtil;

public class ConfigShop extends JDialog {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigShop.class);

	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private Robot config;

	private long maxCredits;
	private long usedCredits;

	private JLabel lblCredits;

	private RoboItem[] items;
	private Attack[] attacks;
	private Defense[] defends;

	private ConfigShop(Robot startConfig, long maxCredits, RoboItem[] items,
			Attack[] attacks, Defense[] defends) {

		this.config = startConfig;
		this.maxCredits = maxCredits;
		this.items = items;
		this.attacks = attacks;
		this.defends = defends;
		this.usedCredits = calculateUsedCredits(config);

		ToolTipManager.sharedInstance().setDismissDelay(60000);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setIconImage(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));

		createGui();
		pack();
	}

	private long calculateUsedCredits(Robot config) {

		long costs = 0;

		costs += config.getConfigurationPointCosts();

		List<RoboItem> equippedItems = config.getEquippedItems();
		for (RoboItem item : equippedItems) {

			if (!item.isRemoveable()) {
				continue;
			}

			costs += item.getConfigurationPointCosts();
		}

		List<Attack> possibleAttacks = config.getPossibleAttacks();
		for (Attack atks : possibleAttacks) {
			costs += atks.getConfigurationPointCosts();
		}

		List<Defense> possibleDefends = config.getPossibleDefends();
		for (Defense defs : possibleDefends) {
			costs += defs.getConfigurationPointCosts();
		}

		return costs;
	}

	private void createGui() {

		setLayout(new BorderLayout());
		setTitle("Shop");

		add(createCreditBar(), BorderLayout.NORTH);
		add(createShop(), BorderLayout.CENTER);
	}

	private JPanel createShop() {
		JPanel shop = new JPanel();
		shop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		shop.setLayout(new BorderLayout());

		JTabbedPane tbPane = new JTabbedPane();
		shop.add(tbPane, BorderLayout.CENTER);

		tbPane.addTab("Angriff", createAttackShop());
		tbPane.addTab("Verteidigung", createDefenseShop());
		tbPane.addTab("Ausrüstung", createItemShop());

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		shop.add(btnPanel, BorderLayout.SOUTH);

		JButton btnOk = new JButton("Kaufen");
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				tryToExit();
			}
		});
		btnPanel.add(btnOk);

		return shop;
	}

	private void tryToExit() {
		if (usedCredits > maxCredits) {
			JOptionPane
					.showMessageDialog(
							this,
							"Du hast nicht genug Credits um dir das leisten zu können.",
							"Keine Credits", JOptionPane.ERROR_MESSAGE);

			return;
		}

		List<Attack> possibleAttacks = config.getPossibleAttacks();
		List<Defense> possibleDefends = config.getPossibleDefends();

		if (possibleAttacks.size() > 4 || possibleDefends.size() > 4) {
			JOptionPane
					.showMessageDialog(
							this,
							"Du hast zu viele Attacken oder Verteidigungen. \nEs sind maximal vier Fähigkeiten erlaubt.",
							"Zu viele Fähigkeiten", JOptionPane.ERROR_MESSAGE);

			return;
		}

		// Wird noch einmal geprüft, bevor das Spiel startet - Verlassen des
		// Shops wird somit möglich auch wenn keine Änderungen vorgenommen
		// wurden
		// boolean foundBasicAttack = false;
		// for (Attack a : possibleAttacks) {
		//
		// if (a.getEnergyCosts() <= 0) {
		//
		// foundBasicAttack = true;
		// break;
		// }
		// }
		//
		// if (!foundBasicAttack) {
		// JOptionPane
		// .showMessageDialog(
		// this,
		// "Es muss mindestens eine Basisattacke(keien Energiekosten) ausgewählt sein.",
		// "Keine Basisattacke", JOptionPane.ERROR_MESSAGE);
		// return;
		// }

		dispose();
	}

	private JPanel createItemShop() {
		JPanel items = new JPanel();
		items.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane();
		items.add(sp, BorderLayout.CENTER);

		JPanel scroll = new JPanel();
		scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
		sp.setViewportView(scroll);
		
		
		Collections.sort(Arrays.asList(this.items));
		
		for (RoboItem item : this.items) {

			final RoboItem fitem = item;

			JPanel row = new JPanel();
			row.setLayout(new BorderLayout());

			JPanel buy = new JPanel();
			buy.setLayout(new BoxLayout(buy, BoxLayout.X_AXIS));
			buy.add(Box.createHorizontalStrut(20));
			row.add(buy, BorderLayout.EAST);

			buy.add(new JLabel("Credits: " + item.getConfigurationPointCosts()));
			buy.add(Box.createHorizontalStrut(10));

			int count = 0;
			for (RoboItem preConfig : config.getEquippedItems()) {

				if (preConfig.getId() == item.getId()) {

					count++;
				}
			}

			final SpinnerNumberModel model = new SpinnerNumberModel(count, 0,
					Integer.MAX_VALUE, 1);
			JSpinner use = new JSpinner(model);
			use.setPreferredSize(new Dimension(50, 0));

			use.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

					int soll = model.getNumber().intValue();

					List<RoboItem> its = config.getEquippedItems();
					int haben = 0;

					for (int i = 0; i < its.size(); i++) {

						if (its.get(i).getId() == fitem.getId()) {

							haben++;

							if (haben > soll) {
								if (its.get(i).isRemoveable()) {

									its.remove(i);
									i--;
								}
							}
						}
					}

					int add = soll - haben;

					for (int i = 0; i < add; i++) {

						its.add(fitem);
					}

					haben = 0;
					for (RoboItem i : its) {

						if (i.getId() == fitem.getId()) {
							haben++;
						}
					}
					model.setValue(haben);

					updateCreditLabel();
				}
			});

			DefaultEditor editor = new JSpinner.DefaultEditor(use);
			editor.getTextField().setEnabled(true);
			editor.getTextField().setEditable(false);
			use.setEditor(editor);
			buy.add(use);

			JPanel info = new JPanel();
			info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
			row.add(info, BorderLayout.CENTER);

			JLabel label = new JLabel(String.format("Name: %s", item.getName()));
			info.add(label);

			scroll.add(row);
		}
		
		return items;
	}

	private JPanel createDefenseShop() {

		JPanel defense = new JPanel();
		defense.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane();
		defense.add(sp, BorderLayout.CENTER);

		JPanel scroll = new JPanel();
		scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
		sp.setViewportView(scroll);
		Collections.sort(Arrays.asList(this.defends));
		
		for (Defense def : defends) {

			final Defense fdef = def;

			JPanel row = new JPanel();
			row.setLayout(new BorderLayout());

			JPanel buy = new JPanel();
			buy.setLayout(new BoxLayout(buy, BoxLayout.X_AXIS));
			buy.add(Box.createHorizontalStrut(20));
			row.add(buy, BorderLayout.EAST);

			JCheckBox use = new JCheckBox("Credits: "
					+ def.getConfigurationPointCosts());

			List<Defense> preSelected = config.getPossibleDefends();
			for (Defense pre : preSelected) {

				if (pre.getId() == fdef.getId()) {

					use.setSelected(true);
					break;
				} else {

					use.setSelected(false);
				}
			}

			use.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {

						List<Defense> defs = config.getPossibleDefends();
						defs.add(fdef);

						config.setPossibleDefends(defs);
					} else {

						List<Defense> defs = config.getPossibleDefends();

						for (int i = 0; i < defs.size(); i++) {

							if (defs.get(i).getId() == fdef.getId()) {

								defs.remove(i);
								break;
							}
						}

						config.setPossibleDefends(defs);
					}

					updateCreditLabel();
				}
			});
			buy.add(use);

			JPanel info = new JPanel();
			info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
			row.add(info, BorderLayout.CENTER);

			JLabel label = new JLabel(
					String.format(
							"Name: %s - Reflektion: %d%% - Energiekosten: %d - Typ: %s",
							def.getName(), (int) (100 * def
									.getBonusOnDefenseFactor()), def
									.getEnergyCosts(), def.getRobotActionType()
									.getHumanReadableString()));
			label.setToolTipText(ItemUtil.createToolTipTextForRobotActions(def));
			ToolTipManager.sharedInstance().registerComponent(label);
			info.add(label);

			scroll.add(row);
		}

		return defense;
	}

	private JPanel createAttackShop() {

		JPanel attack = new JPanel();
		attack.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane();
		attack.add(sp, BorderLayout.CENTER);

		JPanel scroll = new JPanel();
		scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
		sp.setViewportView(scroll);
		
		Collections.sort(Arrays.asList(this.attacks));
		
		for (Attack atk : attacks) {

			final Attack fatk = atk;

			JPanel row = new JPanel();
			row.setLayout(new BorderLayout());

			JPanel buy = new JPanel();
			buy.setLayout(new BoxLayout(buy, BoxLayout.X_AXIS));
			buy.add(Box.createHorizontalStrut(20));
			row.add(buy, BorderLayout.EAST);

			JCheckBox use = new JCheckBox("Credits: "
					+ atk.getConfigurationPointCosts());

			List<Attack> preSelected = config.getPossibleAttacks();
			for (Attack pre : preSelected) {

				if (pre.getId() == fatk.getId()) {

					use.setSelected(true);
					break;
				} else {

					use.setSelected(false);
				}
			}

			use.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {

						List<Attack> atks = config.getPossibleAttacks();
						atks.add(fatk);

						config.setPossibleAttacks(atks);
					} else {

						List<Attack> atks = config.getPossibleAttacks();

						for (int i = 0; i < atks.size(); i++) {

							if (atks.get(i).getId() == fatk.getId()) {

								atks.remove(i);
								break;
							}
						}

						config.setPossibleAttacks(atks);
					}
					updateCreditLabel();
				}
			});
			buy.add(use);

			JPanel info = new JPanel();
			info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
			row.add(info, BorderLayout.CENTER);

			JLabel label = new JLabel(String.format(
					"Name: %s - Schaden: %d - Energiekosten: %d - Typ: %s", atk
							.getName(), atk.getDamage(), atk.getEnergyCosts(),
					atk.getRobotActionType().getHumanReadableString()));
			label.setToolTipText(ItemUtil.createToolTipTextForRobotActions(atk));
			ToolTipManager.sharedInstance().registerComponent(label);
			info.add(label);
			
			scroll.add(row);
		}

		return attack;
	}

	private JPanel createCreditBar() {
		JPanel creditBar = new JPanel();
		creditBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		lblCredits = new JLabel();
		updateCreditLabel();
		creditBar.add(lblCredits);

		return creditBar;
	}

	private void updateCreditLabel() {

		usedCredits = calculateUsedCredits(config);
		lblCredits.setText(String.format("Credits: %d$",
				(maxCredits - usedCredits)));

		LOG.debug("Update credits -> Max available: {}, Used: {}", maxCredits,
				usedCredits);
	}

	public Robot getConfiguration() {

		return config;
	}

	public static Robot getConfiguration(Robot startConfig, long maxCredits,
			RoboItem[] items, Attack[] attacks, Defense[] defends) {

		ConfigShop shop = new ConfigShop(startConfig, maxCredits, items,
				attacks, defends);
		shop.setLocationRelativeTo(null);
		shop.setVisible(true);

		return shop.getConfiguration();
	}
}
