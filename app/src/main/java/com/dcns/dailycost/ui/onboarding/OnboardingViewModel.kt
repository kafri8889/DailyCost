package com.dcns.dailycost.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(

): BaseViewModel<OnboardingState, OnboardingAction>() {

	override fun defaultState(): OnboardingState = OnboardingState()

	override fun onAction(action: OnboardingAction) {
		when (action) {
			is OnboardingAction.UpdateCurrentPage -> {
				viewModelScope.launch {
					val bodyText: Int
					val titleText: Int
					val primaryButtonText: Int
					val secondaryButtonText: Int

					when (action.page) {
						1 -> {
							bodyText = R.string.you_can_see_where_the_money_goes
							titleText = R.string.its_easy_to_spot_the_categories
							primaryButtonText = R.string.next
							secondaryButtonText = R.string.skip
						}

						2 -> {
							bodyText = R.string.achieving_your_goals_is_easier
							titleText = R.string.spending_plan_show_much_money
							primaryButtonText = R.string.next
							secondaryButtonText = R.string.skip
						}

						3 -> {
							bodyText = R.string.all_card_and_accounts_in_one_app
							titleText = R.string.secure_synchronization_with_banks
							primaryButtonText = R.string.next
							secondaryButtonText = R.string.skip
						}

						4 -> {
							bodyText = R.string.no_need_to_add_expenses_manually
							titleText = R.string.your_bank_transaction_are_delivered_automatically
							primaryButtonText = R.string.sign_in
							secondaryButtonText = R.string.sign_up
						}

						else -> throw IllegalArgumentException("invalid page: page ${action.page}")
					}

					updateState {
						copy(
							currentPage = action.page,
							bodyText = bodyText,
							titleText = titleText,
							primaryButtonText = primaryButtonText,
							secondaryButtonText = secondaryButtonText
						)
					}
				}
			}
		}
	}
}