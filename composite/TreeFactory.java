/*
 FileName: treeFactory.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer: haim
*/

package il.co.ilrd.composite;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import il.co.ilrd.factory.Factory;

import static il.co.ilrd.util.Color.*;


public class TreeFactory {
    private final TreeNode root;
    private static final Factory<String, TreeNode, File> componentFactory = new Factory<>();

    static {
        componentFactory.add("dir", TreeDirectory::new);
        componentFactory.add("file", TreeFile::new);
    }

    public TreeFactory(String path) {
        File rootDir = new File(path);

        if (!rootDir.isDirectory() && rootDir.isHidden()) {
            throw new IllegalArgumentException();
        }

        root = componentFactory.create("dir", rootDir);
    }

    public void print() {
        root.print(0);
    }

    private interface TreeNode {
        public void print(int i);
    }

    private static class TreeDirectory implements TreeNode {
        private final File folder;
        private final ArrayList<TreeNode> nodeList = new ArrayList<>();

        private TreeDirectory(File Directory) {
            folder = Directory;
            File[] fileList = folder.listFiles();

            assert fileList != null;

            Arrays.sort(fileList);
            for (File file : fileList) {
                if(!file.isHidden()) {
                    String fileOrDir = "";
                    if (file.isDirectory()) {
                        fileOrDir = "dir";
                    } else{
                        fileOrDir = "file";
                    }
                    nodeList.add(componentFactory.create(fileOrDir, file));
                }

            }
        }

        @Override
        public void print(int i) {
            printIfNeed(i , folder);

            for (TreeNode node : nodeList) {
                node.print(i + 1);
            }
        }
    }

    private static class TreeFile implements TreeNode {
        private final File file;

        private TreeFile(File file) {
            this.file = file;
        }

        @Override
        public void print(int i) {
            printIfNeed(i , file);
        }
    }

    private static void printTebAsNeed(int i) {
        for (int tab = 0; tab < i; tab++) {
            System.out.print("\t");
        }
    }

    private static void printIfNeed(int i , File file){
        printTebAsNeed(i);
        StringBuilder str = new StringBuilder();

        if (file.isDirectory()){
            str.append(BLUE_BOLD);
        }
        str.append(file.getName());
        str.append(RESET);
        System.out.println(str);

    }


}
