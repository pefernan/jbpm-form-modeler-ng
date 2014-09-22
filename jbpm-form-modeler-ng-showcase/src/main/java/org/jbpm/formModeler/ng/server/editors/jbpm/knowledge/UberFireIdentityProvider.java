package org.jbpm.formModeler.ng.server.editors.jbpm.knowledge;

import org.jbpm.kie.services.api.IdentityProvider;
import org.uberfire.security.Identity;
import org.uberfire.security.Role;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
public class UberFireIdentityProvider implements IdentityProvider, Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Identity identity;

    @Inject
    private HttpServletRequest request;

    @Override
    public String getName() {
        try {
            return identity.getName();
        } catch (Exception e) {
            if (request != null && request.getUserPrincipal() != null) {
                return request.getUserPrincipal().getName();
            }
            return null;
        }
    }

    @Override
    public List<String> getRoles() {
        List<String> roles = new ArrayList<String>();

        List<Role> ufRoles = identity.getRoles();
        for (Role role : ufRoles) {
            roles.add(role.getName());
        }

        return roles;
    }

    @Override
    public boolean hasRole(String role) {
        if (request != null) {
            return request.isUserInRole(role);
        }
        return false;
    }
}