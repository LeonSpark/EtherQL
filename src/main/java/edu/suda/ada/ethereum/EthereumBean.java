package edu.suda.ada.ethereum;

import edu.suda.ada.api.BlockchainTemplate.AccountTemplate;
import edu.suda.ada.api.BlockchainTemplate.BlockTemplate;
import edu.suda.ada.api.BlockchainTemplate.TransactionTemplate;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;


public class EthereumBean {

    Ethereum ethereum;

    private BlockTemplate blockTemplate;
    private AccountTemplate accountTemplate;
    private TransactionTemplate transactionTemplate;

    public EthereumBean(BlockTemplate blockTemplate,
                        AccountTemplate accountTemplate,
                        TransactionTemplate transactionTemplate){
        this.blockTemplate = blockTemplate;
        this.accountTemplate = accountTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public void start(){
        this.ethereum = EthereumFactory.createEthereum();
        this.ethereum.addListener(new EthereumListener(ethereum, blockTemplate, accountTemplate, transactionTemplate));
    }
}
