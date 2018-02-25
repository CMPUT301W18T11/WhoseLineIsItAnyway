package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class PhoneNumber
{
    private final int countryCode;

    private final int areaCode;

    private final int exchangeCode;

    private final int lineNumber;

    PhoneNumber(final int countryCode,
                final int areaCode,
                final int exchangeCode,
                final int lineNumber)
    {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.exchangeCode = exchangeCode;
        this.lineNumber = lineNumber;
    }

    final int getCountryCode()
    {
        return this.countryCode;
    }

    final int getAreaCode()
    {
        return this.areaCode;
    }

    final int getExchangeCode()
    {
        return this.exchangeCode;
    }

    final int getLineNumber()
    {
        return this.lineNumber;
    }

    @Override
    public final String toString()
    {
        return "+" + this.countryCode + " (" + this.areaCode + ") "
                + this.exchangeCode + "-" + this.lineNumber;
    }
}
