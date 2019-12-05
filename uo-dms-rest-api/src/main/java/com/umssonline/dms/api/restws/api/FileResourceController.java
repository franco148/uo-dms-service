package com.umssonline.dms.api.restws.api;

import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.dal.dao.DocumentDao;
import com.umssonline.dms.core.dal.dao.DocumentSchema;
import com.umssonline.dms.core.dal.dao.VersionSchema;
import io.swagger.annotations.*;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */

@Component
@Path("/api/v1/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "dms-api", description = "Operations with files on SET DMS Service")
public interface FileResourceController {


    @GET
    @ApiOperation(
            value = "Get all files",
            notes = "Gets all files stored in the database.",
            response = List.class,
            httpMethod = "GET",
            position = 0
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Resources could not be found."),
                    @ApiResponse(code = 500, message = "Process could not be performed.")
            })
    Response getFiles();


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;response-pass-through=true"})
    @ApiOperation(
            value = "Upload file",
            notes = "Uploads a file.",
            response = DocumentSchema.class,
            httpMethod = "POST",
            position = 1
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid parameters, 'File' and 'Schema' can not be empty or null."),
                    @ApiResponse(code = 500, message = "File could not be saved. Upload operation has failed.")
            })
    Response uploadFile(@ApiParam(value = "InputStream of the file to be uploaded", required = true, name = "file")
                        @FormDataParam("file") InputStream uploadedInputStream,
                        @FormDataParam("file") FormDataContentDisposition fileDetail,
                        @ApiParam(value = "All parameters of the file to be uploaded", required = true, name = "parameters")
                        @FormDataParam("parameters") String parameters);


    @Path("/{id}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;response-pass-through=true"})
    @ApiOperation(
            value = "Update file",
            notes = "Updates a file data.",
            response = DocumentSchema.class,
            httpMethod = "POST",
            position = 1
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid parameters, 'File' and 'Schema' can not be empty or null."),
                    @ApiResponse(code = 500, message = "File could not be saved. Upload operation has failed.")
            })
    Response updateFile(@ApiParam(value = "ID of the file that needs to be fetched to update its data information", required = true, name = "id")
                        @PathParam("id")String fileId,
                        @ApiParam(value = "InputStream of the file to be uploaded", required = true, name = "file")
                        @FormDataParam("file") InputStream uploadedInputStream,
                        @FormDataParam("file") FormDataContentDisposition fileDetail,
                        @ApiParam(value = "Description of the file to be uploaded", required = true, name = "schema")
                        @FormDataParam("schema") FormDataBodyPart documentSchema);


    @Path("/{id}")
    @GET
    @ApiOperation(
            value = "Get file",
            notes = "Gets the specified version of a file.",
            response = DocBinaryInfo.class,
            httpMethod = "GET",
            position = 2
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid parameters, 'Id' and 'Version' can not be empty or null."),
                    @ApiResponse(code = 500, message = "File could not be processed. Operation has failed")
            })
    Response getFileById(@ApiParam(value = "ID of the file that needs to be fetched to get its version", required = true, name = "id")
                         @PathParam("id")String fileId);


    @Path("/{id}/view")
    @GET
    @ApiOperation(
            value = "Get file",
            notes = "Gets the specified version of a file. The returned resource can be displayed directly in a document viewer.",
            response = DocBinaryInfo.class,
            httpMethod = "GET",
            position = 3
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid parameters, 'Id' and 'Version' can not be empty or null."),
                    @ApiResponse(code = 500, message = "File could not be processed. Operation has failed")
            })
    Response getFileByIdForViewer(@ApiParam(value = "ID of the file that needs to be fetched to get its version", required = true, name = "id")
                                  @PathParam("id")String fileId);


    @Path("/{id}/schema")
    @GET
    @ApiOperation(
            value = "Get file",
            notes = "Gets the specified file schema.",
            response = DocumentSchema.class,
            httpMethod = "GET",
            position = 4
    )
    Response getFileSchemaById(@ApiParam(value = "ID of the file that needs to be fetched", required = true, name = "id")
                               @PathParam("id") String fileId);


    @Path("/inclusive/query")
    @GET
    @ApiOperation(
            value = "Query files",
            notes = "Gets files based on specified properties.",
            response = List.class,
            httpMethod = "GET",
            position = 5
    )
    Response getFileByInclusiveQuery(@ApiParam(value = "parameters to query the files that need to be fetched", required = true, name = "parameters")
                                     @QueryParam("parameters") String parameters);


    @Path("/{id}")
    @DELETE
    @ApiOperation(
            value = "Delete File",
            notes = "Deletes a specified file.",
            response = String.class,
            httpMethod = "DELETE",
            position = 6
    )
    Response deleteFile(@ApiParam(value = "ID of the file that needs to be fetched to be deleted", required = true, name = "id")
                        @PathParam("id")String fileId);


    @Path("/{id}/edit")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;response-pass-through=true"})
    @ApiOperation(
            value = "Add or Edit a document property",
            notes = "Add a new property o edits a existing one.",
            response = String.class,
            httpMethod = "POST"
    )
    Response editFileProperty(@ApiParam(value = "ID of the file that needs to be fetched to be edited", required = true, name = "id")
                              @PathParam("id")String fileId,
                              @ApiParam(value = "Name of the property to be added or edited", required = true, name = "propertyName")
                              @FormDataParam("propertyName") String propertyName,
                              @ApiParam(value = "Value of the property to be added or edited", required = true, name = "propertyValue")
                              @FormDataParam("propertyValue") FormDataBodyPart propertyValue);


    @Path("/{id}/versions")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;response-pass-through=true"})
    @ApiOperation(
            value = "Upload file version",
            notes = "Uploads a new file version.",
            response = VersionSchema.class,
            httpMethod = "POST",
            position = 7
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid parameters, 'File' and 'Schema' can not be empty or null."),
                    @ApiResponse(code = 500, message = "File could not be saved. Upload operation has failed.")
            })
    Response uploadFileVersion(@ApiParam(value = "ID of the file that needs to be fetched to get its version", required = true, name = "id")
                               @PathParam("id")String fileId,
                               @ApiParam(value = "InputStream of the file to be uploaded", required = true, name = "file")
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               @ApiParam(value = "Title of the file to be uploaded", required = true, name = "title")
                               @FormDataParam("title") String title,
                               @ApiParam(value = "Description of the file to be uploaded", required = true, name = "description")
                               @FormDataParam("description") String description);


    @Path("/{id}/versions/{version}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"application/json;response-pass-through=true"})
    @ApiOperation(
            value = "Update file version",
            notes = "Updates a file version data information.",
            response = VersionSchema.class,
            httpMethod = "POST",
            position = 7
    )
    @ApiResponses(value =
            {
                    @ApiResponse(code = 400, message = "Invalid parameters, 'File' and 'Schema' can not be empty or null."),
                    @ApiResponse(code = 500, message = "File could not be saved. Upload operation has failed.")
            })
    Response updateFileVersion(@ApiParam(value = "ID of the file that needs to be fetched to update its version data information.", required = true, name = "id")
                               @PathParam("id")String fileId,
                               @ApiParam(value = "Version ID of the file that needs to be fetched", required = true, name = "version")
                               @PathParam("version") String versionId,
                               @ApiParam(value = "InputStream of the file to be uploaded", required = true, name = "file")
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               @ApiParam(value = "Description of the file to be uploaded", required = true, name = "schema")
                               @FormDataParam("schema") FormDataBodyPart versionSchema);


    @Path("/{id}/versions")
    @GET
    @ApiOperation(
            value = "Get All Versions",
            notes = "Gets all versions of a specific file.",
            response = List.class,
            httpMethod = "GET",
            position = 8
    )
    Response getFileVersions(@ApiParam(value = "ID of the file that needs to be feched to get its version", required = true, name = "id")
                             @PathParam("id")String fileId);


    @Path("/{id}/versions/{version}/schema")
    @GET
    @ApiOperation(
            value = "Get file version",
            notes = "Gets the specified version.",
            response = VersionSchema.class,
            httpMethod = "GET",
            position = 9
    )
    Response getFileVersionMetadata(@ApiParam(value = "ID of the file that needs to be fetched to get its specified version", required = true, name = "id")
                                    @PathParam("id") String fileId,
                                    @ApiParam(value = "Version ID of the file that needs to be fetched", required = true, name = "version")
                                    @PathParam("version") String versionId);


    @Path("/{id}/versions/{version}/data")
    @GET
    @ApiOperation(
            value = "Get file version",
            notes = "Gets the specified version.",
            response = DocumentDao.class,
            httpMethod = "GET",
            position = 9
    )
    Response getFileVersionData(@ApiParam(value = "ID of the file that needs to be fetched to get its specified version", required = true, name = "id")
                                @PathParam("id") String fileId,
                                @ApiParam(value = "Version ID of the file that needs to be fetched", required = true, name = "version")
                                @PathParam("version") String versionId);


    @Path("/{id}/versions/{version}")
    @GET
    @ApiOperation(
            value = "Get file version",
            notes = "Gets the specified version.",
            response = String.class,
            httpMethod = "GET",
            position = 10
    )
    Response getFileVersionById(@ApiParam(value = "ID of the file that needs to be fetched to get its specified version", required = true, name = "id")
                                @PathParam("id") String fileId,
                                @ApiParam(value = "Version ID of the file that needs to be fetched", required = true, name = "version")
                                @PathParam("version") String versionId);


    @Path("/{id}/versions/{version}/change")
    @POST
    @ApiOperation(
            value = "Select version",
            notes = "Sets a specified version as a default one.",
            response = String.class,
            httpMethod = "POST",
            position = 11
    )
    Response selectVersion(@ApiParam(value = "ID of the file that needs to be fetched", required = true, name = "id")
                           @PathParam("id") String fileId,
                           @ApiParam(value = "Version ID of the file that needs to be fetched", required = true, name = "version")
                           @PathParam("version") String versionId);


    @Path("/{id}/versions/{version}")
    @DELETE
    @ApiOperation(
            value = "Delete File Version",
            notes = "Deletes a specified file version.",
            response = String.class,
            httpMethod = "DELETE",
            position = 12
    )
    Response deleteFileVersion(@ApiParam(value = "ID of the file that needs to be fetched to be deleted", required = true, name = "id")
                               @PathParam("id")String fileId,
                               @ApiParam(value = "Version ID of the file that needs to be fetched", required = true, name = "version")
                               @PathParam("version") String versionId);
}
