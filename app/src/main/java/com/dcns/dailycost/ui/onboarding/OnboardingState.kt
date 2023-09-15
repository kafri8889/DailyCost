package com.dcns.dailycost.ui.onboarding

import android.os.Parcelable
import com.dcns.dailycost.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingState(
	val currentPage: Int = 1,
	val pageCount: Int = 4,
	val bodyText: Int = R.string.you_can_see_where_the_money_goes,
	val titleText: Int = R.string.its_easy_to_spot_the_categories,
	val primaryButtonText: Int = R.string.next,
	val secondaryButtonText: Int = R.string.skip,
	val imageAssetPath: String = "wallet_with_cash.png"
): Parcelable
