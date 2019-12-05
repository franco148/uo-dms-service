package com.umssonline.dms.core;

import com.umssonline.dms.core.dal.dao.VersionSchema;
import com.umssonline.dms.core.facade.DmsCoreFacade;
import com.umssonline.dms.core.handler.factory.ActionHandlerOptions;
import com.umssonline.dms.core.dal.dao.DocumentSchema;
import com.umssonline.dms.core.facade.dto.RepositoryDTO;
import com.umssonline.dms.core.utils.constants.DMSConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author franco.arratia@umssonline.com
 */
public class MainUpload {

    public static void main(String args[]) throws Exception {

        Map<String, Object> fileProperties = new HashMap<>();

        DmsCoreFacade dmsCoreFacade = DmsCoreFacade.getInstance();
        RepositoryDTO repo = new RepositoryDTO();

        //---------------------------------------------- SAVE FILE -----------------------------------
        //File filePath = new File("/Users/franco.fral/SourceCode/Tests/Resources/files/GoogleAPIs.pdf");
        File filePath = new File("D:\\GoogleAPIs.pdf");
        InputStream is = new FileInputStream(filePath);

        String fileName = filePath.getName();
        String fileTitle = "Test title file";
        String description = "Test nodes, and versioning features...";
        Long fileSize = filePath.length();

        fileProperties.put("name", fileName);
        fileProperties.put("title", fileTitle);
        fileProperties.put("description", description);
        fileProperties.put("mimeType", "application/pdf");
        fileProperties.put("createdBy", UUID.randomUUID().toString());
        fileProperties.put("modifiedBy", UUID.randomUUID().toString());
        fileProperties.put("version", 1L);
        fileProperties.put("extension", "PDF");
        fileProperties.put("size", fileSize);

        fileProperties.put("fieldId", UUID.randomUUID());
        fileProperties.put("sectionId", UUID.randomUUID());
        fileProperties.put("subformId", UUID.randomUUID());
        fileProperties.put("treeId", UUID.randomUUID());
        fileProperties.put("formId", UUID.randomUUID());
        fileProperties.put("schemaId", UUID.randomUUID());

        repo.setActionType(ActionHandlerOptions.DMS_UPLOAD);
        repo.setParams(fileProperties);
        repo.setInputStream(is);
        Object response = dmsCoreFacade.executeAction(repo);
        System.out.println("Result is null: " + response == null);

        //-------------------------------------- UPLOAD VERSION 1 FILE 1 -------------------------------
        filePath = new File("D:\\MyFile.pdf");
        //filePath = new File("/Users/franco.fral/SourceCode/Tests/Resources/files/MyFile.pdf");
        is = new FileInputStream(filePath);

        fileName = filePath.getName();
        fileTitle = "Test1 title1 file1";
        description = "Test nodes, and versioning features...version1";
        fileSize = filePath.length();

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put("name", fileName);
        fileProperties.put("title", fileTitle);
        fileProperties.put("description", description);
        fileProperties.put("mimeType", "application/pdf");
        fileProperties.put("createdBy", UUID.randomUUID().toString());
        fileProperties.put("modifiedBy", UUID.randomUUID().toString());
        fileProperties.put("version", 2L);
        fileProperties.put("extension", "PDF");
        fileProperties.put("size", fileSize);
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());

        repo.setActionType(ActionHandlerOptions.DMS_UPLOAD_VERSION);
        repo.setParams(fileProperties);
        repo.setInputStream(is);
        Object version1File1 = dmsCoreFacade.executeAction(repo);
        System.out.println("Result is null: " + version1File1 == null);

        //-------------------------------------- UPLOAD VERSION 2 FILE 1 -------------------------------
        //filePath = new File("/Users/franco.fral/SourceCode/Tests/Resources/files/ErrorHandling.pdf");
        filePath = new File("D:\\ErrorHandling.pdf");
        is = new FileInputStream(filePath);

