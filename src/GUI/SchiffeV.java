import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;


// Geeignete Implementierung der Schnittstelle TableModel.
//class MyTableModel extends AbstractTableModel {
class MyTableModel extends AbstractTableModel {

    // Anzahl der Tabellenzeilen.
	private int rows = 0;
	private int spielfeldgr;

    // Inhalte der Tabellenspalten.
    private List<String> gnames = new ArrayList<String>(); // Vornamen.
    private List<String> names = new ArrayList<String>();  // Nachnamen.
    private List<Icon> icons = new ArrayList<Icon>();	   // Piktogramme.

    // Neue leere Tabellenzeile hinzufügen.
    public void addRow () {
	// Leere Inhalte hinzufügen
	// und die GUI über die neue Zeile informieren.
	gnames.add("");
	names.add("");
	icons.add(null);
	fireTableRowsInserted(rows, rows);//informiert alle Listener über das Einfügen von Zeilen
		//einfügen am Ende aller Zeilen oder
	rows++;
    }

	// Tabelle auf Eingabe von Spielfeldgröße setzen
	public void setSpielfeld (int groesse) {

		this.spielfeldgr = groesse;

	}

	public int getSpielfeld () {

		return this.spielfeldgr;

	}






    // Tabellenzeile row entfernen.
    public void removeRow (int row) {
	// Inhalte der Zeile entfernen
	// und die GUI über die Entfernung informieren.
	gnames.remove(row);
	names.remove(row);
	icons.remove(row);
	fireTableRowsDeleted(row, row);
	rows--;
    }

    // Inhalt der Tabelle zu Testzwecken ausgeben.
    // (Achtung: Eine Änderung am Inhalt einer Zelle wird offenbar erst wirksam,
    // wenn man die Zelle wieder verlassen hat.)
    public void dump () {
	for (int row = 0; row < rows; row++) {
	    System.out.println(gnames.get(row) + " | " + names.get(row)
	      + " | " + icons.get(row));
	}
    }

    /*
     *  Implementierung von Methoden der Schnittstelle TableModel.
     */

    // Anzahl Zeilen liefern.
    public int getRowCount () { return rows; }

    // Anzahl Spalten liefern.
    public int getColumnCount () { return 3; }

    // Name von Spalte col liefern.
    public String getColumnName (int col) {
	switch (col) {
	case 0: return "Vorname";
	case 1: return "Nachname";
	case 2: return "Zustand";
	default: return null; // Sollte nicht auftreten.
	}
    }

    // Klasse der Objekte in Spalte col liefern
    // (damit die Objekte sinnvoll angezeigt werden).
    public Class getColumnClass (int col) {
	return col < 2 ? String.class : ImageIcon.class;
    }

    // Wert in Zeile row und Spalte col liefern.
    public Object getValueAt (int row, int col) {
	switch (col) {
	case 0: return gnames.get(row);
	case 1: return names.get(row);
	case 2: return icons.get(row);
	default: return null; // Sollte nicht auftreten.
	}
    }

    // Wert val in Zeile row und Spalte col speichern.
    public void setValueAt (Object val, int row, int col) {
	// Inhalt ändern
	// und die GUI über die Änderung informieren.
	switch (col) {
	case 0: gnames.set(row, (String)val); break;
	case 1: names.set(row, (String)val); break;
	case 2: icons.set(row, (Icon)val); break;
	}
	fireTableCellUpdated(row, col);
    }

    // Darf die Zelle in Zeile row und Spalte col geändert werden?
    public boolean isCellEditable (int row, int col) {
	return col < 2;
    }
}

// Zweites Beispiel zur Verwendung von (AWT und) Swing.
class SchiffeV {
	//Frame mit verschiedenen Panels für Startbildschirm und eigentliches Spiel
	// für Schiffe platzieren mit z.B. Mouse Listener


