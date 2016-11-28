package edu.suda.ada.ethereum;

import edu.suda.ada.core.BlockCache;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;

public class EthereumBean {

    public static Ethereum ethereum;

    private BlockCache container;

    public EthereumBean(BlockCache container){
        this.container = container;
    }
    public void start(){

        if (ethereum == null){
            this.ethereum = EthereumFactory.createEthereum();
            this.ethereum.addListener(new EthereumListener(ethereum, container));
        }
    }
}
