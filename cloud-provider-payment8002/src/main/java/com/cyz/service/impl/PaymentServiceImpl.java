package com.cyz.service.impl;

import com.cyz.bean.Payment;
import com.cyz.dao.PaymentDao;
import com.cyz.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cyz
 * @date 2021/7/18 0018 14:01
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentDao paymentDao;

    public int create(String serial) {
        return paymentDao.create(serial);
    }

    public Payment getPayment(Long id) {
        return paymentDao.getPayment(id);
    }
}
