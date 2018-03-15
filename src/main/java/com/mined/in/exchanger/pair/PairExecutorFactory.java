package com.mined.in.exchanger.pair;

import com.mined.in.exchanger.Exchanger;
import com.mined.in.exchanger.pair.bitfinex.BitfinexLimiter;
import com.mined.in.exchanger.pair.bitfinex.BitfinexPairExecutor;
import com.mined.in.exchanger.pair.bitstamp.BitstampLimiter;
import com.mined.in.exchanger.pair.bitstamp.BitstampPairExecutor;
import com.mined.in.exchanger.pair.cex.CexPairExecutor;
import com.mined.in.exchanger.pair.exmo.ExmoLimiter;
import com.mined.in.exchanger.pair.exmo.ExmoPairExecutor;
import com.mined.in.exchanger.pair.gdax.GdaxLimiter;
import com.mined.in.exchanger.pair.gdax.GdaxPairExecutor;
import com.mined.in.exchanger.pair.kraken.KrakenPairExecutor;
import com.mined.in.exchanger.pair.yobit.YobitPairExecutor;

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
        case BITFINEX: {
            okHttpBuilder.addInterceptor(BitfinexLimiter.get());
            return new BitfinexPairExecutor(okHttpBuilder.build());
        }
        case GDAX: {
            okHttpBuilder.addInterceptor(GdaxLimiter.get());
            return new GdaxPairExecutor(okHttpBuilder.build());
        }
        case BITSTAMP: {
            okHttpBuilder.addInterceptor(BitstampLimiter.get());
            return new BitstampPairExecutor(okHttpBuilder.build());
        }
        case KRAKEN: {
            return new KrakenPairExecutor(okHttpBuilder.build());
        }
        case CEX: {
            return new CexPairExecutor(okHttpBuilder.build());
        }
        case YOBIT: {
            return new YobitPairExecutor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(exchanger.name());
        }
    }

}
