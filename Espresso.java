public class Espresso extends Coffee {

    private String type;

    private int numShots;

    private String milk;

    private static final double SMALL = 3.00;

    private static final double MEDIUM = 3.25;

    private static final double LARGE = 3.50;

    private static final double EXTRA = 0.50;

    private static final double EXTRA_SHOT = 0.50;


    public Espresso (String size, String temp, String sweetenerType, String sweetenerVolume, String extras, String type, int numShots, String milk){
        super(size, temp,sweetenerType, sweetenerVolume, extras);
        this.type = type;
        this.numShots = numShots;
        this.milk = milk;
        double cost = 0;
        int numExtraShots = 0;
        if (size.equals("Small")) {
            cost += SMALL;
            if (this.numShots > 1) {
                numExtraShots = numShots - 1;
            }
        } else if (size.equals("Medium")) {
            cost += MEDIUM;
            if (numShots > 2) {
                numExtraShots = numShots - 2;
            }
        } else if (size.equals("Large")){
            cost += LARGE;
            if (numShots > 3) {
                numExtraShots = numShots - 3;
            }
        }
        int numExtras = 0;
        for (int i = 0; i < extras.length(); i++) {
            if (extras.charAt(i) == '*')
                numExtras += 1;
        }
        cost = cost + numExtras * EXTRA + numExtraShots * EXTRA_SHOT;

        super.setPrice(cost);

    }

    public void setType (String type) {
        this.type = type;
    }

    public String getType (){
        return this.type;
    }

    public void setNumShots (int numShots){
        this.numShots = numShots;
    }

    public int getNumShots(){
        return this.numShots;
    }

    public void setMilk (String milk){
        this.milk = milk;
    }

    public String getMilk(){
        return this.milk;
    }

    public String prepare (){
        return String.format ("Size: %s%nType: %s%nTemp: %s%nNumber of Shots: %s%nMilk: %s%nSweetener: %s%nSweetener Amount: %s%nExtras: %s%n", this.getSize(), this.getType(), this.getTemp(), this.getNumShots(),this.getMilk(), this.getSweetenerType(), this.getSweetenerVolume(), this.getExtras());

    }

    public String toString(){
        return super.toString() + "," + this.getType() + "," + this.getNumShots() + "," +
                this.getMilk();

    }

}
