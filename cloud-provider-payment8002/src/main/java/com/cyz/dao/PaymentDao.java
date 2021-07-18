package com.cyz.dao;

import com.cyz.bean.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author cyz
 * @date 2021/7/18 0018 13:45
 */
@Repository
@Mapper
public interface PaymentDao {

    int create(String serial);

    Payment getPayment(@Param("id") Long id);
}
