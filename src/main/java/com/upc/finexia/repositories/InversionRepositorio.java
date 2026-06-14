package com.upc.finexia.repositories;

import com.upc.finexia.dtos.ReportePortafolioDTO;
import com.upc.finexia.dtos.ReporteTopPosicionesPortafolioDTO;
import com.upc.finexia.entities.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio de Inversion. Soporta HU 21-25.
@Repository
public interface InversionRepositorio extends JpaRepository<Inversion, Long> {

    // HU 24 - Listar inversiones por cuenta.
    List<Inversion> findByCuentaIdCuenta(Long idCuenta);
    void deleteByCuentaIdCuenta(Long idCuenta);

    @Query("SELECT COALESCE(SUM(inv.valorTotal), 0) " +
            "FROM Inversion inv " +
            "INNER JOIN inv.cuenta c " +
            "WHERE c.usuario.idUsuario = :usuarioId " +
            "AND inv.valorTotal > 0")
    Double valorTotalPorUsuario(@Param("usuarioId") Long usuarioId);

    // HU 25 - Visualizar portafolio: distribucion por tipo de activo.
    @Query("SELECT new com.upc.finexia.dtos.ReportePortafolioDTO(" +
            "inv.tipoActivo, " +
            "inv.categoria, " +
            "COUNT(inv.id), " +
            "SUM(inv.valorTotal), " +
            "SUM(inv.cantidad), " +
            "AVG(inv.precioCompra), " +
            "MIN(inv.fechaCompra), " +
            "MAX(inv.fechaCompra)) " +
            "FROM Inversion inv " +
            "INNER JOIN inv.cuenta c " +
            "WHERE c.usuario.idUsuario = :usuarioId " +
            "AND inv.valorTotal > 0 " +
            "GROUP BY inv.tipoActivo, inv.categoria " +
            "ORDER BY SUM(inv.valorTotal) DESC")
    List<ReportePortafolioDTO> reportePortafolio(@Param("usuarioId") Long usuarioId);

    // HU 25 - Visualizar portafolio: top posiciones (holdings) ordenadas por valor total.
    @Query("SELECT new com.upc.finexia.dtos.ReporteTopPosicionesPortafolioDTO(" +
            "inv.nombreActivo, " +
            "inv.ticker, " +
            "inv.tipoActivo, " +
            "inv.cantidad, " +
            "inv.precioCompra, " +
            "inv.valorTotal, " +
            "inv.broker, " +
            "inv.fechaCompra) " +
            "FROM Inversion inv " +
            "INNER JOIN inv.cuenta c " +
            "WHERE c.usuario.idUsuario = :usuarioId " +
            "AND inv.valorTotal > 0 " +
            "ORDER BY inv.valorTotal DESC")
    List<ReporteTopPosicionesPortafolioDTO> topPosicionesPortafolio(@Param("usuarioId") Long usuarioId);
}
