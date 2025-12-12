package com.mmoney.apps.core.data.repository.offline

import com.mmoney.apps.core.data.model.asEntity
import com.mmoney.apps.core.data.model.asExternalModel
import com.mmoney.apps.core.data.repository.AccountRepository
import com.mmoney.apps.core.database.dao.AccountDao
import com.mmoney.apps.core.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineAccountRepository @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    override fun getAccounts(): Flow<List<Account>> =
        accountDao.getAccounts().map { entities -> entities.map { it.asExternalModel() } }

    override suspend fun getAccount(id: String): Account? =
        accountDao.getAccount(id)?.asExternalModel()

    override suspend fun upsertAccount(account: Account) =
        accountDao.upsertAccount(account.asEntity())

    override suspend fun deleteAccount(id: String) =
        accountDao.deleteAccount(id)
}
