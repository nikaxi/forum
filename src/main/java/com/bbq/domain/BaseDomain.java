package com.bbq.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class BaseDomain implements Serializable{

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public static void main(String[] args) {
        BaseDomain baseDomain = new BaseDomain();
        System.out.println(baseDomain);
    }

}



