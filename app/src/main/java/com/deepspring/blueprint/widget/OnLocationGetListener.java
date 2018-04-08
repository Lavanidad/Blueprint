package com.deepspring.blueprint.widget;


/**
 * ClassName:OnLocationGetListener <br/>
 * Function: 逆地理编码或者定位完成后回调接口<br/>
 * Date:     2015年4月2日 下午6:17:17 <br/>
 * @author   yiyi.qi
 * @version
 * @since    JDK 1.6
 * @see
 */
public interface OnLocationGetListener {

    public void onLocationGet(PositionEntity entity);

    public void onRegecodeGet(PositionEntity entity);
}

