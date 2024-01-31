import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OperazioniMath conversione = new OperazioniMath();

        System.out.println("CONVERSIONE DI NUMERO:");

        System.out.println("Inserisci il numero decimale da convertire: (numero positivo)");
        int numeroDecimale = scanner.nextInt();
        System.out.println("Inserisci la base di destinazione (2 per binario, 8 per ottale, 16 per esadecimale): ");
        int baseDestinazione = scanner.nextInt();
        String numeroConvertito = conversione.convertiBase(numeroDecimale, baseDestinazione);
        if (numeroConvertito.equals("")) {
            numeroConvertito="numero inserito <= 0";
        }
        System.out.println("Il numero convertito è: " + numeroConvertito);


        System.out.println("\nSOLUZIONI EQUAZIONE DI SECONDO GRADO:");

        System.out.println("Inserire il coefficiente di X^2: (a)");
        double a=scanner.nextDouble();
        System.out.println("Inserire il coefficiente di X: (b)");
        double b=scanner.nextDouble();
        System.out.println("Inserire il termine noto: (c)");
        double c=scanner.nextDouble();
        try{
            double[] soluzioni = conversione.calcolaSoluzioniEquazioneSecondoGrado(a, b, c);
            if(soluzioni==null){
                System.out.println("a minore di zero impossibile eseguire i calcoli");
            }else if (!Double.isNaN(soluzioni[0])) {
                if (soluzioni[0] == soluzioni[1]) {
                    System.out.println("L'equazione ha una sola soluzione: " + soluzioni[0]);
                } else {
                    System.out.println("Le soluzioni dell'equazione sono: " + soluzioni[0] + " e " + soluzioni[1]);
                }
            } else {
                System.out.println("L'equazione non ha soluzioni reali.");
            }
        }catch(ArithmeticException e) {
            System.out.println("coefficienti troppo grandi!!");
        }

    }
}
