package com.nta.paymentservice.service;

import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.entity.Payment;
import com.nta.paymentservice.enums.ErrorCode;
import com.nta.paymentservice.exception.AppException;
import com.nta.paymentservice.mapper.PaymentMapper;
import com.nta.paymentservice.repository.PaymentRepository;
import com.nta.paymentservice.repository.internal.PostClient;
import java.time.LocalDateTime;
import java.util.List;
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
    if(!isVerify) {
      throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
    }
    final Payment payment = findByPostId(postId);
    payment.setPaidAt(LocalDateTime.now());
    paymentRepository.save(payment);
  }
}
