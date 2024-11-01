import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    // https://dribbble.com/tags/coffee-cartoon
    // pulled coffee cup image from this website.
    ImageIcon cup = new ImageIcon("cup.jpeg");

    Color backgroundColor = new Color(224, 158, 43);
    Color fontColor = new Color(3, 89, 70);
    Color accentColor = new Color(56, 45, 13);

    JPanel homePage;

    JPanel orderScreen;

    JPanel prepareOrders;

    JPanel viewOrders;

    JPanel bottomPanel1;

    JPanel bottomPanel2;

    JPanel bottomPanel3;

    JPanel bottomPanel4;

    JPanel bottomPanel5;

    JPanel bottomPanel6;

    Customer currentCustomer;

    ArrayList<Coffee> currentOrder = new ArrayList<>();

    JFrame frame;

    int activeNum;

    int itemNum;

    String orderHistoryAll;
    String orderHistoryActive;
    String orderHistoryPassive;

    public GUI() {

    }

    public void start() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new JFrame();

                    frame.setSize(1000, 1000);
                    frame.setTitle("Coffee Shop");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    frame.add(createHomePage());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Creates Homepage JPanel
    public JPanel createHomePage() {
        homePage = new JPanel();
        homePage.setLayout(new BoxLayout(homePage, BoxLayout.Y_AXIS));
        homePage.setOpaque(true);
        homePage.setBackground(backgroundColor);
        homePage.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JPanel secondPanel = new JPanel();
        secondPanel.setOpaque(false);
        JPanel thirdPanel = new JPanel();
        thirdPanel.setOpaque(false);

        // https://www.geeksforgeeks.org/jlabel-java-swing/
        // https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/javax/swing/JLabel.html
        // https://stackoverflow.com/questions/2966334/how-do-i-set-the-colour-of-a-label-coloured-text-in-java
        JLabel welcome = new JLabel("Elisabeth's Coffee Shop", SwingConstants.CENTER);
        welcome.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
        welcome.setForeground(fontColor);
        topPanel.add(welcome);
        homePage.add(topPanel);

        //https://stackoverflow.com/questions/3775373/java-how-to-add-image-to-jlabel
        JLabel coffee = new JLabel(cup, SwingConstants.CENTER);
        secondPanel.add(coffee);
        coffee.setBorder(BorderFactory.createEtchedBorder());
        homePage.add(secondPanel);

        thirdPanel.setLayout(new GridLayout(2, 2, 5, 5));

        // https://stackoverflow.com/questions/4585867/transparent-jbutton
        JButton order = new JButton("Place Order");
        order.setOpaque(true);
        order.setBorderPainted(true);
        order.setContentAreaFilled(true);
        order.setBorder(BorderFactory.createRaisedBevelBorder());

        JButton view = new JButton("View Orders");
        view.setOpaque(true);
        view.setBorderPainted(true);
        view.setContentAreaFilled(true);
        view.setBorder(BorderFactory.createRaisedBevelBorder());

        JButton prepare = new JButton("Prepare Orders");
        prepare.setOpaque(true);
        prepare.setBorderPainted(true);
        prepare.setContentAreaFilled(true);
        prepare.setBorder(BorderFactory.createRaisedBevelBorder());

        JButton exit = new JButton("Exit");
        exit.setOpaque(true);
        exit.setBorderPainted(true);
        exit.setContentAreaFilled(true);
        exit.setBorder(BorderFactory.createRaisedBevelBorder());

        thirdPanel.add(order);
        thirdPanel.add(view);
        thirdPanel.add(prepare);
        thirdPanel.add(exit);

        for (Component c : thirdPanel.getComponents()) {
            c.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 38));
            c.setBackground(fontColor);
            c.setForeground(Color.white);
        }

        order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCustomer = null;
                currentOrder.clear();
                frame.remove(homePage);
                frame.add(createOrderScreen());
                frame.revalidate();
                frame.repaint();
            }
        });

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(homePage);
                frame.add(createViewOrders());
                frame.revalidate();
                frame.repaint();
            }
        });

        prepare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(homePage);
                frame.add(createPrepareOrders());
                frame.revalidate();
                frame.repaint();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeToCSV();
                System.exit(0);
            }
        });

        homePage.add(thirdPanel);

        return homePage;
    }

    // creates prepareOrders JPanel
    public JPanel createPrepareOrders() {
        ArrayList<Order> activeOrders = new ArrayList<>();
        for (Order order : Order.getOrderHistory()) {
            if (order.getOrderStatus().equals("Active")) {
                activeOrders.add(order);
            }
        }

        activeNum = 0;
        itemNum = 0;

        prepareOrders = new JPanel();
        prepareOrders.setOpaque(true);
        prepareOrders.setLayout(new BoxLayout(prepareOrders, BoxLayout.Y_AXIS));
        prepareOrders.setBackground(backgroundColor);
        prepareOrders.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel actives = new JLabel("Active Orders");
        actives.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
        actives.setForeground(fontColor);
        topPanel.add(actives);

        JPanel secondPanel = new JPanel();
        secondPanel.setOpaque(false);
        secondPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setAlignmentX(0);

        JLabel prepLabel = new JLabel("Current Order");
        prepLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        prepLabel.setForeground(fontColor);
        prepLabel.setAlignmentX(10);
        left.add(prepLabel);

        JTextArea prepOrder = new JTextArea();
        prepOrder.setOpaque(true);
        prepOrder.setBackground(backgroundColor);
        prepOrder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        prepOrder.setLineWrap(true);
        prepOrder.setForeground(fontColor);
        if (!activeOrders.isEmpty()) {
            prepOrder.setText(activeOrders.get(activeNum).display());
        } else {
            prepOrder.setText("No active orders remaining.");
        }
        prepOrder.setEditable(false);
        ScrollPane scroll3 = new ScrollPane();
        scroll3.setSize(400, 600);
        scroll3.add(prepOrder);
        left.add(scroll3);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel itemLabel = new JLabel("Current Item");
        itemLabel.setAlignmentX(10);
        itemLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        itemLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        itemLabel.setForeground(fontColor);
        right.add(itemLabel);

        JTextArea progress = new JTextArea();
        progress.setBackground(backgroundColor);
        progress.setForeground(fontColor);
        progress.setOpaque(true);
        progress.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        if (!activeOrders.isEmpty()) {
            progress.setText(activeOrders.get(activeNum).getItems().get(itemNum).prepare());
        }
        progress.setLineWrap(true);
        progress.setEditable(false);

        right.add(progress);

        JButton nextItem = new JButton("Next Item");
        nextItem.setSize(200, 100);
        nextItem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        nextItem.setBackground(accentColor);
        nextItem.setForeground(fontColor);

        right.add(nextItem);

        JButton nextOrder = new JButton("Next Order");
        nextOrder.setSize(200, 100);
        nextOrder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        nextOrder.setBackground(accentColor);
        nextOrder.setForeground(fontColor);


        nextItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (itemNum < activeOrders.get(activeNum).getItems().size() - 1) {
                    progress.setText(activeOrders.get(activeNum).getItems().get(itemNum + 1).prepare());
                    itemNum += 1;
                    right.revalidate();
                    right.repaint();
                } else if (activeNum < activeOrders.size() - 1) {
                    progress.setText("Order Complete.\nSelect Next Order to mark order\ncomplete and proceed.");
                    right.remove(nextItem);
                    right.add(nextOrder);
                    right.revalidate();
                    right.repaint();
                }
                else  {
                    progress.setText("All orders prepared.\nSelect Return to Homepage to\nmark order prepared\nand return to homepage");
                    right.remove(nextItem);
                    right.revalidate();
                    right.repaint();
                }

            }
        });

        nextOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeNum < activeOrders.size() - 1) {
                    activeOrders.get(activeNum).setOrderStatus("Passive");
                    itemNum = 0;
                    activeNum += 1;
                    prepOrder.setText(activeOrders.get(activeNum).display());
                    progress.setText(activeOrders.get(activeNum).getItems().get(itemNum).prepare());
                    right.remove(nextOrder);
                    right.add(nextItem);
                    left.revalidate();
                    left.repaint();
               }
                writeToCSV();
            }
        });
        secondPanel.add(left);
        secondPanel.add(right);

        prepareOrders.add(topPanel);
        prepareOrders.add(secondPanel);
        JPanel thirdPanel = new JPanel();
        thirdPanel.setOpaque(false);

        thirdPanel.setLayout(new FlowLayout());

        JButton home = new JButton("Return to Homepage");

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeNum == activeOrders.size() - 1){
                    activeOrders.get(activeNum).setOrderStatus("Passive");
                    writeToCSV();
                }

                frame.remove(prepareOrders);
                frame.add(homePage);
                frame.revalidate();
                frame.repaint();

            }
        });

        home.setSize(200, 100);
        home.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        home.setBackground(fontColor);
        home.setForeground(Color.WHITE);
        home.setOpaque(true);
        home.setBorder(BorderFactory.createRaisedBevelBorder());
        home.setBorderPainted(true);
        home.setContentAreaFilled(true);

        thirdPanel.add(home);

        prepareOrders.add(thirdPanel);

        return prepareOrders;
    }

    // creates orderScreen JPanel
    public JPanel createOrderScreen() {
        orderScreen = new JPanel();
        orderScreen.setLayout(new BoxLayout(orderScreen, BoxLayout.Y_AXIS));
        orderScreen.setOpaque(true);
        orderScreen.setBackground(backgroundColor);
        orderScreen.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel create = new JLabel("Create Order");
        create.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
        create.setForeground(fontColor);
        topPanel.add(create);
        orderScreen.add(topPanel);

        orderScreen.add(createBottomPanel1());

        return orderScreen;

    }

    // orderScreen bottomPanel for customer information
    // https://forums.oracle.com/ords/apexds/post/adding-removing-jpanel-in-a-java-swing-application-3028
    // https://stackoverflow.com/questions/7979811/how-to-validate-if-textfield-entered-is-a-mobile-number-in-javaswing
    // https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
    // https://stackoverflow.com/questions/4252257/jformattedtextfield-with-maskformatter
    public JPanel createBottomPanel1() {
        bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new BoxLayout(bottomPanel1, BoxLayout.Y_AXIS));
        bottomPanel1.setOpaque(false);

        JPanel contactInfo = new JPanel();
        contactInfo.setOpaque(false);
        contactInfo.setLayout(new GridLayout(4, 2));
        contactInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel first = new JLabel("First Name: ");
        JTextField customerFirstName = new JTextField(20);
        customerFirstName.setEditable(true);
        JLabel last = new JLabel("Last Name: ");
        JTextField customerLastName = new JTextField(20);
        customerLastName.setEditable(true);
        JLabel phone = new JLabel("Phone: ");

        JFormattedTextField customerPhone = new JFormattedTextField();
        customerPhone.setColumns(10);
        try {
            MaskFormatter phoneFormat = new MaskFormatter("(###)-###-####");
            phoneFormat.install(customerPhone);
        } catch (ParseException f) {
            f.printStackTrace();
        }
        customerPhone.setEditable(true);
        JLabel email = new JLabel("Email: ");
        JTextField customerEmail = new JTextField(25);
        customerEmail.setEditable(true);

        contactInfo.add(first);
        contactInfo.add(customerFirstName);
        contactInfo.add(last);
        contactInfo.add(customerLastName);
        contactInfo.add(phone);
        contactInfo.add(customerPhone);
        contactInfo.add(email);
        contactInfo.add(customerEmail);

        for (Component c : contactInfo.getComponents()) {
            c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            c.setForeground(fontColor);
        }

        bottomPanel1.add(contactInfo);

        // https://stackoverflow.com/questions/8505812/jcheckbox-change-the-text-position

        JCheckBox premium = new JCheckBox("Premium Customer");
        premium.setOpaque(false);
        premium.setHorizontalTextPosition(SwingConstants.LEFT);
        premium.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        premium.setForeground(fontColor);
        JPanel premiumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        premiumPanel.setOpaque(false);

        premiumPanel.add(premium);

        bottomPanel1.add(premiumPanel);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout());

        JButton home = new JButton("Return to Homepage");

        JButton next = new JButton("Next");

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(orderScreen);
                frame.add(homePage);
                frame.revalidate();
                frame.repaint();
            }
        });


        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customerFirstName.getText().isEmpty() || customerLastName.getText().isEmpty() ||
                        customerEmail.getText().isEmpty() || customerPhone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All customer fields must be completed.");
                } else if ((customerPhone.getText().length() != 14) || (customerPhone.getText().contains(" "))){
                    JOptionPane.showMessageDialog(null, "Please enter valid U.S. phone number.");
                } else if (!customerEmail.getText().contains(".") || !customerEmail.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Please enter valid e-mail address.");
                }
                else {
                    orderScreen.remove(bottomPanel1);
                    orderScreen.add(createBottomPanel2());
                    orderScreen.revalidate();
                    orderScreen.repaint();

                    if (premium.isSelected()) {
                        currentCustomer = new PremiumCustomer(customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhone.getText());
                    } else {
                        currentCustomer = new RegularCustomer(customerFirstName.getText(), customerLastName.getText(),
                                customerEmail.getText(), customerPhone.getText());
                    }
                }
            }
        });


        buttons.add(home);
        buttons.add(next);

        for (Component c : buttons.getComponents()) {
            c.setSize(250, 150);
            c.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            c.setBackground(fontColor);
            c.setForeground(Color.white);
        }

        home.setBorder(BorderFactory.createRaisedBevelBorder());
        home.setContentAreaFilled(true);
        home.setBorderPainted(true);
        home.setOpaque(true);
        next.setBorder(BorderFactory.createRaisedBevelBorder());
        next.setContentAreaFilled(true);
        next.setBorderPainted(true);
        next.setOpaque(true);

        bottomPanel1.add(buttons);

        return bottomPanel1;
    }

    // orderScreen bottomPanel for selecting Coffee type
    JPanel createBottomPanel2() {
        bottomPanel2 = new JPanel();
        bottomPanel2.setLayout(new BoxLayout(bottomPanel2, BoxLayout.Y_AXIS));
        bottomPanel2.setOpaque(false);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        top.setOpaque(false);

        JPanel selectType = new JPanel();
        selectType.setOpaque(false);
        selectType.setLayout(new BoxLayout(selectType, BoxLayout.Y_AXIS));

        JButton drip = new JButton("Filtered Coffee");
        JButton espresso = new JButton("Espresso");

        selectType.add(drip);
        selectType.add(espresso);

        for (Component c : selectType.getComponents()) {
            c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 45));
            c.setForeground(fontColor);
            c.setBackground(accentColor);
        }

        drip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel2);
                orderScreen.add(createBottomPanel3());
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        espresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel2);
                orderScreen.add(createBottomPanel4());
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        top.add(selectType);
        JLabel picture = new JLabel(cup);
        top.add(picture);

        bottomPanel2.add(top);

        JPanel last = new JPanel();
        last.setOpaque(false);
        last.setLayout(new FlowLayout());
        JButton back = new JButton("Back");
        back.setSize(200, 100);
        back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        back.setBackground(fontColor);
        back.setForeground(Color.WHITE);
        back.setOpaque(true);
        back.setContentAreaFilled(true);
        back.setBorderPainted(true);
        back.setBorder(BorderFactory.createRaisedBevelBorder());
        last.add(back);

        JButton checkout = new JButton("Checkout");
        checkout.setSize(200,100);
        checkout.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        checkout.setBackground(fontColor);
        checkout.setForeground(Color.WHITE);
        checkout.setOpaque(true);
        checkout.setContentAreaFilled(true);
        checkout.setBorderPainted(true);
        checkout.setBorder(BorderFactory.createRaisedBevelBorder());

        checkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel2);
                orderScreen.add(bottomPanel5);
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });
        if (!currentOrder.isEmpty()){
            last.add(checkout);
        }

        bottomPanel2.add(last);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel2);
                orderScreen.add(bottomPanel1);
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        return bottomPanel2;
    }

    // Filtered Coffee Order Screen
    JPanel createBottomPanel3() {
        bottomPanel3 = new JPanel();
        bottomPanel3.setLayout(new BoxLayout(bottomPanel3, BoxLayout.Y_AXIS));
        bottomPanel3.setOpaque(false);
        bottomPanel3.setBackground(backgroundColor);

        JPanel selection = new JPanel();
        selection.setOpaque(false);
        selection.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        selection.setLayout(new GridLayout(8, 2, 10, 0));

        JTextArea size = new JTextArea();
        String sizeText = "Size:\nSmall - $2.75, Medium - $3.00, Large - $3.25";
        size.setText(sizeText);
        size.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        size.setOpaque(false);
        JLabel brew = new JLabel("Brew: ");
        brew.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        JLabel temp = new JLabel("Temperature: ");
        temp.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        JLabel creamerType = new JLabel("Creamer Type: ");
        creamerType.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        JLabel creamerVolume = new JLabel("Creamer Amount: ");
        creamerVolume.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        JLabel sweetenerType = new JLabel("Sweetener Type: ");
        sweetenerType.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        JLabel sweetenerVolume = new JLabel("Sweetener Amount: ");
        sweetenerVolume.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        JTextArea extras = new JTextArea();
        extras.setText("Extras:\n$0.50 each");
        extras.setOpaque(false);
        extras.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));

        String[] sizeSelectionItems = {"", "Small", "Medium", "Large"};
        JComboBox<String> sizeSelection = new JComboBox<>(sizeSelectionItems);
        sizeSelection.setEditable(false);

        String[] brewSelectionItems = {"", "Blonde Roast", "Medium Roast", "Dark Roast"};
        JComboBox<String> brewSelection = new JComboBox<>(brewSelectionItems);
        brewSelection.setEditable(false);

        String[] tempSelectionItems = {"", "Iced", "Hot"};
        JComboBox<String> tempSelection = new JComboBox<>(tempSelectionItems);
        tempSelection.setEditable(false);

        String[] creamerTypeSelectionItems = {"", "None", "2% Milk", "Whole Milk", "Dairy Creamer", "Almond Milk", "Soy Milk", "Oat Milk"};
        JComboBox<String> creamerTypeSelection = new JComboBox<>(creamerTypeSelectionItems);
        creamerTypeSelection.setEditable(false);

        String[] creamerVolumeSelectionItems = {"", "NA", "Light", "Medium", "Heavy"};
        JComboBox<String> creamerVolumeSelection = new JComboBox<>(creamerVolumeSelectionItems);
        creamerVolumeSelection.setEditable(false);
        creamerTypeSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (creamerTypeSelection.getSelectedItem().toString().equals("None")){
                    creamerVolumeSelection.setSelectedIndex(1);
                }
            }
        });

        String[] sweetenerTypeSelectionItems = {"", "None", "Sugar", "Brown Sugar", "Splenda", "Stevia", "Agave"};
        JComboBox<String> sweetenerTypeSelection = new JComboBox<>(sweetenerTypeSelectionItems);
        sweetenerTypeSelection.setEditable(false);

        String[] sweetenerVolumeSelectionItems = {"", "NA", "Light", "Medium", "Heavy"};
        JComboBox<String> sweetenerVolumeSelection = new JComboBox<>(sweetenerVolumeSelectionItems);
        sweetenerVolumeSelection.setEditable(false);
        sweetenerTypeSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sweetenerTypeSelection.getSelectedItem().toString().equals("None")){
                    sweetenerVolumeSelection.setSelectedIndex(1);
                }
            }
        });

        String[] extrasSelectionItems = {"None","*Whipped Cream", "*Chocolate Syrup", "*Caramel Syrup", "*Cocoa Powder"};
        JList<String> extraSelection = new JList<>(extrasSelectionItems);

        selection.add(size);
        selection.add(sizeSelection);
        selection.add(brew);
        selection.add(brewSelection);
        selection.add(temp);
        selection.add(tempSelection);
        selection.add(creamerType);
        selection.add(creamerTypeSelection);
        selection.add(creamerVolume);
        selection.add(creamerVolumeSelection);
        selection.add(sweetenerType);
        selection.add(sweetenerTypeSelection);
        selection.add(sweetenerVolume);
        selection.add(sweetenerVolumeSelection);
        selection.add(extras);
        selection.add(extraSelection);

        bottomPanel3.add(selection);

        JPanel last = new JPanel(new FlowLayout());
        last.setOpaque(false);
        JButton back = new JButton("Back");
        JButton addToOrder = new JButton("Add Item to Order");

        back.setSize(250, 100);
        back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        back.setBackground(fontColor);
        back.setForeground(Color.WHITE);
        back.setOpaque(true);
        back.setBorder(BorderFactory.createRaisedBevelBorder());
        back.setBorderPainted(true);
        back.setContentAreaFilled(true);
        last.add(back);

        addToOrder.setSize(250, 100);
        addToOrder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        addToOrder.setBackground(fontColor);
        addToOrder.setForeground(Color.WHITE);
        addToOrder.setOpaque(true);
        addToOrder.setBorder(BorderFactory.createRaisedBevelBorder());
        addToOrder.setBorderPainted(true);
        addToOrder.setContentAreaFilled(true);
        last.add(addToOrder);

        bottomPanel3.add(last);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel3);
                orderScreen.add(bottomPanel2);
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        // https://stackoverflow.com/questions/4962416/preferred-way-of-getting-the-selected-item-of-a-jcombobox
        addToOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((sizeSelection.getSelectedIndex() == 0) || (tempSelection.getSelectedIndex() == 0)
                    || (sweetenerTypeSelection.getSelectedIndex() == 0)  || (sweetenerTypeSelection.getSelectedIndex() == 0)
                    || (sweetenerVolumeSelection.getSelectedIndex() == 0) ||
                    (brewSelection.getSelectedIndex() == 0) ||
                    (creamerTypeSelection.getSelectedIndex() == 0) ||
                        (creamerVolumeSelection.getSelectedIndex() == 0)) {
                       JOptionPane.showMessageDialog(null, "Please make all selections.");
                } else if (!creamerTypeSelection.getSelectedItem().toString().equals("None") && creamerVolumeSelection.getSelectedItem().toString().equals("NA")) {
                    JOptionPane.showMessageDialog(null, "Please select a creamer amount.");
                }
                else if (!sweetenerTypeSelection.getSelectedItem().toString().equals("None") && sweetenerVolumeSelection.getSelectedItem().toString().equals("NA")) {
                    JOptionPane.showMessageDialog(null, "Please select a sweetener amount.");
                }
                else if ((creamerTypeSelection.getSelectedIndex() == 1) && !(creamerVolumeSelection.getSelectedIndex() == 1)){
                    JOptionPane.showMessageDialog(null, "Please select a creamer type or change creamer amount selection to 'NA.'");
                }
                else if ((sweetenerTypeSelection.getSelectedIndex() == 1) && !(sweetenerVolumeSelection.getSelectedIndex() == 1)){
                    JOptionPane.showMessageDialog(null, "Please select a sweetener type or change sweetener amount selection to 'NA.'");

                }
                else {
                    List<String> extraStuff = extraSelection.getSelectedValuesList();
                    String addOns = "";
                    for (String s : extraStuff) {
                        addOns += s;
                    }
                    if (addOns.equals("")) {
                        addOns = "None";
                    }
                    Coffee order = new FilteredCoffee(sizeSelection.getSelectedItem().toString(), tempSelection.getSelectedItem().toString(),
                            sweetenerTypeSelection.getSelectedItem().toString(), sweetenerVolumeSelection.getSelectedItem().toString(),
                            addOns, brewSelection.getSelectedItem().toString(),
                            creamerTypeSelection.getSelectedItem().toString(), creamerVolumeSelection.getSelectedItem().toString());
                    currentOrder.add(order);
                    orderScreen.remove(bottomPanel3);
                    orderScreen.add(createBottomPanel5());
                    orderScreen.revalidate();
                    orderScreen.repaint();
                }
            }
        });
        return bottomPanel3;
    }

    //Espresso Order Screen
    public JPanel createBottomPanel4() {
        bottomPanel4 = new JPanel();
        bottomPanel4.setOpaque(false);
        bottomPanel4.setLayout(new BoxLayout(bottomPanel4, BoxLayout.Y_AXIS));
        JPanel selection2 = new JPanel(new GridLayout(8, 2));
        selection2.setOpaque(false);
        selection2.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JTextArea size = new JTextArea("Size:\nSmall (standard 1 shot) - $3.00\nMedium (standard 2 shots) - $3.25\nLarge (standard 3 shots) - $3.50");
        size.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        size.setOpaque(false);
        JLabel type = new JLabel("Type: ");
        type.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JLabel temp = new JLabel("Temperature: ");
        temp.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JTextArea numShots = new JTextArea("Number of shots:\n$0.50 for each shot above standard amount");
        numShots.setOpaque(false);
        numShots.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JLabel milkType = new JLabel("Milk Type: ");
        milkType.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JLabel sweetenerType = new JLabel("Sweetener Type: ");
        sweetenerType.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JLabel sweetenerVolume = new JLabel("Sweetener Amount: ");
        sweetenerVolume.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        JTextArea extras = new JTextArea("Extras:\n$0.50 each");
        extras.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        extras.setOpaque(false);

        String[] sizeSelectionItems = {"", "Small", "Medium", "Large"};
        JComboBox<String> sizeSelection = new JComboBox<>(sizeSelectionItems);
        sizeSelection.setEditable(false);

        String[] typeSelectionItems = {"", "Americano", "Cappuccino", "Espresso","Latte"};
        JComboBox<String> typeSelection = new JComboBox<>(typeSelectionItems);
        typeSelection.setEditable(false);

        String[] tempSelectionItems = {"", "Iced", "Hot"};
        JComboBox<String> tempSelection = new JComboBox<>(tempSelectionItems);
        tempSelection.setEditable(false);

        String[] numShotsSelectionItems = {"", "1", "2", "3", "4"};
        JComboBox<String> numShotsSelection = new JComboBox<>(numShotsSelectionItems);
        numShotsSelection.setEditable(false);
        sizeSelection.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               if (sizeSelection.getSelectedIndex() == 1) {
                   numShotsSelection.setSelectedIndex(1);
               } else if (sizeSelection.getSelectedIndex() == 2) {
                   numShotsSelection.setSelectedIndex(2);
               } else if (sizeSelection.getSelectedIndex() == 3) {
                   numShotsSelection.setSelectedIndex(3);
               }
           }
       });

        String[] milkTypeSelectionItems = {"", "None", "2% Milk", "Whole Milk", "Almond Milk", "Soy Milk", "Oat Milk"};
        JComboBox<String> milkTypeSelection = new JComboBox<>(milkTypeSelectionItems);
        milkTypeSelection.setEditable(false);

        String[] sweetenerTypeSelectionItems = {"", "None", "Sugar", "Brown Sugar", "Splenda", "Stevia", "Agave"};
        JComboBox<String> sweetenerTypeSelection = new JComboBox<>(sweetenerTypeSelectionItems);
        sweetenerTypeSelection.setEditable(false);

        String[] sweetenerVolumeSelectionItems = {"", "NA", "Light", "Medium", "Heavy"};
        JComboBox<String> sweetenerVolumeSelection = new JComboBox<>(sweetenerVolumeSelectionItems);
        sweetenerVolumeSelection.setEditable(false);
        sweetenerTypeSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sweetenerTypeSelection.getSelectedIndex() == 1){
                    sweetenerVolumeSelection.setSelectedIndex(1);
                }
            }
        });

        String[] extrasSelectionItems = {"None","*Whipped Cream", "*Chocolate Syrup", "*Caramel Syrup", "*Cocoa Powder"};
        JList<String> extraSelection = new JList<>(extrasSelectionItems);

        selection2.add(size);
        selection2.add(sizeSelection);
        selection2.add(type);
        selection2.add(typeSelection);
        selection2.add(temp);
        selection2.add(tempSelection);
        selection2.add(numShots);
        selection2.add(numShotsSelection);
        selection2.add(milkType);
        selection2.add(milkTypeSelection);
        selection2.add(sweetenerType);
        selection2.add(sweetenerTypeSelection);
        selection2.add(sweetenerVolume);
        selection2.add(sweetenerVolumeSelection);
        selection2.add(extras);
        selection2.add(extraSelection);

        bottomPanel4.add(selection2);

        JPanel last = new JPanel(new FlowLayout());
        last.setOpaque(false);
        JButton back = new JButton("Back");
        JButton addToOrder = new JButton("Add Item to Order");

        back.setSize(200, 100);
        back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        back.setBackground(fontColor);
        back.setForeground(Color.WHITE);
        back.setOpaque(true);
        back.setBorder(BorderFactory.createRaisedBevelBorder());
        back.setBorderPainted(true);
        back.setContentAreaFilled(true);
        last.add(back);

        addToOrder.setSize(200, 100);
        addToOrder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        addToOrder.setBackground(fontColor);
        addToOrder.setForeground(Color.WHITE);
        addToOrder.setOpaque(true);
        addToOrder.setBorder(BorderFactory.createRaisedBevelBorder());
        addToOrder.setBorderPainted(true);
        addToOrder.setContentAreaFilled(true);
        last.add(addToOrder);

        bottomPanel4.add(last);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel4);
                orderScreen.add(bottomPanel2);
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        addToOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> extraStuff = extraSelection.getSelectedValuesList();
                String addOns = "";
                for (String s : extraStuff) {
                    addOns += s;
                }
                if (addOns.equals("")) {
                    addOns = "None";
                }
                if ((sizeSelection.getSelectedIndex() == 0) || (tempSelection.getSelectedIndex() == 0) ||
                        (sweetenerTypeSelection.getSelectedIndex() == 0) || (sweetenerVolumeSelection.getSelectedIndex() == 0)
                        || (typeSelection.getSelectedIndex() == 0) || (numShotsSelection.getSelectedIndex() == 0)
                        || milkTypeSelection.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Please make all selections.");

                } else if (!sweetenerTypeSelection.getSelectedItem().toString().equals("None") &&
                        sweetenerVolumeSelection.getSelectedItem().toString().equals("NA")) {
                    JOptionPane.showMessageDialog(null, "Please select a sweetener amount.");
                } else if ((sweetenerTypeSelection.getSelectedIndex() == 1) && !(sweetenerVolumeSelection.getSelectedIndex() == 1)){
                    JOptionPane.showMessageDialog(null, "Please select a sweetener type or change sweetener amount selection to 'NA.'");

                }else if (((typeSelection.getSelectedIndex() == 2) || (typeSelection.getSelectedIndex() == 4)) && (milkTypeSelection.getSelectedIndex() == 1)) {
                    JOptionPane.showMessageDialog(null, "Please select a milk type or change order to Americano or Espresso.");
                }else {
                    Coffee order = new Espresso(sizeSelection.getSelectedItem().toString(), tempSelection.getSelectedItem().toString(),
                            sweetenerTypeSelection.getSelectedItem().toString(), sweetenerVolumeSelection.getSelectedItem().toString(),
                            addOns, typeSelection.getSelectedItem().toString(), Integer.parseInt(numShotsSelection.getSelectedItem().toString().trim()),
                            milkTypeSelection.getSelectedItem().toString());

                    currentOrder.add(order);
                    orderScreen.remove(bottomPanel4);
                    orderScreen.add(createBottomPanel5());
                    orderScreen.revalidate();
                    orderScreen.repaint();
                }
            }
        });

        return bottomPanel4;
    }

    // createOrder bottomPanel for selecting Checkout or Add Another Item
    public JPanel createBottomPanel5() {
        bottomPanel5 = new JPanel();
        bottomPanel5.setOpaque(false);

        bottomPanel5.setLayout(new FlowLayout());

        JButton checkOut = new JButton("Checkout");
        JButton addMore = new JButton("Add Another Item");

        bottomPanel5.add(checkOut);
        bottomPanel5.add(addMore);

        for (Component c : bottomPanel5.getComponents()) {
            c.setSize(400, 200);
            c.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 60));
            c.setBackground(accentColor);
            c.setForeground(fontColor);
        }

        addMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel5);
                orderScreen.add(createBottomPanel2());
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        checkOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order.addOrderToHistory(new Order(currentCustomer, currentOrder));
                orderScreen.remove(bottomPanel5);
                orderScreen.add(createBottomPanel6());
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        return bottomPanel5;
    }

    // orderScreen bottomPanel for Checkout
    public JPanel createBottomPanel6() {

        bottomPanel6 = new JPanel();
        bottomPanel6.setOpaque(false);
        bottomPanel6.setLayout(new BoxLayout(bottomPanel6, BoxLayout.Y_AXIS));

        JPanel middle = new JPanel();
        middle.setOpaque(false);
        middle.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 10));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JTextArea orderSpecs = new JTextArea();
        orderSpecs.setOpaque(false);
        orderSpecs.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        orderSpecs.setForeground(fontColor);
        orderSpecs.setText(Order.getOrderHistory().getLast().display());
        orderSpecs.setEditable(false);
        orderSpecs.setLineWrap(true);
        ScrollPane scroll2 = new ScrollPane();
        scroll2.setSize(400, 650);
        scroll2.add(orderSpecs);
        scroll2.setBackground(backgroundColor);
        left.add(scroll2);

        middle.add(left);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JTextArea payCoffeeMessage = new JTextArea(currentCustomer.payCoffee());
        payCoffeeMessage.setOpaque(false);
        payCoffeeMessage.setSize(400, 650);
        payCoffeeMessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        payCoffeeMessage.setForeground(fontColor);
        payCoffeeMessage.setEditable(false);
        right.add(payCoffeeMessage);

        middle.add(right);
        bottomPanel6.add(middle);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new FlowLayout());

        JButton back = new JButton("Back");

        JButton finalize = new JButton("Submit Order & Pay");

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderScreen.remove(bottomPanel6);
                Order.getOrderHistory().removeLast();
                orderScreen.add(bottomPanel5);
                orderScreen.revalidate();
                orderScreen.repaint();
            }
        });

        back.setOpaque(true);
        back.setBorder(BorderFactory.createRaisedBevelBorder());
        back.setBorderPainted(true);
        back.setContentAreaFilled(true);
        finalize.setOpaque(true);
        finalize.setBorder(BorderFactory.createRaisedBevelBorder());
        finalize.setBorderPainted(true);
        finalize.setContentAreaFilled(true);

        bottom.add(back);
        bottom.add(finalize);

        for (Component c : bottom.getComponents()) {
            c.setSize(200, 100);
            c.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            c.setBackground(fontColor);
            c.setForeground(Color.WHITE);
        }

        finalize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeToCSV();
                frame.remove(orderScreen);
                frame.add(homePage);
                frame.revalidate();
                frame.repaint();
            }
        });

        bottomPanel6.add(bottom);

        return bottomPanel6;
    }

    public JPanel createViewOrders() {
        viewOrders = new JPanel();
        viewOrders.setOpaque(true);
        viewOrders.setLayout(new BoxLayout(viewOrders, BoxLayout.Y_AXIS));
        viewOrders.setBackground(backgroundColor);
        viewOrders.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel orders = new JLabel("Order History");
        orders.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
        orders.setForeground(fontColor);
        topPanel.add(orders);
        viewOrders.add(topPanel);

        // https://stackoverflow.com/questions/10177183/add-scroll-into-text-area
        // https://docs.oracle.com/javase/8/docs/api/javax/swing/JTextArea.html
        JPanel secondPanel = new JPanel();
        secondPanel.setOpaque(false);
        secondPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        orderHistoryAll = "";
        orderHistoryActive = "";
        orderHistoryPassive = "";

        for (Order order : Order.getOrderHistory()) {
            orderHistoryAll += (order.display() + "\n");
            if (order.getOrderStatus().equals("Active")) {
                orderHistoryActive += (order.display() + "\n");
            } else if (order.getOrderStatus().equals("Passive")){
                orderHistoryPassive += (order.display() + "\n");
            }
        }


        JPanel secondLeft = new JPanel();

        secondLeft.setLayout(new BoxLayout(secondLeft, BoxLayout.Y_AXIS));
        String [] orderTypeSelections = {"All Orders", "Active Orders", "Passive Orders"};
        JComboBox <String> orderType = new JComboBox<>(orderTypeSelections);
        orderType.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        secondLeft.add(orderType);
        secondLeft.setOpaque(false);

        JTextArea orderDetails = new JTextArea();
        orderDetails.setOpaque(true);
        orderDetails.setBackground(Color.white);
        orderDetails.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        orderDetails.setText(orderHistoryAll);
        orderDetails.setEditable(false);
        orderDetails.setLineWrap(true);
        orderDetails.setBorder(BorderFactory.createEmptyBorder(5,9,5,5));
        ScrollPane scroll = new ScrollPane();
        scroll.setSize(500, 600);
        scroll.add(orderDetails);
        secondLeft.add(scroll);
        secondPanel.add(secondLeft);

        orderType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (orderType.getSelectedIndex() == 0) {
                    orderDetails.setText(orderHistoryAll);
                } else if (orderType.getSelectedIndex() == 1){
                    orderDetails.setText(orderHistoryActive);
                } else if (orderType.getSelectedIndex() == 2){
                    orderDetails.setText(orderHistoryPassive);
                }
            }
        });

        JLabel picture = new JLabel(cup);
        picture.setBorder(BorderFactory.createEtchedBorder());
        secondPanel.add(picture);

        viewOrders.add(secondPanel);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new FlowLayout());

        JButton home = new JButton("Return to Homepage");
        home.setSize(50, 30);
        home.setOpaque(true);
        home.setBackground(fontColor);
        home.setForeground(Color.WHITE);
        home.setContentAreaFilled(true);
        home.setBorderPainted(true);
        home.setBorder(BorderFactory.createRaisedBevelBorder());

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(viewOrders);
                frame.add(homePage);
                frame.revalidate();
                frame.repaint();
            }
        });

        home.setSize(200, 100);
        home.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,30));

        bottom.add(home);

        viewOrders.add(bottom);

        return viewOrders;
    }

    public void writeToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("orderHistory.csv"));
            for (Order order : Order.getOrderHistory()) {
                writer.append(order.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}