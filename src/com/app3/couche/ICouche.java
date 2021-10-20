package com.app3.couche;

import com.app3.PDU;

public interface ICouche {
    /**
     * Gestion du trafic des paquets d'une couche à l'autre.
     *
     * @param pdu          Information recue a traiter.
     * @param estReception Gestion de l'orientation du trafic.
     * @throws Exception
     */
    void handle(PDU pdu, boolean estReception) throws Exception;

    /**
     * Assigne les couches avec lesquelles communiquer.
     *
     * @param precedente
     * @param suivante
     */
    void setCouchesVoisines(ICouche precedente, ICouche suivante);

    /**
     * Au moment de la fin du processus de communication.
     */
    void close();
}
