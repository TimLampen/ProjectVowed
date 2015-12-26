package me.vowed.api.money.currency;

/**
 * Created by JPaul on 11/22/2015.
 */
public class CurrencyFactory
{
    public static CurrencyFX getInstance(String currency){
        if(currency == null){
            return null;
        }
        if(currency.equalsIgnoreCase("DWARF"))
        {
            return new DwarfCurrency();
        }
        else if (currency.equalsIgnoreCase("ELF"))
        {
            return new ElfCurrency();
        }
        else if (currency.equalsIgnoreCase("HUMAN"))
        {
            return new HumanCurrency();
        }
        return null;
    }
}
