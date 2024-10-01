package com.conversormoedas.model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// Classe para gerenciar o histórico de conversões
public class HistoricoDeConversao {
    // Lista para armazenar as conversões
    private List<String> conversoes;
    // Número máximo de conversões armazenadas
    private static final int TAMANHO_MAXIMO = 10;
    // Nome do arquivo para salvar o histórico
    private static final String ARQUIVO_HISTORICO = "resources/historico_conversoes.json";
    // Objeto Gson para serialização/deserialização JSON
    private final Gson gson;

    // Construtor da classe
    public HistoricoDeConversao() {
        this.conversoes = new ArrayList<>();
        this.gson = new Gson();
        carregarHistorico();
    }

    // Método para adicionar uma nova conversão ao histórico
    public void adicionarConversao(String conversao) {
        // Remove a conversão mais antiga se o limite for atingido
        if (conversoes.size() >= TAMANHO_MAXIMO) {
            conversoes.remove(0);
        }
        // Adiciona a nova conversão
        conversoes.add(conversao);
        // Salva o histórico atualizado
        salvarHistorico();
    }

    // Método para exibir o histórico de conversões
    public void exibirHistorico() {
        if (conversoes.isEmpty()) {
            System.out.println("Aviso: O histórico de conversões está vazio.");
            System.out.println("Realize uma conversão para preencher o histórico.");
        } else {
            System.out.println("Histórico de Conversões:");
            for (int i = 0; i < conversoes.size(); i++) {
                System.out.println((i + 1) + ". " + conversoes.get(i));
            }
        }
    }

    // Método para salvar o histórico em um arquivo JSON
    private void salvarHistorico() {
        try (Writer writer = new FileWriter(ARQUIVO_HISTORICO)) {
            gson.toJson(conversoes, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o histórico: " + e.getMessage());
        }
    }

    // Método para carregar o histórico de um arquivo JSON
    private void carregarHistorico() {
        try (Reader reader = new FileReader(ARQUIVO_HISTORICO)) {
            // Define o tipo da lista para deserialização
            Type tipoLista = new TypeToken<ArrayList<String>>(){}.getType();
            // Carrega o histórico do arquivo
            List<String> historicoCarregado = gson.fromJson(reader, tipoLista);
            if (historicoCarregado != null) {
                conversoes = historicoCarregado;
                // Remove conversões excedentes, se necessário
                while (conversoes.size() > TAMANHO_MAXIMO) {
                    conversoes.remove(0);
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda, não é um erro
        } catch (IOException e) {
            System.out.println("Erro ao carregar o histórico: " + e.getMessage());
        }
    }
}