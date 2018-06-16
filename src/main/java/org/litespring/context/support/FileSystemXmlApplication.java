package org.litespring.context.support;

import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

public class FileSystemXmlApplication extends AbstractApplicationContext {


    public FileSystemXmlApplication(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourcePath(String path) {
        return new FileSystemResource(path);
    }
}
