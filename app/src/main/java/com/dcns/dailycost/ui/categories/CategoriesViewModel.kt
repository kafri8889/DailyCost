package com.dcns.dailycost.ui.categories

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
): BaseViewModel<CategoriesState, CategoriesAction>() {

    init {
        viewModelScope.launch {
            categoryUseCases.getLocalCategoryUseCase().collect { categoryList ->
                updateState {
                    copy(
                        categories = categoryList
                    )
                }
            }
        }
    }

    override fun defaultState(): CategoriesState = CategoriesState()

    override fun onAction(action: CategoriesAction) {

    }
}