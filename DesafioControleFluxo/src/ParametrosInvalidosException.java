public class ParametrosInvalidosException extends Exception{

    public ParametrosInvalidosException(String message) {

        super(message);
    }

    private void message(String s) {
        s ="O segundo parâmetro deve ser maior que o primeiro";
    }
}
