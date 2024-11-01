public class FilteredCoffee extends Coffee {

    private String brew;

    private String creamerType;

    private String creamerVolume;

    private final static double SMALL = 2.75;

    private final static double MEDIUM = 3.00;

    private final static double LARGE = 3.25;

    private final static double EXTRA = 0.5;


    public FilteredCoffee (String size, String temp, String sweetenerType, String sweetenerVolume, String extras, String brew, String creamerType,
                           String creamerVolume){
        super (size, temp, sweetenerType, sweetenerVolume, extras);
        this.brew = brew;
        this.creamerType = creamerType;
        this.creamerVolume = creamerVolume;

        double cost = 0;
        if (size.equals("Small")) {
            cost += SMALL;
        } else if (size.equals("Medium")) {
            cost += MEDIUM;
        } else if (size.equals("Large")){
            cost += LARGE;
        }
        int numExtras = 0;
        for (int i = 0; i < extras.length(); i++) {
            if (extras.charAt(i) == '*')
                numExtras += 1;
        }
        cost = cost + numExtras * EXTRA;
        super.setPrice(cost);
    }


    public void setCreamerType(String creamerType) {
        this.creamerType = creamerType;
    }

    public String getCreamerType(){
        return this.creamerType;
    }

    public void setCreamerVolume (String creamerVolume){
        this.creamerVolume = creamerVolume;
    }

    public String getCreamerVolume(){
        return this.creamerVolume;
    }


    public void setBrew(String brew) {
        this.brew = brew;
    }

    public String getBrew () {
        return this.brew;
    }

    public String prepare () {

        return String.format ("Size: %s%nType: Filtered Coffee%nTemp: %s%nBrew: %s%nCreamer: %s%nCreamer Amount: %s%nSweetener: %s%nSweetener Amount: %s%nExtras: %s%n",
                this.getSize(),this.getTemp(), this.getBrew(), this.getCreamerType(), this.getCreamerVolume(), this.getSweetenerType(), this.getSweetenerVolume(), this.getExtras());
    }

    // Fix Me
    public String toString (){
        return super.toString() + ",Filtered Coffee," + this.getBrew() + "," +
                this.getCreamerType() + "," + this.getCreamerVolume();
    }


}
