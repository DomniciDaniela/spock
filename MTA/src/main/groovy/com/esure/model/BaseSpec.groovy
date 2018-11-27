package com.esure.model

import spock.lang.Shared
import spock.lang.Specification

abstract class BaseSpec extends Specification {
    @Shared
    Context context = new Context()
}
