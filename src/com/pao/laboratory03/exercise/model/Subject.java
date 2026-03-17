package com.pao.laboratory03.exercise.model;

public enum Subject {
    PAOJ("Programare Avansata pe Obiecte", 6),
    BD("Baze de date", 3),
    SO("Sisteme de operare", 5),
    RC("Retele si calculatoare", 4);

    private final String fullName;
    private final int credits;

    private Subject(String fullName, int credits){
        this.fullName = fullName;
        this.credits = credits;
    }

    @Override
    public String toString(){
        return name() + " (" + fullName + ", " + credits + " credite)";
    }

    public String getFullName(){
        return this.fullName;
    }
    public int getCredits(){
        return this.credits;
    }
}
