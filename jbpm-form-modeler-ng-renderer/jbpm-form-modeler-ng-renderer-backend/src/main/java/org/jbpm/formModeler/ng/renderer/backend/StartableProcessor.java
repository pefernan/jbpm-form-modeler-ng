package org.jbpm.formModeler.ng.renderer.backend;

import org.jbpm.formModeler.ng.services.management.Startable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.commons.services.cdi.Startup;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
@Startup
public class StartableProcessor {
    protected Logger log = LoggerFactory.getLogger(StartableProcessor.class);
    @Inject
    private Instance<Startable> startables;

    @PostConstruct
    public void init() {
        for (Startable startable : startables) {
            try {
                log.debug("Starting bean {}", startable.getClass().getName());
                startable.start();
            } catch (Exception e) {
                log.error("Error starting bean " + startable.getClass().getName(), e);
            }
        }
    }
}
