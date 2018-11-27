package com.esure.model

abstract class Process {
    abstract List<Step> steps()

    void run(Context context) {
        steps().each { step ->
            step.run(context)
        }
    }
}
