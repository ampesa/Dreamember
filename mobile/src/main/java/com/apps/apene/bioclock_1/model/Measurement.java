package com.apps.apene.bioclock_1.model;

import java.time.Instant;

/*
 * La clase Measurement modela los datos de las medidas que recogemos con el sensor de pulso.
 * Básicamente contempla dos daos, las pulsaciones por minuto bpm (beats per minute) y el momento en que se
 * registran dichas pulsaciones Instant**/
public class Measurement {

    private Instant instant;
    private int bpm;

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "instant=" + instant +
                ", bpm=" + bpm +
                '}';
    }
}
