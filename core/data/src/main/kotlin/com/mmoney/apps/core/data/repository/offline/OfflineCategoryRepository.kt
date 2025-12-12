package com.mmoney.apps.core.data.repository.offline

import com.mmoney.apps.core.data.model.asEntity
import com.mmoney.apps.core.data.model.asExternalModel
import com.mmoney.apps.core.data.repository.CategoryRepository
import com.mmoney.apps.core.database.dao.CategoryDao
import com.mmoney.apps.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> =
        categoryDao.getCategories().map { entities -> entities.map { it.asExternalModel() } }

    override suspend fun getCategory(id: String): Category? =
        categoryDao.getCategory(id)?.asExternalModel()

    override suspend fun upsertCategory(category: Category) =
        categoryDao.upsertCategory(category.asEntity())

    override suspend fun deleteCategory(id: String) =
        categoryDao.deleteCategory(id)
}
