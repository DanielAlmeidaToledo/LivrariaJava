package ifsp.edu.source.Util;

import java.util.UUID;

public class GeradorID {

    public static String getNextId() {
        return UUID.randomUUID().toString();
    }

    // private static long contador = 0;

    // public static synchronized long getNextId() { //O uso do synchronized garante
    // que apenas um thread pode executar esse método por vez, garantindo o uso do
    // método simultaneamente.
    // return ++contador;
    // }
}
