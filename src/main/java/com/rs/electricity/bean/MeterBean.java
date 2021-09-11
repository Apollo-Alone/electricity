package com.rs.electricity.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MeterBean {

    private String deviceId;

    private String electricity;

    private Integer valid;// 1:有效  0：无效

    private Integer online;// 1:在线  0：离线

}
