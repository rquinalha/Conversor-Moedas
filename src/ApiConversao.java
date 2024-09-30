import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.time.Duration;

public class ApiConversao {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/d09e04891d333a2c49c6ac98/latest/";

    public static double obterTaxaCambio(String moedaOrigem, String moedaDestino) throws Exception {
        String urlStr = API_URL + moedaOrigem;

        // Criação do cliente HTTP com timeout de conexão
        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        // Construção da requisição HTTP
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(urlStr))
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(30))
            .GET()
            .build();

        // Envio da requisição e obtenção da resposta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificação do código de status da resposta
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new Exception("Falha na requisição. Código de status: " + statusCode);
        }

        // Verificação do tipo de conteúdo da resposta
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        if (!contentType.contains("application/json")) {
            throw new Exception("Tipo de conteúdo inesperado: " + contentType);
        }

        // Análise do corpo da resposta JSON usando Gson
        String responseBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        String resultado = jsonObject.get("result").getAsString();
        
        // Extração da taxa de câmbio
        if ("success".equals(resultado)) {
            JsonObject taxas = jsonObject.getAsJsonObject("conversion_rates");
            return taxas.get(moedaDestino).getAsDouble();
        } else {
            throw new Exception("Falha ao obter taxa de câmbio: " + jsonObject.get("error-type").getAsString());
        }
    }
}
