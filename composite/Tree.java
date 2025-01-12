/*
 FileName: Tree.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer:
*/

package il.co.ilrd.composite;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import il.co.ilrd.util.Color;

public class Tree {
    private TreeDirectory root;
    private static int numFolder;
    private static int numFile;

    public Tree(String path){

        if (null == path){
            System.out.println(Color.RED_BOLD + "Error cant do null" + Color.RESET);
            throw new NullPointerException();
        }

        File testDir = new File(path);

        if(!testDir.isDirectory()){
            System.out.println(testDir.getName()  + "[error opening dir]");
            return;
        }
        if(testDir.isHidden()){
            System.out.println(testDir.getName()  + "[is Hidden]");
            return;
        }

        root = new TreeDirectory(testDir);

    }

    public void print(){
        root.print(0);

        System.out.println(numFolder -1 +  Color.BLUE_BOLD + " : directories, "  +   Color.RESET + numFile +
                Color.GREEN_BOLD +  " files" + Color.RESET);

        numFile = 0;
        numFolder = 0;
    }

    private interface TreeNode {

        void print(int i);
    }

    private static class TreeDirectory implements TreeNode {
        private final File folder;
        private final ArrayList<TreeNode> nodeList = new ArrayList<>();

        private TreeDirectory(File Directory){

            folder = Directory;
            File[] fileList= folder.listFiles();
            Arrays.sort(fileList);
            for(File file : fileList){
                if(file.isDirectory()){
                    nodeList.add(new  TreeDirectory(file));
                }
                else if (!file.isHidden()){
                    nodeList.add(new TreeFile(file));
                }
            }
        }

        @Override
        public void print(int i) {
            TebAsNeed(i);
            if(!folder.isHidden()){
                numFolder++;
                System.out.println(Color.BLUE_BOLD +  folder.getName() + Color.RESET);
            }

            for(TreeNode node : nodeList){
                node.print(i+1);
            }
        }
    }

    private static class TreeFile implements TreeNode{
        private final File file;

        private TreeFile(File file){
            this.file = file;
        }

        @Override
        public void print(int i) {
            TebAsNeed(i);
            if(!file.isHidden()){
                numFile++;
                System.out.println( file.getName());

            }
        }
    }

    private static void TebAsNeed(int i){
        for(int tab = 0  ; tab < i ; tab++){
            System.out.print("\t");
        }
    }

    public static void main(String[] args) {

        Tree tree = new Tree("/home/lior/Desktop/git/lior.shalom/quizzes/.testLior");
        tree.print();

    }

}
