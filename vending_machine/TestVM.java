/*
 FileName: TestVM.java
 Author: Lior Shalom
 Date: 21/07/24
 reviewer: Maya
*/

package il.co.ilrd.vending_machine;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import il.co.ilrd.util.Color;

public class TestVM {

    private static final DisplayUser dsp = new DisplayUser();

    private static ArrayList<Product> initPizza(){
        ArrayList<Product> pizzeTuble = new ArrayList<>();
        pizzeTuble.add(new Pizza("Hawaiian" , 45.0));
        pizzeTuble.add(new Pizza("Pepperoni" , 35.0));
        pizzeTuble.add(new Pizza("Veggie" , 23.0));
        pizzeTuble.add(new Pizza("Supreme" , 51.0));
        pizzeTuble.add(new Pizza("Margherita" , 25.0));
        pizzeTuble.add(new Pizza("Buffalo Chicken" , 36.0));
        pizzeTuble.add(new Pizza("Four Cheese" , 41.0));

        return pizzeTuble;
    }

    @Test
    public void StartInit() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        assertNotNull(pizzeTuble);
        assertEquals(7, pizzeTuble.size());

        for (Product pizza : pizzeTuble){
            System.out.println(Color.GREEN + pizza.getName() +  " : " + Color.RESET + pizza.getPrice());
        }
        assertNotNull(dsp);
        assertNotNull(vm);
    }

    @Test
    public void offStat() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.cancel();
        vm.selectProduct(pizzeTuble.get(2));
        vm.off();
    }

    @Test
    public void onStat() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.selectProduct(pizzeTuble.get(1));
        vm.cancel();
        vm.off();
    }

    @Test
    public void onAddCoidAndPick() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.selectProduct(pizzeTuble.get(1));
        vm.selectProduct(pizzeTuble.get(4));
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        vm.insertMoney(Coin.TWENTY_SHEKELS);
        vm.off();
    }

    @Test
    public void onTestIfProdFound() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        Product mayaPiza = new Pizza("maya" , 65);

        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        vm.selectProduct(mayaPiza);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.selectProduct(pizzeTuble.get(1));
        vm.selectProduct(pizzeTuble.get(4));
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        vm.insertMoney(Coin.TWENTY_SHEKELS);
        vm.off();
    }

    @Test
    public void onAddCoidCancel() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.selectProduct(pizzeTuble.get(3));
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        vm.insertMoney(Coin.TWENTY_SHEKELS);
        vm.cancel();
        vm.off();
    }

    @Test
    public void CancelMe() {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.cancel();

        vm.insertMoney(Coin.SHNEKEL);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.cancel();

        vm.selectProduct(pizzeTuble.get(0));
        vm.cancel();

        vm.off();
        vm.cancel();
    }

    @Test
    public void timerTest() throws InterruptedException {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        Thread.sleep(2000);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        Thread.sleep(2000);
        vm.selectProduct(pizzeTuble.get(1));
        Thread.sleep(3000);
        vm.selectProduct(pizzeTuble.get(4));
        Thread.sleep(1000);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        Thread.sleep(2000);
        vm.insertMoney(Coin.SHNEKEL);
        Thread.sleep(2000);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        Thread.sleep(7000); //will cansel timeout is 5 sec
        vm.insertMoney(Coin.TWENTY_SHEKELS);
        Thread.sleep(2000);
        vm.off();


    }

    @Test
    public void timerTestRestart() throws InterruptedException {
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        Thread.sleep(2000);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        Thread.sleep(2000);
        vm.selectProduct(pizzeTuble.get(1));
        Thread.sleep(3000);
        vm.selectProduct(pizzeTuble.get(4));
        Thread.sleep(1000);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        Thread.sleep(2000);
        vm.insertMoney(Coin.SHNEKEL);
        Thread.sleep(2000);
        vm.insertMoney(Coin.FIFTY_SHEKELS);
        Thread.sleep(2000);
        vm.insertMoney(Coin.TWENTY_SHEKELS);
        Thread.sleep(2000);
        vm.off();

    }

    @Test
    public void timerOnOff() throws InterruptedException{
        ArrayList<Product> pizzeTuble = initPizza();
        VendingMachine vm = new VendingMachine(pizzeTuble, dsp, 5000);

        vm.on();
        vm.insertMoney(Coin.SHNEKEL);
        Thread.sleep(7000);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        vm.off();
        Thread.sleep(7000);
        vm.on();
        Thread.sleep(3000);
        vm.on();
        Thread.sleep(3000);
        vm.insertMoney(Coin.SHNEKEL);
        Thread.sleep(2000);
        vm.insertMoney(Coin.FIVE_SHEKELS);
        Thread.sleep(1000);
        vm.off();
        vm.on();
        Thread.sleep(7000);
        vm.off();

    }

}