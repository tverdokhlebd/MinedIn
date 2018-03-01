package com.mined.in.exchanger.pair;

import com.mined.in.exchanger.Exchanger;
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
        OkHttpClient httpClient = new OkHttpClient();
        switch (exchanger) {
        case EXMO: {
            return new ExmoPairExecutor(httpClient);
        }
        default:
            throw new IllegalArgumentException(exchanger.name());
        }
    }

}
