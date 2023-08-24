package com.dcns.dailycost.ui.onboarding

sealed interface OnboardingAction {

	data class UpdateCurrentPage(val page: Int): OnboardingAction

}