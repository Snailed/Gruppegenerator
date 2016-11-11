import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rasmus on 31-10-2016.
 */
public class RedigerElever {
    private JList list1;
    public JPanel panel1;
    private DefaultListModel model;
    private static File aktivklasse;
    private JScrollPane jScrollPane;
    private JTable table1;
    private JButton updateKnap;
    private JButton tilføjKnap;
    private JButton fjernKnap;
    private String[] testArray = {"Hej", "Med", "Dig", "Min", "Ven"};
    private DefaultTableModel tableModel;
    private static DefaultTableModel nyTableModel;
    public static boolean ændret = false;
    public RedigerElever(File aktivklasse) {
        this.aktivklasse = aktivklasse;
        tableModel=new DefaultTableModel();


        System.out.println("List: "+ Arrays.toString(læsKlasse().toArray()));

        updateKnap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printFromTableModel();
                System.out.println("Klik!");
                ændret = false;
            }
        });
        tilføjKnap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nyTableModel.addRow(new Object[]{"", ""});
                ændret = true;
            }

        });
        fjernKnap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nyTableModel.removeRow(table1.getSelectedRow());
                ændret = true;
            }
        });
        nyTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                ændret = true;

            }
        });
    }
    public ArrayList<String> læsKlasse() {
        ArrayList<String> liste = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(aktivklasse)); // Så opretter vi en reader
            String x = in.readLine();
            while(x != null) { // så længe at der bliver læst en linje skal den tilføjes til en liste. Dette ville måske være et godt tidspunkt for et do-while loop men jeg er for doven til at ændre det.
                liste.add(x);
                x = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    private DefaultTableModel fillTableModel() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Navn:");
        tableModel.addColumn("Kategori:");
        for (int i = 0; i < læsKlasse().size();i++) {
            //System.out.println("Ting: "+Arrays.toString(new Object[]{læsKlasse().get(i)," "}));
            tableModel.addRow(new Object[] {læsKlasse().get(i),""});

        }
        System.out.println("Antal rækker: "+tableModel.getRowCount());
        System.out.println("Antal kollonner: "+tableModel.getColumnCount());
        return tableModel;
    }
    public static void printFromTableModel() {
        System.out.println("Prøver på at printe... Antal rækker: " + nyTableModel.getRowCount());
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < nyTableModel.getRowCount(); i++) {
            //System.out.println("For...");

                output.add(nyTableModel.getValueAt(i, 0).toString());
                //System.out.println("Printede "+nyTableModel.getValueAt(i,0).toString());

        }
        System.out.println("Outputliste: "+output.toString());
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(aktivklasse));
            for (int i = 0; i < output.size(); i++) {
                bufferedWriter.write(output.get(i)+"\n");
                //System.out.println("Printede: "+output.get(i));
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        table1 = new JTable();
        nyTableModel = fillTableModel();
        table1.setModel(nyTableModel);
        jScrollPane = new JScrollPane(table1);
    }
}
