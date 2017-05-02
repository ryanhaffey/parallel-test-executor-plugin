package org.jenkinsci.plugins.parallel_test_executor;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//import hudson.tasks.junit.ClassResult;
import hudson.tasks.junit.CaseResult;
import org.jenkinsci.plugins.parallel_test_executor.ParallelTestExecutor.Knapsack;

/**
 * Execution time of a specific test case.
 */
@SuppressFBWarnings(value="EQ_COMPARETO_USE_OBJECT_EQUALS", justification="Cf. justification in Knapsack.")
public class TestClass implements Comparable<TestClass> {
    String caseName;
    long duration;
    /**
     * Knapsack that this test class belongs to.
     */
    Knapsack knapsack;

    public TestClass(CaseResult cr) {
        String className = cr.getClassName();
        // String pkgName = cr.getPackageName();

        // if (pkgName.equals("(root)"))   // UGH
        //     pkgName = "";
        // else
        //     pkgName += '.';
        this.caseName = className+"::"+cr.getName();
        this.duration = (long)(cr.getDuration()*1000);  // milliseconds is a good enough precision for us
    }

    public int compareTo(TestClass that) {
        long l = this.duration - that.duration;
        // sort them in the descending order
        if (l>0)    return -1;
        if (l<0)    return 1;
        return 0;
    }

    public String getSourceFileName(String extension) {
        return caseName.replace('.','/')+extension;
    }
}
