package me.vowed.api.money;

/**
 * Created by JPaul on 11/14/2015.
 */
public class Money implements IMoney
{
    double amount;

    public Money(int amount)
    {
        this.amount = amount;
    }

    @Override
    public double getAmount()
    {
        return this.amount;
    }

    @Override
    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    @Override
    public void add(double amount)
    {
        this.amount = this.amount + amount;
    }

    @Override
    public void subtract(double amount)
    {
        this.amount = this.amount - amount;
    }
}
