package cn.exp.proj.module.mk.vo;

import cn.exp.proj.common.core.util.CollectionUtil;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料分配明细
 *
 * @date: 2022-10-25  19:26
 * @version: 1.0
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class MaterialKitVo {
    private Map<Long, List<BatchKitVo>> kitMap = CollectionUtil.newHashMap();

    public void putAll(MaterialKitVo kitVo) {
        putAll(kitVo.getKitMap());
    }

    public void putAll(Map<Long, List<BatchKitVo>> kitMap) {
        this.kitMap.putAll(kitMap);
    }

    public void put(Long key, List<BatchKitVo> value) {
        this.kitMap.put(key, value);
    }

    public void addKit(MaterialKitVo kitVo) {
        addKit(kitVo.getKitMap());
    }

    private void addKit(Map<Long, List<BatchKitVo>> kits) {
        if (CollectionUtil.isEmpty(kits)) {
            return;
        }
        kits.forEach((k, v) -> this.kitMap.compute(k, (k1, v1) -> CollectionUtil.orAddAll(v1, v)));
    }

    public List<BatchKitVo> get(Long key) {
        return this.kitMap.get(key);
    }

    public List<BatchKitVo> flatValue() {
        return CollectionUtil.flatMap(this.kitMap);
    }
}
