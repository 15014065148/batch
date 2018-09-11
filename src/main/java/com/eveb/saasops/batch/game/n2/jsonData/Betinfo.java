package com.eveb.saasops.batch.game.n2.jsonData;
import java.util.List;

/**
 * 投注区域
 */
public class Betinfo {

    private List<Clientbet> clientbet;
    public void setClientbet(List<Clientbet> clientbet) {
         this.clientbet = clientbet;
     }
     public List<Clientbet> getClientbet() {
         return clientbet;
     }

}