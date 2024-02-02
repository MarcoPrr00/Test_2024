import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class OperazioniMathTest {
    OperazioniMath operazioniMath = new OperazioniMath();


    //TEST METODO convertiBase()
    @Test
    void testConversioneBinaria() {
        assertEquals("1110", operazioniMath.convertiBase(14, 2));
    }

    @Test
    void testConversioneOttale() {
        assertEquals("33", operazioniMath.convertiBase(27, 8));
    }

    @Test
    void testConversioneEsadecimale() {
        assertEquals("FF", operazioniMath.convertiBase(255, 16));
    }

    @ParameterizedTest
    @ValueSource (ints={2,8,16})
    void testNumeroDecimaleZeroBaseCorretta(int base) {
        assertEquals("", operazioniMath.convertiBase(0, base));
    }

    @Test
    public void testBaseNonSupportata() {
        assertEquals("Base di destinazione non supportata", operazioniMath.convertiBase(10, 10));
    }

    @Test
    public void testBaseNonSupportataAndNumeroDecimaleZero() {
        assertEquals("Base di destinazione non supportata", operazioniMath.convertiBase(0, 10));
    }
    @ParameterizedTest
    @ValueSource (ints={2,8,16})
    void testNumeroDecimaleUnoAndBaseCorretta(int base) {
        assertEquals("1", operazioniMath.convertiBase(1, base));
    }


    //TEST METODO calcolaSoluzioniEquazioneSecondoGrado()
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
    public void testCoefficienteAUgualeZero() {
        assertNull(operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(0, 10, 20));
    }
    @Test
    public void testCoefficienteAUgualeUno() {
        assertArrayEquals(new double[]{3.0, 2.0},operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(1, -5, 6));
    }

    //Test aggiuntivi
    @Test
    public void testNumeroDecimaleNegativo(){
        assertEquals("",operazioniMath.convertiBase(-1,2));
    }
    @Test
    public void testMaxNumeroDecimiale(){
        assertEquals("1111111111111111111111111111111",operazioniMath.convertiBase(Integer.MAX_VALUE,2));
    }
    @Test
    public void testMinNumeroDecimale(){
        assertEquals("",operazioniMath.convertiBase(Integer.MIN_VALUE,2));
    }
    @Test
    public void testSoluzioniZero() {
        assertArrayEquals(new double[] { 0.0, 0.0 }, operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(1, 0, 0),0.0001);
    }

    @Test
    public void testDeltaQuadratoPerfetto() {
        assertArrayEquals(new double[] { 1.0, -1.0 }, operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(1, 0, -1));
    }

    @Test
    public void testCoefficientiZero(){
        assertNull(operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(0,0,0));
    }
    @Test
    public void testCoefficientiMaxMin(){
        assertArrayEquals(new double[]{0.6180339887498949,-1.618033988749895},operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(1000000000,1000000000,-1000000000));
    }

    @Test
    public void testCoefficientiSuperioriInferioriSogliaMaxSogliaMin()
    {
        assertThrows(ArithmeticException.class,() ->{
        operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(-1000000001,1000000001,1000000001);
    });
    }

}