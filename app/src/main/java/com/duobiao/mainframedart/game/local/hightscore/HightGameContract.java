package com.duobiao.mainframedart.game.local.hightscore;

import com.duobiao.mainframedart.game.base.IBasePresenter;
import com.duobiao.mainframedart.game.base.IBaseView;
import com.duobiao.mainframedart.game.bean.PlayerBean;

/**
 * Author:Admin
 * Time:2019/8/2 10:41
 * 描述：
 */
public class HightGameContract  {
    public interface  presenter extends IBasePresenter {

    }
    public interface view<T> extends IBaseView {
        void setPresenter(T presenter);
        void showResult(PlayerBean playerBean);
    }
}
