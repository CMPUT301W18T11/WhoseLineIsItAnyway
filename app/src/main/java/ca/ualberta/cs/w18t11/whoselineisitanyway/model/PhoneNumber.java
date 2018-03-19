package ca.ualberta.cs.w18t11.whoselineisitanyway.model;

import java.util.Objects;

public final class PhoneNumber
{
    private final int countryCode;

    private final int areaCode;

    private final int exchangeCode;

    private final int lineNumber;

    public PhoneNumber(final int countryCode,
                       final int areaCode,
                       final int exchangeCode,
                       final int lineNumber)
    {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.exchangeCode = exchangeCode;
        this.lineNumber = lineNumber;
    }

    public final int getCountryCode()
    {
        return this.countryCode;
    }

    public final int getAreaCode()
    {
        return this.areaCode;
    }

    public final int getExchangeCode()
    {
        return this.exchangeCode;
    }

    public final int getLineNumber()
    {
        return this.lineNumber;
    }

    @Override
    public final String toString()
    {
        return "+" + this.countryCode + " (" + this.areaCode + ") "
                + this.exchangeCode + "-" + this.lineNumber;
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.countryCode, this.areaCode, this.exchangeCode, this.lineNumber);
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof PhoneNumber))
        {
            return false;
        }

        PhoneNumber phoneNumber = (PhoneNumber) object;

        return this.countryCode == phoneNumber.getCountryCode()
                && this.areaCode == phoneNumber.getAreaCode()
                && this.exchangeCode == phoneNumber.getExchangeCode()
                && this.lineNumber == phoneNumber.getLineNumber();
    }
}
