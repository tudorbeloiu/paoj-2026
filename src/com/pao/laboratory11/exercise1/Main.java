package com.pao.laboratory11.exercise1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(br.readLine());

        List<Transaction> tranzactii = new ArrayList<>();
        Map<Integer, Transaction> tranzactiiById = new HashMap<>();

        for(int i=0; i<n; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int id = Integer.parseInt(st.nextToken());
            BigDecimal amount = new BigDecimal(st.nextToken());
            LocalDate date = LocalDate.parse(st.nextToken());
            String country = st.nextToken();
            String channel = st.nextToken();

            Transaction tx = new Transaction(id, amount, date, country, channel);
            tranzactii.add(tx);
            tranzactiiById.put(id, tx);
        }

        tranzactii.sort(BY_RISK_DESC_THEN_ID_ASC);

        int m = Integer.parseInt(br.readLine());

        for(int i=0; i<m; i++){
            String cmd = br.readLine();

            if(cmd.startsWith("CHECK")){
                int id = Integer.parseInt(cmd.substring(6));
                Transaction t = tranzactiiById.get(id);
                if(t == null){
                    sb.append("CHECK ").append(id).append(" => NOT_FOUND\n");
                }
                else{
                    sb.append("CHECK ").append(id)
                            .append(" => ").append(t.verdict())
                            .append(" score=").append(t.getScore()).append('\n');
                }
            }
            else if(cmd.startsWith("LIST_FLAGGED")){
                List<Transaction> flagged = tranzactii.stream()
                        .filter(Transaction::isFlagged)
                        .collect(Collectors.toList());

                if (flagged.isEmpty()) {
                    sb.append("NONE\n");
                } else {
                    for (Transaction t : flagged) {
                        sb.append('[').append(t.getId()).append("] ")
                                .append(t.verdict())
                                .append(" score=").append(t.getScore()).append('\n');
                    }
                }
            }
            else if(cmd.startsWith("TOP_RISK")){
                int k = Integer.parseInt(cmd.substring(9));
                int limit = Math.min(k, tranzactii.size());

                for(int j=0; j<limit; j++){
                    Transaction t = tranzactii.get(j);
                    sb.append('[').append(t.getId()).append("] ")
                            .append(t.verdict())
                            .append(" score=").append(t.getScore()).append('\n');
                }
            }
            else{
                sb.append("ERR UNKNOWN_COMMAND\n");
            }
        }

        System.out.print(sb);

    }

    private static final Comparator<Transaction> BY_RISK_DESC_THEN_ID_ASC =
            Comparator.comparingInt(Transaction::getScore).reversed()
                    .thenComparingInt(Transaction::getId);
}