package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 拼团推荐商品
 */
@Entity
@Table(name = "shop_spell_recom_goods")
public class SpellRecomGoods implements Serializable {

    /**
     * 推荐ID
     */
    @Id
    @Column(name = "recom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recomId;

    @Column(name = "goods_id")
    private Long goodsId;

    public Long getRecomId() {
        return recomId;
    }

    public void setRecomId(Long recomId) {
        this.recomId = recomId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}