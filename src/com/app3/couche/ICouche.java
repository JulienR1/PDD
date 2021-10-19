package com.app3.couche;

import com.app3.PDU;

public interface ICouche {
    void handle(PDU pdu, boolean estReception) throws Exception;

    void setCouchesVoisines(ICouche precedente, ICouche suivante);

    void close();
}
