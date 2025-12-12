package com.mmoney.apps.core.database.util

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MMoneyTypeConverters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun fromYearMonth(value: YearMonth?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toYearMonth(value: String?): YearMonth? {
        return value?.let { YearMonth.parse(it) }
    }

    @TypeConverter
    fun fromAccountType(value: com.mmoney.apps.core.model.AccountType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toAccountType(value: String?): com.mmoney.apps.core.model.AccountType? {
        return value?.let { com.mmoney.apps.core.model.AccountType.valueOf(it) }
    }

    @TypeConverter
    fun fromTransactionType(value: com.mmoney.apps.core.model.TransactionType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toTransactionType(value: String?): com.mmoney.apps.core.model.TransactionType? {
        return value?.let { com.mmoney.apps.core.model.TransactionType.valueOf(it) }
    }
}
