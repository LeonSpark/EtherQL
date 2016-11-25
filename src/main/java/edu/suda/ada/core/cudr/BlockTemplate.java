package edu.suda.ada.core.cudr;

import edu.suda.ada.entity.PlainBlock;
/**
 * Created by LiYang on 2016/11/22.
 */
public interface BlockTemplate{
    boolean exist(PlainBlock block);
    PlainBlock getBlock(Long number);
    void saveBlock(PlainBlock block);
    void deleteBlock(PlainBlock block);
    void updateBlock(PlainBlock block);
}

