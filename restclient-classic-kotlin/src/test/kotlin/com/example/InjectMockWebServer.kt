package com.example

import mockwebserver3.MockWebServer

/**
 * Marks the field that [MockWebServerTestResource] fills with the running MockWebServer, so a
 * test can enqueue responses and read the recorded requests.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectMockWebServer
