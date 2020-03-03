package PlantsInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Plants implements  Comparable<Plants>, Serializable {
    static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int z;
    private int size;
    /**
     * Wasp WaspNearPlants - object, which spin near Plants
     */
    private Date date;
    private Wasp WaspNearPlants = null;
    /**
     * Place StartLocation - it's starting location where the plant appears
     */
    private Bee BeeNearPlants = null;
    /**
     * Place NowLocation - it's location where the plant located now
     */
    private Place NowLocation;
    /**
     * String nameLocation - it's name of location where the plant located
     */
    private String nameLocation;
    /**
     * String name - name of the plant
     */
    private String name;
    /**
     * PlantsCharacteristic characteristic - characteristic of the plant, values are
     * taken from enum - PlantsCharacteristic
     */
    private PlantsCharacteristic characteristic;

    /**
     * Constructor for Plants
     * @param n - name of the plant
     * @param ch - characteristic of the plant
     * @param pl - place where plant located now
     */
    public Plants (String n, PlantsCharacteristic ch, Place pl){
        this.name = n;
        this.characteristic = ch;
        this.NowLocation = pl;
        this.nameLocation = pl.getName();
        date = new Date();
        randomPlace();
        size = name.length() + characteristic.toString().length() + NowLocation.getName().length();
    }

    private void randomPlace(){
        this.x = (int) (Math.random()*100);
        this.y = (int) (Math.random()*100);
        this.z = (int) (Math.random()*100);
    }

    /**
     * Method which return name of the plants
     * @return - name of the plant
     */
    public String getName(){
        return this.name;
    }
    /**
     * Method which return characteristic of the plants
     * @return - characteristic of the plant
     */
    public String getCharicter(){
        return this.characteristic.toString();
    }

    public PlantsCharacteristic getChar(){
        return this.characteristic;
    }
    /**
     * Method which return name of the place where plants located now
     * @return - name of the place where plant located now
     */
    public String getLocation(){
        return this.NowLocation.getName();
    }

    /**
     *Method which set WaspNearPlants and BeeNearPlants and prints text message
     * @param w - Wasp class object
     * @param b - Bee class object
     */
    public void NearSpin(Wasp w,Bee b){
        this.WaspNearPlants = w;
        this.BeeNearPlants = b;
        System.out.println("Рядом с растением " + this.getName() + " летает" + w.getName() + " " + b.getName());
    }

    /**
     * Method which comparing two objects
     * @param obj - object for comparing
     * @return - if objects equal return true, else return false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else
            return false;
    }

    /**
     *Returns a hash code value for the object.
     * @return - hashcode of the objects
     */
    @Override
    public int hashCode()
    {
        final float kek = 14.88f;
        int sis = 17;
        sis = ((int)kek * sis)/3 + Objects.hash(getCharicter(),getLocation(),getName());
        return sis;
    }

    /**
     *Method which comparing this object with the specified object for order.
     * @param plants - object for comparing objects
     * @return - value of the compare, 0 - objects equal, -1 - this object less plants,
     * 1 - this object better plants.
     */
    @Override
    public int compareTo(Plants plants){
        int count = (int)(Math.random()*10);
        if ((this.name.compareTo(plants.name) == 0) & (this.nameLocation.compareTo(plants.nameLocation) == 0) & (this.characteristic.toString().compareTo(plants.characteristic.toString()) == 0))
            count= 0;
        else if (this.getName().length()==plants.getName().length()){
            if (this.hashCode() - plants.hashCode() >=0)
                count = 1;
            else if (this.hashCode() - plants.hashCode() <0)
                count = -1;
        }
        else if (this.getName().length() > plants.getName().length())
            count = 1;
        else
            count = -1;
        return count;
    }

    /**
     * This method return name of the plant
     * @return - name of the plant
     */
    @Override
    public String toString(){
        return this.name;
    }

    /**
     * Local static class Rjabina
     */
    static class Rjabina extends Plants {
        /**
         * String aroma - characteristic of the Rjabina
         */
        private String aroma;
        /**
         * String time - time of the day
         */
        private String time;
        public Rjabina(String n, PlantsCharacteristic ch,Place pl, String a, String t){
            super(n,ch,pl);
            this.aroma = a;
            this.time = t;
        }

        /**
         * This method print text message and set NowLocation and nameLocation
         * @param p - place where Rjabina located
         */
        public void fill(Place p){
            System.out.println(time + " " + aroma + " " + this.getName() + " " + this.getCharicter() + " " + " заполонил " + p.getName());
            super.NowLocation = p;
            super.nameLocation = p.getName();
        }

    }

    /**
     *This
     * @param n
     * @param ch
     * @param pl
     * @return
     */
    public Flower CreateFlower(String n,PlantsCharacteristic ch,Place pl){
        return new Flower(n,ch,pl);
    }
    public class Flower extends Plants {
        public  Flower(String n, PlantsCharacteristic ch, Place pl){
            super(n,ch,pl);
        }
    }

}
