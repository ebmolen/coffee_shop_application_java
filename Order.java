import java.util.ArrayList;

public class Order {

    private int orderNumber;

    private String orderStatus;

    private double totalCost;

    private static int nextOrderNumber;

    private ArrayList <Coffee> items;

    private Customer orderCustomer;

    private boolean premiumOrder;

    private static ArrayList <Order> orderHistory = new ArrayList<>();

    // Constructor for when placing order.
    public Order (Customer orderCustomer, ArrayList<Coffee> items) {
        this.orderNumber = nextOrderNumber;
        this.orderStatus = "Active";
        this.orderCustomer = orderCustomer;
        this.items = items;
        double tempCost = 0;
        for (Coffee coffee : items) {
            tempCost += coffee.getPrice();
        }
        if (orderCustomer instanceof PremiumCustomer){
            tempCost  = tempCost * (1.00 - PremiumCustomer.getDiscount());
            this.premiumOrder = true;
        }
        else {
            this.premiumOrder = false;
        }
        this.totalCost = tempCost;
        nextOrderNumber += 1;
    }


    // Constructor for when reading csv.
    public Order (String orderStatus, int orderNumber, Customer orderCustomer, boolean premiumOrder, double totalCost, ArrayList<Coffee> items){
        this.orderStatus = orderStatus;
        this.orderNumber = orderNumber;
        this.orderCustomer = orderCustomer;
        this.totalCost = totalCost;
        this.premiumOrder = premiumOrder;
        this.items = items;
    }

    public static void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    public static ArrayList<Order> getOrderHistory(){
        return orderHistory;
    }

    public void setPremiumOrder(boolean premiumOrder){
        this.premiumOrder = premiumOrder;
    }

    public boolean getPremiumOrder(){
        return this.premiumOrder;
    }

    public void setOrderNumber (int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber () {
        return this.orderNumber;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus(){
        return this.orderStatus;
    }

    public void setOrderCustomer(Customer orderCustomer){
        this.orderCustomer = orderCustomer;
    }

    public Customer getOrderCustomer (){
        return this.orderCustomer;
    }

    public void setTotalCost(double totalCost){
        this.totalCost = totalCost;
    }

    public double getTotalCost () {
        return this.totalCost;
    }

    public static void setNextOrderNumber(int next) {
        nextOrderNumber = next;
    }

    public static int getNextOrderNumber(){
        return nextOrderNumber;
    }

    public ArrayList <Coffee> getItems (){
        return this.items;
    }

    public void setItems (ArrayList <Coffee> items) {
        this.items = items;
    }

    public String display() {
        String drinks = "";
        int number = 1;
        for (Coffee coffee: this.items) {
            drinks += String.format("Item %d - %n", number);
            drinks += coffee.prepare() + "\n";
            number += 1;
        }
        return String.format("*********************%nOrder Number: %d%nOrder Status: %s%nCustomer Name: %s %s%nPhone: %s%nE-mail: %s%nCost: $%.2f%nPremium Order: %s%n%n%s*********************%n",
                this.getOrderNumber(), this.getOrderStatus(), this.getOrderCustomer().getFirstName(), this.getOrderCustomer().getLastName(),
                this.getOrderCustomer().getPhoneNumber(), this.getOrderCustomer().getEmail(), this.getTotalCost(), this.getPremiumOrder(), drinks);
    }

    public String toString(){
        String orders = "";
        for(Coffee coffee: this.items) {
            orders = orders + "," + coffee.toString() ;
        }
        return this.getOrderStatus() + "," + this.getOrderNumber() + "," + this.getOrderCustomer().getFirstName() + "," + this.getOrderCustomer().getLastName()
                + "," + this.getOrderCustomer().getEmail() + "," + this.getOrderCustomer().getPhoneNumber() +"," + this.getTotalCost() + "," + this.getPremiumOrder() + orders;
    }

}
