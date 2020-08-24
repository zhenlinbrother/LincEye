package com.linc.linceye.net.rx;

import com.linc.linceye.bean.RespData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * author       : zinc
 * time         : 2019/4/23 上午9:49
 * desc         : rx 拆壳 解析器
 * version      : 1.3.0
 */

public class RxParser {

//    /**
//     * 拆壳
//     *
//     * @param <T>
//     * @return
//     */
//    public static <T> SingleTransformer<RespData<T>, T> handleSingleDataResult() {
//        return new SingleTransformer<RespData<T>, T>() {
//            @Override
//            public @NonNull SingleSource<T> apply(io.reactivex.rxjava3.core.@NonNull Single<RespData<T>> upstream) {
//                return upstream
//                        .map(new TransToData<T>())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }

//    /**
//     * 拆壳
//     *
//     * @param <T>
//     * @return
//     */
//    public static <T> ObservableTransformer<RespData<T>, T> handleObservableDataResult() {
//        return new ObservableTransformer<RespData<T>, T>() {
//            @Override
//            public @NonNull ObservableSource<T> apply(io.reactivex.rxjava3.core.@NonNull Observable<RespData<T>> upstream) {
//                return upstream
//                        .map(new TransToData<T>())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }

    /**
     * 不拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> SingleTransformer<RespData<T>, RespData<T>> handleSingleToResult() {
        return new SingleTransformer<RespData<T>, RespData<T>>() {
            @Override
            public @NonNull SingleSource<RespData<T>> apply(@NonNull Single<RespData<T>> upstream) {
                return upstream
                        .map(new TransToResult<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 不拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> SingleTransformer<T, T> handleSingleToResult1() {
        return new SingleTransformer<T, T>() {
            @Override
            public @NonNull SingleSource<T> apply(@NonNull Single<T> upstream) {
                return upstream
                        .map(new TransToData<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 拆壳
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> handleObservableToResult() {
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .map(new TransToData<T>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }


        };
    }

//    private static class TransToData<T> implements Function<RespData<T>, T> {
//
//        @Override
//        public T apply(RespData<T> tResult) throws Exception {
////            if (tResult.getStatus() == 0
////                    || tResult.getStatus() == NetCode.CODE_INVALIDATE
////                    || tResult.getStatus() == NetCode.ARTICLE_REMOVE_ERROR_CODE
////                    || tResult.getStatus() == NetCode.NO_PERMISSION_CODE
////                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_CODE
////                    || tResult.getStatus() == NetCode.COIN_NO_ENOUGH_CODE
////                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_PERMISSION_CODE) {
////                if (tResult.getData() == null) {
////                    return (T) new Object();
////                } else {
////                    return tResult.getData();
////                }
////            } else {
////                throw new ServerException(tResult.getStatus(), tResult.getReason());
////            }
//
//            if (tResult.getData() == null){
//                return (T) new Object();
//            } else {
//                return tResult.getData();
//            }
//        }
//    }

    private static class TransToData<T> implements Function<T, T> {

        @Override
        public T apply(T tResult) throws Exception {
//            if (tResult.getStatus() == 0
//                    || tResult.getStatus() == NetCode.CODE_INVALIDATE
//                    || tResult.getStatus() == NetCode.ARTICLE_REMOVE_ERROR_CODE
//                    || tResult.getStatus() == NetCode.NO_PERMISSION_CODE
//                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_CODE
//                    || tResult.getStatus() == NetCode.COIN_NO_ENOUGH_CODE
//                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_PERMISSION_CODE) {
//                if (tResult.getData() == null) {
//                    return (T) new Object();
//                } else {
//                    return tResult.getData();
//                }
//            } else {
//                throw new ServerException(tResult.getStatus(), tResult.getReason());
//            }

            if (tResult == null){
                return (T) new Object();
            } else {
                return tResult;
            }
        }
    }

    private static class TransToResult<T> implements Function<RespData<T>, RespData<T>> {

        @Override
        public RespData<T> apply(RespData<T> tResult) throws Exception {

//            if (tResult.getStatus() == 0
//                    || tResult.getStatus() == NetCode.CODE_INVALIDATE
//                    || tResult.getStatus() == NetCode.ARTICLE_REMOVE_ERROR_CODE
//                    || tResult.getStatus() == NetCode.NO_PERMISSION_CODE
//                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_CODE
//                    || tResult.getStatus() == NetCode.COIN_NO_ENOUGH_CODE
//                    || tResult.getStatus() == NetCode.GRADE_NO_ENOUGH_PERMISSION_CODE) {
//                return tResult;
//            } else {
//                throw new ServerException(tResult.getStatus(), tResult.getReason());
//            }
            return tResult;

        }
    }

}
