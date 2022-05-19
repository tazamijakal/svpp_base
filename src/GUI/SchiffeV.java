package GUI;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;


// Geeignete Implementierung der Schnittstelle TableModel.

class DefaultTableModel extends AbstractTableModel {

    // Anzahl der Tabellenzeilen.
	private int rows = 0;
	private int spielfeldgr = 0;
	private int index_zeile;
	private int index_spalte;

	private int index_zeile_eig;
	private int index_spalte_eig;

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
		//return 5;

	}

	//Gegner:
	public void setIndex_zeile (int groesse) {

		this.index_zeile = groesse;

	}

	public int getIndex_zeile () {

		return this.index_zeile;
		//return 5;

	}

	public void setIndex_spalte (int groesse) {

		this.index_spalte = groesse;

	}

	public int getIndex_spalte () {

		return this.index_spalte;
		//return 5;

	}

	//Eigene:
	public void setIndex_zeile_eig (int groesse) {

		this.index_zeile_eig = groesse;

	}

	public int getIndex_zeile_eig () {

		return this.index_zeile_eig;
		//return 5;

	}

	public void setIndex_spalte_eig (int groesse) {

		this.index_spalte_eig = groesse;

	}

	public int getIndex_spalte_eig () {

		return this.index_spalte_eig;
		//return 5;

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


		// Zwischenraum der Breite 50 oder mehr.
		startbildschirm.add(Box.createVerticalStrut(50));
		startbildschirm.add(Box.createVerticalGlue());

		Box vbox_1_start = Box.createVerticalBox();
		{
			JLabel start = new JLabel("Spiele Schiffe versenken!");
			vbox_1_start.add(start);
		}
		startbildschirm.add(vbox_1_start);

		startbildschirm.add(Box.createVerticalStrut(50));
		startbildschirm.add(Box.createVerticalGlue());

		Box start_hbox_1 = Box.createHorizontalBox();
		{
			//Auswahl zwischen lokalem und online Spiel:

			Box start_vbox_1 = Box.createVerticalBox();
			{
				//Auswahl lokales Spiel:

				start_vbox_1.add(Box.createVerticalStrut(100));
				start_vbox_1.add(Box.createVerticalGlue());

				//JLabel lokal = new JLabel("Lokales Spiel");

				JCheckBox cb_lokal = new JCheckBox("Lokales Spiel");
				cb_lokal.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {


					}
				});

				String[] list_Lokal = {"Spieler vs Spieler", "Spieler vs KI"};
				JComboBox<String> auswahl_lokal = new JComboBox<String>(list_Lokal);

				start_vbox_1.add(cb_lokal);
				start_vbox_1.add(auswahl_lokal);

				start_vbox_1.add(Box.createVerticalStrut(100));
				start_vbox_1.add(Box.createVerticalGlue());

			}

		//startbildschirm.add(start_vbox_1);
			start_hbox_1.add(start_vbox_1);


		//startbildschirm.add(Box.createHorizontalStrut(20));
		//startbildschirm.add(Box.createHorizontalGlue());

			start_hbox_1.add(Box.createHorizontalStrut(20));
			start_hbox_1.add(Box.createHorizontalGlue());



			//start_hbox_1.add(start_vbox_1);
		//startbildschirm.add(start_vbox_1);

			Box start_vbox_2 = Box.createVerticalBox();
			{
				//Auswahl online Spiel:

				start_vbox_2.add(Box.createVerticalStrut(100));
				start_vbox_2.add(Box.createVerticalGlue());

				//JLabel online = new JLabel("Online Spiel");

				JCheckBox cb_online = new JCheckBox("Online Spiel");
				cb_online.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {



					}
				});


				String[] list_online = {"Spieler vs Spieler", "Spieler vs KI", "KI vs KI"};
				JComboBox<String> auswahl_online = new JComboBox<String>(list_online);

				start_vbox_2.add(cb_online);
				start_vbox_2.add(auswahl_online);


				start_vbox_2.add(Box.createVerticalStrut(100));
				start_vbox_2.add(Box.createVerticalGlue());

			}

			start_hbox_1.add(start_vbox_2);


		}

		startbildschirm.add(start_hbox_1);


		startbildschirm.add(Box.createVerticalStrut(20));
		startbildschirm.add(Box.createVerticalGlue());

		//Einstellung Spielfeldgroesse:

		JSlider slide = new JSlider(5, 30, 10);//Standard: rang: 0-100, initial value: 50
		//->siehe Info Konstruktor, kann man auch selber noch anpassen

		JLabel label_slider = new JLabel();
		JLabel label = new JLabel("Spielfeldgröße");

		startbildschirm.add(label_slider);
		startbildschirm.add(label);
		startbildschirm.add(slide);

		slide.setPaintLabels(true); //Anzeige der Zahlen
		slide.setPaintTrack(true); //Balken werden angezeigt
		slide.setSnapToTicks(true); //Zu ganzzahligen Werten "springen"


		startbildschirm.add(Box.createVerticalStrut(20));
		startbildschirm.add(Box.createVerticalGlue());


		JButton play = new JButton("Play");
		startbildschirm.setLayout(new FlowLayout());
		play.setPreferredSize(new Dimension(100, 50));

		startbildschirm.add(play);


		//Ende: Komponenten Startbildschirm



		JFrame frame = new JFrame("Schiffeversenken");


			// Beim Schließen des Fensters (z. B. durch Drücken des
			// X-Knopfs in Windows) soll das Programm beendet werden.
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		frame.setContentPane(Box.createHorizontalBox());


		frame.add(Box.createHorizontalStrut(20));
		frame.add(Box.createHorizontalGlue());


		//MyTableModel model = new MyTableModel();
		DefaultTableModel model = new DefaultTableModel();

		DefaultTableModel model2 = new DefaultTableModel();


		slide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = slide.getValue();
				//gibt Wert zurück, den Slider gerade hat (zwischen 0 und 100)
				//pb.setValue(value);//Fortschrittsbalken zählt nach oben

				//Wenn sich slider auf einen anderen Wert bewegt soll dieser
				//Wert im Label angezeigt werden
				label_slider.setText(Integer.toString(slide.getValue()));

				model.setSpielfeld(value);
				model2.setSpielfeld(value);

			}
		});

		JTable table = new JTable(slide.getValue(), slide.getValue());
		JTable table2 = new JTable(slide.getValue(), slide.getValue());


		JScrollPane scrollPane = new JScrollPane(table);
		JScrollPane scrollPane2 = new JScrollPane(table2);


		Box vbox_1 = Box.createVerticalBox();
		{
			JLabel label_gegner = new JLabel("Gegner Schiffe");
			vbox_1.add(label_gegner);

			//MyTableModel model = new MyTableModel();
			//JTable table = new JTable(model);
			//table1.setRowHeight(40);//setzt Höhe der einzelnen Zeilen

			table.setRowHeight(40);
			//table.setPreferredSize(new Dimension(1000, 1000));
			//table1.setSize(new Dimension(200, 200));

			vbox_1.add(scrollPane);

		}

		frame.add(vbox_1);


		frame.add(Box.createHorizontalStrut(70));
		frame.add(Box.createHorizontalGlue());



		Box vbox_2 = Box.createVerticalBox();
		{
			JLabel label_eigene = new JLabel("Eigene Schiffe");
			vbox_2.add(label_eigene);

			//MyTableModel model = new MyTableModel();
			//JTable table = new JTable(model);
			table2.setRowHeight(40);//setzt Höhe der einzelnen Zeilen

			//table2.setSize(new Dimension(200, 200));

			vbox_2.add(scrollPane2);

		}

		frame.add(vbox_2);



		frame.add(Box.createHorizontalStrut(50));
		frame.add(Box.createHorizontalGlue());



			// Vertikale Box mit diversen Knöpfen.

			//Box vbox_4 = Box.createVerticalBox();
			//{
				//vbox_4.add(Box.createHorizontalStrut(20));
				//vbox_4.add(Box.createHorizontalGlue());

				// "Neuer Eintrag" fügt eine neue Tabellenzeile am Ende hinzu
				// und "zieht" den Scrollbar ggf. ganz nach unten,
				// damit die Zeile auch sichtbar ist.
				//JButton button = new JButton("Neuer Eintrag");
				//button.setAlignmentX(Component.CENTER_ALIGNMENT);
				/*button.addActionListener(
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
			}*/
			/*vbox_4.add(Box.createVerticalStrut(20));
			{
				// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
				JButton button = new JButton("Eintrag entfernen");
				button.setAlignmentX(Component.CENTER_ALIGNMENT);
				/*button.addActionListener(
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
			}*/

			//vbox_4.add(Box.createVerticalStrut(20));
			//{
				// Horizontale Box mit drei Knöpfen (rot, gelb, grün).
				// Wenn einer der Knöpfe gedrückt wird, wird sein
				// Piktogramm in der dritten Spalte der selektierten
				// Tabellenzeile gespeichert.
				//Box hbox = Box.createHorizontalBox();
				//for (String file : new String[]
						//{"C:/Users/Saskia/IdeaProjects/PPgoSV/GUIversion/src/red.png", "C:/Users/Saskia/IdeaProjects/PPgoSV/GUIversion/src/yellow.png", "C:/Users/Saskia/IdeaProjects/PPgoSV/GUIversion/src/green.png"}) {
					/*final*/

					//Icon icon = new ImageIcon(file);
					//JButton button = new JButton(icon);

					// Damit die Knöpfe mit einer ganz bestimmten, festen
					// Größe angezeigt werden, setzt man am besten alle
					// drei möglichen Größenangaben (preferred, minimum
					// und maximum) auf den gewünschten Wert.
					//button.setPreferredSize(new Dimension(50, 50));
					//button.setMinimumSize(new Dimension(50, 50));
					//button.setMaximumSize(new Dimension(50, 50));

					/*
					button.addActionListener(
							(e) -> {
								// Selektierte Tabellenzeile abfragen.
								int row = table.getSelectedRow();
								if (row < 0 || row >= model.getRowCount()) return;

								// Piktogramm speichern.
								model.setValueAt(icon, row, 2);
							}
					);*/
					//hbox.add(button);
				//}
				//vbox_4.add(hbox);
			//}

		Box vbox_4 = Box.createVerticalBox();
		{

		}
			vbox_4.add(Box.createVerticalGlue());


			vbox_4.add(Box.createVerticalStrut(20));
			{
				// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
				JButton delete = new JButton("Schiffe entfernen");
				delete.setAlignmentX(Component.CENTER_ALIGNMENT);
				vbox_4.add(delete);

				vbox_4.add(Box.createVerticalStrut(10));
				vbox_4.add(Box.createVerticalGlue());


			}

			vbox_4.add(Box.createVerticalStrut(10));
			{
				// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
				JButton zufall = new JButton("Schiffe zufaellig");
				zufall.setAlignmentX(Component.CENTER_ALIGNMENT);
				vbox_4.add(zufall);

				vbox_4.add(Box.createVerticalStrut(10));
				vbox_4.add(Box.createVerticalGlue());


			}


		vbox_4.add(Box.createVerticalStrut(10));
		{
			// "Eintrag entfernen" entfernt die selektierte Tabellenzeile.
			JButton feld_gegner = new JButton("Feld Platz Gegner");
			feld_gegner.setAlignmentX(Component.CENTER_ALIGNMENT);
			vbox_4.add(feld_gegner);

			feld_gegner.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

						model.setIndex_spalte(table.getSelectedColumn());
						model.setIndex_zeile(table.getSelectedRow());

						System.out.println("Tabelle1 " + model.getIndex_zeile() + "," + model.getIndex_spalte());

				}


			});

			JButton feld_eigene = new JButton("Feld Platz Eigene");
			feld_eigene.setAlignmentX(Component.CENTER_ALIGNMENT);
			vbox_4.add(feld_eigene);

			feld_eigene.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					model2.setIndex_spalte_eig(table.getSelectedColumn());
					model2.setIndex_zeile_eig(table.getSelectedRow());



					System.out.println("Tabelle2 " + model2.getIndex_zeile_eig() + "," + model2.getIndex_spalte_eig());

				}
			});



			vbox_4.add(Box.createVerticalStrut(10));
			vbox_4.add(Box.createVerticalGlue());


		}

		frame.add(vbox_4);



		vbox_4.add(Box.createHorizontalStrut(10));
		vbox_4.add(Box.createHorizontalGlue());


		startbildschirm.pack();
		startbildschirm.setExtendedState(JFrame.MAXIMIZED_BOTH);
		startbildschirm.setVisible(true);

		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {


				startbildschirm.setVisible(false);

				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

				frame.setVisible(true);


			}
		});


		// Menüzeile (JMenuBar) erzeugen und einzelne Menüs (JMenu)
		// mit Menüpunkten (JMenuItem) hinzufügen.
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
							//model.dump();
						}
				);
				menu.add(item);
			}
			bar.add(menu);
		}

		// Menüzeile zum Fenster hinzufügen.
		frame.setJMenuBar(bar);

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
