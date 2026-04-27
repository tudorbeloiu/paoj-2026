package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        scanner.nextLine();

        new File("output").mkdirs();

        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("output/lab09_ex2.bin"))){
            for(int i=0; i< n; i++){
                String[] linie = scanner.nextLine().split(" ");
                int id = Integer.parseInt(linie[0].trim());
                double suma = Double.parseDouble(linie[1].trim());
                String data = linie[2];
                TipTranzactie tip = TipTranzactie.valueOf(linie[3]);
                Status status = Status.PENDING;

                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());

                byte[] dataBytes = new byte[10];
                Arrays.fill(dataBytes, (byte)' ');
                byte[] dataRaw = data.getBytes();
                System.arraycopy(dataRaw, 0, dataBytes, 0, dataRaw.length);
                dos.write(dataBytes);

                if(tip == TipTranzactie.CREDIT){
                    dos.write(0);
                }
                else{
                    dos.write(1);
                }
                dos.write(0);
                dos.write(new byte[8]);
            }
        }
        try(RandomAccessFile raf = new RandomAccessFile("output/lab09_ex2.bin", "rw")){
            while(scanner.hasNextLine()){
                String linie = scanner.nextLine();
                if(linie.isEmpty()){
                    continue;
                }
                if(linie.startsWith("READ")){
                    int index = Integer.parseInt(linie.split(" ")[1]);

                    String t = getTranzactie(raf, index);
                    System.out.println(t);
                }
                else if(linie.startsWith("UPDATE")){
                    String[] parts = linie.split(" ");
                    int index = Integer.parseInt(parts[1].trim());
                    String stareStr = parts[2];

                    byte status = switch (stareStr){
                        case "PENDING" -> 0;
                        case "PROCESSED" -> 1;
                        case "REJECTED"  -> 2;
                        default -> throw new IllegalArgumentException("da");
                    };
                    raf.seek(index * 32L + 23);
                    raf.write(status);
                    System.out.println("Updated [" + index + "]: " + stareStr);
                }
                else if(linie.startsWith("PRINT_ALL")){
                    long sz = raf.length() / 32;
                    for(int i=0; i<sz; i++){
                        System.out.println(getTranzactie(raf, i));
                    }
                }
            }
        }

    }

    private static String getTranzactie(RandomAccessFile raf, int index) throws IOException{
        byte[] bytes = new byte[32];
        raf.seek(index * 32L);
        raf.readFully(bytes);

        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        int id = buf.getInt();
        double suma = buf.getDouble();
        byte[] dataBytes = new byte[10];
        buf.get(dataBytes);
        String data = new String(dataBytes);
        byte tipByte = buf.get();
        byte statusByte = buf.get();

        String tip = "CREDIT";
        if(tipByte == 1){
            tip = "DEBIT";
        }
        String status = switch (statusByte){
            case 0 -> "PENDING";
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "UNKNOWN";
        };

        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                index, id, data, tip, suma, status);
    }
}
