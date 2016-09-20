/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcpforwardport;

/**
 *
 * @author Alessio
 */
public enum KeysConnection {
    BUFFER_SIZE("buffer.size"),
    SOURCE_PORT("source.port"),
    DESTINATION_HOST("destination.host"),
    DESTINATION_PORT("destination.port");
    
    private String value;

    private KeysConnection(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeysConnection{" + "value=" + value + '}';
    }
    
}
