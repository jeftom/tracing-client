package com.bdfint.bdtrace.test;

import org.slf4j.Logger;
import zipkin.Span;
import zipkin.reporter.Reporter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author heyb
 * @date 2017/5/22.
 * @desriptioin
 */
public class Test {
    //    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static Set<Entry> relationship = new HashSet<Entry>();

    static {
        relationship.add(new Entry("com.bdfint.bdtrace.service.Buy", "com.bdfint.bdtrace.service.BuyA"));
        relationship.add(new Entry("com.bdfint.bdtrace.service.Buy", "com.bdfint.bdtrace.service.BuyB"));
        relationship.add(new Entry("com.bdfint.bdtrace.service.BuyA", "com.bdfint.bdtrace.service.BuyAA"));
        relationship.add(new Entry("com.bdfint.bdtrace.service.BuyA", "com.bdfint.bdtrace.service.BuyAB"));
        relationship.add(new Entry("com.bdfint.bdtrace.service.BuyAB", "com.bdfint.bdtrace.service.BuyABA"));
        relationship.add(new Entry("com.bdfint.bdtrace.service.BuyAB", "com.bdfint.bdtrace.service.BuyABB"));
    }

    public static void testForParentChildrenRelationship(String parent, String child, Logger logger) {
        if (!relationship.contains(new Entry(parent, child))) {
            logger.info("=================ERROR===================");
            logger.info(String.format("parent is <<%s>>,child is <<%s>>", parent, child));
            logger.info("=================ERROR===================");
        }
    }

    public static void testServiceName(String name) {
        if (name.contains(".")) {
            System.out.println("ERROR :" + name);
        }
    }

    public static void main(String[] args) {
//        testForParentChildrenRelationship("BuyABB.buyAB", "BuyABB.buyAB");
        Reporter.CONSOLE.report(Span.builder().traceId(0L).id(0L).build());
    }

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

}
