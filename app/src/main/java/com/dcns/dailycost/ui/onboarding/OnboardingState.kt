package com.dcns.dailycost.ui.onboarding

import com.dcns.dailycost.R

data class OnboardingState(
    val currentPage: Int = 0,
    val pageCount: Int = 4,
    val bodyText: Int = R.string.you_can_see_where_the_money_goes,
    val titleText: Int = R.string.its_easy_to_spot_the_categories,
    val primaryButtonText: Int = R.string.next,
    val secondaryButtonText: Int = R.string.skip,
)
