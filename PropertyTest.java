import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsCollector;
import net.jqwik.api.statistics.StatisticsReport;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


public class PropertyTest {
    OperazioniMath operazioniMath = new OperazioniMath();

    //TEST CONVERTIBASE()
    @Provide
    Arbitrary<Integer> baseDestinazione() {
        return Arbitraries.of(2, 8, 16);
    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void testConvertiBase(@ForAll @IntRange(min=1, max=Integer.MAX_VALUE) int numeroDecimale, @ForAll @From ("baseDestinazione") int baseDestinazione) {

        String risultatoMetodo = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);

        assertTrue(verificaCaratteriValidi(risultatoMetodo, baseDestinazione));

        // Aggiungiamo una statistica per tenere traccia del numero di volte che il metodo è stato testato con successo per una specifica base di destinazione.
        // Se baseDestinazione è 2, incrementiamo la statistica "Binario",
        // se è 8, incrementiamo la statistica "Ottale",
        // se è 16, incrementiamo la statistica "Esadecimale",
        // altrimenti incrementiamo la statistica "Base Sbagliata"
        Statistics.collect(baseDestinazione == 2 ? "Binario" : baseDestinazione == 8 ? "Ottale" : baseDestinazione == 16 ? "Esadecimale" : "Base Sbagliata" );
    }

    boolean verificaCaratteriValidi(String risultato, int baseDestinazione) {
        switch (baseDestinazione) {
            case 2:
                return risultato.matches("[01]+");
            case 8:
                return risultato.matches("[0-7]+");
            case 16:
                return risultato.matches("[0-9A-Fa-f]+");
            default:
                return false;
        }
    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void conversioneBaseGenericaRestituisceStringa(@ForAll @IntRange(min=1, max=Integer.MAX_VALUE)int numeroDecimale, @ForAll @From("baseDestinazione") int baseDestinazione) {

        // Assicura che il metodo convertiBase restituisca una stringa
        // Non importa il valore specifico della stringa, ma solo che non sia null
        // e che la base di destinazione sia una delle basi supportate (2, 8, 16)

        String risultato = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);
        assertNotNull(risultato);

        // Ora aggiungiamo una statistica per tenere traccia del numero di volte che la conversione ha restituito una stringa non nulla.
        // Se risultato non è null, incrementiamo la statistica "Converte a stringa non nulla",
        // altrimenti incrementiamo la statistica "Converte a stringa nulla"

        Statistics.collect(risultato != null ? "Converte a stringa non nulla" : "Converte a stringa nulla");
    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void conversioneBaseRestituisceStringaVuotaPerNumeroNegativo(@ForAll @IntRange(min=Integer.MIN_VALUE, max=0)int numeroDecimale, @ForAll @From("baseDestinazione") int baseDestinazione) {

        // Assicura che la conversione in base restituisca una stringa vuota
        // quando il numero decimale è negativo

        String risultato = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);
        assertEquals("",risultato);

        // Ora aggiungiamo una statistica per tenere traccia del numero di volte che la conversione ha restituito una stringa vuota.
        // Se risultato è vuoto, incrementiamo la statistica "Converte a stringa vuota",
        // altrimenti incrementiamo la statistica "Non converte a stringa vuota"

        Statistics.collect(risultato.isEmpty() ? "Converte a stringa vuota" : "Non converte a stringa vuota");

    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void conversioneBaseNonSupportata(@ForAll int numeroDecimale,@ForAll int baseDestinazione) {

        // Assicura quando viene passato una base non supportata verga restituito il messaggio di errore

        Assume.that(baseDestinazione != 2 && baseDestinazione != 8 && baseDestinazione != 16);
        String risultato = operazioniMath.convertiBase(numeroDecimale, baseDestinazione);
        assertEquals("Base di destinazione non supportata",risultato);

        // Ora aggiungiamo una statistica per tenere traccia del numero di volte che la conversione ha restituito il messaggio di errore.
        // Se risultato è "Base di destinazione non supportata", incrementiamo la statistica "Conversione non effettuata per basi errate",
        // altrimenti incrementiamo la statistica "Conversione effettuata per basi errate"

        Statistics.collect(risultato == "Base di destinazione non supportata" ? "Conversione non effettuata per basi errate":"Conversione effettuata per basi errate");
    }

    //TEST SOLUZIONIEQUAZIONESECONDOGRADO()

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void soluzioniEquazioneSecondoGradoConAZeroRestituisconoNull(
            @ForAll("bNonZeroECNonZero") double b,
            @ForAll("bNonZeroECNonZero") double c
    ) {

        // Assicura che il metodo calcolaSoluzioniEquazioneSecondoGrado restituisca null
        // quando il coefficiente 'a' è zero

        OperazioniMath operazioniMath = new OperazioniMath();
        double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(0, b, c);
        assertNull(soluzioni);

        // Ora aggiungiamo una statistica per tener traccia del numero di volte che il calcolo delle soluzioni ha restituito null.
        // Se soluzioni è null, incrementiamo la statistica "Calcolo delle soluzioni restituito null",
        // altrimenti incrementiamo la statistica "Calcolo delle soluzioni non restituito null"

        Statistics.collect(soluzioni == null ? "Calcolo delle soluzioni restituito null" : "Calcolo delle soluzioni non restituito null");
    }

    @Provide
    Arbitrary<Double> bNonZeroECNonZero() {
        return Arbitraries.doubles().filter(d -> d != 0);
    }


    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void soluzioniEquazioneSecondoGradoCoefficientiOltreLeSoglie(@ForAll double a, @ForAll double b, @ForAll double c) {

        // Assicura che venga lanciata un eccezione quando a o b o c superano le sogli minime o massime consentite

        double sogliaMax = 1000000000;
        double sogliaMin = -1000000000;
        Assume.that(a>sogliaMax || a<sogliaMin || b>sogliaMax || b<sogliaMin || c>sogliaMax || c<sogliaMin);
        Assume.that(a != 0);

        assertThrows(ArithmeticException.class,() ->{
            operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a,b,c);
        });

        // Aggiungiamo una statistica per tenere traccia del numero di volte che l'eccezione è stata lanciata con coefficienti oltre le soglie.
        // Se a è molto grande, incrementiamo la statistica "A Molto Grande",
        // se b è molto grande, incrementiamo la statistica "B Molto Grande",
        // se c è molto grande, incrementiamo la statistica "C Molto Grande",
        // se a è molto piccolo, incrementiamo la statistica "A Molto Piccolo",
        // se b è molto piccolo, incrementiamo la statistica "B Molto Piccolo",
        // se c è molto piccolo, incrementiamo la statistica "C Molto Piccolo",
        // altrimenti incrementiamo la statistica "Coefficienti con buoni valori"

        Statistics.collect(a>sogliaMax ?  "A Molto Grande" : b>sogliaMax ?  "B Molto Grande" :c>sogliaMax ?  "C Molto Grande" :a<sogliaMin-1 ?  "A Molto Piccolo" :b<sogliaMin ?  "B Molto Piccolo" :c<sogliaMin ?  "C Molto Piccolo" :"Coefficienti con buoni valori");
    }


    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
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

