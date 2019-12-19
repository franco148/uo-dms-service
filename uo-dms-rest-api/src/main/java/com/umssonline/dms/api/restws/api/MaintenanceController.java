package com.umssonline.dms.api.restws.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */

@Path("/api/v1/jcr-indexers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "dms-maintenance", description = "Maintenance on DH DMS Service")
public interface MaintenanceController {

    @Path("/init")
    @POST
    @ApiOperation(
            value = "Initialize",
            notes = "Initializes the connection to DMS database and starts indexing the whole database.",
            response = Map.class,
            httpMethod = "POST",
            position = 0
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 500, message = "Initialization could not be performed. Operation has failed")
            })
    Response init();

    @Path("/shutdown")
    @POST
    @ApiOperation(
            value = "Shutdown",
            notes = "Shutdowns the connection to DMS database when the service is going to turn off.",
            response = Map.class,
            httpMethod = "POST",
            position = 1
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 500, message = "Shut down process could not be performed. Operation has failed")
            })
    Response shutDown();
}
