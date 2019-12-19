package com.umssonline.dms.api.service.impl;

import com.umssonline.dms.api.service.api.MaintenanceService;
import com.umssonline.dms.api.util.common.RepositoryOptions;
import com.umssonline.dms.core.facade.DmsCoreFacade;

import javax.jcr.RepositoryException;

/**
 * @author franco.arratia@umssonline.com
 */
public class MaintenanceServiceImpl implements MaintenanceService {
    @Override
    public void init(RepositoryOptions option) throws RepositoryException {
        DmsCoreFacade dmsCoreFacade = DmsCoreFacade.getInstance();

        if (option == RepositoryOptions.RE_INDEX_REPOSITORY)
            dmsCoreFacade.createIndexForMigration("");
    }

    @Override
    public void dispose() {
        DmsCoreFacade dmsCoreFacade = DmsCoreFacade.getInstance();
        dmsCoreFacade.disposeRepository();
    }
}
