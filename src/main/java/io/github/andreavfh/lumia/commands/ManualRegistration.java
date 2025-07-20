
package io.github.andreavfh.lumia.commands;

import java.lang.annotation.*;

/**
 * Indicates that a class requires manual registration.
 * This annotation is used to mark classes that need to be manually registered
 * in the application's command or event system.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ManualRegistration {}