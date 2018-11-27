package com.esure.model

class Scenario {
    Context context
    List<Step> steps = []

    Scenario(Context context) {
        this.context = context
    }

    Scenario addStep(Step step) {
        steps << step
        this
    }

    Scenario addSteps(List<Step> steps) {
        steps.each { step ->
            this.steps << step
        }
        this
    }

    Scenario addProcess(Process process) {
        process.steps().each { step ->
            steps << step
        }
        this
    }

    void run() {
        steps.each { step ->
            if(!context.exitTest) {
                step.run(context)
            }
        }
    }
}
