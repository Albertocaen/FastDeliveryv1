package org.proyecto.fastdeliveryp_v1.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.proyecto.fastdeliveryp_v1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/paypal")
public class PayPalController {

    private static final Logger logger = LoggerFactory.getLogger(PayPalController.class);

    @Autowired
    private PaypalService paypalService;


    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @PostMapping("/pagar")
    public RedirectView pagar(@RequestParam("total") String totalStr) {
        try {
            double total = Double.parseDouble(totalStr.replace(",", "."));
            String formattedTotal = String.format(Locale.US, "%.2f", total);

            Payment payment = paypalService.createPayment(
                    Double.parseDouble(formattedTotal),
                    "USD",
                    "paypal",
                    "sale",
                    "Descripción del pago",
                    cancelUrl,
                    successUrl
            );

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException | NumberFormatException e) {
            logger.error("Error al crear el pago de PayPal", e);
        }
        return new RedirectView("/carrito");
    }
}
