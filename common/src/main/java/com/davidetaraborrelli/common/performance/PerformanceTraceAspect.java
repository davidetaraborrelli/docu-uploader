package com.davidetaraborrelli.common.performance;

import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceTraceAspect {

    private final WriteApi writeApi;

    @Around("@annotation(tracePerformance)")
    public Object traceMethod(ProceedingJoinPoint joinPoint, TracePerformance tracePerformance) throws Throwable {
        long startTime = System.currentTimeMillis();
        String status = "success";

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            status = "error";
            throw ex;
        } finally {
            long deltaT = System.currentTimeMillis() - startTime;

            try {
                Point point = Point.measurement("method_execution")
                        .addTag("repo", tracePerformance.repo())
                        .addTag("data", tracePerformance.data())
                        .addTag("method", joinPoint.getSignature().toShortString())
                        .addTag("status", status)
                        .addField("execution_time_ms", deltaT)
                        .time(Instant.now(), WritePrecision.MS);

                writeApi.writePoint(point);
            } catch (Exception e) {
                log.warn("Failed to write performance trace to InfluxDB: {}", e.getMessage());
            }
        }
    }
}
