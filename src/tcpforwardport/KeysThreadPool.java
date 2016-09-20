/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcpforwardport;

/**
 *
 * @author a.oglialoro
 */
public enum KeysThreadPool {
    MINPOOLSIZE("minpoolsize"),
    MAXPOOLSIZE("maxpoolsize"),
    QUEUESIZE("queuesize"),
    KEEPALIVETIMEMILLISECONDS("keepalivetimemilliseconds");
    
    private String value;

    private KeysThreadPool(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeysThreadPool{" + "value=" + value + '}';
    }
    
    
}
