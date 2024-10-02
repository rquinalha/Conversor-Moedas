package com.conversormoedas.controller;

import com.conversormoedas.model.HistoricoDeConversao;
import com.conversormoedas.model.Moedas;
import com.conversormoedas.service.ApiConversao;
import com.conversormoedas.util.ConfiguracaoApi;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

// Classe principal para interação com o usuário
public class InteracaoUsuario {
  // Atributo para armazenar o histórico de conversões
  private final HistoricoDeConversao historico;
  private final String[] moedasSimbolos;
  private final String[] moedasSiglas;
  private final Moedas[] moedas;

  // Construtor da classe
  public InteracaoUsuario() {
    this.historico = new HistoricoDeConversao();
    this.moedas = Moedas.values();
    this.moedasSiglas = Moedas.siglas;
    this.moedasSimbolos = Moedas.simbolos;

  }

  // Método principal para converter moedas
  public void converterMoeda() {
    // Inicializa o scanner para entrada do usuário
    Scanner scanner = new Scanner(System.in);

    // Solicita a API Key no início do programa
    solicitarApiKey(scanner);

    // Loop principal do programa
    boolean continuar = true;
    while(true) {
      try {
        // Exibe o menu de opções para o usuário
        exibirMenu();

        // Obtém a opção de moeda de origem do usuário
        int opcaoOrigem;
        try {
          opcaoOrigem = obterOpcaoMenuPrincipal(scanner);

          // Verifica se o usuário escolheu sair da aplicação
          if(opcaoOrigem == this.moedas.length + 1) {
            System.out.println("Obrigado por usar o Conversor de Moedas. Até logo!");
            break;
          }

          // Verifica se o usuário escolheu exibir o histórico de conversões
          if(opcaoOrigem == this.moedas.length + 2) {
            historico.exibirHistorico();
            System.out.println("\nPressione 'Enter' para continuar...");
            scanner.nextLine(); // Aguarda o usuário pressionar Enter
            continue;
          }

        } catch(Exception e) {
          // Trata erros na entrada do usuário
          System.out.println(e.getMessage());
          if(!tratarErroContinuar(scanner)) break;
          else continue;
        }

        Moedas moeda = this.moedas[opcaoOrigem - 1];
        System.out.println("===>>> Você escolheu a moeda " + moeda.name() + " (" + moeda.getDescricao() + ")");

        // Obtém a opção de moeda de destino do usuário
        boolean opcaoDestinoValida = false;
        int opcaoDestino = 0;
        while(!opcaoDestinoValida) {
          System.out.println("\nPara qual moeda você quer converter?");
          exibirOpcoesMoedas();

          try {
            opcaoDestino = obterOpcaoMoedas(scanner);
            opcaoDestinoValida = true;
          } catch(Exception e) {
            System.out.println(e.getMessage());
            if(!tratarErroContinuar(scanner)) {
              continuar = false;
              break;
            }
          }
        }

        // Verifica se o usuário deseja continuar após escolher a moeda de destino
        if(!continuar) break;

        moeda = this.moedas[opcaoDestino - 1];
        System.out.println("===>>> Você escolheu a moeda " + moeda.name() + " (" + moeda.getDescricao() + ")");
        System.out.println();

        // Obtém as informações das moedas selecionadas
        String moedaOrigem = moedasSiglas[opcaoOrigem - 1];
        String moedaDestino = moedasSiglas[opcaoDestino - 1];
        String simboloOrigem = moedasSimbolos[opcaoOrigem - 1];
        String simboloDestino = moedasSimbolos[opcaoDestino - 1];

        // Obtém o valor a ser convertido do usuário
        double valor = 0;
        boolean valorValido = false;

        // Loop para obter um valor válido do usuário
        while(!valorValido) {
          System.out.printf("Digite o valor em %s (%s): ", moedaOrigem, simboloOrigem);
          String valorString = scanner.nextLine().trim();

          // Verifica se o usuário digitou algum valor
          if(valorString.isEmpty()) {
            System.out.println("Você não digitou nenhum valor.");
            if(!tratarErroContinuar(scanner)) {
              continuar = false;
              break;
            } else continue;
          }

          // Substitui vírgula por ponto para facilitar a conversão
          valorString = valorString.replace(',', '.');

          try {
            // Converte a string para um número
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = format.parse(valorString);
            valor = number.doubleValue();

            // Verifica se o valor é negativo
            if(valor < 0) {
              System.out.println("Número negativo não é aceito. Favor digitar um número positivo maior que zero.");
              continue;
            }

            // Verifica se o valor é zero
            if(valor == 0) {
              System.out.println("Valor inválido. Favor digitar um número maior que zero.");
              continue;
            }
            valorValido = true;
          } catch(Exception e) {
            System.out.println("Somente números são aceitos!");
            System.out.println("Favor digitar novamente.");
          }
        }

        // Verifica se o usuário deseja continuar após inserir o valor
        if(!continuar) break;

        try {
          // Obtém a taxa de câmbio e calcula o resultado
          double taxaCambio = ApiConversao.obterTaxaCambio(moedaOrigem, moedaDestino);
          double resultado = valor * taxaCambio;

          // Exibe o resultado da conversão
          System.out.printf("O valor em (%s) %s%.2f equivale a %s%.2f (%s)%n",
              obterNomeMoeda(moedaOrigem), simboloOrigem, valor, simboloDestino, resultado, obterNomeMoeda(moedaDestino));

          // Adiciona a conversão ao histórico
          adicionarConversaoAoHistorico(valor, moedaOrigem, simboloOrigem, resultado, moedaDestino, simboloDestino);
        } catch(Exception e) {
          System.out.println("Erro ao converter moeda: " + e.getMessage());
          if(!tratarErroContinuar(scanner)) break;
          else continue;
        }

        // Verifica se o usuário deseja realizar outra conversão
        if(!tratarDesejaRealizarOutraConversao(scanner)) break;
      } catch(Exception e) {
        System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        if(!tratarErroContinuar(scanner)) break;
      }
    }
    // Fecha o scanner ao finalizar o programa
    scanner.close();
  }

