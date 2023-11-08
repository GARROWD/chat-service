package ru.garrowd.chatservice.configs;

import com.university.userservice.grpc.user.UserGrpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GrpcConfig {
    private final GrpcPropertiesConfig grpcPropertiesConfig;

    @Bean
    public UserGrpcServiceGrpc.UserGrpcServiceBlockingStub grpcClient() {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(grpcPropertiesConfig.getClient().get("users").getAddress())
                                                      .usePlaintext()
                                                      .build();
        return UserGrpcServiceGrpc.newBlockingStub(channel);
    }
}
