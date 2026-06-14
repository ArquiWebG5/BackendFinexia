package com.upc.finexia.services;

import com.upc.finexia.dtos.ActivoMercadoDTO;

import java.util.List;

public interface MercadoService {
    List<ActivoMercadoDTO> buscarActivos(String query, int limit);
    ActivoMercadoDTO cotizacion(String symbol);
}
