package com.nta.paymentservice.configuration;

import com.nta.paymentservice.components.VNPayHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class VNPayConfig {
    private final VNPayHelper vnPayHelper;
    @Getter
    @Value("${application.payment.vnPay.url}")
    private String vnp_PayUrl;
    @Value("${application.payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${application.payment.vnPay.tmnCode}")
    private String vnp_TmnCode;
    @Getter
    @Value("${application.payment.vnPay.secretKey}")
    private String secretKey;
    @Value("${application.payment.vnPay.version}")
    private String vnp_Version;
    @Value("${application.payment.vnPay.command}")
    private String vnp_Command;
    @Value("${application.payment.vnPay.orderType}")
    private String orderType;

    public Map<String, String> getVNPayConfig() {
        final Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef", vnPayHelper.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");

        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnpParamsMap.put("vnp_CreateDate", formatter.format(calendar.getTime()));

        calendar.add(Calendar.MINUTE, 15);
        vnpParamsMap.put("vnp_ExpireDate", formatter.format(calendar.getTime()));
        return vnpParamsMap;
    }
}
