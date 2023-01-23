package com.codestates.culinari.payment.controller;

import com.codestates.culinari.config.security.dto.CustomPrincipal;
import com.codestates.culinari.global.exception.BusinessLogicException;
import com.codestates.culinari.global.exception.ExceptionCode;
import com.codestates.culinari.payment.dto.request.PaymentRequest;
import com.codestates.culinari.payment.dto.request.RefundRequest;
import com.codestates.culinari.payment.dto.response.PaymentFailResponse;
import com.codestates.culinari.payment.dto.response.PaymentInfoResponse;
import com.codestates.culinari.payment.dto.response.PaymentSuccessResponse;
import com.codestates.culinari.payment.service.PaymentService;
import com.codestates.culinari.response.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity postPayment(
            @Valid @RequestBody PaymentRequest request,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        PaymentInfoResponse paymentInfo = PaymentInfoResponse.from(paymentService.createPayment(request, principal));

        return new ResponseEntity<>(
                new SingleResponseDto<>(paymentInfo),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/success")
    public ResponseEntity requestPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam BigDecimal amount
    ) {
        paymentService.verifyRequest(paymentKey, orderId, amount);
        try {
            PaymentSuccessResponse response = paymentService.requestApprovalPayment(paymentKey, orderId, amount);

            return new ResponseEntity<>(
                    new SingleResponseDto<>(response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.TOSS_REQUEST_FAIL);
        }
    }

    @GetMapping("/fail")
    public ResponseEntity requestPaymentFail(
        @RequestParam(name = "code") String errorCode,
        @RequestParam(name = "message") String errorMsg,
        @RequestParam String orderId
    ) {
        PaymentFailResponse paymentFailResponse = paymentService.handleRequestFail(errorCode, errorMsg, orderId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(paymentFailResponse),
                HttpStatus.OK
        );
    }

    @PostMapping("/cancel")
    public ResponseEntity requestPaymentCancel(
            @RequestBody RefundRequest request,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        try {
            PaymentSuccessResponse response = paymentService.requestPaymentCancel(request, principal);

            return new ResponseEntity<>(
                    new SingleResponseDto<>(response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.TOSS_REQUEST_FAIL);
        }
    }
}