import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Rasmus on 06-10-2016.
 */
public class Tabeller {
    int antaleleverprgruppe = 1;
    int gruppenr = 1;
    int stringnr = -1;
    ArrayList<String> kollonnenavneliste = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> elever = new ArrayList<>();
    private JTable gruppeTabel;
    private JPanel panel1;
    private JButton bLavGrupper;
    private JComboBox comboBox1;
    DefaultTableModel tableModel;


    public Tabeller() {
        elever = scramble();
        System.out.println(elever);
        opdaterKollonne();
        bLavGrupper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data.clear();
                elever.clear();
                elever = scramble();
                stringnr = -1;
                opdaterKollonne();
            try {
                while (data.size() < elever.size()/antaleleverprgruppe) {
                    stringnr++;
                    data.add(elever.get(stringnr));
                    if (data.size() == elever.size()/antaleleverprgruppe) {
                        tableModel.addRow(data.toArray());
                        data.clear();
                    }
                }
            } catch (IndexOutOfBoundsException e2) {
                for (int i = 0; i < elever.size()%antaleleverprgruppe; i++) {
                    stringnr++;
                    try {

                        data.add(elever.get(stringnr));

                    } catch (IndexOutOfBoundsException e3) {
                        data.add(" ");
                    }

                }
                tableModel.addRow(data.toArray());
                }
                System.out.println("Array : "+data.toString());
                System.out.println("Tal : "+elever.size()/antaleleverprgruppe);
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(comboBox1.getSelectedItem());
                antaleleverprgruppe = Integer.parseInt(comboBox1.getSelectedItem().toString());
                opdaterKollonne();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Matrix");
        frame.setContentPane(new Tabeller().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public ArrayList<String> scramble() {
        ArrayList<String> liste = new ArrayList<>();
        ArrayList<String> liste2 = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File("src/Klasser/Elever.txt")));
            String x = in.readLine();
            while(x != null) {
                liste.add(x);
                //System.out.println(x);
                x = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (liste.size()>0) {
            int temp = 0;
            temp = (int) Math.floor(Math.random() * liste.size());
            liste2.add(liste.get(temp));
            liste.remove(temp);

        }
        return liste2;
    }
    public void opdaterKollonne() {
        kollonnenavneliste.clear();
        for (int i = 0; i < (int) Math.floor(elever.size()/antaleleverprgruppe);i++) {
            kollonnenavneliste.add(Integer.toString(i+1));
        }
        tableModel = new DefaultTableModel(kollonnenavneliste.toArray(),0);
        gruppeTabel.setModel(tableModel);
    }
}
