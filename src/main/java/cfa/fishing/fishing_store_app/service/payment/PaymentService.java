package cfa.fishing.fishing_store_app.service.payment;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import cfa.fishing.fishing_store_app.dto.response.PaymentResponse;
import cfa.fishing.fishing_store_app.entity.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public PaymentResponse createPaymentIntent(Order order) {
        try {
            Stripe.apiKey = stripeSecretKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(order.getTotalAmount().movePointRight(2).longValue()) // Convert to cents
                    .setCurrency("eur")
                    .setDescription("Order #" + order.getId())
                    .putMetadata("orderId", order.getId().toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods
                                    .builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return PaymentResponse.builder()
                    .clientSecret(paymentIntent.getClientSecret())
                    .paymentIntentId(paymentIntent.getId())
                    .amount(order.getTotalAmount())
                    .currency("EUR")
                    .orderId(order.getId())
                    .build();
        } catch (Exception e) {
            log.error("Error creating payment intent", e);
            throw new RuntimeException("Error processing payment", e);
        }
    }
}