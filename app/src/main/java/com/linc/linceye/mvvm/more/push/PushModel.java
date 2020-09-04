package com.linc.linceye.mvvm.more.push;

import androidx.annotation.NonNull;

import com.linc.base.base.utils.DateTimeUtils;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.mvvm.more.push.bean.Message;
import com.linc.linceye.mvvm.more.push.bean.MessageViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/4
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PushModel<T> extends BasePagingModel<T> {

    private int start = 1;
    private int num = 10;

    public void loadData(boolean isFirst){
        if (isFirst){
            start = 1;
        }
        RetrofitFactory
                .getApiService()
                .getPushData("591", "Che1-CL20", start, num)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<MessageViewModel>>() {
                    @Override
                    public List<MessageViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<MessageViewModel> viewModels = new ArrayList<>();

                        Message message = GsonUtils.fromLocalJson(jsonObject.toString(), Message.class);

                        if (message != null){
                            for (Message.MessageListBean itemBean : message.getMessageList()) {
                                MessageViewModel viewModel = new MessageViewModel();
                                viewModel.coverUrl = itemBean.getIcon();
                                viewModel.title = itemBean.getTitle();
                                viewModel.comtent = itemBean.getContent();
                                viewModel.messageDate = formatDate(itemBean.getDate());
                                viewModels.add(viewModel);
                            }
                        }
                        return viewModels;
                    }
                })
                .subscribe(new RxSingleSubscriber<List<MessageViewModel>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        loadFail(code + message, isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull List<MessageViewModel> value) {
                        loadSuccess((T)value, value.size() <= 0, isFirst);
                    }
                });
    }

    /**
     * 根据时间戳转换成对应格式的时间
     * */
    private String formatDate(long date){
        return DateTimeUtils.getDate(String.valueOf(date),"HH");

    }
}
