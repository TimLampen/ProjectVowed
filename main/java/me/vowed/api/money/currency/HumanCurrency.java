package me.vowed.api.money.currency;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/22/2015.
 */
public class HumanCurrency implements CurrencyFX
{
    @Override
    public BigDecimal convertCurrencyToDWARF(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal humanDwarfRate = humanFactor.divide(dwarfFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal dwarf = inValue.multiply(humanDwarfRate);
        dwarf = dwarf.setScale(5, BigDecimal.ROUND_HALF_UP);
        return dwarf;
    }

    @Override
    public BigDecimal convertCurrencyToELF(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal humanElfRate = humanFactor.divide(elfFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal elf = inValue.multiply(humanElfRate);
        elf = elf.setScale(5, BigDecimal.ROUND_HALF_UP);
        return elf;
    }

    @Override
    public BigDecimal convertCurrencyToHUMAN(BigDecimal inValue) throws ArithmeticException
    {
        return inValue;
    }
}
