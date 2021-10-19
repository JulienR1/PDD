package com.app3.couche;

import com.app3.Log;
import com.app3.PDU;
import com.app3.crc.CRC;
import com.app3.stats.StatRecord;
import com.app3.stats.Statistiques;

import java.nio.ByteBuffer;

public class CoucheLiaison implements ICouche {
    ICouche nextCouche;

    public CoucheLiaison() {

    }

    @Override
    public void handle(PDU pdu, boolean estReception) {
        //TODO

    }

    @Override
    public void setNextCouche(ICouche next) {
        //TODO
    }

    private PDU ajouteCRC(PDU _pdu) {
        PDU pdu = _pdu.clone();

        int crc = CRC.generer(pdu.getBytes());
        ByteBuffer buffer = ByteBuffer.allocate(4).putInt(crc);
        pdu.ajouterEntete(buffer.array());

        Log.enregistrer("Ajout du CRC au PDU.");
        Statistiques.Instance().augmenterEnregistrement(StatRecord.QUANTITE_ENVOI);

        return pdu;
    }

    private PDU retirerCRC(PDU _pdu) throws Exception {
        PDU pdu = _pdu.clone();

        Log.enregistrer("Retrait et validation du CRC sur le PDU.");
        Statistiques.Instance().augmenterEnregistrement(StatRecord.QUANTITE_RECUE);

        try {
            byte[] crc = pdu.enleverEntete(32);
            byte[] contenu = pdu.getBytes();

            if (CRC.verifier(crc, contenu)) {
                return pdu;
            }
            // TODO
            throw new Exception("CRC invalide");
        } catch (Exception ex) {
            Statistiques.Instance().augmenterEnregistrement(StatRecord.QUANTITE_ERREUR);
            throw ex;
        }
    }

    @Override
    public void close() {

    }
}
