package com.esure.framework

import com.esure.model.Context
import com.esure.model.Step

class TestStep extends Step {

    public static String stringy = 'testy'

    void run(Context context) {
        assert true
        print('Step ran')
    }
}
