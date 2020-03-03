package PlantsInfo;

import java.io.Serializable;

public enum PlantsCharacteristic implements Serializable {
    Blooming("цветущая"),
    White("белые"),
    High("высокое"),
    Woody("древесный"),
    Nothing("");
    static final long serialVersionUID = 1L;

    private final String data;
    PlantsCharacteristic(String ch){
        this.data = ch;
    }
    @Override
    public String toString(){
        return data;
    }

    public String returnType(){
        String type;
        switch (this.toString()){
            case "цветущая":
                type = "Blooming";
                break;
            case "белые":
                type = "White";
                break;
            case "высокое":
                type = "High";
                break;
            case "древесный":
                type = "Woody";
                break;
            case "":
                type = "Nothing";
                break;
                default:
                    type = "Неопознаный тип";
                    break;
        }
        return type;
    }
}