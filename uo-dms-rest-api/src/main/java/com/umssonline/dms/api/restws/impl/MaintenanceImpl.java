package com.umssonline.dms.api.restws.impl;

import com.umssonline.dms.api.restws.api.MaintenanceController;
import com.umssonline.dms.api.service.api.MaintenanceService;
import com.umssonline.dms.api.service.impl.MaintenanceServiceImpl;
import com.umssonline.dms.api.util.common.AppConstants;
import com.umssonline.dms.api.util.common.RepositoryOptions;
import com.umssonline.dms.api.util.errorhandling.AppException;

import javax.jcr.RepositoryException;
import javax.ws.rs.core.Response;

/**
 * @author franco.arratia@umssonline.com
 */
public class MaintenanceImpl implements MaintenanceController {

    //region Fields
    private MaintenanceService maintenanceService;
    //endregion

    //region Constructors
    public MaintenanceImpl() {
        maintenanceService = new MaintenanceServiceImpl();

        try {
            initRepository(RepositoryOptions.INITIALIZE);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Initialization could not be performed. Operation has failed.",
                    "Dev Message: " + e.getMessage(),
                    AppConstants.BLOG_POST_URL);
        }
    }
    //endregion

    //region Maintenance Members
    @Override
    public Response init() {
        try {

            maintenanceService.init(RepositoryOptions.RE_INDEX_REPOSITORY);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Shut down process could not be performed. Operation has failed.",
                    "Dev Message: " + e.getMessage(),
                    AppConstants.BLOG_POST_URL);
        }

        return Response.ok("{\"status\":\"Repository has been re indexed.\"}").build();
    }

    @Override
    public Response shutDown() {
        maintenanceService.dispose();

        return Response.ok("{\"status\":\"Repository has been shut down.\"}").build();
    }
    //endregion

    //region Methods
    private void initRepository(RepositoryOptions option) throws RepositoryException {
        maintenanceService.init(option);
    }
    //endregion
}
