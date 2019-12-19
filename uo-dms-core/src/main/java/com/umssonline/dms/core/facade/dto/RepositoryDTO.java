package com.umssonline.dms.core.facade.dto;

import com.umssonline.dms.core.handler.factory.ActionHandlerOptions;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author franco.arratia@umssonline.com
 */
public class RepositoryDTO {

    /** The params. */
    private Map<String, Object> params = new ConcurrentHashMap<String, Object>();

    /** The exception. */
    private Throwable exception;

    /** The result. */
    private Object result;

    /** The action type. */
    private ActionHandlerOptions actionType;

    //private ByteArrayOutputStream inputStream;
    private InputStream inputStream;

    /**
     * Gets the params.
     *
     * @return the params
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * Sets the params.
     *
     * @param params the new params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * Adds the param.
     *
     * @param key the key
     * @param paramValue the param value
     */
    public void addParam(String key, Object paramValue) {
        params.put(key, paramValue);
    }

    /**
     * Gets the param.
     *
     * @param index the index
     * @return the param
     */
    public Object getParam(int index) {
        return params.get(index);
    }

    /**
     * Gets the exception.
     *
     * @return the exception....
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * Sets the exception.
     *
     * @param exception the new exception
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * Sets the result.
     *
     * @param result the new result
     */
    public void setResult(Object result) {
        this.result = result;
    }


    /**
     * Gets the size.
     *
     * @return the size
     */
    public int getSize(){
        return params.size();
    }

    /**
     * Gets the action type.
     *
     * @return the action type
     */
    public ActionHandlerOptions getActionType() {
        return actionType;
    }

    /**
     * Sets the action type.
     *
     * @param actionType the new action type
     */
    public void setActionType(ActionHandlerOptions actionType) {
        this.actionType = actionType;
    }

    /*public ByteArrayOutputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ByteArrayOutputStream inputStream) {
        this.inputStream = inputStream;
    }*/

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
