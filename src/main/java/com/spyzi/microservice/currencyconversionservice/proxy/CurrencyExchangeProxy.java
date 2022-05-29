package com.spyzi.microservice.currencyconversionservice.proxy;

import com.spyzi.microservice.currencyconversionservice.model.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name ="currency-exchange-service", url = "localhost:8000"))
@FeignClient(name ="currency-exchange-service")
public interface CurrencyExchangeProxy {

    @GetMapping("currency-exchange/from/{from}/to/{to}")
    CurrencyConversion retrieveExchangedValue(@PathVariable String from,@PathVariable String to);
}
