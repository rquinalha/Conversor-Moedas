import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class InteracaoUsuario {

public void converterMoeda() {
    DecimalFormat df = new DecimalFormat("#.##");
    Scanner scanner = new Scanner(System.in);
    
    boolean continuar = true;
    while (continuar) {
        try {
            System.out.println("**************************************************");
            System.out.println("Bem-vindo ao Conversor de Moedas!");
            System.out.println("De qual moeda você quer converter?");
            System.out.println("1 - ARS (Peso argentino)");
            System.out.println("2 - BOB (Boliviano boliviano)");
            System.out.println("3 - BRL (Real brasileiro)");
            System.out.println("4 - CLP (Peso chileno)");
            System.out.println("5 - COP (Peso colombiano)");
            System.out.println("6 - USD (Dólar americano)");
            System.out.println("7 - Sair da aplicação");
            System.out.println("**************************************************");
            System.out.print("Digite aqui o número da sua escolha: ");

            int opcaoOrigem = scanner.nextInt();
            
            if (opcaoOrigem == 7) {
                System.out.println("Obrigado por usar o Conversor de Moedas. Até logo!");
                break;
            }
            
            if (opcaoOrigem < 1 || opcaoOrigem > 6) {
                System.out.println("Opção inválida!");
                continue;
            }
            
            String[] moedas = {"ARS (Peso argentino)", "BOB (Boliviano boliviano)", "BRL (Real brasileiro)", "CLP (Peso chileno)", "COP (Peso colombiano)", "USD (Dólar americano)"};
            System.out.println("Você escolheu a moeda " + moedas[opcaoOrigem - 1]);
            
            System.out.println("\nPara qual moeda você quer converter?");
            System.out.println("1 - ARS (Peso argentino)");
            System.out.println("2 - BOB (Boliviano boliviano)");
            System.out.println("3 - BRL (Real brasileiro)");
            System.out.println("4 - CLP (Peso chileno)");
            System.out.println("5 - COP (Peso colombiano)");
            System.out.println("6 - USD (Dólar americano)");
            System.out.print("Digite aqui o número da sua escolha: ");

            int opcaoDestino = scanner.nextInt();
            
            if (opcaoDestino < 1 || opcaoDestino > 6) {
                System.out.println("Opção inválida!");
                continue;
            }
            
            System.out.println("Você escolheu a moeda " + moedas[opcaoDestino - 1]);
            System.out.println();
            
            String[] moedasAbreviadas = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};
            String[] simbolos = {"$", "Bs", "R$", "$", "$", "U$"};
            
            String moedaOrigem = moedasAbreviadas[opcaoOrigem - 1];
            String moedaDestino = moedasAbreviadas[opcaoDestino - 1];
            String simboloOrigem = simbolos[opcaoOrigem - 1];
            String simboloDestino = simbolos[opcaoDestino - 1];
            
            System.out.printf("Digite o valor em %s (%s): ", moedaOrigem, simboloOrigem);
            scanner.nextLine(); // Limpa o buffer
            String valorString = scanner.nextLine().replace(',', '.');
            
            double valor;
            try {
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                Number number = format.parse(valorString);
                valor = number.doubleValue();
                
                if (valor < 0) {
                    System.out.println("Valor inválido. Favor digitar um número positivo.");
                    continue;
                }
            } catch (ParseException e) {
                System.out.println("Valor inválido. Favor digitar um número válido.");
                continue;
            }

            double resultado = 0;

            try {
                double taxaCambio = ApiConversao.obterTaxaCambio(moedaOrigem, moedaDestino);
                resultado = valor * taxaCambio;

                System.out.printf("O valor %s%.2f equivale a %s%.2f.%n", simboloOrigem, valor, simboloDestino, resultado);
            } catch (Exception e) {
                System.out.println("Erro ao converter moeda: " + e.getMessage());
            }

            System.out.println("Deseja realizar outra conversão? (Digite 's' para sim ou 'n' para não)");
            String resposta = scanner.next().trim().toUpperCase();
            if (!resposta.equals("S")) {
                continuar = false;
                System.out.println("Obrigado por usar o Conversor de Moedas. Até logo!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Por favor, digite um número válido.");
        }
    }
    scanner.close();
}
}
