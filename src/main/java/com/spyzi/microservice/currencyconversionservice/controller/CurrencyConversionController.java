package com.spyzi.microservice.currencyconversionservice.controller;

import com.spyzi.microservice.currencyconversionservice.model.CurrencyConversion;
import com.spyzi.microservice.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment environment;

    @Autowired
    CurrencyExchangeProxy exchangeProxy;

    //http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10

    @GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getCalculatedCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        final Map<String, String> uriMap = new ConcurrentHashMap<>();
        uriMap.put("from", from);
        uriMap.put("to", to);
        final ResponseEntity<CurrencyConversion> responseEntity
                = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriMap);
        final CurrencyConversion currencyConversion = responseEntity.getBody();
        return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
                currencyConversion.getConversionMultiple() , quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment()+" Rest Template");
    }
    @GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getCalculatedCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        final CurrencyConversion currencyConversion = exchangeProxy.retrieveExchangedValue(from,to);
        return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
                currencyConversion.getConversionMultiple() , quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment()+" Feign");
    }
}
