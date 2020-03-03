package Parser;

import PlantsInfo.*;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class CSVWriter {

    public ConcurrentSkipListSet<Plants> getArrPlants(String txt){
        ConcurrentSkipListSet<Plants> lhs = new ConcurrentSkipListSet<Plants>();
        String str ="\"\"";
        String[] arrstring = txt.split(str);
        arrstring[0] = arrstring[0] + "\"";
        arrstring[arrstring.length-1] = "\"" + arrstring[arrstring.length-1];
        for (int j = 1; j<arrstring.length-1;j++) {
            arrstring[j] = "\"" + arrstring[j] + "\"";
        }
        for (int i =0;i<arrstring.length;i++)
            try {
                lhs.add(objectParse(arrstring[i],arrstring.length));
            }catch (CSVException e) {
                System.out.println("----\nОбнаружена ошибка при парсинге элемента №" + i + e.getMessage()+ "\n----");
            }
        return lhs;
    }

    protected Plants objectParse(String str,int l){
        boolean name = false;
        boolean characteristic = false;
        boolean location = false;
        boolean namelocation = false;
        String Pname = null;
        String Pchar;
        Place plantsLocation = null;
        PlantsCharacteristic plantsCharacteristic = null;
        int countOfField = 3;
        while (str.contains(",")){
            if (countOfField<0)
                throw new CSVException("Неверный ввод или недопустимое количество полей");
            else{
                switch (countOfField){
                    case 3:
                        String helpstr = str.substring(0,str.indexOf(","));
                        if (helpstr.indexOf("\"") != 0 || helpstr.indexOf("\"",2)  != helpstr.length() -1)
                            throw new CSVException("ошибка ввода поля name");
                        else {
                            Pname = str.substring(str.indexOf("\"") + 1,str.indexOf("\"",2));
                        }
                        if(Pname.matches(".*[0-9].*")){
                            throw new CSVException("в name не может быть цифр");
                        }
                        name = true;
                        countOfField--;
                        if (!str.contains(","))
                            throw new CSVException("поле characteristic отсутствует или указанно неверно");
                        str = str.substring(str.indexOf(","),str.length()-1) + "\"";
                        break;
                    case 2:
                        Pchar = str.substring(str.indexOf(",")+1,str.indexOf(",",2));
                        switch (Pchar){
                            case "Blooming":
                                plantsCharacteristic = PlantsCharacteristic.Blooming;
                                characteristic = true;
                                break;
                            case "Woody":
                                plantsCharacteristic = PlantsCharacteristic.Woody;
                                characteristic = true;
                                break;
                            case "White":
                                plantsCharacteristic = PlantsCharacteristic.White;
                                characteristic = true;
                                break;
                            case "High":
                                plantsCharacteristic = PlantsCharacteristic.High;
                                characteristic = true;
                                break;
                            case "Nothing":
                                plantsCharacteristic = PlantsCharacteristic.Nothing;
                                characteristic = true;
                                break;
                                default:
                                    throw new CSVException("characteristic отсутствует или указан неверно");
                        }
                        if (!str.contains(","))
                            throw new CSVException("поле location отсутствует или указанно неверно");
                        countOfField--;
                        str = str.substring(str.indexOf(",",2),str.length()-1) + "\"";
                        break;
                    case 1:
                        String loc = str.substring(str.indexOf("\"") + 1, str.indexOf("\"",2));
                        if (loc.matches(".*[0-9].*"))
                            throw new CSVException("в поле namelocation не может быть цифр");
                        plantsLocation = new Place(loc);
                        location = true;
                        namelocation = true;
                        countOfField--;
                        str = str.substring(str.indexOf("\"",2),str.length()-1);
                        break;
                        default:
                            break;
                }
            }
        }
        if (!name) throw new CSVException("отсутствует поле name");
        if (!characteristic) throw new CSVException("отсутствует поле characteristic");
        if (!namelocation) throw new CSVException("отсутствует поле namelocation");
        if (!location) throw new CSVException("отсутствует поле location");
        return new Plants(Pname, plantsCharacteristic,plantsLocation);
    }

    protected String getPlants(Plants plants) {
        return "\"" + plants.getName() + "\"," + plants.getChar().returnType() + ",\"" + plants.getLocation() + "\"" ;
    }

    public String getWrittenPlants(ConcurrentSkipListSet<Plants> lhsp){
        String str = "";
        Iterator<Plants> iterator = lhsp.iterator();
        for(int i = 0; i < lhsp.size() ; i++){
            str += getPlants(iterator.next()) + "\n";
        }
        return str;
    }
}
