/*
 FileName: testTreeFactory.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer:
*/

package il.co.ilrd.composite;

import org.junit.Test;

public class TestTreeFactory {

    @Test
    public void HiddenFile(){
        TreeFactory tree = new TreeFactory("/home/lior/Desktop/git/lior.shalom/quizzes/.testLior");
    }

    @Test
    public void OkDir(){
        TreeFactory tree = new TreeFactory("/home/lior/Desktop/git/lior.shalom/quizzes");
        tree.print();
    }


}
