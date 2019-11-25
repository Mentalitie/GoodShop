package com.blizzard.war.mvp.Tree;

public class TestBinaryTree {


    public static void main(String[] args) {
        //创建一棵树
        BinaryTree binaryTree = new BinaryTree();
        //创建一个根节点
        Node root = new Node(1);
        //把根节点赋值给树
        binaryTree.setRoot(root);
        //创建一个左节点
        Node rootL = new Node(2);
        //把新创建的节点设置为根节点的子节点
        root.setLeftNode(rootL);
        //创建一个右节点
        Node rootR = new Node(3);
        //把新创建的节点设置为根节点的子节点
        root.setRightNode(rootR);
        //为第二层创建两个子节点
        root.setLeftNode(new Node(4));
        root.setRightNode(new Node(5));
        //前序遍历树
        binaryTree.frontShow();
        System.out.println("===========================");
        //中序遍历树
        binaryTree.midShow();
        System.out.println("===========================");
        //后序遍历树
        binaryTree.afterShow();
        System.out.println("===========================");
        //前序查找
        Node result = binaryTree.frontSearch(5);
        System.out.println(result);
        System.out.println("===========================");
        //删除一个子树
        binaryTree.delete(1);
        binaryTree.frontShow();
    }
}
