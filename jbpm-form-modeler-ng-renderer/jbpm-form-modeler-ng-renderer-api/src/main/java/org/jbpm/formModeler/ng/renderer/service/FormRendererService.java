package org.jbpm.formModeler.ng.renderer.service;

import org.jboss.errai.bus.server.annotations.Remote;

@Remote
public interface FormRendererService {

    String initTest(String locale);

    String getUnmarshalledContext(String ctxUID);

    void unMarshallContext(String ctxUID, String ctxJson);

    String initEmptyTest(String locale);
}
