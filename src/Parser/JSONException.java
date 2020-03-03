package Parser;

public class JSONException extends RuntimeException {
    /**
     *Exception which thrown when parser error
     * @param msg
     */
    protected JSONException(String msg){
        super("\nПри парсинге возникла ошибка: " + msg + "\n----");
    }
}