        fileName = filePath.getName();
        fileTitle = "Test2 title2 file2";
        description = "Test nodes, and versioning features...version2";
        fileSize = filePath.length();

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put("name", fileName);
        fileProperties.put("title", fileTitle);
        fileProperties.put("description", description);
        fileProperties.put("mimeType", "application/pdf");
        fileProperties.put("createdBy", UUID.randomUUID().toString());
        fileProperties.put("modifiedBy", UUID.randomUUID().toString());
        fileProperties.put("version", 3L);
        fileProperties.put("extension", "PDF");
        fileProperties.put("size", fileSize);
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());

        repo.setActionType(ActionHandlerOptions.DMS_UPLOAD_VERSION);
        repo.setParams(fileProperties);
        repo.setInputStream(is);
        Object version2File1 = dmsCoreFacade.executeAction(repo);
        System.out.println("Result is null: " + version2File1 == null);

        //-------------------------------------- UPLOAD VERSION 3 FILE 1 -------------------------------
        //filePath = new File("/Users/franco.fral/SourceCode/Tests/Resources/files/Styles.pdf");
        filePath = new File("D:\\Styles.pdf");
        is = new FileInputStream(filePath);

        fileName = filePath.getName();
        fileTitle = "Test2 title2 file2";
        description = "Test nodes, and versioning features...version2";
        fileSize = filePath.length();

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put("name", fileName);
        fileProperties.put("title", fileTitle);
        fileProperties.put("description", description);
        fileProperties.put("mimeType", "application/pdf");
        fileProperties.put("createdBy", UUID.randomUUID().toString());
        fileProperties.put("modifiedBy", UUID.randomUUID().toString());
        fileProperties.put("version", 3L);
        fileProperties.put("extension", "PDF");
        fileProperties.put("size", fileSize);
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());

        repo.setActionType(ActionHandlerOptions.DMS_UPLOAD_VERSION);
        repo.setParams(fileProperties);
        repo.setInputStream(is);
        Object version3File1 = dmsCoreFacade.executeAction(repo);
        System.out.println("Result is null: " + version3File1 == null);

        //-------------------------------------------- SAVE FILE 2 -------------------------------------
        //filePath = new File("/Users/franco.fral/SourceCode/Tests/Resources/files/spring_tutorial.pdf");
        filePath = new File("D:\\spring_tutorial.pdf");
        is = new FileInputStream(filePath);

        fileName = filePath.getName();
        fileTitle = "Test title file - SecondFile";
        description = "Test nodes, and versioning features...File2222222222222222222222";
        fileSize = filePath.length();

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put("name", fileName);
        fileProperties.put("title", fileTitle);
        fileProperties.put("description", description);
        fileProperties.put("mimeType", "application/pdf");
        fileProperties.put("createdBy", UUID.randomUUID().toString());
        fileProperties.put("modifiedBy", UUID.randomUUID().toString());
        fileProperties.put("version", 1L);
        fileProperties.put("extension", "PDF");
        fileProperties.put("size", fileSize);

        fileProperties.put("fieldId", UUID.randomUUID());
        fileProperties.put("sectionId", UUID.randomUUID());
        fileProperties.put("subformId", UUID.randomUUID());
        fileProperties.put("treeId", UUID.randomUUID());
        fileProperties.put("formId", UUID.randomUUID());
        fileProperties.put("schemaId", UUID.randomUUID());

        repo.setActionType(ActionHandlerOptions.DMS_UPLOAD);
        repo.setParams(fileProperties);
        repo.setInputStream(is);
        Object response2 = dmsCoreFacade.executeAction(repo);
        System.out.println("Result is null: " + response2 == null);

        //-------------------------------------- UPLOAD VERSION 1 FILE 2 -------------------------------
        //filePath = new File("/Users/franco.fral/SourceCode/Tests/Resources/files/UML.pdf");
        filePath = new File("D:\\UML.pdf");
        is = new FileInputStream(filePath);

        fileName = filePath.getName();
        fileTitle = "Test2 title2 file2";
        description = "Test nodes, and versioning features...version2";
        fileSize = filePath.length();

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put("name", fileName);
        fileProperties.put("title", fileTitle);
        fileProperties.put("description", description);
        fileProperties.put("mimeType", "application/pdf");
        fileProperties.put("createdBy", UUID.randomUUID().toString());
        fileProperties.put("modifiedBy", UUID.randomUUID().toString());
        fileProperties.put("version", 3L);
        fileProperties.put("extension", "PDF");
        fileProperties.put("size", fileSize);
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response2).getId());

        repo.setActionType(ActionHandlerOptions.DMS_UPLOAD_VERSION);
        repo.setParams(fileProperties);
        repo.setInputStream(is);
        Object version1File2 = dmsCoreFacade.executeAction(repo);
        System.out.println("Result is null: " + version1File2 == null);

        //-------------------------------------------- GET ALL FILES -----------------------------------
        repo.setActionType(ActionHandlerOptions.DMS_ALL_SCHEMES);
        Object allMetadata = dmsCoreFacade.executeAction(repo);
        System.out.println(allMetadata);

        //------------------------------------------ GET SPECIFIC FILE ---------------------------------
        fileProperties = new HashMap<>();
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());
        repo.setParams(fileProperties);
        repo.setActionType(ActionHandlerOptions.DMS_DOWNLOAD);
        Object documentInfo = dmsCoreFacade.executeAction(repo);
        System.out.println(documentInfo);

        //----------------------------------------- GET SPECIFIC VERSION -------------------------------

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());
        fileProperties.put(DMSConstants.DMS_VERSION_ID, ((VersionSchema)version1File1).getVersionId());
        repo.setParams(fileProperties);
        repo.setActionType(ActionHandlerOptions.DMS_DOWNLOAD_VERSION);
        Object versionInfo = dmsCoreFacade.executeAction(repo);
        System.out.println(versionInfo);

        //----------------------------------------- GET ALL VERSION OF A FILE -------------------------------

        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());
        repo.setParams(fileProperties);
        repo.setActionType(ActionHandlerOptions.DMS_ALL_VERSIONS);
        Object versionsInfo = dmsCoreFacade.executeAction(repo);
        System.out.println(versionsInfo);

        //----------------------------------------- GET ALL VERSION OF A FILE -------------------------------
        fileProperties = new HashMap<>();
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());
        repo.setParams(fileProperties);
        repo.setActionType(ActionHandlerOptions.DMS_SCHEMA);
        Object verifyStatus = dmsCoreFacade.executeAction(repo);
        System.out.println(verifyStatus);


        fileProperties = new HashMap<>();
        repo = new RepositoryDTO();

        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());
        fileProperties.put(DMSConstants.DMS_VERSION_ID, ((VersionSchema)version2File1).getVersionId());
        repo.setParams(fileProperties);
        repo.setActionType(ActionHandlerOptions.DMS_SELECT_VERSION);
        Object changedVersion = dmsCoreFacade.executeAction(repo);
        System.out.println(changedVersion);

        fileProperties = new HashMap<>();
        fileProperties.put(DMSConstants.DMS_ID, ((DocumentSchema)response).getId());
        repo.setParams(fileProperties);
        repo.setActionType(ActionHandlerOptions.DMS_SCHEMA);
        Object documentInfo2 = dmsCoreFacade.executeAction(repo);
        System.out.println(documentInfo2);
    }
}
