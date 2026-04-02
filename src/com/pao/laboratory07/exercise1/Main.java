package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Stack<StareComanda> st = new Stack<>();
        Scanner scanner = new Scanner(System.in);
        // Part A
        // load initial state
        String initialStateString = scanner.next();
        StareComanda stareCurenta = StareComanda.valueOf(initialStateString);
        System.out.println("Initial order state: " + stareCurenta);

        while (true) {
            String orderCommand = scanner.next();

            switch (orderCommand) {
                case "next":
                    try{
                        if(stareCurenta.isFinal()){
                            throw new OrderIsAlreadyFinalException();
                        }
                        st.push(stareCurenta);
                        stareCurenta = stareCurenta.moveNext();
                        System.out.println("Order state updated to: " + stareCurenta);
                    }
                    catch(OrderIsAlreadyFinalException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "cancel":
                    try{
                        if(stareCurenta.isFinal()){
                            throw new CannotCancelFinalOrderException();
                        }
                        st.push(stareCurenta);
                        stareCurenta = StareComanda.CANCELED;
                        System.out.println("Order has been canceled.");
                    }
                    catch(CannotCancelFinalOrderException e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case "undo":
                    try{
                        if(st.isEmpty()){
                            throw new CannotRevertInitialOrderStateException();
                        }
                        stareCurenta = st.pop();
                        System.out.println("Order state reverted to: " + stareCurenta);
                    }
                    catch(CannotRevertInitialOrderStateException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "QUIT":
                    System.out.println("User quit the program.");
                    return;
            }

        }
    }
}
