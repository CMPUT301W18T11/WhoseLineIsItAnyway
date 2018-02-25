package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class PhoneNumber
{
    private final int countryCode;

    PhoneNumber(final int countryCode,
                final int areaCode,
                final int exchangeCode,
                final int lineNumber)
    {
        this.countryCode = countryCode;
    }

    final int getCountryCode()
    {
        return this.countryCode;
    }
}
