package ClientSoft;

import Parser.CSVWriter;
import PlantsInfo.Plants;

import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListSet;

public class Loader {

    private Scanner scanner;
    private ConcurrentSkipListSet<Plants> LinkHSPlants;
    private Date date;
    private File sourceFile;
    public String textOfFile;
    private boolean neadSave = true;
    private boolean allOK = false;
    private boolean IsCSV=false;

    public void setAllOK(boolean allOK) {
        this.allOK = allOK;
    }

    protected Loader() {
        scanner = new Scanner(System.in);
        sourceFile = new File(getFilePath());
            readFile(sourceFile);

    }

    public ConcurrentSkipListSet<Plants> getLinkHSPlants() {
        return LinkHSPlants;
    }

    private String getFilePath(){
        String path = System.getenv("PATH_CSV");
        System.out.println(System.getProperty("user.dir"));
        if (path == null){
            System.out.println("----\nПуть через переменную окружения PATH_CSV не указан\nНапишите адрес вручную(в консоль)\n----");
            return scanner.nextLine();
        } else {
            return path;
        }
    }

    public String readCSV(File file) {
        String txt = "";
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                txt += scanner.nextLine();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                saveAndExit();
            }));
            System.out.println("----\nФайл считан\n----");
        } catch (IOException e){
            System.out.println("----\nОшибка чтения файла.\nРабота невозможна.\n----");

        }
        try{
            FileWriter fileWrite = new FileWriter(file);
            fileWrite.write(txt);
        }catch (IOException e){
            e.getMessage();
        }
        //AfterReadSave(txt);
        this.textOfFile = txt;
        return txt;
    }

    protected void BeforeSaveDelete(File file){
        String txt = "";
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                txt += scanner.nextLine();
            }
        } catch (IOException e){
        }
    }

    public void readFile(File impFile){
        try{
            if(!(impFile.isFile()))
                throw new FileNotFoundException("----\nПуть ведёт не к файлу.\nРабота невозможна.\n----");
            if(!(impFile.exists()))
                throw new FileNotFoundException("----\nПо указанному пути файл не найден.\nДальнейшая работа невозможна.\n----");
            if (!(impFile.canRead()))
                throw new SecurityException("----\nНет прав на чтение.\nДальнейшая работа невозможна.\n----");
            CSVtoLHS(readCSV(impFile));
        }catch (FileNotFoundException | SecurityException e){
            System.out.println("----\nОшибка чтения файла.\n----");
        }
        catch (NullPointerException e){
            System.out.println("----\nПуть указывает не на файл.\n----");
        }
    }

    public boolean getOK(){
        return allOK;
    }

    public void CSVtoLHS(String infoCSV){
        setAllOK(true);
        CSVWriter csvWriter = new CSVWriter();
        LinkHSPlants = csvWriter.getArrPlants(infoCSV);
        if (LinkHSPlants.size()>0)
            IsCSV =true;
        System.out.println("----\nЭлементов было добавлено: " + LinkHSPlants.size() + "\n----");
        date = new Date();
    }

    protected void saveAndExit(){
        String format = sourceFile.getName().substring(sourceFile.getName().indexOf(".") +1);
        if (!format.equals("csv"))
            save();
        if (!format.equals("csv") | sourceFile == null | !(sourceFile.canWrite())) {
            System.out.println("----\nФайл не существует или в него нельзя записать данные или он не соответствует необходимому для записи формату.\n----");
            String newFile = "CSVObject" + (Math.random()*100);
            String directory = System.getProperty("user.dir");
            String separator = System.getProperty("file.separator");
            sourceFile = new File(directory + separator + newFile + ".csv");
            if (!sourceFile.exists() | !sourceFile.isFile()) {
                try {
                    if (sourceFile.createNewFile()) {
                        System.out.println("----\nНовый файл успешно создан.\nИмя файла:" + newFile +"\n----");
                        AskSave();
                    }
                } catch (IOException e) {
                    System.out.println("----\nОшибка при создании файла:\n----" + e.getMessage());
                }
            } else {
                AskSave();
            }
        } else {
            AskSave();
        }
    }

    protected void AskSave(){
        if (neadSave) {
            try {
                if (IsCSV) {
                    save();
                } else {
                    FileWriter fileWritercsv = new FileWriter(sourceFile);
                    try (BufferedWriter bufferedWriter = new BufferedWriter(fileWritercsv)) {
                        bufferedWriter.write("");
                        System.out.println("----\nКоллекция пуста\n---- ");
                    } catch (IOException e) {
                        System.out.println("----\nОшибка записи\n----");
                    }
                }
            } catch (IOException e) {
                System.out.println("----\nОшибка записи\n----" + e.getMessage());
            }
        }
    }

    protected void save(){
        try {
            BeforeSaveDelete(sourceFile);
            FileWriter fileWriterCSV = new FileWriter(sourceFile);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriterCSV)) {
                if(LinkHSPlants != null) {
                    if (IsCSV) {
                        CSVWriter csvWriter = new CSVWriter();
                        bufferedWriter.write(csvWriter.getWrittenPlants(LinkHSPlants));
                        System.out.println("----\nКоллекция сохранена в файле: " + sourceFile.getAbsolutePath() + "\n----");
                    } else {
                        bufferedWriter.write(textOfFile);
                        System.out.println("----\nСохранено изначальное значение файла\n----");
                    }
                }
                else{
                    bufferedWriter.write(textOfFile);
                }
                System.out.println("----\nРабота над данной коллекцией завершена.\n----");
            }catch(IOException e ){
                System.out.println("----\nОшибка записи файла\n----" + e.getMessage());
            }
        }catch(IOException e){
            System.out.println("----\nОшибка записи файла\n----" + e.getMessage());
        }
    }



}