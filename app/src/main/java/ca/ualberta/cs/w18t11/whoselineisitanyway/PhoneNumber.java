package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class PhoneNumber
{
    private final int countryCode;

    private final int areaCode;

    private final int exchangeCode;

    PhoneNumber(final int countryCode,
                final int areaCode,
                final int exchangeCode,
                final int lineNumber)
    {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.exchangeCode = exchangeCode;
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
}
