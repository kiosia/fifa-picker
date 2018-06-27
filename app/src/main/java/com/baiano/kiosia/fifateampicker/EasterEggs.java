package com.baiano.kiosia.fifateampicker;

class EasterEggs {
    public static String getString(Team homeTeam, Team awayTeam) {
        if (("Real Madrid".equals(homeTeam.getName())) || ("Real Madrid".equals(awayTeam.getName()))) {
            return ":leo_intensifies: ROBOZAUMMMMM :leo_intensifies:";
        } else if ((homeTeam.getName() == "Mexico") && (awayTeam.getName() == "Peru")) {
            // this will never happen :(
            return "ESPECIAL 5ª SÉRIE";
        } else if ((homeTeam.getName() == "Peru") && (awayTeam.getName() == "Mexico")) {
            // this will never happen :(
            return "Foi por pouco...";
        } else if (homeTeam.getRating() == "05") {
            return "ONE STAR OPEN!!!!!!";
        } else if (("New Zealand".equals(homeTeam.getName())) && ("Canada".equals(awayTeam.getName()))) {
            return "sdds timoteo e junim";
        } else if (("Canada".equals(homeTeam.getName())) && ("New Zealand".equals(awayTeam.getName()))) {
            return "sdds junim e timoteo";
        } else if (("New Zealand".equals(homeTeam.getName())) || ("New Zealand".equals(awayTeam.getName()))) {
            return "sdds timoteo";
        } else if (("Canada".equals(homeTeam.getName())) || ("Canada".equals(awayTeam.getName()))) {
            return "sdds junim";
        } else if (("Chelsea".equals(homeTeam.getName())) || ("Chelsea".equals(awayTeam.getName()))) {
            return ":gil_rage:";
        } else if (("Barclays PL".equals(homeTeam.getLeague())) || ("Barclays PL".equals(awayTeam.getLeague()))) {
            return ":gil_speechless: PL overrated. :gil_speechless:";
        } else if (("Portugal".equals(homeTeam.getName())) || ("Portugal".equals(awayTeam.getName()))) {
            return ":leo_intensifies: ROBOZAAAAAAAUM! :leo_intensifies:";
        } else if (("Vitória".equals(homeTeam.getName())) || ("Vitória".equals(awayTeam.getName()))) {
            return "Perdi...";
        } else if (("Korea Republic".equals(homeTeam.getName())) || ("Korea Republic".equals(awayTeam.getName()))) {
            return "CAN YOU STILL FEEL THE POWER OF THE DONG?";
        } else if (("Ponte Preta".equals(homeTeam.getName())) || ("Ponte Preta".equals(awayTeam.getName()))) {
            return "ÉÉÉÉÉééééÉÈÈ`´e´´eééeééeéÉÉDSOOOOON BAAAAAASTOS, entende?";
        } else if (("Japan".equals(homeTeam.getCountry())) || ("Japan".equals(awayTeam.getCountry()))) {
            return "NANI!?";
        }
        return "";
    }
}
