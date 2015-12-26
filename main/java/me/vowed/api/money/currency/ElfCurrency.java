package me.vowed.api.money.currency;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/22/2015.
 */
public class ElfCurrency implements CurrencyFX
{
    @Override
    public BigDecimal convertCurrencyToDWARF(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal elfDwarfRate = elfFactor.divide(dwarfFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal dwarf = inValue.multiply(elfDwarfRate);
        dwarf = dwarf.setScale(5, BigDecimal.ROUND_HALF_UP);
        return dwarf;
    }

    @Override
    public BigDecimal convertCurrencyToELF(BigDecimal inValue) throws ArithmeticException
    {
        return inValue;
    }

    @Override
    public BigDecimal convertCurrencyToHUMAN(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal dwarfHumanRate = elfFactor.divide(humanFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal human = inValue.multiply(dwarfHumanRate);
        human = human.setScale(5, BigDecimal.ROUND_HALF_UP);
        return human;
    }
}
