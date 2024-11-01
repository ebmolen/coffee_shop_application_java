public class PremiumCustomer extends Customer {

    private static final double DISCOUNT = .10;

    public static double getDiscount(){
        return DISCOUNT;
    }

    public PremiumCustomer(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber);
    }

    @Override
    public String payCoffee() {
        int discount = (int) (DISCOUNT * 100);
        return "Premium customers can pay \nusing bitcoin, credit card,\nor cash.\n\nA " + discount + "% discount has been applied.";
    }
}
