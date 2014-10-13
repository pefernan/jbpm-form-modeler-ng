package org.jbpm.formModeler.ng.server.impl;

import org.guvnor.common.services.backend.metadata.attribute.OtherMetaView;
import org.kie.uberfire.metadata.backend.lucene.LuceneConfig;
import org.kie.uberfire.metadata.io.IOServiceIndexedImpl;
import org.uberfire.backend.server.IOWatchServiceNonDotImpl;
import org.uberfire.commons.cluster.ClusterServiceFactory;
import org.uberfire.commons.services.cdi.Startup;
import org.uberfire.commons.services.cdi.StartupType;
import org.uberfire.io.IOService;
import org.uberfire.io.attribute.DublinCoreView;
import org.uberfire.io.impl.cluster.IOServiceClusterImpl;
import org.uberfire.java.nio.base.version.VersionAttributeView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class should contain all ApplicationScoped producers
 * required by the application.
 */
@Startup(StartupType.BOOTSTRAP)
@ApplicationScoped
public class ApplicationScopedProvider {

    @Inject
    private IOWatchServiceNonDotImpl watchService;

    @Inject
    @Named("clusterServiceFactory")
    private ClusterServiceFactory clusterServiceFactory;

    private IOService ioService;

    @Inject
    @Named("luceneConfig")
    private LuceneConfig config;

    @PostConstruct
    public void setup() {
        final IOService service = new IOServiceIndexedImpl( watchService,
                                                            config.getIndexEngine(),
                                                            DublinCoreView.class,
                                                            VersionAttributeView.class,
                                                            OtherMetaView.class );

        if ( clusterServiceFactory == null ) {
            ioService = service;
        } else {
            ioService = new IOServiceClusterImpl( service,
                                                  clusterServiceFactory,
                                                  false );
        }
    }

    @Produces
    @Named("ioStrategy")
    public IOService ioService() {
        return ioService;
    }
}
