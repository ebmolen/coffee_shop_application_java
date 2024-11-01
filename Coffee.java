public abstract class Coffee {

    private String size;

    private double price;

    private String temp;

    private String sweetenerType;

    private String sweetenerVolume;

    private String extras;


    public Coffee(String size, String temp, String sweetenerType, String sweetenerVolume, String extras) {
        this.size = size;
        this.temp = temp;
        this.sweetenerType = sweetenerType;
        this.sweetenerVolume = sweetenerVolume;
        this.extras = extras;
    }

    public void setSize (String size){
        this.size = size;
    }

    public String getSize(){
        return this.size;
    }


    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp(){
        return this.temp;
    }

    public void setSweetenerType(String sweetenerType) {
        this.sweetenerType = sweetenerType;
    }

    public String getSweetenerType() {
        return this.sweetenerType;
    }

    public void setSweetenerVolume(String sweetenerVolume){
        this.sweetenerVolume = sweetenerVolume;
    }

    public String getSweetenerVolume(){
        return this.sweetenerVolume;
    }

    public void setExtras(String extras){
        this.extras = extras;
    }

    public String getExtras(){
        return this.extras;
    }

    public String prepare () {
        return String.format("Size: %s%nTemp: %s%nExtras: %s%n", this.getSize(), this.getTemp());
    }

    public String toString(){
        return String.format("%s,%s,%s,%s,%s", this.getSize(), this.getTemp(), this.getSweetenerType(), this.getSweetenerVolume(), this.getExtras());
    }
}
