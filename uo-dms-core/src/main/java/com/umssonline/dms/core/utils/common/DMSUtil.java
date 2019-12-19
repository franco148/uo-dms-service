package com.umssonline.dms.core.utils.common;

/**
 * @author franco.arratia@umssonline.com
 */
public class DMSUtil {

    public static String getMimeTypeByFileName(String fileName) {
        String mimetype = "application/octet-stream";
        fileName = fileName.toLowerCase();
        if(fileName.endsWith(".txt")) {
            mimetype = "text/plain";
        } else if(fileName.endsWith(".doc")) {
            mimetype = "application/msword";
        } else if(fileName.endsWith(".docx")) {
            mimetype = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if(fileName.endsWith(".xls")) {
            mimetype = "application/vnd.ms-excel";
        } else if(fileName.endsWith(".xlsx")) {
            mimetype = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if(fileName.endsWith(".ppt")) {
            mimetype = "application/mspowerpoint";
        } else if(fileName.endsWith(".pptx")) {
            mimetype = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        } else if(fileName.endsWith(".pdf")) {
            mimetype = "application/pdf";
        } else if(fileName.endsWith(".rtf")) {
            mimetype = "application/rtf";
        } else if(fileName.endsWith(".htm")) {
            mimetype = "text/html";
        } else if(fileName.endsWith(".html")) {
            mimetype = "text/html";
        } else if(fileName.endsWith(".mht")) {
            mimetype = "text/html";
        } else if(fileName.endsWith(".cfm")) {
            mimetype = "text/html";
        } else if(fileName.endsWith(".jsp")) {
            mimetype = "text/html";
        } else if(fileName.endsWith(".jspx")) {
            mimetype = "text/html";
        } else if(fileName.endsWith(".xml")) {
            mimetype = "text/xml";
        } else if(fileName.endsWith(".xsl")) {
            mimetype = "text/xml";
        } else if(fileName.endsWith(".java")) {
            mimetype = "text/plain";
        } else if(fileName.endsWith(".bat")) {
            mimetype = "text/plain";
        } else if(fileName.endsWith(".ods")) {
            mimetype = "application/vnd.oasis.opendocument.spreadsheet";
        } else if(fileName.endsWith(".odt")) {
            mimetype = "application/vnd.oasis.opendocument.text";
        } else if(fileName.endsWith(".odf")) {
            mimetype = "application/vnd.oasis.opendocument.formula";
        } else if(fileName.endsWith(".odg")) {
            mimetype = "application/vnd.oasis.opendocument.graphics";
        } else if(fileName.endsWith(".odd")) {
            mimetype = "application/vnd.oasis.opendocument.database";
        } else if(fileName.endsWith(".odp")) {
            mimetype = "application/vnd.oasis.opendocument.presentation";
        } else if(fileName.endsWith(".jpg")) {
            mimetype = "image/jpeg";
        } else if(fileName.endsWith(".jpeg")) {
            mimetype = "image/jpeg";
        } else if(fileName.endsWith(".gif")) {
            mimetype = "image/gif";
        } else if(fileName.endsWith(".png")) {
            mimetype = "image/png";
        }

        return mimetype;
    }
}
