package com.blizzard.war.mvp.Tree;

public class BinaryTree {


    Node root;

    //设置根节点
    public void setRoot(Node root) {
        this.root = root;
    }

    //获取根节点
    public Node getRoot(Node root) {
        return root;
    }

    //前序遍历
    public void frontShow() {
        if(root!=null) {
            root.frontShow();
        }
    }

    //中序遍历
    public void midShow() {
        if(root!=null) {
            root.midShow();
        }
    }

    //后序遍历
    public void afterShow() {
        if(root!=null) {
            root.afterShow();
        }
    }

    //前序查找
    public Node frontSearch(int i) {
        return root.frontSearch(i);
    }

    //删除一个子树
    public void delete(int i) {
        if(root.value==i) {
            root = null;
        }else {
            root.delete(i);
        }
    }

}
