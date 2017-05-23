package com.bdfint.bdtrace.test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author heyb
 * @date 2017/5/22.
 * @desriptioin
 */
public class Test {

    static class Entry {
        final String parent;
        final String child;

        public Entry(String parent, String child) {
            this.parent = parent;
            this.child = child;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            if (parent != null ? !parent.equals(entry.parent) : entry.parent != null) return false;
            return child != null ? child.equals(entry.child) : entry.child == null;
        }

        @Override
        public int hashCode() {
            int result = parent != null ? parent.hashCode() : 0;
            result = 31 * result + (child != null ? child.hashCode() : 0);
            return result;
        }

    }

    private static Set<Entry> relationship = new HashSet<Entry>();

    static {
        relationship.add(new Entry("Buy.buy", "BuyA.buyA"));
        relationship.add(new Entry("Buy.buy", "BuyB.buyB"));
        relationship.add(new Entry("BuyA.buyA", "BuyAA.buyAA"));
        relationship.add(new Entry("BuyA.buyA", "BuyAB.buyAB"));
        relationship.add(new Entry("BuyAB.buyAB", "BuyABA.buyAB"));
        relationship.add(new Entry("BuyAB.buyAB", "BuyABB.buyAB"));
    }

    public static void testForParentChildrenRelationship(String parent, String child) {
        if (!relationship.contains(new Entry(parent, child))) {
            System.out.println("=================ERROR===================");
            System.out.println(String.format("parent is <<%s>>,child is <<%s>>", parent, child));
            System.out.println("=================ERROR===================");
        }
    }

    public static void main(String[] args) {
        testForParentChildrenRelationship("BuyAB.buyAB", "BuyABB.buyAB");
        testForParentChildrenRelationship("BuyABB.buyAB", "BuyABB.buyAB");
    }

}
