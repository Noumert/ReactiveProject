package com.reactLab.kpi.configs;

import org.springframework.context.ApplicationEvent;

public class RecentChangesEvent extends ApplicationEvent {

    public RecentChangesEvent(RecentChange source) {
        super(source);
    }
}