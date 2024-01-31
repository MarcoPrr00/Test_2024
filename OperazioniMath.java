public class OperazioniMath {

    // Metodo per la conversione della base (basi ammissibili 2, 8, 16; altrimenti restituisce una stringa di errore)
    public String convertiBase(int numeroDecimale, int baseDestinazione) {
        switch (baseDestinazione) {
            case 2:
                return convertiBinario(numeroDecimale);
            case 8:
                return convertiOttale(numeroDecimale);
            case 16:
                return convertiEsadecimale(numeroDecimale);
            default:
                return "Base di destinazione non supportata";
        }
    }

    // Metodo per la conversione in binario (se il numero è minore di zero restituiremo una stringa vuota)
    private String convertiBinario(int numeroDecimale) {
        StringBuilder risultato = new StringBuilder();
        while (numeroDecimale > 0) {
            int resto = numeroDecimale % 2;
            risultato.insert(0, resto);
            numeroDecimale /= 2;
        }
        return risultato.toString();
    }

    // Metodo per la conversione in ottale (se il numero è minore di zero restituiremo una stringa vuota)
    private String convertiOttale(int numeroDecimale) {
        StringBuilder risultato = new StringBuilder();
        while (numeroDecimale > 0) {
            int resto = numeroDecimale % 8;
            risultato.insert(0, resto);
            numeroDecimale /= 8;
        }
        return risultato.toString();
    }

    // Metodo per la conversione in esadecimale (se il numero è minore di zero restituiremo una stringa vuota)
    private String convertiEsadecimale(int numeroDecimale) {
        StringBuilder risultato = new StringBuilder();
        while (numeroDecimale > 0) {
            int resto = numeroDecimale % 16;
            risultato.insert(0, Integer.toHexString(resto).toUpperCase());
            numeroDecimale /= 16;
        }
        return risultato.toString();
    }

    public double[] calcolaSoluzioniEquazioneSecondoGrado(double a, double b, double c) {
        double[] soluzioni = new double[2];
        double sogliaMax = 1000000000;
        double sogliaMin = -1000000000;
        if (a==0){
            return null;
        }
        // Se uno tra i tre coefficienti dovesse risultare maggiore di sogliaMax o minore di sogliaMin allora lancia l'eccezione
        if (a>sogliaMax||b>sogliaMax||c>sogliaMax||a<sogliaMin||b<sogliaMin||c<sogliaMin){
            throw new ArithmeticException();
        }
        double delta = b * b - 4 * a * c;


        if (delta > 0) {
            // Due soluzioni reali
            double radiceDelta = Math.sqrt(delta);
            soluzioni[0] = (-b + radiceDelta) / (2 * a);
            soluzioni[1] = (-b - radiceDelta) / (2 * a);
        } else if (delta == 0) {
            // Una soluzione reale (delta uguale a zero)
            soluzioni[0] = -b / (2 * a);
            soluzioni[1] = soluzioni[0]; // La stessa soluzione in entrambi i casi
        } else {
            // Nessuna soluzione reale (delta negativo)
            soluzioni[0] = Double.NaN;
            soluzioni[1] = Double.NaN;
        }

        return soluzioni;
    }

}
