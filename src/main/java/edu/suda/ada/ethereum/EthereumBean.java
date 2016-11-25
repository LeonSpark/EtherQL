package edu.suda.ada.ethereum;

import edu.suda.ada.core.BlockContainer;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;

public class EthereumBean {

    public static Ethereum ethereum;

    private BlockContainer container;

    public EthereumBean(BlockContainer container){
        this.container = container;
    }
    public void start(){

        if (ethereum == null){
            this.ethereum = EthereumFactory.createEthereum();
            this.ethereum.addListener(new EthereumListener(ethereum, container));
        }
    }
}
