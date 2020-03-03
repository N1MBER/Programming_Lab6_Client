package PlantsInfo;

import java.io.Serializable;

public class Insect implements Serializable {//DoAny,Spinning, Getname, Notice{
    private String name;
    static final long serialVersionUID = 1L;
    public Insect(String n){
        this.name = n;
    }

    public void Spin(Bee b, Wasp w, Plants.Flower f){
        f.NearSpin(w,b);
    }
    public String getName(){
        return this.name;
    }

    @Override
    public int hashCode()
    {
        final float kek = 14.88f;
        int sis = 17;
        sis = ((int)kek * sis)/3 + hashCode();
        return sis;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.toString() == this.toString()) {
            return true;
        }
        else
            return false;
    }
    @Override
    public String toString(){
        return this.name;
    }
    public class Spider extends Insect {
        public Spider(String n){
            super(n);
        }
    }
    public Spider createSpider(String n){
        return new Spider(n);
    }
}
