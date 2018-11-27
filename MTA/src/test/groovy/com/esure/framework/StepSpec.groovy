package com.esure.framework

import com.esure.model.BaseSpec
import com.esure.model.Scenario

class StepSpec extends BaseSpec {

    void 'Can run a step'() {
        expect:
        new TestStep()
            .run(context)
    }

    void 'Can test static'() {
        given:
        TestStep step1 = new TestStep()
        step1.stringy = 'Testy2'
        TestStep step2 = new TestStep()

        expect:
        step2.stringy == 'Testy2'
    }

    void 'Can test object equality'() {
        given:
        def step1 = new TestStep()
        def step2 = new TestStep()

        expect:
        step1 != step2
    }

    void 'Can run a process'() {
        expect:
        new TestProcess()
                .run(context)
    }

    void 'Can add a step to a scenario and run it'() {
        expect:
        new Scenario(context)
            .addStep(new TestStep())
            .run()
    }

    void 'Can add several steps to a scenario and run it'() {
        expect:
        new Scenario(context)
                .addSteps(
                [
                new TestStep(),
                new TestStep()
                ]
        )
                .run()
    }

    void 'Can add a process to a scenario and run it'() {
        expect:
        new Scenario(context)
                .addProcess(new TestProcess())
                .run()
    }

    void 'Can add either Processes or Steps to a scenario and run it'() {
        expect:
        new Scenario(context)
            .addProcess(new TestProcess())
            .addStep(new TestStep())
            .run()
    }
}
