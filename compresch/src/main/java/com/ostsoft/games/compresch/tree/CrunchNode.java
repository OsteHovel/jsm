package com.ostsoft.games.compresch.tree;

import java.util.LinkedList;

public class CrunchNode {
    public CrunchNode parent;
    public Crunch data = new Crunch();
    public LinkedList<CrunchNode> dn = new LinkedList<>();

    public CrunchNode(CrunchNode parent) {
        this.parent = parent;
    }
}
