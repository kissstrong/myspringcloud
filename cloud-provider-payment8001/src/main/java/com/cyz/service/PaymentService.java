package com.cyz.service;

import com.cyz.bean.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:01
 */
public interface PaymentService {
    int create(String serial);

    Payment getPayment(@Param("id")Long id);
}
