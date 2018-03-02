package com.mined.in.exchanger.pair;

import com.mined.in.exchanger.Exchanger;
import com.mined.in.exchanger.pair.exmo.ExmoLimiter;
import com.mined.in.exchanger.pair.exmo.ExmoPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating of pair executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class PairExecutorFactory {

    /**
     * Returns pair executor.
     *
     * @param exchanger exchanger name
     * @return pair executor
     */
    public static PairExecutor getPairExecutor(Exchanger exchanger) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        switch (exchanger) {
        case EXMO: {
            okHttpBuilder.addInterceptor(ExmoLimiter.get());
            return new ExmoPairExecutor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(exchanger.name());
        }
    }

}
