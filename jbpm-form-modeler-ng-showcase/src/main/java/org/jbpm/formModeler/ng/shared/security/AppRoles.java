package org.jbpm.formModeler.ng.shared.security;

import org.uberfire.security.Role;

public enum AppRoles implements Role {
    ADMIN, SUDO, MANAGER, DIRECTOR;

    @Override
    public String getName() {
        return toString();
    }
}
