package com.ley.springboot.generator;

import java.io.*;
import java.util.Properties;

import com.ley.springboot.generator.utils.GeneratorConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * common page parser
 **/
@Slf4j
public class CommonPageParser {

    private static VelocityEngine velocityEngine;

    public static void writerPage(VelocityContext context, String templateName, String fileDirPath, String targetFile) {
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        try {
            File e = new File(fileDirPath);
            File file = new File(e, targetFile);
            FileUtils.forceMkdir(file.getParentFile());
            if (file.exists()) {
                log.info("替换文件:" + file.getAbsolutePath());
            }
            Template template = velocityEngine.getTemplate(templateName, GeneratorConstants.DEFAULT_ENCODING);
            fos = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(fos, GeneratorConstants.DEFAULT_ENCODING));
            template.merge(context, writer);
            log.info("生成文件：" + file.getAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    static {
        String templateBasePath = DbCodeGenerateFactory.getProjectPath() + "src/main/resources/templates";
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
        properties.setProperty("file.resource.loader.path", templateBasePath);
        properties.setProperty("file.resource.loader.cache", "true");
        properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
        properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.Log4JLogChute");
        properties.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
        properties.setProperty("directive.set.null.allowed", "true");
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(properties);
        CommonPageParser.velocityEngine = velocityEngine;
    }
}