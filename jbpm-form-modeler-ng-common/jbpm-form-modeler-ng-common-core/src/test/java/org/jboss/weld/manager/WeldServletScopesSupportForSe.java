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
package org.jboss.weld.manager; // required for visibility to BeanManagerImpl#getContexts()

import org.jboss.weld.context.AbstractBoundContext;
import org.jboss.weld.context.bound.MutableBoundRequest;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Taken from http://www.jtips.info/index.php?title=WeldSE/Scopes,
 * it simulates request and session scopes outside of an application server.
 */
public class WeldServletScopesSupportForSe implements Extension {

    /**
     * {@inheritDoc}
     */
    public void afterDeployment(@Observes final AfterDeploymentValidation event,
                                final BeanManager beanManager) {

        Map<String, Object> sessionMap = new HashMap<String, Object>();
        activateContext(beanManager, SessionScoped.class, sessionMap);

        Map<String, Object> requestMap = new HashMap<String, Object>();
        activateContext(beanManager, RequestScoped.class, requestMap);

        activateContext(beanManager, ConversationScoped.class,
                new MutableBoundRequest(requestMap, sessionMap));
    }

    /**
     * Activates a context for a given manager.
     *
     * @param beanManager in which the context is activated
     * @param cls         the class that represents the scope
     * @param storage     in which to put the scoped values
     * @param <S>         the type of the storage
     */
    private <S> void activateContext(final BeanManager beanManager,
                                     final Class<? extends Annotation> cls, final S storage) {
        BeanManagerImpl beanManagerImpl = (BeanManagerImpl) beanManager;
        @SuppressWarnings("unchecked")
        AbstractBoundContext<S> context =
                (AbstractBoundContext<S>) beanManagerImpl.getContexts().get(cls).get(0);

        context.associate(storage);
        context.activate();
    }
}