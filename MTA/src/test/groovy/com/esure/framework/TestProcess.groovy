package com.esure.framework

import com.esure.model.Process
import com.esure.model.Step

class TestProcess extends Process {

    List<Step> steps() {
        return [
                new TestStep(),
                new TestStep()
        ]
    }
}
