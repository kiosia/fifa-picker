package com.baiano.kiosia.fifateampicker.Model;

public class Flavor {
    private final int id;
    private String conditionType;
    private String operator;
    private String factor1;
    private String factor2;
    private String result;

    public Flavor(int id, String conditionType, String operator, String factor1, String factor2, String result) {
        this.id = id;
        this.conditionType = conditionType;
        this.operator = operator;
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFactor1() {
        return factor1;
    }

    public void setFactor1(String factor1) {
        this.factor1 = factor1;
    }

    public String getFactor2() {
        return factor2;
    }

    public void setFactor2(String factor2) {
        this.factor2 = factor2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
