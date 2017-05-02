package org.jenkinsci.plugins.parallel_test_executor;

import hudson.Extension;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;
import org.jenkinsci.Symbol;

/**
 * @author Kohsuke Kawaguchi
 */
public class CountDrivenParallelism extends Parallelism {
    public int size;
    public String testRetrieveMatch;
    
    @DataBoundConstructor
    public CountDrivenParallelism(int size, String testRetrieveMatch ) {
        this.size = size;
        this.testRetrieveMatch = testRetrieveMatch;        
    }

    @Override
    public int calculate(List<TestClass> tests) {
        return size;
    }
    
    @Symbol("count")
    @Extension
    public static class DescriptorImpl extends Descriptor<Parallelism> {
        @Override
        public String getDisplayName() {
            return "Fixed number of batches";
        }
    }
}