  // Método para exibir o menu principal
  private void exibirMenu() {
    System.out.println("**************************************************");
    System.out.println(">>>>>> Bem-vindo(a) ao Conversor de Moedas! <<<<<<\n");
    System.out.println("De qual moeda você quer converter?");
    exibeMoedas();
    int opcoesExtras = this.moedas.length + 1;
    System.out.printf("%d - Sair da Aplicação\n", opcoesExtras++);
    System.out.printf("%d - Exibir Histórico de Conversões\n", opcoesExtras);
    System.out.println("**************************************************");
    System.out.print("Digite aqui o número da sua escolha: ");
  }

  // Método para exibir as opções de moedas
  private void exibirOpcoesMoedas() {
    exibeMoedas();
    System.out.print("Digite aqui o número da sua escolha: ");
  }

  private void exibeMoedas() {
    for(Moedas moeda : this.moedas) {
      System.out.printf("%d - %s (%s)\n", moeda.ordinal() + 1, moeda.name(), moeda.getDescricao());
    }
  }

  // Método para obter a opção do usuário no menu principal
  private int obterOpcaoMenuPrincipal(Scanner scanner) {
    int limit = this.moedas.length + 2;
    String entrada = scanner.nextLine().trim();
    String mensagemErro = "Favor digitar um número entre 1 e " + limit + ".";
    if(entrada.isEmpty()) {
      throw new IllegalArgumentException("Você não escolheu nenhuma opção. " + mensagemErro);
    }
    try {
      int opcao = Integer.parseInt(entrada);
      if(opcao < 1 || opcao > limit) {
        throw new IllegalArgumentException("Opção inválida! " + mensagemErro);
      }
      return opcao;
    } catch(NumberFormatException e) {
      throw new IllegalArgumentException("Opção inválida! " + mensagemErro);
    }
  }

  // Método para obter a opção do usuário no menu de moedas
  private int obterOpcaoMoedas(Scanner scanner) throws IllegalArgumentException {
    String entrada = scanner.nextLine().trim();
    String mensagemErro = "Favor digitar um número entre 1 e " + this.moedas.length + ".";
    if(entrada.isEmpty()) {
      throw new IllegalArgumentException("Você não escolheu nenhuma opção. " + mensagemErro);
    }
    try {
      int opcao = Integer.parseInt(entrada);
      if(opcao < 1 || opcao > this.moedas.length) {
        throw new IllegalArgumentException("Opção inválida! " + mensagemErro);
      }
      return opcao;
    } catch(NumberFormatException e) {
      throw new IllegalArgumentException("Opção inválida! " + mensagemErro);
    }
  }

