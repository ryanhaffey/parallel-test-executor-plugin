package org.jenkinsci.plugins.parallel_test_executor;

import com.google.inject.Inject;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;

import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import java.lang.reflect.Field;



/**
 * Allows the splitting logic to be accessed from a workflow.
 */
public final class SplitStep extends AbstractStepImpl {

    private final Parallelism parallelism;

    private boolean generateInclusions;
    
    private String testRetrieveMatch;
    
    
    @DataBoundConstructor public SplitStep(Parallelism parallelism) {
        this.parallelism = parallelism;
    }

    public Parallelism getParallelism() {
        return parallelism;
    }

    public String getTestRetrieveMatch() {
        return testRetrieveMatch;
    }

    @DataBoundSetter
    public void setTestRetrieveMatch(String testRetrieveMatch) {
        this.testRetrieveMatch = testRetrieveMatch;
    }

    public boolean isGenerateInclusions() { return generateInclusions; }

    @DataBoundSetter
    public void setGenerateInclusions(boolean generateInclusions) {
        this.generateInclusions = generateInclusions;
    }
        

    @Extension public static final class DescriptorImpl extends AbstractStepDescriptorImpl {

        public DescriptorImpl() {
            super(Execution.class);
        }

        @Override public String getFunctionName() {
            return "splitTests";
        }

        @Override public String getDisplayName() {
            return "Split Test Runs";
        }

    }

    public static final class Execution extends AbstractSynchronousStepExecution<List<?>> {

        private static final long serialVersionUID = 1L;

        @Inject private transient SplitStep step;
        @StepContextParameter private transient Run<?,?> build;
        @StepContextParameter private transient TaskListener listener;

        @Override protected List<?> run() throws Exception {
            
            Field f = step.parallelism.getClass().getDeclaredField("testRetrieveMatch");
            String testsToInclude = (String) f.get(step.parallelism);
                                    
            if (step.generateInclusions) {
                return ParallelTestExecutor.findTestSplits(step.parallelism, build, listener, step.generateInclusions, testsToInclude);
            } else {
                List<List<String>> result = new ArrayList<>();
                for (InclusionExclusionPattern pattern : ParallelTestExecutor.findTestSplits(step.parallelism, build, listener, step.generateInclusions, testsToInclude)) {
                    result.add(pattern.getList());
                }
                System.out.println(result.size());
                return result;
            }
        }

    }

}
