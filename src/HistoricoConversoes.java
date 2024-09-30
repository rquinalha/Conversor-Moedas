import java.util.ArrayList;
import java.util.List;

public class HistoricoConversoes {
    private List<String> conversoes;
    private static final int TAMANHO_MAXIMO = 10;

    public HistoricoConversoes() {
        this.conversoes = new ArrayList<>();
    }

    public void adicionarConversao(String conversao) {
        if (conversoes.size() >= TAMANHO_MAXIMO) {
            conversoes.remove(0);
        }
        conversoes.add(conversao);
    }

    public void exibirHistorico() {
        System.out.println("Histórico de Conversões:");
        for (int i = conversoes.size() - 1; i >= 0; i--) {
            System.out.println(conversoes.get(i));
        }
    }
}