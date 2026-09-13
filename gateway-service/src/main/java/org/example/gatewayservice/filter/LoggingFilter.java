package org.example.gatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        log.info("[GATEWAY REQUEST] {} {} - Received at: {}ms", method, path, startTime);

        exchange.getResponse().beforeCommit(() -> {
            long executionTime = System.currentTimeMillis() - startTime;
            exchange.getResponse().getHeaders().add("X-Response-Time", executionTime + "ms");
            return Mono.empty();
        });

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("[GATEWAY RESPONSE] {} {} - Status: {} - Duration: {}ms",
                    method, path, exchange.getResponse().getStatusCode(), executionTime);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
