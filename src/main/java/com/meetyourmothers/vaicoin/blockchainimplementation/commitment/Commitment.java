/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meetyourmothers.vaicoin.blockchainimplementation.commitment;

/**
 *
 * @author vavasing
 * 
 * Interface for Commitment API.
 */
public interface Commitment {
    /**
     * Takes in the message as a string and returns a hashed string containing 
     * digest or commitment and the key appended to each other.
     * @param msg
     * @return 
     */
    public String commit(String msg);
    
    /**
     * Verifies whether the combination of key and digest matches the decrypted 
     * message.
     * 
     * @param com
     * @param key
     * @param msg
     * @return 
     */
    public boolean verify (String com, String key, String msg);
    
    // TODO: Add publish methods in Publish api.
}
