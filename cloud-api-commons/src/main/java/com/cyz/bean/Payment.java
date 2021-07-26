package com.cyz.bean;



import java.io.Serializable;

/**
 * @author cyz
 * @date 2021/7/18 0018 13:39
 */
public class Payment implements Serializable {
    private Long id;
    private String serial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Payment() {
    }

    public Payment(Long id, String serial) {
        this.id = id;
        this.serial = serial;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", serial='" + serial + '\'' +
                '}';
    }
}
