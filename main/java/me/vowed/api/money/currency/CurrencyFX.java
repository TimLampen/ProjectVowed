package me.vowed.api.money.currency;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/22/2015.
 */
public interface CurrencyFX
{
    BigDecimal elfFactor = new BigDecimal("1.35");
    BigDecimal humanFactor = new BigDecimal("1.24");
    BigDecimal dwarfFactor = new BigDecimal("1.12");

    BigDecimal convertCurrencyToELF(BigDecimal inValue) throws ArithmeticException;

    BigDecimal convertCurrencyToHUMAN(BigDecimal inValue) throws ArithmeticException;

    BigDecimal convertCurrencyToDWARF(BigDecimal inValue) throws ArithmeticException;

}
