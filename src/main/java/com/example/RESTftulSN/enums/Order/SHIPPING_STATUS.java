package com.example.RESTftulSN.enums.Order;

import com.example.RESTftulSN.util.exceptions.InvalidDataException;

public enum SHIPPING_STATUS {
    PROCESSED,SHIPPED,ARRIVED,TAKEN;
    public static SHIPPING_STATUS findByName(String name){
        for(SHIPPING_STATUS status : values()){
            if (status.name().equals(name)){
                return status;
            }
        }
        throw new InvalidDataException("Invalid Status");
    }
}
