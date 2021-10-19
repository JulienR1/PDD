package com.app3.couche;

import com.app3.Log;
import com.app3.PDU;
import com.app3.crc.CRC;
import com.app3.stats.StatRecord;
import com.app3.stats.Statistiques;

import java.nio.ByteBuffer;

public class CoucheLiaison extends Couche {

    @Override
    public void handle(PDU pdu, boolean estReception) throws Exception {
        if (estReception) {
            PDU pduSansCRC = retirerCRC(pdu);
            couchePrecedente.handle(pduSansCRC, true);
        } else {
            PDU pduAvecCRC = ajouteCRC(pdu);
            coucheSuivante.handle(pduAvecCRC, false);
        }
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
            byte[] crcBytes = pdu.enleverEntete(4);
            byte[] contenu = pdu.getBytes();

            int crc = ByteBuffer.wrap(crcBytes).getInt();
            if (CRC.verifier(crc, contenu)) {
                return pdu;
            }
            
            Log.enregistrer("CRC invalide");
            Statistiques.Instance().augmenterEnregistrement(StatRecord.QUANTITE_ERREUR);
            return new PDU(pdu.getNom(), pdu.getBytes(), false);
        } catch (Exception ex) {
            Statistiques.Instance().augmenterEnregistrement(StatRecord.QUANTITE_ERREUR);
            throw ex;
        }
    }
}
