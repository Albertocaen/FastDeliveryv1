package org.proyecto.fastdeliveryp_v1.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PaypalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    private APIContext apiContext;

    @PostConstruct
    public void init() {
        this.apiContext = new APIContext(clientId, clientSecret, mode);
    }

    public Payment createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        // Formatea el total a una cadena con dos decimales y un punto decimal
        String formattedTotal = String.format(Locale.US, "%.2f", total);
        amount.setTotal(formattedTotal);

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}
