/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.dataModeler.integration;

import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.guvnor.common.services.project.service.POMService;
import org.jbpm.formModeler.dataModeler.model.DataModelerDataHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.RangedDataHolderBuilder;
import org.kie.api.builder.KieModule;
import org.kie.scanner.KieModuleMetaData;
import org.kie.workbench.common.screens.datamodeller.model.DataModelTO;
import org.kie.workbench.common.screens.datamodeller.model.DataObjectTO;
import org.kie.workbench.common.services.backend.builder.LRUBuilderCache;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;

@ApplicationScoped
public class DataModelerService implements RangedDataHolderBuilder {

    public static final String HOLDER_TYPE_DATA_MODEL = "dataModelerEntry";

    private Logger log = LoggerFactory.getLogger( DataModelerService.class );

    @Inject
    private org.kie.workbench.common.screens.datamodeller.service.DataModelerService dataModelerService;

    @Inject
    private FormRenderContextManager contextManager;

    @Inject
    private LRUBuilderCache builderCache;

    @Inject
    private KieProjectService projectService;

    @Inject
    private POMService pomService;

    @Inject
    @Named("ioStrategy")
    private IOService ioService;

    @Override
    public Map<String, String> getHolderSources( String ctxUID ) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        Map<String, String> result = new TreeMap<String, String>();
        try {
            DataModelTO dataModelTO = dataModelerService.loadModel( projectService.resolveProject((Path) context.getAttributes().get("path")) );
            if ( dataModelTO != null && dataModelTO.getDataObjects() != null ) {
                String className = "";
                for ( DataObjectTO dataObjectTO : dataModelTO.getDataObjects() ) {
                    className = dataObjectTO.getClassName();
                    result.put( className, className );
                }
            }
        } catch ( Throwable e ) {
            result.put( "-", "-" );
        }
        return result;
    }

    @Override
    public DataHolder buildDataHolder( DataHolderBuildConfig config ) {
        DataModelerDataHolder dataHolder = null;

        Path path = (Path) config.getAttribute( "path" );
        if (path == null) {
            dataHolder = new DataModelerDataHolder( config.getHolderId(), config.getClassName(), config.getRenderColor() );
        } else {
            Class holderClass = findHolderClass( config.getClassName(), path);
            if ( holderClass == null ) {
                return null;
            }
            DataModelTO dataModelTO = dataModelerService.loadModel( projectService.resolveProject(path));
            dataHolder = new DataModelerDataHolder( config.getHolderId(), holderClass, config.getRenderColor() );
        }

        return dataHolder;
    }

    @Override
    public boolean supportsPropertyType(String type, Map<String, Object> context) {
        Path path = (Path) context.get("path");
        Class holderClass = findHolderClass(type, path);
        return holderClass != null;
    }

    private Class findHolderClass( String className, Path path ) {
        ClassLoader classLoader = getProjectClassLoader( projectService.resolveProject( path ) );
        try {
            return classLoader.loadClass( className );
        } catch ( ClassNotFoundException e ) {
            log.warn( "Unable to load class '{}': {}", className, e );
        }
        return null;
    }

    @Override
    public String getId() {
        return HOLDER_TYPE_DATA_MODEL;
    }

    protected Path getPath( String path ) {
        try {
            return Paths.convert( ioService.get( new URI( path ) ) );
        } catch ( Exception e ) {
            log.error( "Unable to build Path for {}': {}", path, e );
        }
        return null;
    }

    protected DataObjectTO getDataObject( String className,
                                          Path path ) {
        DataModelTO dataModelTO = getDataModel( path );

        DataObjectTO result = dataModelTO.getDataObjectByClassName( className );

        if ( result == null ) {
            for ( DataObjectTO externalDataObject : dataModelTO.getExternalClasses() ) {
                if ( className.equals( externalDataObject.getClassName() ) ) {
                    return externalDataObject;
                }
            }
        }

        return result;
    }

    protected DataModelTO getDataModel( Path path ) {
        KieProject project = projectService.resolveProject( path );
        return dataModelerService.loadModel( project );
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean needsConfig() {
        return true;
    }

    protected ClassLoader getProjectClassLoader( KieProject project ) {
        final KieModule module = builderCache.assertBuilder( project ).getKieModuleIgnoringErrors();
        final ClassLoader classLoader = KieModuleMetaData.Factory.newKieModuleMetaData( module ).getClassLoader();
        return classLoader;
    }

    @Override
    public String getDataHolderName( Locale locale ) {
        ResourceBundle bundle = ResourceBundle.getBundle( "org.jbpm.formModeler.dataModeler.messages", locale );
        return bundle.getString( "dataHolder_dataModeler" );
    }
}
