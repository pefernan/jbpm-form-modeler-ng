package org.jbpm.formModeler.ng.renderer.service;

import org.jboss.errai.bus.server.annotations.Remote;

@Remote
public interface FormRendererService {

    String initTest();

    String getUnmarshalledContext(String ctxUID);

    void marshallContext(String ctxUID, String ctxJson);
}