	// Graphische Oberfläche aufbauen und anzeigen.
	private static void start() {




		//Komponenten Startbildschirm:
		JFrame startbildschirm = new JFrame("Schiffeversenken");
		startbildschirm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JLabel start = new JLabel("Spiele Schiffe versenken!");
		startbildschirm.add(start);
		JButton play = new JButton("Play");
		startbildschirm.setLayout(new FlowLayout());
		play.setPreferredSize(new Dimension(100, 50));
		startbildschirm.add(play);





			// Hauptfenster mit Titelbalken etc. (JFrame) erzeugen.
			// "Schiffeversenken" wird in den Titelbalken geschrieben.
			//JFrame frame = new JFrame("Schiffeversenken");

		//JPanel frame = new JPanel(new FlowLayout());

		JFrame frame = new JFrame("Schiffeversenken");


			// Beim Schließen des Fensters (z. B. durch Drücken des
			// X-Knopfs in Windows) soll das Programm beendet werden.
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Der Inhalt des Fensters soll von einem BoxLayout-Manager
			// verwaltet werden, der seine Bestandteile horizontal (von
			// links nach rechts) anordnet.


		frame.setContentPane(Box.createHorizontalBox());
		//frame.add(Box.createHorizontalBox());

			// Zwischenraum der Breite 50 oder mehr.
		frame.add(Box.createHorizontalStrut(20));
		frame.add(Box.createHorizontalGlue());

			JSlider slide = new JSlider();//Standard: rang: 0-100, initial value: 50
			//->siehe Info Konstruktor, kann man auch selber noch anpassen


			Box vbox_1 = Box.createVerticalBox();
			{

				vbox_1.add(Box.createVerticalStrut(5));
				vbox_1.add(Box.createVerticalGlue());

				JLabel label = new JLabel("Spielfeldgröße");

				//JTextField tf = new JTextField();
				//tf.setPreferredSize(new Dimension(200, 24));
				//frame.add(tf);


				//Abstand label und slider vergrößern
				//label.setVerticalAlignment(SwingConstants.);

				vbox_1.add(label);

				//vbox_1.add(Box.createVerticalStrut(5));
				//vbox_1.add(Box.createVerticalGlue());

				//vbox_1.add(tf);

				//vbox_1.add(Box.createHorizontalStrut(10));
				//vbox_1.add(Box.createHorizontalGlue());


				//JProgressBar pb = new JProgressBar();//Standard von 0-100
				//pb.setValue(0);//Fortschrittsbalken: z.B. Berechnung durchführen
				//pb.setBackground(Color.blue);


				JLabel label_slider = new JLabel();

				vbox_1.add(slide);

				slide.setPaintLabels(true); //Anzeige der Zahlen
				slide.setPaintTrack(true); //Balken werden angezeigt
				slide.setSnapToTicks(true); //Zu ganzzahligen Werten "springen"


				//Höhe zwischen den Komponenten passt noch nicht!!

				//frame.add(pb);
				//vbox_1.add(pb);


				//vbox_1.add(Box.createVerticalStrut(5));
				//vbox_1.add(Box.createVerticalGlue());

				vbox_1.add(label_slider);


				vbox_1.add(Box.createVerticalStrut(50));
				vbox_1.add(Box.createVerticalGlue());

				slide.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						//int value = slide.getValue();
						//gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)
						//pb.setValue(value);//Fortschrittsbalken zählt nach oben

						//Wenn sich slider auf einen anderen Wert bewegt soll dieser
						//Wert im Label angezeigt werden
						label_slider.setText(Integer.toString(slide.getValue()));

					}
				});


			}

			//frame.pack();
		frame.add(vbox_1);

			//Hier sind wir wieder im horizontalen Fenster:

		frame.add(Box.createHorizontalStrut(50));
		frame.add(Box.createHorizontalGlue());


			MyTableModel model = new MyTableModel();
			//DefaultTableModel model = new DefaultTableModel();
			JTable table = new JTable(model);
			//JTable table = new JTable(model.getSpielfeld(), model.getSpielfeld());
			//table.setC
			JScrollPane scrollPane = new JScrollPane(table);

			//model.setSpielfeld(slide.getValue());

			Box vbox_2 = Box.createVerticalBox();
			{
				JLabel label_gegner = new JLabel("Gegner Schiffe");
				vbox_2.add(label_gegner);

				//MyTableModel model = new MyTableModel();
				//JTable table = new JTable(model);
				table.setRowHeight(40);//setzt Höhe der einzelnen Zeilen
				//JScrollPane scrollPane = new JScrollPane(table);

				//table.set, Methode um Zeilen und Spaltenanzahl zu setzen?!

				vbox_2.add(scrollPane);

			}

			//frame.pack();
		frame.add(vbox_2);

		frame.add(Box.createHorizontalStrut(50));
		frame.add(Box.createHorizontalGlue());


			// Dreispaltige Tabelle mit Scrollmöglichkeit.
			// (Lokale Variablen, die später in anonymen Funktionen verwendet
			// werden, müssen entweder "final" deklariert werden oder "effectively
			// final" sein, d. h. ihr Wert darf nirgends geändert werden.)
			/*final*/ //MyTableModel model = new MyTableModel();
			MyTableModel model2 = new MyTableModel();

			//model reicht für table1 und table2: wenn jetzt auf Neuer Eintrag
			//dann hinzufügen von neuen Zeilen in beiden Tabellen!!!

			//DefaultTableModel model2 = new DefaultTableModel();
			/*final*/
			JTable table2 = new JTable(model2);
			//JTable table2 = new JTable(model2.getSpielfeld(), model2.getSpielfeld());
			//JTable table2 = new JTable(model2);
			//table.setRowHeight(40);
			//table2.setRowHeight(40);
			/*final*/
			JScrollPane scrollPane2 = new JScrollPane(table2);
			//frame.add(scrollPane);

			Box vbox_3 = Box.createVerticalBox();
			{

				JLabel label_eigene = new JLabel("Eigene Schiffe");
				vbox_3.add(label_eigene);

				//MyTableModel model = new MyTableModel();
				//JTable table = new JTable(model);
				table2.setRowHeight(40);//setzt Höhe der einzelnen Zeilen
				//JScrollPane scrollPane = new JScrollPane(table);

				//table.set, Methode um Zeilen und Spaltenanzahl zu setzen?!

				vbox_3.add(scrollPane2);
			}

		//frame.pack();
		frame.add(vbox_3);


		frame.add(Box.createHorizontalStrut(50));
		frame.add(Box.createHorizontalGlue());

			// Zwischenraum der Breite 50 oder mehr.
			//frame.add(Box.createHorizontalStrut(50));
			//frame.add(Box.createHorizontalGlue());

			// Vertikale Box mit diversen Knöpfen.
			Box vbox_4 = Box.createVerticalBox();
			{
				// "Neuer Eintrag" fügt eine neue Tabellenzeile am Ende hinzu
				// und "zieht" den Scrollbar ggf. ganz nach unten,
				// damit die Zeile auch sichtbar ist.
				JButton button = new JButton("Neuer Eintrag");
				button.setAlignmentX(Component.CENTER_ALIGNMENT);
				button.addActionListener(
						(e) -> {
							model.addRow();
							model2.addRow();
							SwingUtilities.invokeLater(
									() -> {
										// Scrollbar ganz nach unten "ziehen".
										// Damit dies wie gewünscht funktioniert,
										// darf es erst nach der Verarbeitung des aktuellen
										// "Knopfdrucks" ausgeführt werden, weil die neue
										// Zeile erst dann berücksichtigt wird.
										JScrollBar sb = scrollPane.getVerticalScrollBar();
										JScrollBar sb2 = scrollPane2.getVerticalScrollBar();

										sb.setValue(sb.getMaximum());
										sb2.setValue(sb2.getMaximum());
										//model.setSpielfeld(slide.getValue());

									}
							);
						}
				);
				vbox_4.add(button);
			}
			vbox_4.add(Box.createVerticalStrut(20));
			{
				// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
				JButton button = new JButton("Eintrag entfernen");
				button.setAlignmentX(Component.CENTER_ALIGNMENT);
				button.addActionListener(
						(e) -> {
							// Selektierte Tabellenzeile abfragen.
							// (Negativ, falls keine Zeile selektiert ist.
							// Wenn zuvor die letzte Zeile entfernt wurde,
							// ist diese eventuell immer noch die selektierte Zeile.)
							int row = table.getSelectedRow();
							if (row < 0 || row >= model.getRowCount()) return;

							// Diese Tabellenzeile entfernen.
							model.removeRow(row);
						}
				);
				vbox_4.add(button);
			}
			vbox_4.add(Box.createVerticalStrut(20));
			{
				// Horizontale Box mit drei Knöpfen (rot, gelb, grün).
				// Wenn einer der Knöpfe gedrückt wird, wird sein
				// Piktogramm in der dritten Spalte der selektierten
				// Tabellenzeile gespeichert.
				Box hbox = Box.createHorizontalBox();
				for (String file : new String[]
						{"C:/Users/Saskia/IdeaProjects/PPgoSV/GUIversion/src/red.png", "C:/Users/Saskia/IdeaProjects/PPgoSV/GUIversion/src/yellow.png", "C:/Users/Saskia/IdeaProjects/PPgoSV/GUIversion/src/green.png"}) {
					/*final*/
					Icon icon = new ImageIcon(file);
					JButton button = new JButton(icon);

					// Damit die Knöpfe mit einer ganz bestimmten, festen
					// Größe angezeigt werden, setzt man am besten alle
					// drei möglichen Größenangaben (preferred, minimum
					// und maximum) auf den gewünschten Wert.
					button.setPreferredSize(new Dimension(50, 50));
					button.setMinimumSize(new Dimension(50, 50));
					button.setMaximumSize(new Dimension(50, 50));

					button.addActionListener(
							(e) -> {
								// Selektierte Tabellenzeile abfragen.
								int row = table.getSelectedRow();
								if (row < 0 || row >= model.getRowCount()) return;

								// Piktogramm speichern.
								model.setValueAt(icon, row, 2);
							}
					);
					hbox.add(button);
				}
				vbox_4.add(hbox);
			}
			vbox_4.add(Box.createVerticalGlue());


			vbox_4.add(Box.createVerticalStrut(20));
			{
				// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
				JButton delete = new JButton("Schiffe entfernen");
				delete.setAlignmentX(Component.CENTER_ALIGNMENT);
				delete.addActionListener(
						(e) -> {

						}
				);
				vbox_4.add(delete);

				vbox_4.add(Box.createVerticalStrut(20));
				vbox_4.add(Box.createVerticalGlue());


			}

			vbox_4.add(Box.createVerticalStrut(20));
			{
				// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
				JButton zufall = new JButton("Schiffe zufaellig");
				zufall.setAlignmentX(Component.CENTER_ALIGNMENT);
				zufall.addActionListener(
						(e) -> {

						}
				);
				vbox_4.add(zufall);

				vbox_4.add(Box.createVerticalStrut(20));
				vbox_4.add(Box.createVerticalGlue());


			}
		frame.add(vbox_4);

			// Zwischenraum der Breite 50 oder mehr.
		frame.add(Box.createHorizontalStrut(50));
		frame.add(Box.createHorizontalGlue());

			// Menüzeile (JMenuBar) erzeugen und einzelne Menüs (JMenu)
			// mit Menüpunkten (JMenuItem) hinzufügen.
		//ist jetzt bei startbildschirm (siehe startbildschirm)
			JMenuBar bar = new JMenuBar();
			{
				JMenu menu = new JMenu("Programm");
				{
					JMenuItem item = new JMenuItem("Beenden");
					item.addActionListener(
							(e) -> {
								System.exit(0);
							}
					);
					menu.add(item);
				}
				bar.add(menu);
			}
			{
				JMenu menu = new JMenu("Tabelle");
				{
					JMenuItem item = new JMenuItem("Ausgeben");
					item.addActionListener(
							(e) -> {
								model.dump();
							}
					);
					menu.add(item);
				}
				bar.add(menu);
			}

			// Menüzeile zum Fenster hinzufügen.
			frame.setJMenuBar(bar);
		//startbildschirm.setJMenuBar(bar);


		//startbildschirm.setVisible(true);

			// Am Schluss (!) die optimale Fenstergröße ermitteln (pack)
			// und das Fenster anzeigen (setVisible).
			//frame.pack();
			//frame.setVisible(true);


		//Container mit allen Komponenten sichtbar machen
		startbildschirm.pack();
		startbildschirm.setExtendedState(JFrame.MAXIMIZED_BOTH);
		startbildschirm.setVisible(true);
		//Container pane_start = startbildschirm.getContentPane();

		//frame.setVisible(true);

		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {


				//JPanel spiel = frame;
				//startbildschirm.getContentPane();
				//pane = startbildschirm.getContentPane();
				//spiel.setVisible(true);
				//System.exit(0);

				startbildschirm.setVisible(false);

				//frame.setLocationRelativeTo(null);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				//frame.setSize(1000, 1000);
				frame.setVisible(true);

				//startbildschirm.setContentPane(frame);



			}
		});





		//pane = startbildschirm.getContentPane();
		//frame.setVisible(true);
		//pane.add(frame);
		//pane.add();

			//pane.setVisible(true);


		}

    // Hauptprogramm.
    public static void main (String [] args) {
	// Laut Swing-Dokumentation sollte die graphische Oberfläche
	// nicht direkt im Hauptprogramm (bzw. im Haupt-Thread) erzeugt
	// und angezeigt werden, sondern in einem von Swing verwalteten
	// separaten Thread.
	// Hierfür wird der entsprechende Code in eine parameterlose
	// anonyme Funktion () -> { ...... } "verpackt", die an
	// SwingUtilities.invokeLater übergeben wird.
	SwingUtilities.invokeLater(
	    () -> { start(); }
	);
    }
}
