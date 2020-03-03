package PlantsInfo;

import java.io.Serializable;

public class Place implements  Serializable {
    private String name;
    static final long serialVersionUID = 1L;
    public Place(String n){
        this.name=n;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
