package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

public class ATMThread extends Thread{
    private final int whatATM;
    private final CoadaTranzactii coada;

    private static int contor = 1;

    public ATMThread(int whatATM, CoadaTranzactii coada){
        this.whatATM = whatATM;
        this.coada = coada;
    }

    @Override
    public void run(){
        for(int i=0; i<4; i++){
            int id;
            synchronized (ATMThread.class){
                id = contor++;
            }
            double suma = 1337.0 * id;
            Tranzactie t = new Tranzactie(id, suma, "2026-02-30", "REVO9840" + whatATM + "RMA", "REVO8888" + whatATM + "BAR", TipTranzactie.CREDIT, null);
            try{
                System.out.println("[ATM-" + whatATM + "] trimite: Tranzactie #" + id + " " + suma + " RON");
                coada.adauga(t);
                Thread.sleep(50);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
