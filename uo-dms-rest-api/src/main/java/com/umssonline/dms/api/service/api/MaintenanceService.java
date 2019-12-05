package com.umssonline.dms.api.service.api;

import com.umssonline.dms.api.util.common.RepositoryOptions;

import javax.jcr.RepositoryException;

/**
 * @author franco.arratia@umssonline.com
 */
public interface MaintenanceService {

    void init(RepositoryOptions option) throws RepositoryException;
    void dispose();
}
