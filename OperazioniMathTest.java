import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperazioniMathTest {
    OperazioniMath operazioniMath = new OperazioniMath();

    @Test
    void testEquazioneDueSoluzioniReali() {
        assertArrayEquals(new double[]{2.0, 1.0}, operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(1, -3, 2));
    }

    @Test
    void testEquazioneUnaSoluzioneReale() {
        assertArrayEquals(new double[]{1.0, 1.0}, operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(1, -2, 1));
    }

    @Test
    void testEquazioneNessunaSoluzioneReale() {
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(2, 1, 2));
    }

    @Test
    void testEquazioneCoeffNulli() {
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(0, 0, 0));
    }

}