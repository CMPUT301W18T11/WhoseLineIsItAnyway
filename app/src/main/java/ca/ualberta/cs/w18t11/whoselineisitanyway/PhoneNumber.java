package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class PhoneNumber
{
    private final int countryCode;

    private final int areaCode;

    PhoneNumber(final int countryCode,
                final int areaCode,
                final int exchangeCode,
                final int lineNumber)
    {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
    }

    final int getCountryCode()
    {
        return this.countryCode;
    }

    final int getAreaCode()
    {
        return this.areaCode;
    }
}
