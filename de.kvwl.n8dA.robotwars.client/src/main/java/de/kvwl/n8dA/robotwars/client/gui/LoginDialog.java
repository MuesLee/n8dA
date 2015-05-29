package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bno.swing2.widget.BTextField;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;

public class LoginDialog extends JDialog implements ActionListener
{

	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private long credits = 0;
	private boolean canceled = false;

	private JButton btnOK;

	private JButton btnCancel;

	private CreditAccess creditClient;

	private BTextField tfName;

	private LoginDialog(CreditAccess creditClient)
	{

		this.creditClient = creditClient;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);

		createGui();
		pack();
	}

	private void createGui()
	{

		setIconImage(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
		setTitle("Login");
		setLayout(new BorderLayout());

		add(createLogin(), BorderLayout.CENTER);
		add(createButtonRow(), BorderLayout.SOUTH);
	}

	private JPanel createLogin()
	{
		JPanel login = new JPanel();
		login.setLayout(new BorderLayout());

		JLabel msg = new JLabel("Bitte gebe deinen Benutzername ein:");
		msg.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		login.add(msg, BorderLayout.NORTH);

		tfName = new BTextField();
		tfName.setHint("Benutzername...");
		tfName.setIgnoreHintFocus(true);
		login.add(tfName, BorderLayout.CENTER);

		return login;
	}

	private JPanel createButtonRow()
	{

		JPanel btns = new JPanel();
		btns.setLayout(new FlowLayout(FlowLayout.RIGHT));

		btnOK = new JButton("Anmelden");
		btnOK.addActionListener(this);
		btns.add(btnOK);

		btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(this);
		btns.add(btnCancel);

		return btns;
	}

	private void cancel()
	{

		canceled = true;
		credits = 0;
		dispose();
	}

	private void login()
	{

		try
		{
			canceled = false;
			credits = creditClient.getConfigurationPointsForPerson(getLoginName());
			dispose();
		}
		catch (NoSuchPersonException e)
		{

			JOptionPane.showMessageDialog(this, "Der angegebene Benutzer '" + getLoginName()
				+ "' ist nicht bekannt. \n" + e.getMessage(), "Login nicht gefunden", JOptionPane.WARNING_MESSAGE);
		}
		catch (RemoteException e)
		{
			JOptionPane.showMessageDialog(this, "Es ist ein unbekannter Fehler aufgetreten. \n" + e.getMessage(),
				"Feheler bei dem Verbindungsaufbau", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String getLoginName()
	{

		return tfName.getText();
	}

	private long getCredits()
	{
		return credits;
	}

	private boolean isCanceled()
	{

		return canceled;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		Object source = e.getSource();

		if (source == btnOK)
		{

			login();
		}
		else if (source == btnCancel)
		{

			cancel();
		}
	}

	public static LoginResult getCreditPoints(CreditAccess creditClient) throws CanceledException
	{
		LoginDialog login = new LoginDialog(creditClient);
		login.setLocationRelativeTo(null);
		login.setVisible(true);

		if (login.isCanceled())
		{

			throw new CanceledException();
		}

		LoginResult result = new LoginResult();
		result.setCredits(login.getCredits());
		result.setPlayerName(login.getLoginName());

		return result;
	}

	public static class CanceledException extends Exception
	{

		private static final long serialVersionUID = 1L;

	}
}
