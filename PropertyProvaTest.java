import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Positive;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


public class PropertyProvaTest {
    OperazioniMath operazioniMath = new OperazioniMath();

    //TEST CONVERTIBASE()
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
    void conversioneBaseNonSupportata(@ForAll int numeroDecimale,@ForAll int baseDestinazione) {
        // Assicura quando viene passato una base non supportata verga restituito il messaggio di errore

        Assume.that(baseDestinazione != 2 && baseDestinazione != 8 && baseDestinazione != 16);
        String risultato = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);
        assertEquals("Base di destinazione non supportata",risultato);
    }

    //TEST SOLUZIONIEQUAZIONESECONDOGRADO()

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
        // Assicura che venga lanciata un eccezione quando a o b o c superano le sogli minime o massime consentite

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
    void soluzioniEquazioneSecondoGradoDeltaNegativoRestituisconoNaN(
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double a,
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double b,
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double c
    ) {
        // Assicura che le soluzioni siano NaN quando il delta è negativo
        Assume.that(a != 0);
        Assume.that(b * b - 4 * a * c < 0);
        OperazioniMath operazioniMath = new OperazioniMath();
        double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
        assertArrayEquals(new double[]{Double.NaN, Double.NaN}, soluzioni);
    }

    @Property
    void soluzioniEquazioneSecondoGradoDeltaPositivo( @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double a,
                                                      @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double b,
                                                      @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double c) {
        // Assicura che le soluzioni siano differenti con il delta positivo

        Assume.that(a != 0);
        Assume.that(b * b - 4 * a * c > 0);
        double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
        assertThat(soluzioni).doesNotHaveDuplicates(); //se il delta è positivo sono due soluzioni e saranno due valori differenti

    }

    @Property
    void soluzioniEquazioneSecondoGradoDeltaUgualeAZero(
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double a,
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double b,
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double c
    ) {
        // Assicura che il delta è uguale a zero

        Assume.that(a != 0);

        if (isValidInput(a, b, c)) {
            double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
            assertThat(soluzioni[0]).isEqualTo(soluzioni[1]);
        }
        // se il delta è uguale a zero sono due soluzioni con valore uguale
    }

    private boolean isValidInput(double a, double b, double c) {
        double delta = b * b - 4 * a * c;
        return delta == 0;
    }


}

