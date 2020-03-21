package com.github.onlinestore.orderservice.eventsourcing

/**
 * Annotate those events that are not meant to be persisted on the event store
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DoNotPersistEvent
