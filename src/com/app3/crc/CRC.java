package com.app3.crc;


public class CRC {
    // Generateur norme IEEE802.3 (Ethernet)
    private static final int generateur_reverse = 0xEDB88320;

    public static int generer(byte[] donnees) {
        int crc = 0xffffffff;

        for (byte b : donnees) {
            int temp = (crc ^ b) & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((temp & 1) == 1) {
                    temp = (temp >>> 1) ^ generateur_reverse;
                } else {
                    temp = temp >>> 1;
                }
            }
            crc = (crc >>> 8) ^ temp;
        }

        return crc ^ 0xffffffff;
    }

    public static boolean verifier(int crc, byte[] donnees) {
        int nouveauCrc = generer(donnees);
        return nouveauCrc == crc;
    }
}
