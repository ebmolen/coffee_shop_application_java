import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        File f = new File("orderHistory.csv");
        BufferedReader reader = new BufferedReader((new FileReader(f)));
        try {
            String line = reader.readLine();
            String [] elms;
            int maxOrderNumber = 0;
            while(line!= null) {
                elms = line.split(",");
                ArrayList <Coffee> itemList = new ArrayList<>();
                boolean premium = Boolean.parseBoolean(elms[7]);
                Customer thisCustomer;
                if (premium){
                    thisCustomer = new PremiumCustomer(elms[2], elms[3], elms[4], elms[5]);
                } else {
                    thisCustomer = new RegularCustomer(elms[2], elms[3], elms[4], elms[5]);
                }
                int i = 8;
                while (i < elms.length) {
                   if (elms[i + 5].equals("Filtered Coffee")) {
                       itemList.add(new FilteredCoffee(elms[i].strip(), elms[i + 1], elms[i + 2], elms [i + 3], elms[i + 4], elms[i + 6],elms[i + 7], elms [i + 8]));
                       i += 9;
                    } else {
                       itemList.add(new Espresso(elms[i].strip(), elms[i + 1], elms[i + 2], elms [i + 3], elms[i + 4], elms[i + 5], Integer.parseInt(elms[i + 6].trim()), elms[i + 7]));
                       i += 8;
                   }
                   if (Integer.parseInt(elms[1].trim()) > maxOrderNumber) {
                       maxOrderNumber = Integer.parseInt(elms[1].trim());
                   }
              }
                Order.addOrderToHistory(new Order(elms[0].trim(), Integer.parseInt(elms[1].trim()),thisCustomer, premium, Double.parseDouble(elms[6].trim()), itemList));
                line = reader.readLine();
           }
            reader.close();
            Order.setNextOrderNumber(maxOrderNumber + 1);

            GUI gui = new GUI();
            gui.start();

    } catch (IOException e) {
        e.printStackTrace();
        }



    }

}