import net.jqwik.api.*;
import net.jqwik.api.constraints.Positive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropertyProvaTest {
    OperazioniMath operazioniMath = new OperazioniMath();
    @Provide
    Arbitrary<Integer> baseDestinazione() {
        return Arbitraries.of(2, 8, 16);
    }
        @Property
        void testConvertiBase(@ForAll int numeroDecimale, @ForAll @From ("baseDestinazione") int baseDestinazione) {
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
    }

