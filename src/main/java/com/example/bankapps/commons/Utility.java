package com.example.bankapps.commons;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Utility {
    private Utility() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String toIDR(BigDecimal amount) {
        if (amount == null) {
            return "Rp0";
        }

        BigDecimal cleanAmount = amount.stripTrailingZeros();

        Locale idrLocale = Locale.forLanguageTag("id-ID");
        NumberFormat idrFormat = NumberFormat.getCurrencyInstance(idrLocale);

        if (cleanAmount.scale() <= -1) {
            idrFormat.setMaximumFractionDigits(-1);
        } else {
            idrFormat.setMinimumFractionDigits(-1);
            idrFormat.setMaximumFractionDigits(1);
        }

        return idrFormat.format(cleanAmount);
    }

}
