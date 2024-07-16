package com.jiahe.px.common.core.utils.dynamic;


import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class DynamicResource implements Resource {
    private DynamicBean dynamicBean;

    public DynamicResource(DynamicBean dynamicBean) {
        this.dynamicBean = dynamicBean;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(dynamicBean.getXml().getBytes("UTF-8"));
    }


    public Resource createRelative(String arg0) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean exists() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public File getFile() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFilename() {
        // TODO Auto-generated method stub
        return null;
    }

    public URI getURI() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getURL() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isReadable() {
        // TODO Auto-generated method stub
        return false;
    }

    public long lastModified() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    public long contentLength() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

}
