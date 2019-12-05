package com.umssonline.dms.core.dal.repository.impl;

import com.umssonline.dms.core.dal.repository.CustomLoginContext;
import org.apache.jackrabbit.oak.api.AuthInfo;
import org.apache.jackrabbit.oak.spi.security.authentication.AuthInfoImpl;
import org.apache.jackrabbit.oak.spi.security.principal.SystemPrincipal;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author franco.arratia@umssonline.com
 */
public class CustomLoginContextImpl implements CustomLoginContext<String> {


    //region Fields
    private Repository repository;
    //endregion

    //region Constructors
    public CustomLoginContextImpl(Repository repository) {
        this.repository = repository;
    }
    //endregion

    //region CustomLoginContext
    @Override
    public Session login(String userId) throws LoginException {

        Set<Principal> principals = new HashSet<>();
        principals.add(SystemPrincipal.INSTANCE);

        AuthInfo authInfo = new AuthInfoImpl(userId, Collections.emptyMap(), principals);
        Subject subject = new Subject(true, principals, Collections.singleton(authInfo), Collections.emptySet());

        Session session;

        try {

            session = Subject.doAsPrivileged(subject, new PrivilegedExceptionAction<Session>() {

                @Override
                public Session run() throws Exception {
                    return repository.login(null, null);
                }

            }, null);

        } catch (PrivilegedActionException e) {
            throw new LoginException(e.getMessage());
        }

        return session;
    }

    @Override
    public Subject getSubject() {
        return null;
    }

    @Override
    public void login() throws LoginException {

    }

    @Override
    public void logout() throws LoginException {

    }
    //endregion

}
