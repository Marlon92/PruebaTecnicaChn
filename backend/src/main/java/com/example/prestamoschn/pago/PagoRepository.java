package com.example.prestamoschn.pago;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PagoRepository {

    private final EntityManager entityManager;

    public PagoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Pago> listarPorSolicitud(Long solicitudId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_pago_listar_por_solicitud");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, solicitudId);
        query.execute();
        List<Object[]> filas = query.getResultList();
        return filas.stream().map(this::mapearPago).toList();
    }

    public Long insertar(Pago pago) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_pago_crear");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(5, Timestamp.class, ParameterMode.OUT);

        query.setParameter(1, pago.getSolicitudId());
        query.setParameter(2, pago.getMonto());
        query.setParameter(3, pago.getObservacion());
        query.execute();

        pago.setFechaPago(aLocalDateTime(query.getOutputParameterValue(5)));

        return ((Number) query.getOutputParameterValue(4)).longValue();
    }

    public BigDecimal totalPagadoPorSolicitud(Long solicitudId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_pago_total_por_solicitud");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, BigDecimal.class, ParameterMode.OUT);
        query.setParameter(1, solicitudId);

        query.execute();

        return (BigDecimal) query.getOutputParameterValue(2);
    }

    private Pago mapearPago(Object[] fila) {
        Pago pago = new Pago();
        pago.setId(((Number) fila[0]).longValue());
        pago.setSolicitudId(((Number) fila[1]).longValue());
        pago.setMonto((BigDecimal) fila[2]);
        pago.setFechaPago(aLocalDateTime(fila[3]));
        pago.setObservacion((String) fila[4]);
        return pago;
    }

    private LocalDateTime aLocalDateTime(Object valor) {
        if (valor == null) {
            return null;
        }
        return valor instanceof Timestamp fecha ? fecha.toLocalDateTime() : (LocalDateTime) valor;
    }
}

