package edu.suda.ada.dao;

import edu.suda.ada.core.SimpleBlock;
/**
 * Created by LiYang on 2016/11/22.
 */
public interface BlockTemplate{
    boolean exist(SimpleBlock block);
    SimpleBlock getBlock(Long number);
    int saveBlock(SimpleBlock block);
    void deleteBlock(SimpleBlock block);
    void updateBlock(SimpleBlock block);
}

