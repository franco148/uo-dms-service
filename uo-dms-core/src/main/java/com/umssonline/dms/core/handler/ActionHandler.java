package com.umssonline.dms.core.handler;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public interface ActionHandler {

    Object handle(Map<String, Object> params) throws RepositoryException, IOException, IllegalAccessException;
}
