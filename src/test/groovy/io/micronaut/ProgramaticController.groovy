package io.micronaut

import io.micronaut.context.ExecutionHandleLocator
import io.micronaut.web.router.DefaultRouteBuilder

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgramaticController extends DefaultRouteBuilder {
    ProgramaticController(ExecutionHandleLocator executionHandleLocator, UriNamingStrategy uriNamingStrategy) {
        super(executionHandleLocator, uriNamingStrategy)
    }

    @Inject
    void issuesRoutes(SampleController sampleController) {
        GET("/town", sampleController, "city")
    }
}