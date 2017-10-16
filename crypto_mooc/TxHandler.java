//package scroogeCoin_assignment_1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
//import scroogeCoin_assignment_1.Transaction.Output;

public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    
    UTXOPool pool = null;
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        pool = new UTXOPool(utxoPool);
        
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
        
        UTXOPool newUTXO = new UTXOPool();
        
        double prevSum = 0;
        double currSum = 0;
        
        for (int i = 0; i < tx.numInputs(); i++) {
            // Iterate through each transaction, get input
            Transaction.Input in = tx.getInput(i);
            
            // Create new unspent transaction output, compare it with pool contents.
            UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
            Transaction.Output output = pool.getTxOutput(utxo);
            if (!pool.contains(utxo)) return false;
            
            // verify signature of output
            if (!Crypto.verifySignature(output.address, tx.getRawDataToSign(i), in.signature))
                return false;
            
            // remove double spends
            if (newUTXO.contains(utxo)) return false;
            newUTXO.addUTXO(utxo, output);
            prevSum += output.value;
        }
        
        // all o/p values are non-negative
        for (Transaction.Output out : tx.getOutputs()) {
            if (out.value < 0) return false;
            currSum += out.value;
        }
        return prevSum >= currSum;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        
        Set<Transaction> txsToBeAdded = new HashSet<>();
        
        for (Transaction trans : possibleTxs) {
            // verify isValidTransaction
            if(isValidTx(trans)) {
                txsToBeAdded.add(trans);
            }
            // remove trans from pool.
            for (Transaction.Input tr : trans.getInputs()) {
                UTXO utxo = new UTXO(tr.prevTxHash, tr.outputIndex);
                pool.removeUTXO(utxo);
            }
            
            for (int i = 0; i < trans.numOutputs(); i++) {
                Transaction.Output out = trans.getOutput(i);
                UTXO utxo = new UTXO(trans.getHash(), i);
                pool.addUTXO(utxo, out);
            }
        }
        Transaction[] validTxArray = new Transaction[txsToBeAdded.size()];
        return txsToBeAdded.toArray(validTxArray);
    }
}
