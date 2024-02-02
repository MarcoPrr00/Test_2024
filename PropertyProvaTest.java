import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Positive;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyProvaTest {
    OperazioniMath operazioniMath = new OperazioniMath();

    @Provide
    Arbitrary<Integer> baseDestinazione() {
        return Arbitraries.of(2, 8, 16);
    }

        @Property
        void testConvertiBase(@ForAll @IntRange(min=1, max=Integer.MAX_VALUE) int numeroDecimale, @ForAll @From ("baseDestinazione") int baseDestinazione) {
            // Assicurati che il risultato della conversione contenga solo caratteri validi per la base specifica

            String risultatoMetodo = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);

            assertTrue(verificaCaratteriValidi(risultatoMetodo, baseDestinazione));
        }

    boolean verificaCaratteriValidi(String risultato, int baseDestinazione) {
        // Implementa la logica per verificare che i caratteri nel risultato siano validi per la base specifica
        // Ad esempio, per la base binaria (base 2), dovrebbe contenere solo '0' e '1'
        // Puoi adattare questa logica in base alle tue esigenze specifiche
        switch (baseDestinazione) {
            case 2:
                return risultato.matches("[01]+");
            case 8:
                return risultato.matches("[0-7]+");
            case 16:
                return risultato.matches("[0-9A-Fa-f]+");
            default:
                // Aggiungi la logica per altre basi se necessario
                return false;
        }
    }

    @Property
    void conversioneBaseGenericaRestituisceStringa(@ForAll @IntRange(min=1, max=Integer.MAX_VALUE)int numeroDecimale, @ForAll @From("baseDestinazione") int baseDestinazione) {
        // Assicura che il metodo convertiBase restituisca una stringa
        // Non importa il valore specifico della stringa, ma solo che non sia null
        // e che la base di destinazione sia una delle basi supportate (2, 8, 16)

        String risultato = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);
        assertNotNull(risultato);
    }

    @Property
    void conversioneBaseRestituisceStringaVuotaPerNumeroNegativo(@ForAll @IntRange(min=Integer.MIN_VALUE, max=0)int numeroDecimale, @ForAll @From("baseDestinazione") int baseDestinazione) {
        // Assicura che la conversione in base restituisca una stringa vuota
        // quando il numero decimale è negativo
        String risultato = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);
        assertEquals("",risultato);
    }




    @Property
    void soluzioniEquazioneSecondoGradoConAZeroRestituisconoNull(
            @ForAll("bNonZeroECNonZero") double b,
            @ForAll("bNonZeroECNonZero") double c
    ) {
        // Assicura che il metodo calcolaSoluzioniEquazioneSecondoGrado restituisca null
        // quando il coefficiente 'a' è zero
        OperazioniMath operazioniMath = new OperazioniMath();
        double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(0, b, c);
        assertNull(soluzioni);
    }

    @Provide
    Arbitrary<Double> bNonZeroECNonZero() {
        return Arbitraries.doubles().filter(d -> d != 0);
    }


    @Property
    void soluzioniEquazioneSecondoGradoCoefficientiOltreLeSoglie(@ForAll double a, @ForAll double b, @ForAll double c) {
        // Assicura che le soluzioni siano NaN quando il delta è negativo

        double sogliaMax = 1000000000;
        double sogliaMin = -1000000000;
        Assume.that(a>sogliaMax || a<sogliaMin);
        Assume.that(b>sogliaMax || b<sogliaMin);
        Assume.that(c>sogliaMax || c<sogliaMin);
        Assume.that(a != 0);

        assertThrows(ArithmeticException.class,() ->{
            operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a,b,c);
        });

    }

    @Property
    void soluzioniEquazioneSecondoGradoDeltaNegativoRestituisconoNaN(@ForAll double a, @ForAll double b, @ForAll double c) {
        // Assicura che le soluzioni siano NaN quando il delta è negativo


        /*
        double sogliaMax = 1000000000;
        double sogliaMin = -1000000000;
        Assume.that(a<sogliaMax && a>sogliaMin);
        Assume.that(b<sogliaMax && b>sogliaMin);
        Assume.that(c<sogliaMax && c>sogliaMin);*/ //supera i limiti e lancia l'eccezzione artitmetica
        try {
            Assume.that(a != 0);
            Assume.that(b * b - 4 * a * c < 0);
            double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
            assertArrayEquals(new double[]{Double.NaN, Double.NaN}, soluzioni);
        }catch (ArithmeticException e){
            
        }
    }
    
}

