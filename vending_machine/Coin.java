/*
 FileName: Coin.java
 Author: Lior Shalom
 Date: 21/07/24
 reviewer: Maya
*/

package il.co.ilrd.vending_machine;

public enum Coin {
    TEN_PENNIES(0.1), HALF_SHEKEL(0.5), SHEKEL(1.0),
    SHNEKEL(2.0), FIVE_SHEKELS(5.0), TEN_SHEKELS(10.0),
    TWENTY_SHEKELS(20.0), FIFTY_SHEKELS(50.0);

    private final double value;

    Coin(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
