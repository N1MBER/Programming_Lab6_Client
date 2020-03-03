package Parser;

import PlantsInfo.*;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class JSONParser {
    protected CopyOnWriteArraySet<Plants> getArrPlants(String txt){
        CopyOnWriteArraySet<Plants> arr = new CopyOnWriteArraySet<>();
        String str = "\\}[, \n\r]*\\{";
        String[] arrtxt = txt.split(str);
        int i = 0;
        if(arrtxt.length>1){
            i=1;
            arrtxt[0] = arrtxt[0] + "}";
            try{
                arr.add(objParse(arrtxt[0]));
            }catch (JSONException e){
                System.out.println("----\nОбнаружена ошибка при парсинге элемента №" + 1 + e.getMessage());
            }
            arrtxt[arrtxt.length - 1] = "{" + arrtxt[arrtxt.length -1];
            try{
                arr.add(objParse(arrtxt[arrtxt.length - 1]));
            }catch (JSONException e){
                System.out.println("----\nОбнаружена ошибка при парсинге элемента №" + arrtxt.length + e.getMessage());
            }
            for(i = 1; i < arrtxt.length - 1; i++) {
                arrtxt[i] = "{" + arrtxt[i] + "}";
                try {
                    arr.add(objParse(arrtxt[i]));
                }catch (JSONException e){
                    System.out.println("----\nОбнаружена ошибка при парсинге элемента № " + (i+1) + e.getMessage());
                }
            }
        }else {
            try {
                arr.add(objParse(arrtxt[0]));
            }catch(JSONException e){
                System.out.println("----\nОбнаружена ошибка при парсинге элемента № " + 0 + e.getMessage());
            }
        }
        return arr;
    }

    public Plants objParse(String jsontxt)throws JSONException{
        boolean name = false;
        boolean characteristic = false;
        boolean location = false;
        boolean nameloc = false;
        String nameLocation = null;
        String plantsName = null;
        Place plantsLocation = null;
        PlantsCharacteristic plantsCharacteristic = null;
        while(jsontxt.contains("\"")){
            jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
            if (jsontxt.contains("\"")){
                int begin = jsontxt.indexOf("\"");
                switch (jsontxt.substring(0,begin)){
                    case "name":
                        jsontxt = jsontxt.substring(begin + 1);
                        if (!(jsontxt.indexOf(":") == 0) | (!jsontxt.contains("\""))) throw new JSONException("name (отсутствует \':\' или значение)" );
                        else {
                            jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
                            name = true;
                            plantsName = jsontxt.substring(0,jsontxt.indexOf("\""));
                            if(plantsName.matches(".*[0-9].*")){
                                throw new JSONException("в name не может быть цифр");
                            }
                            jsontxt = jsontxt.substring(jsontxt.indexOf("\""));
                            if (jsontxt.indexOf(",") != 1) throw new JSONException("отсутствуют запятые");
                        }
                        break;
                    case "characteristic":
                        jsontxt = jsontxt.substring(begin + 1);
                        if (!(jsontxt.indexOf(":") == 0) | (!jsontxt.contains("\"")))
                            throw new JSONException("characteristic (отсутствует \':\' или значение)");
                        else {
                            jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
                            switch (jsontxt.substring(0,jsontxt.indexOf("\""))){
                                case "Blooming":
                                    plantsCharacteristic = PlantsCharacteristic.Blooming;
                                    characteristic = true;
                                    jsontxt = jsontxt.substring(jsontxt.indexOf("\""));
                                    if (jsontxt.indexOf(",") != 1) throw new JSONException("отсутствуют запятые");
                                    break;
                                case "White":
                                    plantsCharacteristic = PlantsCharacteristic.White;
                                    characteristic = true;
                                    jsontxt = jsontxt.substring(jsontxt.indexOf("\""));
                                    if (jsontxt.indexOf(",") != 1) throw new JSONException("отсутствуют запятые");
                                    break;
                                case "High":
                                    plantsCharacteristic = PlantsCharacteristic.High;
                                    characteristic = true;
                                    jsontxt = jsontxt.substring(jsontxt.indexOf("\""));
                                    if (jsontxt.indexOf(",") != 1) throw new JSONException("отсутствуют запятые");
                                    break;
                                case "Woody":
                                    plantsCharacteristic = PlantsCharacteristic.Woody;
                                    characteristic = true;
                                    jsontxt = jsontxt.substring(jsontxt.indexOf("\""));
                                    if (jsontxt.indexOf(",") != 1) throw new JSONException("отсутствуют запятые");
                                    break;
                                case "Nothing":
                                    plantsCharacteristic = PlantsCharacteristic.Nothing;
                                    characteristic = true;
                                    jsontxt = jsontxt.substring(jsontxt.indexOf("\""));
                                    break;
                                default:
                                    throw new JSONException("characteristic отсутствует или указано неверно");
                            }
                            if (!jsontxt.contains("\"")) throw new JSONException("поле location указано неверно");
                        }
                        break;
                    case "location":
                        jsontxt = jsontxt.substring(begin + 1);
                        if ((!(jsontxt.indexOf(":") == 0)) | !(jsontxt.contains("{"))| !(jsontxt.contains("\"")))
                            throw new JSONException("location (отсутствует \':\' или значение)");
                        else {
                            jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
                            String helpstr = jsontxt.substring(0, jsontxt.indexOf("}"));
                            while (helpstr.contains("\"")) {
                                switch (jsontxt.substring(0, jsontxt.indexOf("\""))) {
                                    case "namelocation":
                                        jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
                                        helpstr = helpstr.substring(helpstr.indexOf("\"") + 1);
                                        if (!(jsontxt.indexOf(":") == 0) | !jsontxt.contains("\""))
                                            throw new JSONException("namelocation (отсутствует \':\' или значение)");
                                        jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
                                        helpstr = helpstr.substring(helpstr.indexOf("\"") + 1);
                                        nameloc = true;
                                        location = true;
                                        nameLocation = jsontxt.substring(0, jsontxt.indexOf("\""));
                                        if (nameLocation == "") {
                                            plantsLocation = null;
                                        } else
                                            plantsLocation = new Place(nameLocation);
                                        jsontxt = jsontxt.substring(jsontxt.indexOf("\"") + 1);
                                        helpstr = helpstr.substring(helpstr.indexOf("\"") + 1);
                                        break;
                                    default:
                                        throw new JSONException("поле отсутствует");
                                }
                            }
                        }
                }
            }

        }
        if (!name) throw new JSONException("поле name отсутствутет");
        if (!characteristic) throw new JSONException("поле characteristic отсутствует");
        if (!location) throw new JSONException("поле location отсутствует");
        if (!nameloc) throw new JSONException("поле namelocation отсутствует");
        return new Plants(plantsName,plantsCharacteristic,plantsLocation);
    }

    private String getCharacterPlants(String type){
        String character="";
        switch (type){
            case "цветущая":
                character = "BLooming";
                break;
            case "белые":
                character = "White";
                break;
            case "высокое":
                character = "High";
                break;
            case "древесный":
                character = "Woody";
                break;
            case "":
                character = "Nothing";
                break;
                default:
                    System.out.println("Plants имеет недопустимое значение для character.");
        }
        return character;
    }

    private String getPlants(Plants writeplants){
            return "{\n \"name\": \"" + writeplants.getName() + "\",\n \"characteristic\": \"" + getCharacterPlants(writeplants.getCharicter()) + "\",\n \"location\": {\n\t" + "\"namelocation\": \"" + writeplants.getLocation() + "\"\n  }\n}";
    }

    public String getWrittenPlants(CopyOnWriteArraySet<Plants> linkPlants){
        String str = "[\n";
        Iterator <Plants> iterator = linkPlants.iterator();
        for(int i = 0; i < linkPlants.size() - 1; i++){
            str += getPlants(iterator.next()) + ",\n";
        }
        str += getPlants(iterator.next()) + "\n]";
        return str;
    }
}
