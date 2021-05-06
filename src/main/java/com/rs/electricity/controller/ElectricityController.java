package com.rs.electricity.controller;

import com.rs.electricity.bean.MeterBean;
import com.rs.electricity.bean.ResultVo;
import com.rs.electricity.util.ElectricityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/electricity")
public class ElectricityController {

    @RequestMapping(value = "/getElectricity", method = RequestMethod.GET)
    public ResultVo getElectricity(@RequestParam(value = "count") int count, @RequestParam(value = "ip") String ip) {

        if (count <= 0) {
            return new ResultVo(ResultVo.CODE_GET_ELECTRICITY_ERROR, "数量不能为空");
        }

        if (ip.isEmpty()) {
            return new ResultVo(ResultVo.CODE_GET_ELECTRICITY_ERROR, "ip地址不能为空");
        }

        List<MeterBean> electricityList = ElectricityUtil.getElectricity(count, ip);
        if (electricityList != null && electricityList.size() > 0) {
            return new ResultVo(electricityList);
        }

        return new ResultVo(ResultVo.CODE_GET_ELECTRICITY_ERROR, ResultVo.MSG_ERROR);
    }
}
