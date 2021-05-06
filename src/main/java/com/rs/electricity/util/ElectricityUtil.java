package com.rs.electricity.util;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.rs.electricity.bean.MeterBean;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ElectricityUtil {

    public static List<MeterBean> getElectricity(int count, String ip) {

        List<MeterBean> list = new ArrayList<>();
        try {
            // 设置主机TCP参数
            TcpParameters tcpParameters = new TcpParameters();
            // 设置TCP的ip地址
//            InetAddress address = InetAddress.getByName("192.168.20.133");
            InetAddress address = InetAddress.getByName(ip);
            // TCP参数设置ip地址
            // tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setHost(address);
            // TCP设置长连接
            tcpParameters.setKeepAlive(true);
            // TCP设置端口，这里设置是默认端口502
            tcpParameters.setPort(Modbus.TCP_PORT);
            // 创建一个主机
            ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);
            int slaveId = 1;//从机地址
            int offset = 1;//寄存器读取开始地址
            int quantity = 50;//读取的寄存器数量
            try {
                if (!master.isConnected()) {
                    master.connect();// 开启连接
                }

                for (int i = 1; i <= count; i++) {
                    // 读取对应从机的数据，readInputRegisters读取的写寄存器，功能码04
                    int[] registerValues = master.readHoldingRegisters(slaveId, offset + quantity * (i - 1), quantity * i);

                    // 控制台输出
//                    for (int value : registerValues) {
//                        System.out.println("Address: " + offset++ + ", Value: " + value);
//                    }

                    if (registerValues.length > 0){
                        MeterBean meterBean = new MeterBean();
                        // 获取设备Id并转换
                        String hex3 = "";
                        if (registerValues[3] != 0){
                            hex3= getHexToString(registerValues[3]);
                        }
                        String hex4= getHexToString(registerValues[4]);
                        String hex5= getHexToString(registerValues[5]);
                        String hex6= getHexToString(registerValues[6]);
                        meterBean.setDeviceId(hex3 + hex4 + hex5 + hex6);

                        if (registerValues[10] == 0 && registerValues[11] == 0 && registerValues[49] == 0){
                            meterBean.setOnline(0);
                        } else {
                            // 正向有功总电能
                            float electricity = getFloat(registerValues[10], registerValues[11]);
                            meterBean.setElectricity(String.valueOf(electricity));
                            meterBean.setOnline(1);
                        }

                        // 数据有效标识
                        meterBean.setValid(registerValues[49]);

                        list.add(meterBean);
                    } else {
                        System.out.println("registerValues: " + registerValues);
                    }

                }

                return list;

            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
//                e.printStackTrace();
                return list;
            } finally {
                try {
                    master.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHexToString(int i){
        String str = Integer.toHexString(i);
        String format = String.format("%4s", str);
        return format.replaceAll("\\s", "0");
    }

    public static float getFloat(Integer P1, Integer P2) {
        int intSign, intSignRest, intExponent, intExponentRest;
        float faResult, faDigit;
        intSign = P1 / 32768;
        intSignRest = P1 % 32768;
        intExponent = intSignRest / 128;
        intExponentRest = intSignRest % 128;
        faDigit = (float) (intExponentRest * 65536 + P2) / 8388608;
        faResult = (float) Math.pow(-1, intSign) * (float) Math.pow(2, intExponent - 127) * (faDigit + 1);
        return faResult;
    }

}
