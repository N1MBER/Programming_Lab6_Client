package Answers;

import PlantsInfo.Plants;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class ClientAnswer implements Serializable {
    private String answer;
    private ConcurrentSkipListSet<Plants> plants;
    private Plants plant;
    private String paths;
    private static final long serialVersionUID = 1L;

    public ClientAnswer(String msg){
        this.answer = msg;
//        this.arg = args;
    }

    public ClientAnswer(Plants plantes, String str){
        this.plant = plantes;
        this.answer = str;
    }

    public ClientAnswer(ConcurrentSkipListSet<Plants> collection, String message){
        this.plants = collection;
        this.answer = message;
    }

    public ClientAnswer(String path, String message){
        this.paths = path;
        this.answer = message;
    }

    public String getPaths() {
        return paths;
    }

    //    public CopyOnWriteArraySet<Plants> getCopyCollection(){
//        return copyCollection;
//    }


    public Plants getPlant() {
        return plant;
    }

    public ConcurrentSkipListSet<Plants> getPlants(){
        return plants;
    }

    public String getAnswer(){
        return answer;
    }

//    public T getArg(){
//        return arg;
//    }

}
