package com.haru.client.module;

/**
 * Registration entry used while a module's behavior is being implemented.
 * A registered module is not necessarily functional yet.
 */
public class BasicModule extends Module {
    public BasicModule(String name, String description, Category category) {
        super(name, description, category);
    }
}
