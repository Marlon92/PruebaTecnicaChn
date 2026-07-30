package com.example.prestamoschn.solicitud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class SolicitudRepository {

    private final EntityManager entityManager;

    public SolicitudRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<SolicitudPrestamo> listar() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_solicitud_listar");
        query.execute();
        return mapearLista(query.getResultList());
    }

    @SuppressWarnings("unchecked")
    public List<SolicitudPrestamo> listarPorCliente(Long clienteId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_solicitud_listar_por_cliente");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, clienteId);
        query.execute();
        return mapearLista(query.getResultList());
    }

    @SuppressWarnings("unchecked")
    public Optional<SolicitudPrestamo> obtener(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_solicitud_obtener");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, id);
        query.execute();
        return query.getResultList().stream().findFirst().map(fila -> mapearSolicitud((Object[]) fila));
    }

    public Long insertar(SolicitudPrestamo solicitud) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_solicitud_crear");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, Long.class, ParameterMode.OUT);

        query.setParameter(1, solicitud.getClienteId());
        query.setParameter(2, solicitud.getMontoSolicitado());
        query.setParameter(3, solicitud.getPlazoMeses());
        query.setParameter(4, solicitud.getMotivo());

        query.execute();

        return ((Number) query.getOutputParameterValue(5)).longValue();
    }

    public void aprobar(Long id, BigDecimal tasaInteres, BigDecimal montoTotalPagar, String comentario) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_solicitud_aprobar");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, BigDecimal.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);

        query.setParameter(1, id);
        query.setParameter(2, tasaInteres);
        query.setParameter(3, montoTotalPagar);
        query.setParameter(4, comentario);

        query.execute();
    }

    public void rechazar(Long id, String comentario) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_solicitud_rechazar");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        query.setParameter(1, id);
        query.setParameter(2, comentario);

        query.execute();
    }

    private List<SolicitudPrestamo> mapearLista(List<Object[]> filas) {
        return filas.stream().map(this::mapearSolicitud).toList();
    }

    private SolicitudPrestamo mapearSolicitud(Object[] fila) {
        SolicitudPrestamo solicitud = new SolicitudPrestamo();
        solicitud.setId(aLong(fila[0]));
        solicitud.setClienteId(aLong(fila[1]));
        solicitud.setClienteNombre((String) fila[2]);
        solicitud.setClienteApellido((String) fila[3]);
        solicitud.setMontoSolicitado((BigDecimal) fila[4]);
        solicitud.setPlazoMeses((Integer) fila[5]);
        solicitud.setMotivo((String) fila[6]);
        solicitud.setEstado(EstadoSolicitud.valueOf((String) fila[7]));
        solicitud.setFechaSolicitud(aLocalDateTime(fila[8]));
        solicitud.setTasaInteres((BigDecimal) fila[9]);
        solicitud.setMontoTotalPagar((BigDecimal) fila[10]);
        solicitud.setFechaResolucion(aLocalDateTime(fila[11]));
        solicitud.setComentarioResolucion((String) fila[12]);
        return solicitud;
    }

    private Long aLong(Object valor) {
        return valor == null ? null : ((Number) valor).longValue();
    }

    private LocalDateTime aLocalDateTime(Object valor) {
        if (valor == null) {
            return null;
        }
        return valor instanceof Timestamp fecha ? fecha.toLocalDateTime() : (LocalDateTime) valor;
    }
}
