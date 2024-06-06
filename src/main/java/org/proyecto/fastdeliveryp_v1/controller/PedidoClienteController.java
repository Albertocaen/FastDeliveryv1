package org.proyecto.fastdeliveryp_v1.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.entity.*;
import org.proyecto.fastdeliveryp_v1.service.ClienteService;
import org.proyecto.fastdeliveryp_v1.service.PedidoClienteService;
import org.proyecto.fastdeliveryp_v1.service.PaypalService;
import org.proyecto.fastdeliveryp_v1.service.ProductoService;
import org.proyecto.fastdeliveryp_v1.service.RepartidorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/pedidos")
public class PedidoClienteController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoClienteController.class);

    @Autowired
    private PedidoClienteService pedidoClienteService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PaypalService paypalService;

    @GetMapping
    public String listPedidos(Model model, Principal principal) {
        List<PedidoCliente> pedidos;
        String username = principal.getName();
        Cliente cliente = clienteService.getClienteByEmail(username);

        if (hasRole("ROLE_ADMIN")) {
            pedidos = pedidoClienteService.getAllPedidos();
        } else {
            pedidos = pedidoClienteService.getPedidosByCliente(cliente);
        }

        model.addAttribute("pedidos", pedidos);
        return "pedidos/list";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/new")
    public String showNewPedidoForm(Model model, Principal principal) {
        model.addAttribute("pedido", new PedidoCliente());
        model.addAttribute("productos", productoService.getAllProductos());

        if (hasRole("ROLE_ADMIN")) {
            model.addAttribute("clientes", clienteService.getAllClientes());
        }

        return "pedidos/new :: new";  // Devolver solo el fragmento del formulario
    }

    @PostMapping
    public String saveNewPedido(@ModelAttribute PedidoCliente pedido, Principal principal, Model model) {
        Repartidor repartidorAsignado = repartidorService.asignarRepartidorDisponible();

        if (repartidorAsignado == null) {
            model.addAttribute("message", "No hay repartidores disponibles en este momento. Por favor, inténtelo más tarde.");
            return "pedidos/new :: new";
        }

        pedido.setDniRepartidorPedido(repartidorAsignado);
        pedido.setEstado("En Curso");

        if (hasRole("ROLE_ADMIN")) {
            // El cliente se asigna a través del formulario
        } else {
            String username = principal.getName();
            Cliente cliente = clienteService.getClienteByEmail(username);
            pedido.setDniClientePedido(cliente);
        }

        pedidoClienteService.savePedido(pedido);
        model.addAttribute("pedido", new PedidoCliente());
        model.addAttribute("productos", productoService.getAllProductos());
        if (hasRole("ROLE_ADMIN")) {
            model.addAttribute("clientes", clienteService.getAllClientes());
        }
        model.addAttribute("message", "El pedido ha sido creado con éxito.");
        return "pedidos/new :: new";  // Devolver solo el fragmento del formulario actualizado
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/estado/{id}")
    public String changeEstadoForm(@PathVariable Integer id, Model model) {
        PedidoCliente pedido = pedidoClienteService.getPedidoById(id);
        model.addAttribute("pedido", pedido);
        return "pedidos/estado :: estado";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/estado")
    public String changeEstado(@ModelAttribute PedidoCliente pedido, Model model) {
        pedidoClienteService.updatePedidoEstado(pedido.getId(), pedido.getEstado());
        messagingTemplate.convertAndSend("/topic/notifications", "El estado del pedido con ID " + pedido.getId() + " ha sido actualizado a " + pedido.getEstado());
        model.addAttribute("pedido", pedido);  // Añadido para devolver el objeto actualizado
        model.addAttribute("message", "El estado del pedido ha sido actualizado con éxito.");
        return "pedidos/estado :: estado";
    }

    @GetMapping("/success")
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             @CookieValue(value = "carrito", defaultValue = "") String carritoCookie,
                             Principal principal,
                             HttpServletResponse response,
                             Model model) {
        logger.info("Entrando en el método successPay");
        logger.info("paymentId: {}, payerId: {}", paymentId, payerId);

        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                logger.info("Pago aprobado");

                // Obtener el cliente actual
                String username = principal.getName();
                Cliente cliente = clienteService.getClienteByEmail(username);
                if (cliente == null) {
                    logger.error("Cliente no encontrado para el email: {}", username);
                    return "redirect:/";
                }
                logger.info("Cliente obtenido: {}", cliente);

                // Asignar un repartidor disponible
                Repartidor repartidorAsignado = repartidorService.asignarRepartidorDisponible();
                if (repartidorAsignado == null) {
                    logger.error("No hay repartidor disponible.");
                    return "redirect:/";
                }
                logger.info("Repartidor asignado: {}", repartidorAsignado);

                // Obtener carrito desde la cookie
                List<CarritoItem> carrito = productoService.obtenerCarritoDesdeCookie(carritoCookie);
                if (carrito.isEmpty()) {
                    logger.error("El carrito está vacío.");
                    return "redirect:/";
                }
                logger.info("Carrito items: {}", carrito);

                // Crear pedidos para cada item del carrito
                for (CarritoItem item : carrito) {
                    PedidoCliente pedido = new PedidoCliente();
                    pedido.setProducto(item.getProducto());
                    pedido.setDniRepartidorPedido(repartidorAsignado);
                    pedido.setEstado("En Curso");
                    pedido.setFechaPedido(LocalDate.now());
                    pedido.setCantidad(item.getCantidad());
                    pedido.setDniClientePedido(cliente);
                    logger.info("Pedido a guardar: {}", pedido);

                    // Guardar el pedido
                    PedidoCliente savedPedido = pedidoClienteService.savePedido(pedido);
                    logger.info("Pedido guardado: {}", savedPedido);
                }

                // invalidar la cookie para borrar el pedido del carrito
                Cookie cookie = new Cookie("carrito", null);
                cookie.setPath("/");
                cookie.setMaxAge(0); // Set the cookie to expire immediately
                response.addCookie(cookie);

                // Generar número de orden
                String orderNumber = generarNumeroOrden();
                model.addAttribute("orderNumber", orderNumber);
                model.addAttribute("paymentId", paymentId);
                model.addAttribute("token", "");
                model.addAttribute("payerId", payerId);

                logger.info("Pedido creado exitosamente");
                return "pedidos/success";
            }
        } catch (PayPalRESTException e) {
            logger.error("Error al ejecutar el pago: ", e);
        }
        return "redirect:/";
    }


    @GetMapping("/cancel")
    public String cancelPay() {
        logger.info("Pago cancelado por el usuario");
        return "pedidos/cancel";
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    private String generarNumeroOrden() {
        return UUID.randomUUID().toString();
    }
}
