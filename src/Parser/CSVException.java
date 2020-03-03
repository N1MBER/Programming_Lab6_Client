package Parser;

public class CSVException extends  RuntimeException{
    protected CSVException(String message){
        super("\nПри парсинге возникла ошибка: " + message + "\n----");
    }
}
