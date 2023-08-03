import java.util.Scanner;

public class Contador {
    public static void main(String[] args)  {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o primeiro número");
        int numero1 = scanner.nextInt();

        System.out.println("Digite o segundo número");
        int numero2 = scanner.nextInt();


        try {
            contar(numero1, numero2);
        }
        catch (ParametrosInvalidosException e){
            System.out.println(e.getMessage());
        }
    }

    static void contar( int numero1, int numero2) throws ParametrosInvalidosException{

            if (numero1 > numero2){
                throw new ParametrosInvalidosException("O segundo parâmetro deve ser maior que o primeiro");
            }

        int contagem = numero2 - numero1;

        for (int i = 0 ;  i < contagem ; i++){
            System.out.println("Imprimiendo o numero "+ i );
            System.out.println("Imprimiendo o numero " + i );
        }
    }
}