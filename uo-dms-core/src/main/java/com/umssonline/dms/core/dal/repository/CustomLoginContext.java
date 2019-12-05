package com.umssonline.dms.core.dal.repository;

import org.apache.jackrabbit.oak.spi.security.authentication.LoginContext;

import javax.jcr.Session;
import javax.security.auth.login.LoginException;
import java.io.Serializable;

/**
 * @author franco.arratia@umssonline.com
 */
public interface CustomLoginContext<ID extends Serializable> extends LoginContext {

    Session login(ID userId) throws LoginException;
}
