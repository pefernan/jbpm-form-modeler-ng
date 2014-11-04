package org.jbpm.formModeler.ng.server.editors.jbpm.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.errai.security.shared.api.Group;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.identity.User;
import org.kie.internal.identity.IdentityProvider;

@SessionScoped
public class UberFireIdentityProvider implements IdentityProvider, Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private User identity;

    @Inject
    @RequestScoped
    private Instance<HttpServletRequest> request;

    @Override
    public String getName() {
        try {
            return identity.getIdentifier();
        } catch (Exception e) {
            if (!request.isUnsatisfied() && request.get().getUserPrincipal() != null) {
                return request.get().getUserPrincipal().getName();
            }
            return "unknown";
        }
    }

    @Override
    public List<String> getRoles() {
        List<String> roles = new ArrayList<String>();

        final Set<Role> ufRoles = identity.getRoles();
        for (Role role : ufRoles) {
            roles.add(role.getName());
        }

        final Set<Group> ufGroups = identity.getGroups();
        for (Group group : ufGroups) {
            roles.add(group.getName());
        }

        return roles;
    }

    @Override
    public boolean hasRole(String role) {
        if (!request.isUnsatisfied()) {
            return request.get().isUserInRole(role);
        }
        return false;
    }
}