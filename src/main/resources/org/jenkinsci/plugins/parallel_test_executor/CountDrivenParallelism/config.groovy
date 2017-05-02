package org.jenkinsci.plugins.parallel_test_executor.CountDrivenParallelism

def f = namespace(lib.FormTagLib)

f.entry(title:"# of parallel tests", field:"size") {
    f.number()
}
f.entry(title:"Optional inclusion file name in the test job", field:"testRetrieveMatch") {
    f.textbox()
}