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
		setSize(new Dimension(200, 200));

		String content = getEditorContent();
		editorPane = new JEditorPane("text/html", content);
		editorPane.setEditable(false);
		scrollPane = new JScrollPane(editorPane);

		add(scrollPane, BorderLayout.CENTER);
	}

	private String getEditorContent() {
		
		//TODO Timo: HTML File erstellen und das ganze übersichtlich machen...
		
		String style = "<style type=\"text/css\">.tg  {border-collapse:collapse;border-spacing:0;} .tg td{font-family:Verdana, sans-serif;font-size:14px;padding:0px 3px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}.tg th{font-family:Verdana, sans-serif;font-size:14px;font-weight:normal;padding:0px 3px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}.tg .tg-9vto{font-family:Verdana, Geneva, sans-serif !important;}.tg .tg-zlxb{background-color:#ffffff;text-align:center}.tg .tg-s6z2{text-align:center}.tg .tg-6997{background-color:#ffffff;color:#000000;text-align:center}</style>";
		String header = "<h2>Angriffstypen</h2>";
		String br = "<br>";
		String end = "</body></html>";
		String table = "<table class=\"tg\">  <tr>    <th class=\"tg-9vto\">DEF -><br>ATT</th>    <th class=\"tg-9vto\">Feuer</th>    <th class=\"tg-9vto\">Wasser</th>    <th class=\"tg-031e\">Blitz</th>  </tr>  <tr>    <td class=\"tg-031e\">Feuer</td>    <td class=\"tg-zlxb\">o</td>    <td class=\"tg-s6z2\">-</td>    <td class=\"tg-s6z2\">+</td>  </tr>  <tr>    <td class=\"tg-031e\">Wasser</td>    <td class=\"tg-s6z2\">+</td>    <td class=\"tg-6997\">o</td>    <td class=\"tg-s6z2\">-</td>  </tr>  <tr>    <td class=\"tg-031e\">Blitz</td>    <td class=\"tg-s6z2\">-</td>    <td class=\"tg-s6z2\">+</td>    <td class=\"tg-6997\">o</td>  </tr></table>";
		
		String content = "<html><head>"+ style +"</head><body>";
		content +=header;
		content +=table;
		content+=end;
		
		return content;
	}

}
