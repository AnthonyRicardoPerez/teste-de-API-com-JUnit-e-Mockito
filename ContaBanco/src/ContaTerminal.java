import java.util.Scanner;

public class ContaTerminal {
    public static void main(String[] args) {

        String agencia;
        String nomeCliente;
        int numero = 1021;
        double saldo = 248.21;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Por favor, digite seu nome completo");
        nomeCliente = scanner.nextLine();

        System.out.println("Por favor, digite o número da agência");
        agencia = scanner.nextLine();

        System.out.println("Ola " + nomeCliente + "  obrigado por crear uma conta em nosso banco," +
                " sua agencia e " + agencia + ", conta" + " " + numero  + " ,e seu saldo " + saldo +
                "  ja ta disponivel para saque");

    }
}