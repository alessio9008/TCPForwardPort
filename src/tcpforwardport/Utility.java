/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpforwardport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Alessio
 */
public class Utility {

    private Properties utilityProperties;

    private static Utility utility;

    private ThreadPoolExecutor threadExecutor;

    public static Utility getInstanceUtility(File fp) {
        if (utility == null) {
            synchronized (Utility.class) {
                if (utility == null) {
                    utility = new Utility(fp);
                    return utility;
                }
            }
        }
        return utility;
    }

    public static Utility getInstanceUtility() {
        return getInstanceUtility(null);
    }

    private Utility() {
        init();
    }

    private Utility(File fp) {
        this();
        File f = checkFile(fp);
        if (f != null) {
            try {
                utilityProperties.load(new FileInputStream(f));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void init() {
        initConnection();
        initThread();
    }

    private synchronized void initConnection() {
        if (utilityProperties == null) {
            utilityProperties = new Properties();
        }
        setProperty(KeysConnection.BUFFER_SIZE.getValue(), "8192");
        setProperty(KeysConnection.SOURCE_PORT.getValue(), "1000");
        setProperty(KeysConnection.DESTINATION_HOST.getValue(), "localhost");
        setProperty(KeysConnection.DESTINATION_PORT.getValue(), "15000");
    }

    private synchronized void initThread() {
        setProperty(KeysThreadPool.MINPOOLSIZE.getValue(), "15");
        setProperty(KeysThreadPool.MAXPOOLSIZE.getValue(), "45");
        setProperty(KeysThreadPool.QUEUESIZE.getValue(), Integer.toString(Integer.MAX_VALUE));
        setProperty(KeysThreadPool.KEEPALIVETIMEMILLISECONDS.getValue(), "10000");
        if (threadExecutor == null) {
            threadExecutor = new ThreadPoolExecutor(Integer.parseInt(getProperty(KeysThreadPool.MINPOOLSIZE.getValue())), Integer.parseInt(getProperty(KeysThreadPool.MAXPOOLSIZE.getValue())), Integer.parseInt(getProperty(KeysThreadPool.KEEPALIVETIMEMILLISECONDS.getValue())), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(Integer.parseInt(getProperty(KeysThreadPool.QUEUESIZE.getValue()))));
        }

    }

    private synchronized File checkFile(File file) {
        if (file == null) {
            return null;
        }
        if (file.exists() && file.isFile()) {
            return file;
        }
        try {
            file.createNewFile();
            try {
                utilityProperties.store(new FileOutputStream(file), "Property for TCP forward port");
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public synchronized Object setProperty(String key, String value) {
        return utilityProperties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return utilityProperties.getProperty(key);
    }

    public String toString() {
        return utilityProperties.toString();
    }

    public Set<Object> keySet() {
        return utilityProperties.keySet();
    }

    public ThreadPoolExecutor getThreadExecutor() {
        return threadExecutor;
    }

}
