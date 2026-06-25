package com.taskflow.core.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import graphql.schema.Coercing;
import graphql.schema.CoercingSerializeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;

@Configuration
public class GraphQlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.GraphQLLong)
                .scalar(instantScalar());
    }

    private GraphQLScalarType instantScalar() {
        return GraphQLScalarType.newScalar()
                .name("Instant")
                .description("An ISO-8601 instant")
                .coercing(new Coercing<Instant, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) {
                        if (dataFetcherResult instanceof Instant instant) {
                            return instant.toString();
                        }
                        throw new CoercingSerializeException("Expected an Instant");
                    }

                    @Override
                    public Instant parseValue(Object input) {
                        return Instant.parse(input.toString());
                    }

                    @Override
                    public Instant parseLiteral(Object input) {
                        return Instant.parse(input.toString());
                    }
                })
                .build();
    }
}
