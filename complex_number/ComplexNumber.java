/*
 FileName: ComplexNumber.java
 Author: Lior Shalom
 Date: 18/07/24
 reviewer: Lior
*/

package il.co.ilrd.complex_number;

import java.util.Objects;
import java.lang.Double;
import java.util.regex.*;

public class ComplexNumber implements Comparable<ComplexNumber> {
    private double real;
    private double img;

    public ComplexNumber(){
        real = 0.0;
        img = 0.0;
    }
    public ComplexNumber(double real_send, double img_send){
        real = real_send;
        img = img_send;
    }

    public ComplexNumber add(ComplexNumber other){
        return new ComplexNumber((this.real + other.getReal()),(this.img + other.getImaginary()));
    }

    public ComplexNumber subtract(ComplexNumber other){
       return new ComplexNumber((this.real - other.getReal()),(this.img - other.getImaginary()));
    }

    public ComplexNumber multiplyBy(ComplexNumber other){
        ComplexNumber res = new ComplexNumber();

        res.setReal(real*other.getReal() -img*other.getImaginary());
        res.setImaginary(real*other.getImaginary() + img*other.getReal());

        return res;
    }

    public ComplexNumber divideBy(ComplexNumber other){
        ComplexNumber conjugate = conjugate(other);
        ComplexNumber res = new ComplexNumber();

        double div = (conjugate.multiplyBy(other)).getReal();
        res.setReal((real*other.getReal() + img*other.getImaginary()) / div );
        res.setImaginary( (img*other.getReal() - real*other.getImaginary()) / div);

        return res;
    }

    public double getReal(){
        return real;
    }
    public void setReal(double real_send){
        real = real_send;
    }
    public double getImaginary(){
        return img;
    }

    public void setImaginary(double img_send){
        img = img_send;
    }
    public void setValue(double real_send, double img_send){
        real = real_send;
        img = img_send;
    }
    public boolean isReal(){
        return Double.compare(img, 0.0)  == 0;
    }
    public boolean isImg(){
        return !isReal() ;
    }

    @Override
    public String toString(){
        String real_str = real+"";
        String img_str = "";
        if(img < 0)
            img_str = img+"i";
        else
            img_str = "+"+img+"i";
        return real_str+img_str;
    }

    public static ComplexNumber parse(String str){
        String regex = "([+-]?\\d*\\.?\\d+)([+-]\\d*\\.?\\d*)i"; // () = grup | [] what i want ?

        Pattern pattern = Pattern.compile(regex);    //make a pattern
        Matcher matcher = pattern.matcher(str);      // use the pattern to find in the str

        if (!matcher.matches()) {
            return null;
        }
        // parseDouble can make from str to double | matcher.group(1) for rael and matcher.group(2) for img

        return new ComplexNumber(Double.parseDouble(matcher.group(1)) , Double.parseDouble(matcher.group(2)));
    }

    @Override
    public boolean equals(Object other){

        if(!(other instanceof ComplexNumber))
        {
            return false;
        }
        return (real == ((ComplexNumber)other).getReal() && img == ((ComplexNumber)other).getImaginary());
    }

    @Override
    public int hashCode(){
        return Objects.hash(real, img);
    }

    @Override
    public int compareTo(ComplexNumber other){
        return Double.compare(this.abs() , other.abs());
    }

    private ComplexNumber conjugate(ComplexNumber other){
        return new ComplexNumber(other.getReal() , -1*other.getImaginary());
    }

    private double abs(){
        ComplexNumber conjugate = conjugate(this);

        return (multiplyBy(conjugate)).getReal();
    }
}