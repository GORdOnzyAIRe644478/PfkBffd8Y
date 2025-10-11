// 代码生成时间: 2025-10-11 17:39:54
package com.example.quarkus期权定价.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/option")
public class OptionPricingService {

    // 参数定义
    private static final double RISK_FREE_RATE = 0.05; // 无风险利率
    private static final double VOLATILITY = 0.2; // 波动率
    private static final int TIME_TO_EXPIRY = 365; // 到期时间（以天为单位）
    private static final double SPOT_PRICE = 100.0; // 现货价格
    private static final double STRIKE_PRICE = 100.0; // 执行价格
    private static final int OPTION_TYPE = 1; // 期权类型，看涨为1，看跌为-1

    // 计算欧式期权的理论价格
    @GET
    @Path("/price")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateOptionPrice() {
        try {
            double price = calculateBlackScholes(OPTION_TYPE, SPOT_PRICE, STRIKE_PRICE, TIME_TO_EXPIRY, RISK_FREE_RATE, VOLATILITY);
            return Response.ok(PriceResponse.<{"double}">builder().price(price).build()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error calculating option price: " + e.getMessage()).build();
        }
    }

    // 计算期权价格的Black-Scholes模型
    private double calculateBlackScholes(int optionType, double S, double K, double T, double r, double sigma) {
        double d1 = (Math.log(S / K) + (r + sigma * sigma / 2) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        double call = S * getCDF(d1) - K * getCDF(d2);
        double put = K * getCDF(-d2) - S * getCDF(-d1);

        if (optionType == 1) {
            return call;
        } else {
            return put;
        }
    }

    // 累积分布函数
    private double getCDF(double x) {
        return 0.5 * (1 + getErfc(x / Math.sqrt(2)));
    }

    // 互补误差函数
    private double getErfc(double x) {
        return 2 / Math.sqrt(Math.PI) * x * getErfc(x * x);
    }

    private double getErfc(double a) {
        double t = 1.0 / (1.0 + 0.5 * a);
        double ans = t * Math.exp(-a * a) * (
                1 - t * (0.5 + 0.3 * t * (0.175 - 0.075 * t * t * t * (0.06191 + 0.010568 * t * (0.0033947 - 0.002184 * t * (0.0035233 - 0.00092378 * t)))))));
        return ans;
    }

    // PriceResponse DTO
    @RegisterForReflection
    public static class PriceResponse {
        private double price;

        public static PriceResponseBuilder<{"double"}> builder() {
            return new PriceResponseBuilder<>();
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "PriceResponse{price=" + price + "}";
        }

        public static class PriceResponseBuilder<T> {
            private double price;

            public PriceResponseBuilder<{"double"}> price(double price) {
                this.price = price;
                return this;
            }

            public PriceResponse build() {
                return new PriceResponse(price);
            }
        }

        private PriceResponse(double price) {
            this.price = price;
        }
    }

    // 启动应用程序
    public static void main(String... args) {
        Quarkus.run(OptionPricingService.class, args);
    }
}
