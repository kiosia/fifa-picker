package com.baiano.kiosia.fifateampicker;

import com.baiano.kiosia.fifateampicker.Model.Team;

class Flavor {
    private String conditionType;
    private String operator;
    private String factor1;
    private String factor2;
    private String result;

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setFactor1(String factor1) {
        this.factor1 = factor1;
    }

    public void setFactor2(String factor2) {
        this.factor2 = factor2;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String execute(Team homeTeam, Team awayTeam) {
        String factor1Value = "";
        String factor2Value = "";
        switch (conditionType) {
            case "teamName":
                factor1Value = homeTeam.getName();
                factor2Value = awayTeam.getName();
                break;
            case "teamRating":
                factor1Value = homeTeam.getRating();
                factor2Value = awayTeam.getRating();
                break;
            case "teamLeague":
                factor1Value = homeTeam.getLeague();
                factor2Value = awayTeam.getLeague();
                break;
        }
        if ("or".equals(operator)) {
            if ((factor1Value.equals(factor1)) || (factor1Value.equals(factor2)) || (factor2Value.equals(factor1)) || (factor2Value.equals(factor2))) {
                return result;
            }
        } else if ("and".equals(operator)) {
            if ((factor1Value.equals(factor1)) && (factor2Value.equals(factor2))) {
                return result;
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "{ 'easterEgg' : '"+this.result +"' }";
    }
}
