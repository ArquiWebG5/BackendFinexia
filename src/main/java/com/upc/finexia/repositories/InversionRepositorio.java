package com.upc.finexia.repositories;

import com.upc.finexia.dtos.ReportePortafolioDTO;
import com.upc.finexia.dtos.ReporteTopPosicionesPortafolioDTO;
import com.upc.finexia.entities.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InversionRepositorio extends JpaRepository<Inversion, Long> {
    List<Inversion> findByCuentaIdCuenta(Long idCuenta);


    // US27: Reporte de portafolio — distribución por tipo de activo

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
            "GROUP BY inv.tipoActivo, inv.categoria " +
            "ORDER BY SUM(inv.valorTotal) DESC")
    List<ReportePortafolioDTO> reportePortafolio(@Param("usuarioId") Long usuarioId);


    // US27: Detalle de posiciones del portafolio (top holdings)

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
            "ORDER BY inv.valorTotal DESC")
    List<ReporteTopPosicionesPortafolioDTO> topPosicionesPortafolio(
            @Param("usuarioId") Long usuarioId);
}