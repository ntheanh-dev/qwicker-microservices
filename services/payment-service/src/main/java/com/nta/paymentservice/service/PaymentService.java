package com.nta.paymentservice.service;

import com.nta.paymentservice.components.VNPayHelper;
import com.nta.paymentservice.configuration.VNPayConfig;
import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.dto.response.VNPayResponse;
import com.nta.paymentservice.entity.Payment;
import com.nta.paymentservice.enums.ErrorCode;
import com.nta.paymentservice.exception.AppException;
import com.nta.paymentservice.mapper.PaymentMapper;
import com.nta.paymentservice.repository.PaymentRepository;
import com.nta.paymentservice.repository.internal.PostClient;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
  PaymentRepository paymentRepository;
  PaymentMapper paymentMapper;
  PostClient postClient;
  VNPayConfig vnPayConfig;
  VNPayHelper vnPayHelper;

  public Payment createPayment(final PaymentCreationRequest payment) {
    return paymentRepository.save(paymentMapper.toPayment(payment));
  }

  public Payment findByPostId(final String postId) {
    return paymentRepository
        .findByPostId(postId)
        .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
  }

  public List<Payment> findByPostIds(final List<String> postIds) {
    return paymentRepository.findByPostIds(postIds);
  }

  public void collectCash(final String postId, final String shipperId) {
    final boolean isVerify = postClient.isExistByShipperIdAndPostId(shipperId, postId).getResult();
    if (!isVerify) {
      throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
    }
    final Payment payment = findByPostId(postId);
    payment.setPaidAt(LocalDateTime.now());
    paymentRepository.save(payment);
  }

  public VNPayResponse createVnPayPayment(final HttpServletRequest request) {
    final String postId = request.getParameter("orderInfo"); // postID
    //        if (!paymentRepository.existsByPostId(postId)) {
    //            throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
    //        }
    final long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
    final String bankCode = request.getParameter("bankCode");

    final Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
    vnpParamsMap.put("vnp_OrderInfo", postId);
    vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

    if (bankCode != null && !bankCode.isEmpty()) {
      vnpParamsMap.put("vnp_BankCode", bankCode);
    }

    final String clientIp = vnPayHelper.getIpAddress(request);
    if (clientIp != null && !clientIp.isEmpty()) {
      vnpParamsMap.put("vnp_IpAddr", clientIp);
    }
    vnpParamsMap.put("vnp_ReturnUrl", vnPayHelper.getPaymentReturnURL());
    // build query url
    String queryUrl = vnPayHelper.getPaymentURL(vnpParamsMap, true);
    final String hashData = vnPayHelper.getPaymentURL(vnpParamsMap, false);
    final String vnpSecureHash = vnPayHelper.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
    queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
    final String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    return VNPayResponse.builder().paymentUrl(paymentUrl).build();
  }
}
