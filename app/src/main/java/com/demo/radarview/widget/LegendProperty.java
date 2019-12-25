package com.demo.radarview.widget;

import java.util.HashMap;

/**
 * Author: ZhengHuaizhi
 * Date: 2019/12/24
 * Description:
 */
public class LegendProperty {
    // 攻击力
    private float AD;
    // 法术强度
    private float AP;
    // 护甲
    private float ARM;
    // 魔抗
    private float MR;
    // 暴击率
    private float critical;
    // 移速
    private float speed;

    private HashMap<String, Float> hashMap = new HashMap<>();

    public float getAD() {
        return AD;
    }

    public void setAD(float AD) {
        this.AD = AD;
    }

    public float getAP() {
        return AP;
    }

    public void setAP(float AP) {
        this.AP = AP;
    }

    public float getARM() {
        return ARM;
    }

    public void setARM(float ARM) {
        this.ARM = ARM;
    }

    public float getMR() {
        return MR;
    }

    public void setMR(float MR) {
        this.MR = MR;
    }

    public float getCritical() {
        return critical;
    }

    public void setCritical(float critical) {
        this.critical = critical;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public HashMap<String, Float> getPropertyMap() {
        hashMap.put("攻击力", AD);
        hashMap.put("法术强度", AP);
        hashMap.put("护甲", ARM);
        hashMap.put("魔抗", MR);
        hashMap.put("暴击率", critical);
        hashMap.put("移速", speed);
        return hashMap;
    }

}
