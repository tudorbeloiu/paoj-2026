package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        CoadaTranzactii coada = new CoadaTranzactii();
        ProcessorThread processorThread = new ProcessorThread(coada);

        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        Thread processor = new Thread(processorThread);

        atm1.start();
        atm2.start();
        atm3.start();
        processor.start();

        atm1.join();
        atm2.join();
        atm3.join();

        processorThread.activ = false;
        synchronized (coada){
            coada.notifyAll();
        }

        processor.join();

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
