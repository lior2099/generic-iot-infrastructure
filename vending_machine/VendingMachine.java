/*
 FileName: VendingMachine.java
 Author: Lior Shalom
 Date: 21/07/24
 reviewer: Maya
*/
package vending_machine;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import il.co.ilrd.util.Color;

public class VendingMachine {
    private double insertedMoney;
    private VMState state;
    private final ArrayList<Product> products;
    private final Display display;
    private Product currProd;
    private static ScheduleTaskVM STVM ;

    public VendingMachine(ArrayList<Product> prodArray, Display dsp, long closeTime) {
        products = prodArray;
        display = dsp;
        state = VMState.OFF;
        STVM = new ScheduleTaskVM(closeTime);
    }

    public void on() {
        state.on(this);
    }

    public void off() {
        state.off(this );
    }

    public void insertMoney(Coin coin) {
        state.insertMoney(this, coin);
    }

    public void selectProduct(Product prod) {
        state.selectProduct(this, prod);
    }

    public void cancel() {
        state.cancel(this);
    }

    private void upDataMoney(double coin) {
        insertedMoney += coin;
    }

    private class ScheduleTaskVM{
        private Timer timer;
        private TimerTask timeoutTask;
        private final long timeClose;

        private ScheduleTaskVM(long closeTime)
        {
            timeClose = closeTime;
        }

        private void stopTimeout() {
            if(timeoutTask != null){
                timeoutTask.cancel();
            }

            if(timer != null){
                timer.cancel();
            }
        }
        private void resetTimeout() {
            if(timeoutTask != null){
                timeoutTask.cancel();
            }

            if(timer != null){
                timer.cancel();
            }
            startTimeout();
        }

        private void startTimeout() {
            if(timer != null){
                timer.cancel();
            }
            timer = new Timer();
            timeoutTask = new TimerTask() {

                @Override
                public void run() {
                    state.cancel(VendingMachine.this);
                }
            };
            timer.schedule(timeoutTask, timeClose); // in milisec  20 seconds timeout
        }

    }

    private enum VMState {
        OFF {
            @Override
            public void on(VendingMachine vm) {
                vm.display.write("the Vending Machine is on plz select a pizza");
                for (Product pizza : vm.products){
                    System.out.printf("%s  :  Price - %.2f\n", pizza.getName() , pizza.getPrice());
                }
                resetVM(vm);
            }

            @Override
            public void off(VendingMachine vm) {
                // vm.display.write( Color.RED_BOLD + "VM is OFF!!! cant off off man" + Color.RESET);
            }

            @Override
            public void insertMoney(VendingMachine vm , Coin coin) {
                // vm.display.write( Color.RED_BOLD + "VM is OFF!!! cant instant coin " + Color.CYAN + coin.getValue() + Color.RESET);
            }

            @Override
            public void selectProduct(VendingMachine vm, Product prod) {
                // vm.display.write( Color.RED_BOLD + "VM is OFF!!! cant select Product man" + Color.RESET);
            }

            @Override
            public void cancel(VendingMachine vm) {
               // vm.display.write( Color.RED_BOLD + "VM is OFF!!! cant cancel Product man" + Color.RESET);
            }
        },

        WAITING_FOR_PRODUCT {
            @Override
            public void insertMoney(VendingMachine vm ,Coin coin ) {
                vm.upDataMoney(coin.getValue());
                vm.display.write(String.format("the Vending Machine have now  : %.2f" ,vm.insertedMoney));
                STVM.resetTimeout();
            }
        },

        WAITING_FOR_MONEY {
            @Override
            public void insertMoney(VendingMachine vm , Coin coin) {
                VMState.WAITING_FOR_PRODUCT.insertMoney(vm, coin);
                selectProduct(vm, vm.currProd);
                vm.display.write(String.format("the Vending Machine have now  :  %.2f"  , vm.insertedMoney));
                STVM.resetTimeout();
            }
        };

        public void on(VendingMachine vm) {
           // vm.display.write( Color.RED_BOLD + "the Vending Machine is allRdy  on " + Color.RESET);
        }

        public void off(VendingMachine vm ) {
            vm.state = VMState.OFF;
            vm.display.write(String.format(Color.GREEN + "Refund is :  %.2f\nBye Bye the Vending Machine is will shutdown" + Color.RESET , vm.insertedMoney) );
            STVM.stopTimeout();
        }

        public void insertMoney(VendingMachine vm, Coin coin) {
            vm.upDataMoney(coin.getValue());
            vm.display.write( String.format(Color.CYAN + "the Vending Machine have now  : %.2f" + Color.RESET ,vm.insertedMoney)) ;
            STVM.resetTimeout();
        }

        public void selectProduct(VendingMachine vm, Product prod) {
            Double money_left = 0.0;

            if(vm.products.contains(prod)){
                vm.currProd = prod;
                money_left = vm.insertedMoney - vm.currProd.getPrice();
                if(money_left.compareTo(0.0) >= 0)
                {
                    vm.display.write(String.format( Color.GREEN + "Pizza : %s will be out now\nThe return change is : %.2f" +Color.RESET ,vm.currProd.getName() , money_left ));
                    resetVM(vm);
                }
                else {
                    vm.display.write( String.format("The Price for Pizza : %s is : %.2f \nThe Money that u enter until now is : %.2f  and miss  : %.2f"
                            ,vm.currProd.getName(),vm.currProd.getPrice() , vm.insertedMoney , money_left*(-1) ));
                    vm.state = VMState.WAITING_FOR_MONEY;
                }
            }
            else {
                vm.display.write( String.format(Color.RED + "Pizza : %s It is out of Stock." + Color.RESET, prod.getName()) );
            }
            STVM.resetTimeout();
        }

        public void cancel(VendingMachine vm) {
            resetVM(vm);
            vm.display.write(String.format(Color.GREEN + "the Vending Machine was Cancel The return change is : %.2f \nplz Pick a Pizza" + Color.RESET, vm.insertedMoney));
            STVM.stopTimeout();
        }
    }

    private static void resetVM(VendingMachine vm){
        vm.state = VMState.WAITING_FOR_PRODUCT;
        vm.insertedMoney = 0 ;
    }

}
