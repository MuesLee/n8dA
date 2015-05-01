package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HelpFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JEditorPane editorPane;
	private JScrollPane scrollPane;

	public HelpFrame() {
		configure();

		setVisible(true);

	}

	private void configure() {
		setMinimumSize(new Dimension(200, 200));

		String content = getEditorContent();
		editorPane = new JEditorPane("text/html", content);
		editorPane.setEditable(false);
		scrollPane = new JScrollPane(editorPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Regeln");
		add(scrollPane, BorderLayout.CENTER);
		pack();
	}

	private String getEditorContent() {
		
		//TODO Timo: HTML File erstellen und das ganze übersichtlich machen...
		
		String style = "<style type=\"text/css\">.tg  {border-collapse:collapse;border-spacing:0;} .tg td{font-family:Verdana, sans-serif;font-size:14px;padding:0px 3px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}.tg th{font-family:Verdana, sans-serif;font-size:14px;font-weight:normal;padding:0px 3px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}.tg .tg-9vto{font-family:Verdana, Geneva, sans-serif !important;}.tg .tg-zlxb{background-color:#ffffff;text-align:center}.tg .tg-s6z2{text-align:center}.tg .tg-6997{background-color:#ffffff;color:#000000;text-align:center}</style>";
		String table = "<table class=\"tg\">  <tr>    <th class=\"tg-9vto\">DEF -><br>ATT</th>    <th class=\"tg-9vto\">Feuer</th>    <th class=\"tg-9vto\">Wasser</th>    <th class=\"tg-031e\">Blitz</th>  </tr>  <tr>    <td class=\"tg-031e\">Feuer</td>    <td class=\"tg-zlxb\">o</td>    <td class=\"tg-s6z2\">-</td>    <td class=\"tg-s6z2\">+</td>  </tr>  <tr>    <td class=\"tg-031e\">Wasser</td>    <td class=\"tg-s6z2\">+</td>    <td class=\"tg-6997\">o</td>    <td class=\"tg-s6z2\">-</td>  </tr>  <tr>    <td class=\"tg-031e\">Blitz</td>    <td class=\"tg-s6z2\">-</td>    <td class=\"tg-s6z2\">+</td>    <td class=\"tg-6997\">o</td>  </tr></table>";
		String end = "</body></html>";
		
		String content = "<html><head>"+ style +"</head><body>";
		content +="<h2>Spielmechanik</h2>";
		content +=table;
		content += "Legende: <br>";
		content += "+ = Starke Verteidigung. Der erlittene Schaden wird vollständig verhindert und anteilig reflektiert.<br>";
		content += "o = Neutrale Verteidigung. Der erlittene Schaden wird auf (75%- Verteidungswert) reduziert.<br>";
		content += "- = Schwache Verteidigung. Voller Schaden.<br>";
		content += "<h2>Statuseffekte</h2>";
		content += "<b>Resistenz:</b> 75% weniger Schaden durch dieses Element<br>";
		content += "<b>Anfälligkeit:</b> 75% mehr Schaden durch dieses Element<br>";
		content += "<i>Neue Statuseffekte konsumieren bzw. verlängern bestehende Statuseffekte des selben Elements!</i>";
		
		content+=end;
		
		return content;
	}

}
