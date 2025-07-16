package org.example.Config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;


public class OtelConfig {

    public static void init()
    {
        Resource serviceNameResource =
                Resource.getDefault()
                        .merge(Resource.create(io.opentelemetry.api.common.Attributes.of(
                                io.opentelemetry.api.common.AttributeKey.stringKey("service.name"),
                                "my-mannual-grpc-server-instrumentation"
                        )));


        OtlpGrpcSpanExporter exporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://localhost:4319")
                .build();

        SdkTracerProvider sdkTracerProvider= SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
                .setResource(serviceNameResource)
                .build();

        OpenTelemetrySdk otelSdk = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .build();

        GlobalOpenTelemetry.set(otelSdk);

    }
}
