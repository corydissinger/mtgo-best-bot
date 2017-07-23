package com.cd.bot.client.system;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Created by Cory on 5/12/2017.
 */
public class ProcessManager {

    @Autowired
    private String executableDir;

    @Autowired
    private String executableName;

    private Process applicationProcess;

    public boolean isMtgoRunningOrLoading() {
        boolean isApplicationRunning = false;

        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if(line.contains(executableName.replace("*", ""))) {
                    isApplicationRunning = true;
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return isApplicationRunning;
    }

    public void startApplication() {
        try {
            applicationProcess = new ProcessBuilder(getExecutable()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExecutable() {
        File supposedExecutable = new File(executableDir + executableName);

        if(supposedExecutable.exists()) {
            return supposedExecutable.getAbsolutePath();
        } else {
            Collection<File> theExecutable = FileUtils.listFiles(new File(executableDir), new WildcardFileFilter(executableName), TrueFileFilter.INSTANCE);

            if(theExecutable != null || theExecutable.size() > 1 || theExecutable.isEmpty()) {
                File newestExecutable = theExecutable.stream().reduce(new File(""),
                        (aFile, newestFile) -> {
                            if(aFile.lastModified() > newestFile.lastModified()) {
                                return aFile;
                            }

                            return newestFile;
                        });

                return newestExecutable.getAbsolutePath();
            } else if(theExecutable.size() == 1) {
                return ((File)CollectionUtils.get(theExecutable, 0)).getAbsolutePath();
            } else {
                throw new RuntimeException("Could not determine executable path");
            }
        }
    }
}
