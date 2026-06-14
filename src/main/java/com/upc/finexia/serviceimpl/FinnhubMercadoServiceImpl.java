package com.upc.finexia.serviceimpl;

import com.upc.finexia.dtos.ActivoMercadoDTO;
import com.upc.finexia.services.MercadoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinnhubMercadoServiceImpl implements MercadoService {

    private final RestClient restClient;

    public FinnhubMercadoServiceImpl(
            RestClient.Builder restClientBuilder,
            @Value("${finnhub.base-url}") String baseUrl,
            @Value("${finnhub.api-key}") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("X-Finnhub-Token", apiKey)
                .build();
    }

    @Override
    public List<ActivoMercadoDTO> buscarActivos(String query, int limit) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("El texto de busqueda es obligatorio");
        }

        int limiteSeguro = Math.max(1, Math.min(limit, 10));
        FinnhubSearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query.trim())
                        .build())
                .retrieve()
                .body(FinnhubSearchResponse.class);

        return Optional.ofNullable(response)
                .map(FinnhubSearchResponse::getResult)
                .orElse(Collections.emptyList())
                .stream()
                .filter(result -> result.getSymbol() != null && !result.getSymbol().isBlank())
                .limit(limiteSeguro)
                .map(this::toActivoMercado)
                .collect(Collectors.toList());
    }

    @Override
    public ActivoMercadoDTO cotizacion(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("El simbolo es obligatorio");
        }
        FinnhubQuoteResponse quote = obtenerCotizacion(symbol.trim());
        return toActivoMercado(symbol.trim(), symbol.trim(), null, null, quote);
    }

    private ActivoMercadoDTO toActivoMercado(FinnhubSearchResult result) {
        FinnhubQuoteResponse quote = obtenerCotizacionSinRomperBusqueda(result.getSymbol());
        return toActivoMercado(
                result.getSymbol(),
                result.getDisplaySymbol(),
                result.getDescription(),
                result.getType(),
                quote
        );
    }

    private FinnhubQuoteResponse obtenerCotizacionSinRomperBusqueda(String symbol) {
        try {
            return obtenerCotizacion(symbol);
        } catch (RestClientException ex) {
            return new FinnhubQuoteResponse();
        }
    }

    private FinnhubQuoteResponse obtenerCotizacion(String symbol) {
        FinnhubQuoteResponse quote = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .body(FinnhubQuoteResponse.class);
        return Objects.requireNonNullElseGet(quote, FinnhubQuoteResponse::new);
    }

    private ActivoMercadoDTO toActivoMercado(String symbol,
                                             String displaySymbol,
                                             String descripcion,
                                             String tipo,
                                             FinnhubQuoteResponse quote) {
        return new ActivoMercadoDTO(
                symbol,
                displaySymbol,
                descripcion,
                tipo,
                quote.getC(),
                quote.getD(),
                quote.getDp(),
                quote.getH(),
                quote.getL(),
                quote.getO(),
                quote.getPc(),
                quote.getT()
        );
    }

    private static class FinnhubSearchResponse {
        private List<FinnhubSearchResult> result;

        public List<FinnhubSearchResult> getResult() {
            return result;
        }

        public void setResult(List<FinnhubSearchResult> result) {
            this.result = result;
        }
    }

    private static class FinnhubSearchResult {
        private String description;
        private String displaySymbol;
        private String symbol;
        private String type;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDisplaySymbol() {
            return displaySymbol;
        }

        public void setDisplaySymbol(String displaySymbol) {
            this.displaySymbol = displaySymbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private static class FinnhubQuoteResponse {
        private Double c;
        private Double d;
        private Double dp;
        private Double h;
        private Double l;
        private Double o;
        private Double pc;
        private Long t;

        public Double getC() {
            return c;
        }

        public void setC(Double c) {
            this.c = c;
        }

        public Double getD() {
            return d;
        }

        public void setD(Double d) {
            this.d = d;
        }

        public Double getDp() {
            return dp;
        }

        public void setDp(Double dp) {
            this.dp = dp;
        }

        public Double getH() {
            return h;
        }

        public void setH(Double h) {
            this.h = h;
        }

        public Double getL() {
            return l;
        }

        public void setL(Double l) {
            this.l = l;
        }

        public Double getO() {
            return o;
        }

        public void setO(Double o) {
            this.o = o;
        }

        public Double getPc() {
            return pc;
        }

        public void setPc(Double pc) {
            this.pc = pc;
        }

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }
    }
}
