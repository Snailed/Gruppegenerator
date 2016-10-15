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
    private int antaleleverprgruppe = 1;
    private int stringnr = 0; //bruges til at bedømme hvilken elev som skal indsættes i data-listen.
    private ArrayList<String> kollonnenavneliste = new ArrayList<>(); //Eftersom at kollonnetitler kun tager stringarrays indsætter vi talene som strings her
    private ArrayList<String> data = new ArrayList<>(); //en arraylist som holder alle de navne som skal til at blive printet ud som en række i tabellen.
    private ArrayList<String> elever = new ArrayList<>(); //Arraylist som først holder alle elementerne læst fra en tekstfil i Klasser-mappen. Den bliver blandet af metoden "scramble()"
    private JTable gruppeTabel;
    private JPanel panel1;
    private JButton bLavGrupper;
    private JComboBox comboBox1;
    private DefaultTableModel tableModel; //Hvis man skal indsætte rækker i en tabel, skal man bruge metoden addrow(string[] values)

    //Først bliver main-metoden kørt, som starter konstruktøren.
    public Tabeller() {

        elever = scramble(); //Først læser og blander vi elementerne fra den valgte klasse.
        System.out.println("Blandede elever: "+elever);  //Den logger vi så vi kan se at den har læst korrekt.
        opdaterKollonne(); //Nu bliver kollonnenavnene dannet.

        //Hver gang at der bliver klikket "Lav grupper!" bliver denne funktion kaldt.
        bLavGrupper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opdaterKollonne(); //Dette gøres egentlig bare for at refreshe tabellen.
                data.clear(); //Først clearer vi lige lister med gammelt data på.
                elever.clear();
                elever = scramble(); // så læser vi nogle nye elementer og blander dem.
                stringnr = 0; //Derefter resetter vi hvilken string som vi er nået til.

            //Nu prøver vi så at indsætte så mange elever som vi kan ind i tabellen. Når stringnr bliver større end listen med elever skabes der en IndexOutOfBoundsException som så gribes inden at der printes de sidste elementer
            try {
                while (true) { //Her kører vi bare et loop indtil at der bliver smidt en fejl.
                    data.add(elever.get(stringnr++)); //Hver gang loopet kører bliver det næste element fra elever tilføjet til data. Læg mærke til hvordan stringnr bliver større efter at kommandoen er kørt.
                    if (data.size() == elever.size()/antaleleverprgruppe) { //Hvis data bliver ligeså stort som det antal grupper som vi vil have, bliver den printet ud til tabellen.
                        tableModel.addRow(data.toArray()); //Her bliver den printet ud i tabellen.
                        data.clear(); //Og så bliver den clearet.
                    }
                }
            } catch (IndexOutOfBoundsException e2) { //Når fejlen så bliver grebet, finder den så de resterende elementer, og hvis der er nogle, printer dem ud.
                for (int i = 0; i < elever.size()%antaleleverprgruppe; i++) { //for hvert resterende element
                    try {
                        data.add(elever.get(stringnr++));//tilføj et resterende element
                    } catch (IndexOutOfBoundsException e3) { //og hvis der ikke er flere
                        data.add(" "); //tilføj et tomt felt
                    }

                }
                if (data.size() != 0) { //Der skal kun tilføjes en sidste række hvis der er elever tilbage.
                    tableModel.addRow(data.toArray()); //Tilføj den sidste række.
                }
                }
            }
        });
        comboBox1.addActionListener(new ActionListener() { //Når der bliver ændret i antal elever...
            @Override
            public void actionPerformed(ActionEvent e) {
                antaleleverprgruppe = Integer.parseInt(comboBox1.getSelectedItem().toString()); //ændrer antal elever per gruppe til det som der står i boxen
                opdaterKollonne(); //Opdaterer kollonnetitlerne
            }
        });
    }

    public static void main(String[] args) {
        //Først laver vi et vindue og opretter et anonymt objekt af selve klassen. Derefter bliver konstruktøren kaldt.

        JFrame frame = new JFrame("Matrix");
        frame.setContentPane(new Tabeller().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //Her læser den alle elementerne i det pågældende dokument, indsætter det i en liste, hvorefter alle elementer bliver taget ud i tilfælde rækkefølge og puttet i en anden liste som returneres.
    public ArrayList<String> scramble() {
        ArrayList<String> liste = new ArrayList<>(); // Først oprettes listerne
        ArrayList<String> liste2 = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File("src/Klasser/Elever.txt"))); // Så opretter vi en reader
            String x = in.readLine();
            while(x != null) { // så længe at der bliver læst en linje skal den tilføjes til en liste. Dette ville måske være et godt tidspunkt for et do-while loop men jeg er for doven til at ændre det.
                liste.add(x);
                x = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (liste.size()>0) { //Så længe at der er elementer i den første liste skal den tage et tilfældigt tal og indsætte det i den liste som returneres.
            int temp = 0;
            temp = (int) Math.floor(Math.random() * liste.size());
            liste2.add(liste.get(temp));
            liste.remove(temp);

        }
        return liste2;
    }
    public void opdaterKollonne() {
        kollonnenavneliste.clear(); //Først clearer den listen med kollonnetitler i tilfælde at metoden er blevet kaldt før.
        for (int i = 0; i < (int) Math.floor(elever.size()/antaleleverprgruppe);i++) { //For hver gruppe bliver der nu tilføjet et tal til listen
            kollonnenavneliste.add(Integer.toString(i+1));
        }
        tableModel = new DefaultTableModel(kollonnenavneliste.toArray(),0); //Nu bliver der så lavet en ny tablemodel med de nye kollonnetitlerne på
        gruppeTabel.setModel(tableModel); //...som derefter bliver indsat i tabellen.
    }
}
