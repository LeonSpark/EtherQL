package edu.suda.ada.core.cudr;

import edu.suda.ada.entity.PlainAccount;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface AccountTemplate {
    boolean exist(PlainAccount account);
    void upsert(PlainAccount account);
    void delete(PlainAccount account);
    void update(PlainAccount account);
    void bulkUpdate(Map<Query, Update> bulk);
}
