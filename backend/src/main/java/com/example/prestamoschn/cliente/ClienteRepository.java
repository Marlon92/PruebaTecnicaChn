package com.example.prestamoschn.cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {

    private final EntityManager entityManager;

    public ClienteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<Cliente> listar() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_cliente_listar");
        query.execute();
        List<Object[]> filas = query.getResultList();
        return filas.stream().map(this::mapearCliente).toList();
    }

    @SuppressWarnings("unchecked")
    public Optional<Cliente> obtener(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_cliente_obtener");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, id);
        query.execute();
        List<Object[]> filas = query.getResultList();
        return filas.stream().findFirst().map(this::mapearCliente);
    }

    @SuppressWarnings("unchecked")
    public Optional<Cliente> buscarPorDpi(String dpi) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_cliente_buscar_por_identificacion");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.setParameter(1, dpi);
        query.execute();
        List<Object[]> filas = query.getResultList();
        return filas.stream().findFirst().map(this::mapearCliente);
    }

    public Long insertar(Cliente cliente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_cliente_crear");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(8, Long.class, ParameterMode.OUT);

        query.setParameter(1, cliente.getNombre());
        query.setParameter(2, cliente.getApellido());
        query.setParameter(3, cliente.getNumeroIdentificacion());
        query.setParameter(4, cliente.getFechaNacimiento());
        query.setParameter(5, cliente.getDireccion());
        query.setParameter(6, cliente.getCorreo());
        query.setParameter(7, cliente.getTelefono());

        query.execute();

        return aLong(query.getOutputParameterValue(8));
    }

    public void actualizar(Cliente cliente) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_cliente_actualizar");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);

        query.setParameter(1, cliente.getId());
        query.setParameter(2, cliente.getNombre());
        query.setParameter(3, cliente.getApellido());
        query.setParameter(4, cliente.getNumeroIdentificacion());
        query.setParameter(5, cliente.getFechaNacimiento());
        query.setParameter(6, cliente.getDireccion());
        query.setParameter(7, cliente.getCorreo());
        query.setParameter(8, cliente.getTelefono());

        query.execute();
    }

    public void eliminar(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_cliente_eliminar");
        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        query.setParameter(1, id);
        query.execute();
    }

    private Cliente mapearCliente(Object[] fila) {
        Cliente cliente = new Cliente();
        cliente.setId(aLong(fila[0]));
        cliente.setNombre((String) fila[1]);
        cliente.setApellido((String) fila[2]);
        cliente.setNumeroIdentificacion((String) fila[3]);
        cliente.setFechaNacimiento(aLocalDate(fila[4]));
        cliente.setDireccion((String) fila[5]);
        cliente.setCorreo((String) fila[6]);
        cliente.setTelefono((String) fila[7]);
        cliente.setFechaRegistro(aLocalDateTime(fila[8]));
        return cliente;
    }

    private Long aLong(Object valor) {
        return valor == null ? null : ((Number) valor).longValue();
    }

    private LocalDate aLocalDate(Object valor) {
        if (valor == null) {
            return null;
        }
        return valor instanceof Date fecha ? fecha.toLocalDate() : (LocalDate) valor;
    }

    private LocalDateTime aLocalDateTime(Object valor) {
        if (valor == null) {
            return null;
        }
        return valor instanceof Timestamp fecha ? fecha.toLocalDateTime() : (LocalDateTime) valor;
    }
}

