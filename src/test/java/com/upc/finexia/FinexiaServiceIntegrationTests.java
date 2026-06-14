package com.upc.finexia;

import com.upc.finexia.dtos.CuentaDTO;
import com.upc.finexia.dtos.EgresoDTO;
import com.upc.finexia.dtos.IngresoDTO;
import com.upc.finexia.dtos.InversionDTO;
import com.upc.finexia.dtos.ReporteAnalisisAhorroDTO;
import com.upc.finexia.dtos.ReporteGastosPorCategoriaDTO;
import com.upc.finexia.dtos.ReporteIngresosRecurrentesDTO;
import com.upc.finexia.dtos.ReportePatrimonioNetoDTO;
import com.upc.finexia.dtos.ReporteResumenFinancieroDTO;
import com.upc.finexia.dtos.VentaActivoDTO;
import com.upc.finexia.entities.Cuenta;
import com.upc.finexia.entities.Inversion;
import com.upc.finexia.entities.Meta;
import com.upc.finexia.entities.Notificacion;
import com.upc.finexia.entities.Portafolio;
import com.upc.finexia.entities.Usuario;
import com.upc.finexia.repositories.CuentaRepositorio;
import com.upc.finexia.repositories.EgresoRepositorio;
import com.upc.finexia.repositories.IngresoRepositorio;
import com.upc.finexia.repositories.InversionRepositorio;
import com.upc.finexia.repositories.MetaRepositorio;
import com.upc.finexia.repositories.NotificacionRepositorio;
import com.upc.finexia.repositories.PortafolioRepositorio;
import com.upc.finexia.repositories.UsuarioRepositorio;
import com.upc.finexia.repositories.VentaActivoRepositorio;
import com.upc.finexia.security.entities.Role;
import com.upc.finexia.security.entities.User;
import com.upc.finexia.security.repositories.RoleRepository;
import com.upc.finexia.security.repositories.UserRepository;
import com.upc.finexia.services.CuentaService;
import com.upc.finexia.services.EgresoService;
import com.upc.finexia.services.IngresoService;
import com.upc.finexia.services.InversionService;
import com.upc.finexia.services.UsuarioService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FinexiaServiceIntegrationTests {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private IngresoRepositorio ingresoRepositorio;

    @Autowired
    private EgresoRepositorio egresoRepositorio;

    @Autowired
    private InversionRepositorio inversionRepositorio;

    @Autowired
    private MetaRepositorio metaRepositorio;

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    @Autowired
    private PortafolioRepositorio portafolioRepositorio;

    @Autowired
    private VentaActivoRepositorio ventaActivoRepositorio;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private IngresoService ingresoService;

    @Autowired
    private EgresoService egresoService;

    @Autowired
    private InversionService inversionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void ingresosActualizanSaldoActualAlCrearEditarYEliminar() {
        Usuario usuario = crearUsuario("ingresos");
        Long cuentaId = crearCuenta(usuario.getIdUsuario(), 1000.0).getIdCuenta();

        IngresoDTO ingreso = ingresoService.insertar(ingresoDTO(cuentaId, 250.0, LocalDate.of(2026, 1, 5), "Salario"));
        assertSaldo(cuentaId, 1250.0);

        ingresoService.actualizar(
                ingreso.getId(),
                ingresoDTO(cuentaId, 400.0, LocalDate.of(2026, 1, 6), "Salario")
        );
        assertSaldo(cuentaId, 1400.0);

        ingresoService.eliminar(ingreso.getId());
        assertSaldo(cuentaId, 1000.0);
    }

    @Test
    void egresosActualizanSaldoActualAlCrearEditarYEliminar() {
        Usuario usuario = crearUsuario("egresos");
        Long cuentaId = crearCuenta(usuario.getIdUsuario(), 1000.0).getIdCuenta();

        EgresoDTO egreso = egresoService.insertar(egresoDTO(cuentaId, 150.0, LocalDate.of(2026, 1, 7), "Hogar"));
        assertSaldo(cuentaId, 850.0);

        egresoService.actualizar(
                egreso.getId(),
                egresoDTO(cuentaId, 220.0, LocalDate.of(2026, 1, 8), "Hogar")
        );
        assertSaldo(cuentaId, 780.0);

        egresoService.eliminar(egreso.getId());
        assertSaldo(cuentaId, 1000.0);
    }

    @Test
    void eliminarUsuarioBorraDependenciasSinEliminarRoles() {
        Usuario usuario = crearUsuario("delete");
        Cuenta cuenta = crearCuenta(usuario.getIdUsuario(), 500.0);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role nuevoRol = new Role();
            nuevoRol.setName("ROLE_USER");
            return roleRepository.save(nuevoRol);
        });

        User user = new User();
        user.setUsername("delete-" + System.nanoTime());
        user.setPassword("password");
        user.setUsuario(usuario);
        user.setRoles(new HashSet<>(Set.of(role)));
        userRepository.save(user);

        crearDependencias(usuario, cuenta);
        entityManager.flush();
        entityManager.clear();

        usuarioService.eliminar(usuario.getIdUsuario());
        entityManager.flush();
        entityManager.clear();

        assertThat(usuarioRepositorio.findById(usuario.getIdUsuario())).isEmpty();
        assertThat(userRepository.findByUsername(user.getUsername())).isEmpty();
        assertThat(roleRepository.findByName("ROLE_USER")).isPresent();
        assertThat(cuentaRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())).isEmpty();
        assertThat(ingresoRepositorio.findByCuentaIdCuenta(cuenta.getIdCuenta())).isEmpty();
        assertThat(egresoRepositorio.findByCuentaIdCuenta(cuenta.getIdCuenta())).isEmpty();
        assertThat(inversionRepositorio.findByCuentaIdCuenta(cuenta.getIdCuenta())).isEmpty();
        assertThat(metaRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())).isEmpty();
        assertThat(notificacionRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())).isEmpty();
        assertThat(portafolioRepositorio.findByUsuarioIdUsuario(usuario.getIdUsuario())).isEmpty();
    }

    @Test
    void reportesNativosMapeanDtosYRespetanFechasOpcionales() {
        Usuario usuario = crearUsuario("reportes");
        Long cuentaId = crearCuenta(usuario.getIdUsuario(), 0.0).getIdCuenta();

        ingresoService.insertar(ingresoDTO(cuentaId, 1000.0, LocalDate.of(2026, 1, 5), "Salario"));
        ingresoService.insertar(ingresoDTO(cuentaId, 1200.0, LocalDate.of(2026, 2, 5), "Salario"));
        egresoService.insertar(egresoDTO(cuentaId, 300.0, LocalDate.of(2026, 1, 10), "Hogar"));
        egresoService.insertar(egresoDTO(cuentaId, 200.0, LocalDate.of(2026, 2, 10), "Hogar"));
        entityManager.flush();
        entityManager.clear();

        List<ReporteIngresosRecurrentesDTO> recurrentes = ingresoService.ingresosRecurrentes(cuentaId, 2);
        assertThat(recurrentes).hasSize(1);
        assertThat(recurrentes.get(0).getCategoria()).isEqualTo("Salario");
        assertThat(recurrentes.get(0).getMesesPresentes()).isEqualTo(2L);
        assertThat(recurrentes.get(0).getMontoPromedio()).isEqualTo(1100.0);

        List<ReporteAnalisisAhorroDTO> ahorro = ingresoService.analisisAhorro(cuentaId);
        assertThat(ahorro).extracting(ReporteAnalisisAhorroDTO::getMes)
                .contains("2026-01", "2026-02");

        List<ReporteResumenFinancieroDTO> resumenEnero = cuentaService.resumenFinanciero(
                usuario.getIdUsuario(),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );
        assertThat(resumenEnero).hasSize(1);
        assertThat(resumenEnero.get(0).getTotalIngresos()).isEqualTo(1000.0);
        assertThat(resumenEnero.get(0).getTotalEgresos()).isEqualTo(300.0);
        assertThat(resumenEnero.get(0).getBalanceNeto()).isEqualTo(700.0);

        List<ReporteGastosPorCategoriaDTO> gastosSinFiltro = egresoService.gastosPorCategoria(cuentaId, null, null);
        assertThat(gastosSinFiltro).hasSize(1);
        assertThat(gastosSinFiltro.get(0).getTotal()).isEqualTo(500.0);
    }

    @Test
    void ventaActivoReducePosicionYRegistraResultado() {
        Usuario usuario = crearUsuario("venta");
        Long cuentaId = crearCuenta(usuario.getIdUsuario(), 1000.0).getIdCuenta();

        InversionDTO inversion = inversionDTO(cuentaId, "AAPL", 10.0, 10.0);
        InversionDTO guardada = inversionService.insertar(inversion);

        VentaActivoDTO venta = new VentaActivoDTO();
        venta.setCantidadVendida(4.0);
        venta.setPrecioVenta(15.0);
        venta.setFechaVenta(LocalDate.of(2026, 4, 10));
        venta.setNota("Venta parcial");

        VentaActivoDTO registrada = inversionService.venderActivo(guardada.getId(), venta);
        entityManager.flush();
        entityManager.clear();

        Inversion posicion = inversionRepositorio.findById(guardada.getId()).orElseThrow();
        assertThat(posicion.getCantidad()).isEqualTo(6.0);
        assertThat(posicion.getValorTotal()).isEqualTo(60.0);
        assertThat(registrada.getMontoTotal()).isEqualTo(60.0);
        assertThat(registrada.getGananciaPerdida()).isEqualTo(20.0);
        assertThat(ventaActivoRepositorio.findByCuentaIdCuenta(cuentaId)).hasSize(1);
    }

    @Test
    void patrimonioNetoSumaSaldosBancariosEInversiones() {
        Usuario usuario = crearUsuario("patrimonio");
        Long cuentaId = crearCuenta(usuario.getIdUsuario(), 1000.0).getIdCuenta();
        inversionService.insertar(inversionDTO(cuentaId, "VOO", 5.0, 50.0));
        entityManager.flush();
        entityManager.clear();

        ReportePatrimonioNetoDTO reporte = cuentaService.patrimonioNeto(usuario.getIdUsuario());

        assertThat(reporte.getTotalCuentas()).isEqualTo(1000.0);
        assertThat(reporte.getTotalInversiones()).isEqualTo(250.0);
        assertThat(reporte.getPatrimonioNeto()).isEqualTo(1250.0);
        assertThat(reporte.getMonedaBase()).isEqualTo("PEN");
    }

    private Usuario crearUsuario(String suffix) {
        String unique = suffix + "-" + System.nanoTime();
        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setApellido("Finexia");
        usuario.setCorreo(unique + "@finexia.test");
        usuario.setPlan("TEST");
        usuario.setIdioma("es");
        usuario.setMonedaPreferida("PEN");
        usuario.setTemaUi("light");
        usuario.setFechaRegistro(LocalDate.now());
        return usuarioRepositorio.save(usuario);
    }

    private Cuenta crearCuenta(Long usuarioId, double saldoInicial) {
        CuentaDTO dto = new CuentaDTO();
        dto.setUsuarioId(usuarioId);
        dto.setNombreCuenta("Cuenta test");
        dto.setBancoNombre("Banco test");
        dto.setTipoCuenta("AHORRO");
        dto.setMoneda("PEN");
        dto.setSaldoInicial(saldoInicial);
        cuentaService.insertar(dto);
        return cuentaRepositorio.findByUsuarioIdUsuario(usuarioId).get(0);
    }

    private IngresoDTO ingresoDTO(Long cuentaId, double monto, LocalDate fecha, String categoria) {
        IngresoDTO dto = new IngresoDTO();
        dto.setCuentaId(cuentaId);
        dto.setMonto(monto);
        dto.setFecha(fecha);
        dto.setCategoria(categoria);
        dto.setNota("Ingreso test");
        return dto;
    }

    private EgresoDTO egresoDTO(Long cuentaId, double monto, LocalDate fecha, String categoria) {
        EgresoDTO dto = new EgresoDTO();
        dto.setCuentaId(cuentaId);
        dto.setMonto(monto);
        dto.setFecha(fecha);
        dto.setCategoria(categoria);
        dto.setNota("Egreso test");
        return dto;
    }

    private InversionDTO inversionDTO(Long cuentaId, String ticker, double cantidad, double precioCompra) {
        InversionDTO dto = new InversionDTO();
        dto.setCuentaId(cuentaId);
        dto.setNombreActivo("Activo " + ticker);
        dto.setTicker(ticker);
        dto.setTipoActivo("ACCION");
        dto.setBroker("Broker test");
        dto.setPrecioCompra(precioCompra);
        dto.setCantidad(cantidad);
        dto.setFechaCompra(LocalDate.of(2026, 4, 1));
        dto.setValorTotal(cantidad * precioCompra);
        dto.setCategoria("Renta variable");
        return dto;
    }

    private void assertSaldo(Long cuentaId, double saldoEsperado) {
        entityManager.flush();
        entityManager.clear();
        Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow();
        assertThat(cuenta.getSaldoActual()).isEqualTo(saldoEsperado);
    }

    private void crearDependencias(Usuario usuario, Cuenta cuenta) {
        ingresoService.insertar(ingresoDTO(cuenta.getIdCuenta(), 100.0, LocalDate.of(2026, 3, 1), "Salario"));
        egresoService.insertar(egresoDTO(cuenta.getIdCuenta(), 25.0, LocalDate.of(2026, 3, 2), "Hogar"));

        Inversion inversion = new Inversion();
        inversion.setCuenta(cuenta);
        inversion.setNombreActivo("ETF Test");
        inversion.setTipoActivo("ETF");
        inversion.setValorTotal(200.0);
        inversionRepositorio.save(inversion);

        Meta meta = new Meta();
        meta.setUsuario(usuario);
        meta.setNombre("Meta test");
        meta.setMontoObjetivo(1000.0);
        meta.setCreadoEn(LocalDate.now());
        metaRepositorio.save(meta);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo("Aviso");
        notificacion.setMensaje("Mensaje test");
        notificacion.setLeido(false);
        notificacion.setCreadoEn(LocalDate.now());
        notificacionRepositorio.save(notificacion);

        Portafolio portafolio = new Portafolio();
        portafolio.setUsuario(usuario);
        portafolio.setValorTotal(200.0);
        portafolioRepositorio.save(portafolio);
    }
}