  // Método para tratar erros e perguntar se o usuário deseja continuar
  private boolean tratarErroContinuar(Scanner scanner) {
    while(true) {
      System.out.print("Deseja continuar? (Digite 's' para sim ou 'n' para não): ");
      String resposta = scanner.nextLine().trim().toLowerCase();
      if(resposta.isEmpty()) {
        System.out.println("Você não escolheu nenhuma opção.");
        continue;
      }
      if(resposta.equals("s")) {
        return true;
      } else if(resposta.equals("n")) {
        System.out.println("Obrigado por usar o Conversor de Moedas. Até logo!");
        return false;
      } else {
        System.out.println("Resposta inválida. Favor digitar 's' para sim ou 'n' para não.");
      }
    }
  }

  // Método para perguntar se o usuário deseja realizar outra conversão
  private boolean tratarDesejaRealizarOutraConversao(Scanner scanner) {
    while(true) {
      System.out.println("Deseja realizar outra conversão? (Digite 's' para sim ou 'n' para não)");
      String resposta = scanner.nextLine().trim().toLowerCase();
      if(resposta.isEmpty()) {
        System.out.println("Você não escolheu nenhuma opção.");
        continue;
      }
      if(resposta.equals("s")) {

        return true;
      } else if(resposta.equals("n")) {

        System.out.println("Deseja exibir o histórico de conversões? (Digite 's' para sim ou 'n' para não)");
        resposta = scanner.nextLine().trim().toLowerCase();
        if(resposta.isEmpty()) {
          System.out.println("Você não escolheu nenhuma opção. O histórico não será exibido.");
        } else if(resposta.equals("s")) {
          historico.exibirHistorico();
        }
        System.out.println("Obrigado por usar o Conversor de Moedas. Até logo!");
        return false;
      } else {
        System.out.println("Resposta inválida. Favor digitar 's' para sim ou 'n' para não.");
      }
    }
  }

  // Método para adicionar uma conversão ao histórico
  private void adicionarConversaoAoHistorico(double valor, String moedaOrigem, String simboloOrigem, double resultado, String moedaDestino, String simboloDestino) {
    // Obtém a data e hora atual formatada
    String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    // Formata o valor de origem
    String nomeValorOrigem = String.format("%.2f %s", valor, moedaOrigem);
    // Formata o valor de destino
    String nomeValorDestino = String.format("%.2f %s", resultado, moedaDestino);
    // Cria a string de conversão
    String conversao = String.format("%s - Valor em %s %s = %s %s em %s.",
        dataHora, obterNomeMoeda(moedaOrigem), simboloOrigem + " " + nomeValorOrigem,
        simboloDestino, nomeValorDestino, obterNomeMoeda(moedaDestino));
    // Adiciona a conversão ao histórico
    historico.adicionarConversao(conversao);
  }

  // Método para obter o nome formatado do valor
  public String obterNomeValor(String moeda, double valor) {
    return String.format("%.2f %s", valor, moeda);
  }

  /**
   * Obtém o nome da moeda baseado no seu símbolo.
   *
   * @param moedaSigla Nome da moeda.
   * @return Nome completo da moeda.
   */
  public String obterNomeMoeda(String moedaSigla) {
    for(Moedas moeda : this.moedas) {
      if(moeda.name().equalsIgnoreCase(moedaSigla)) return moeda.getDescricao();
    }
    return "Moeda Desconhecida";
  }

  private void solicitarApiKey(Scanner scanner) {
    String apiKey;
    do {
      System.out.print("Insira aqui a sua API Key: ");
      apiKey = scanner.nextLine().trim();
      if(apiKey.isEmpty()) {
        System.out.println("Aviso: A API Key não pode estar vazia. Favor tentar novamente.");
      }
    } while(apiKey.isEmpty());
    ConfiguracaoApi.setApiKey(apiKey);
  }
}