        // Aggiungiamo una statistica per tenere traccia del numero di volte che il test ha avuto successo quando il delta è negativo.
        // Se le soluzioni sono NaN, incrementiamo la statistica "Soluzioni NaN con delta negativo",
        // altrimenti incrementiamo la statistica "Soluzioni reali con delta negativo"

        Statistics.collect(Double.isNaN(soluzioni[0]) && Double.isNaN(soluzioni[1]) ?
                "Soluzioni NaN con delta negativo" :
                "Soluzioni reali con delta negativo"
        );
    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void soluzioniEquazioneSecondoGradoDeltaPositivo( @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double a,
                                                      @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double b,
                                                      @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double c) {

        // Assicura che le soluzioni siano differenti con il delta positivo

        Assume.that(a != 0);
        Assume.that(b * b - 4 * a * c > 0);
        double[] soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
        assertThat(soluzioni).doesNotHaveDuplicates(); //se il delta è positivo sono due soluzioni e saranno due valori differenti

        // Aggiungiamo una statistica per tenere traccia del numero di volte che il test ha avuto successo o fallito in base al delta.
        // Se il test ha successo, incrementiamo la statistica "Test con delta positivo eseguito correttamente",
        // altrimenti incrementiamo la statistica "Test con delta positivo fallito"

        Statistics.collect(
                soluzioni.length == 2 ? "Test con delta positivo eseguito correttamente" :
                        "Test con delta positivo fallito"
        );
    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void soluzioniEquazioneSecondoGradoDeltaUgualeAZero(
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double a,
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double b,
            @ForAll @DoubleRange(min = -1000000000, max = 1000000000) double c
    ) {

        // Assicura che il delta è uguale a zero

        Assume.that(a != 0);

        double[] soluzioni = new double[0];
        if (isValidInput(a, b, c)) {
            soluzioni = operazioniMath.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
            assertThat(soluzioni[0]).isEqualTo(soluzioni[1]);
        }

        // se il delta è uguale a zero sono due soluzioni con valore uguale


        // Aggiungiamo una statistica per tenere traccia del numero di volte che il test ha avuto successo o fallito in base al delta.
        // Se il test ha successo, incrementiamo la statistica "Test con delta uguale a zero eseguito correttamente",
        // altrimenti incrementiamo la statistica "Test con delta uguale a zero fallito"

        Statistics.collect(soluzioni.length == 2 && soluzioni[0] == soluzioni[1] ?
                "Test con delta uguale a zero eseguito correttamente" :
                "Test con delta uguale a zero fallito"
        );
    }

    private boolean isValidInput(double a, double b, double c) {
        double delta = b * b - 4 * a * c;
        return delta == 0;
    }


}

