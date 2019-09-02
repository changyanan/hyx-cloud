package org.xuenan.hyx.model;

import org.xuenan.hyx.StringUtils;

import java.io.Serializable;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 14:56:00
 */
public abstract class SysModel implements Serializable {
    public static final long serialVersionUID = 1L;

    public final String toJSONString() {
        return JSONUtils.toJSONString(this);
    }

    public final String toJSONHTML() {
        String model = toJSONString();
        if (StringUtils.isEmpty(model)) {
            return null;
        }
        return model.replace("'", "&apos;").replace("\"", "&quot;");
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
