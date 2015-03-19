package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Countdown extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private javax.swing.Timer timer = new javax.swing.Timer(1000, this);

	private int seconds = 0;

	private JLabel lblTime;

	public Countdown() {

		setLayout(new BorderLayout());

		lblTime = new JLabel();
		lblTime.setHorizontalAlignment(JLabel.CENTER);
		lblTime.setVerticalAlignment(JLabel.CENTER);
		lblTime.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		add(lblTime, BorderLayout.CENTER);

		updateTime();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == timer) {

			seconds--;
			if (seconds <= 0) {

				timer.stop();
				seconds = 0;
			}

			updateTime();
			fireTimeChangedEvent();
		}
	}

	private void updateTime() {

		lblTime.setText("" + seconds);
	}

	public void setTime(int seconds) {

		if (seconds < 0) {
			seconds = 0;
		}

		this.seconds = seconds;

		updateTime();
	}

	public int getTime() {

		return this.seconds;
	}

	public void startCountdown() {

		timer.start();
	}

	public void stopCountdown() {

		timer.stop();
		seconds = 0;
	}

	private void fireTimeChangedEvent() {

		ActionListener[] listeners = listenerList
				.getListeners(ActionListener.class);

		for (ActionListener al : listeners) {

			al.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, "timeChanged"));
		}
	}

	public void addActionListener(ActionListener al) {

		listenerList.add(ActionListener.class, al);
	}

	public void removeActionListener(ActionListener al) {

		listenerList.remove(ActionListener.class, al);
	}
}
