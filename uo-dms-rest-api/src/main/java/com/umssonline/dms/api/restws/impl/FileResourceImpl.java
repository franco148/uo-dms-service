package com.umssonline.dms.api.restws.impl;

import com.umssonline.dms.api.restws.api.FileResourceController;
import com.umssonline.dms.api.service.api.CoreServiceAdapter;
import com.umssonline.dms.api.service.impl.SetJcrCoreAdapter;
import com.umssonline.dms.api.util.common.AppConstants;
import com.umssonline.dms.api.util.errorhandling.AppException;
import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author franco.arratia@umssonline.com
 */
public class FileResourceImpl implements FileResourceController {


    //region Fields
    private CoreServiceAdapter serviceAdapter;

    @Context
    private HttpServletRequest servletRequest;
    //endregion

    //region Constructors
    public FileResourceImpl() {
        serviceAdapter = new SetJcrCoreAdapter();
    }

    public FileResourceImpl(CoreServiceAdapter serviceAdapter) {
        this.serviceAdapter = serviceAdapter;
    }
    //endregion
    
    //region FileResourceController Members
    public Response getFiles() {

        try {

            Object schemaListResponse = serviceAdapter.getAllMetadata();
            return Response.ok(schemaListResponse).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }
    
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               @FormDataParam("parameters") String parameters) {

        // fileStructure parameter can not be empty, if so a BAD_REQUEST is returned.
        if (uploadedInputStream == null || fileDetail == null || parameters == null || parameters.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }


        try {

            Object response = serviceAdapter.upload(fileDetail, uploadedInputStream, servletRequest, parameters);
            return Response.ok(response).build();

        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response updateFile(String fileId,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               @FormDataParam("schema") FormDataBodyPart documentSchema) {

        if (fileId == null || fileId.isEmpty() || documentSchema == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object response = serviceAdapter.update(fileId, uploadedInputStream, documentSchema);
            return Response.ok(response).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileById(String fileId) {

        if (fileId == null || fileId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {
            Object fileResponse = serviceAdapter.get(fileId);
            return Response.ok(((DocBinaryInfo)fileResponse).getInputStream()).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileByIdForViewer(String fileId) {

        if (fileId == null || fileId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {
            DocBinaryInfo binaryByViewer = (DocBinaryInfo)serviceAdapter.get(fileId);
            StreamingOutput outputStream = getStreamingOutputFromInputStream(binaryByViewer.getInputStream());

            return Response.ok(outputStream, binaryByViewer.getMimeType())
                           .header("content-disposition", "inline; filename =" + binaryByViewer.getName())
                           .build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileSchemaById(String fileId) {

        if (fileId == null || fileId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {
            Object documentSchema = serviceAdapter.getMetadata(fileId);

            return Response.ok(documentSchema).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileByInclusiveQuery(String parameters) {
        if (parameters == null || parameters.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'parameters' can not be empty or null.",
                    "required properties - properties" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object response = serviceAdapter.getInclusiveBy(parameters);
            return Response.ok(response).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response deleteFile(String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {
            boolean wasFileDeleted = (boolean)serviceAdapter.delete(fileId);
            return Response.ok(wasFileDeleted).build();
        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response editFileProperty(String fileId,
                                     @FormDataParam("propertyName") String propertyName,
                                     @FormDataParam("propertyValue") FormDataBodyPart propertyValue) {

        if (fileId == null || fileId.isEmpty() || propertyName == null || propertyName.isEmpty() || propertyValue == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, None of them can be empty or null.",
                    "required properties - fileId, propertyName, propertyValue" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {
            Object response = serviceAdapter.editProperty(fileId, propertyName, propertyValue);
            return Response.ok(response).build();
        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response uploadFileVersion(String fileId,
                                      @FormDataParam("file") InputStream uploadedInputStream,
                                      @FormDataParam("file") FormDataContentDisposition fileDetail,
                                      @FormDataParam("title") String title,
                                      @FormDataParam("description") String description) {

        if (fileId == null || fileId.isEmpty() || uploadedInputStream == null || fileDetail == null || title == null) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object response = serviceAdapter.uploadVersion(fileId, fileDetail.getFileName(), title, description, uploadedInputStream, servletRequest.getContentLength());
            return Response.ok(response).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response updateFileVersion(String fileId,
                                      String versionId,
                                      @FormDataParam("file") InputStream uploadedInputStream,
                                      @FormDataParam("file") FormDataContentDisposition fileDetail,
                                      @FormDataParam("schema") FormDataBodyPart versionSchema) {

        if (fileId == null || fileId.isEmpty() || versionId == null || versionId.isEmpty() ||
            versionSchema == null) {

            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object response = serviceAdapter.updateVersion(fileId, versionId, uploadedInputStream, versionSchema);
            return Response.ok(response).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileVersions(String fileId) {

        if (fileId == null || fileId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object responseVersions = serviceAdapter.getAllVersions(fileId);
            return Response.ok(responseVersions).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response countFileVersions(String fileId, boolean includeRoot) {

        try {
            Integer response = serviceAdapter.countFileVersions(fileId, includeRoot);
            return Response.ok(response).build();
        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileVersionMetadata(String fileId, String versionId) {
        return null;
    }

    @Override
    public Response getFileVersionData(String fileId, String versionId) {

        if (fileId == null || fileId.isEmpty() || versionId == null || versionId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object response = serviceAdapter.getVersionData(fileId, versionId);
            return Response.ok(response).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response getFileVersionById(String fileId, String versionId) {

        if (fileId == null || fileId.isEmpty() || versionId == null || versionId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            DocBinaryInfo versionResponse = (DocBinaryInfo)serviceAdapter.getVersion(fileId, versionId);
            StreamingOutput outputStream = getStreamingOutputFromInputStream(versionResponse.getInputStream());
            return Response.ok(outputStream, versionResponse.getMimeType())
                           .header("content-disposition", "inline; filename =" + versionResponse.getName())
                           .build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response selectVersion(String fileId, String versionId) {

        if (fileId == null || fileId.isEmpty() || versionId == null || versionId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {

            Object wasSelected = serviceAdapter.selectVersion(fileId, versionId);
            return Response.ok(wasSelected).build();

        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    @Override
    public Response deleteFileVersion(String fileId, String versionId) {
        if (fileId == null || fileId.isEmpty() || versionId == null || versionId.isEmpty()) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'versionId' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }

        try {
            boolean wasVersionDeleted = (boolean)serviceAdapter.deleteVersion(fileId, versionId);
            return Response.ok(wasVersionDeleted).build();
        } catch (RepositoryException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IllegalAccessException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        } catch (IOException e) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Invalid parameters, 'File' and 'Title' can not be empty or null.",
                    "required properties - file, title" ,
                    AppConstants.BLOG_POST_URL);
        }
    }

    //endregion

    //region Utilities
    private StreamingOutput getStreamingOutputFromInputStream(InputStream inputStream) {
        //Converting an InputStream object to StreamingOutput to help with PREVIEW operations.
        StreamingOutput streamingResponse = output -> {
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
        };

        return streamingResponse;
    }
    //endregion
}
