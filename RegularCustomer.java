public class RegularCustomer extends Customer {

    public RegularCustomer(String firstName, String lastName, String email, String phoneNumber) {
        super (firstName, lastName, email, phoneNumber);
    }

    @Override
    public String payCoffee() {
        return "Regular customers can pay\nusing credit card or cash.";
    }
}
