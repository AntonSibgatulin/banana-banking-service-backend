package ru.antonsibgatulin.bankingservice.config.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.antonsibgatulin.bankingservice.advise.MyMessageInterpolator;

@Configuration
public class ValidationConfig {

    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new MyMessageInterpolator(
                        Validation.byDefaultProvider().configure().getDefaultMessageInterpolator()))
                .buildValidatorFactory();
    }

    @Bean
    public Validator validator() {
        return validatorFactory().getValidator();
    }
}