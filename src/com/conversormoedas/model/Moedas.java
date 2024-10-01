package com.conversormoedas.model;

public enum Moedas {
  BRL("Real Brasileiro", "R$"),
  ARS("Peso Argentino", "$"),
  BOB("Boliviano Boliviano", "Bs"),
  CLP("Peso Chileno", "$"),
  COP("Peso Colombiano", "$"),
  USD("Dólar Americano", "U$"),
  CAD("Dólar Canadense", "C$"),
  JPY("Iene Japonês", "¥"),
  CNY("Yuan Chinês", "¥");

  public static final String[] descricoes, siglas, simbolos;

  static {
    Moedas[] m = Moedas.values();
    descricoes = populaArrays(m, "descricao");
    simbolos = populaArrays(m, "simbolo");
    siglas = populaArrays(m, "sigla");
  }

  private static String[] populaArrays(Moedas[] moedas, String campo) {
    String[] array = new String[moedas.length];
    for(int i = 0; i < moedas.length; i++) {
      array[i] = switch(campo.toLowerCase()){
        case "descricao" -> moedas[i].descricao;
        case "simbolo" -> moedas[i].simbolo;
        default -> moedas[i].name();
      };
    }
    return array;
  }
  private String descricao, simbolo;

  Moedas(String descricao, String simbolo) {
    this.descricao = descricao;
    this.simbolo = simbolo;
  }
}
