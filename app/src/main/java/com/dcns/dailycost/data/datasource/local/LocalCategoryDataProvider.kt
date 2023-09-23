package com.dcns.dailycost.data.datasource.local

import androidx.compose.ui.graphics.toArgb
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.DailyCostColorPalette
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.foundation.theme.daily_cost_theme_light_silver_chalice

/**
 * Data provider lokal untuk category (berguna untuk testing)
 */
object LocalCategoryDataProvider {

	val Unspecified = Category(
		id = -1,
		name = "",
		icon = CategoryIcon.Other,
		defaultCategory = false
	)

	val other = Category(
		id = 8,
		name = "Other",
		icon = CategoryIcon.Other,
		defaultCategory = true,
		colorArgb = daily_cost_theme_light_silver_chalice.toArgb()
	)

	object Expense {
		val food = Category(
			id = 1,
			name = "Food",
			icon = CategoryIcon.Coffee,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.caribbeanGreen.toArgb()
		)

		val shopping = Category(
			id = 2,
			name = "Shopping",
			icon = CategoryIcon.ShoppingCartSimple,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.electricViolet.toArgb()
		)

		val transport = Category(
			id = 3,
			name = "Transport",
			icon = CategoryIcon.Bus,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.saffron.toArgb()
		)

		val electronic = Category(
			id = 4,
			name = "Electronic",
			icon = CategoryIcon.Flash,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.lima.toArgb()
		)

		val entertainment = Category(
			id = 5,
			name = "Entertainment",
			icon = CategoryIcon.Ticket,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.sunshade.toArgb()
		)

		val gadget = Category(
			id = 6,
			name = "Gadget",
			icon = CategoryIcon.DesktopTower,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.blazeOrange.toArgb()
		)

		val defaults = arrayOf(
			food,
			shopping,
			transport,
			electronic,
			entertainment,
			gadget,
			other
		)
	}

	object Income {

		val salary = Category(
			id = 999,
			name = "Salary",
			icon = CategoryIcon.Money,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.saffron.toArgb()
		)

		val investment = Category(
			id = 998,
			name = "Investment",
			icon = CategoryIcon.ChartLineUp,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.lima.toArgb()
		)

		val bonus = Category(
			id = 997,
			name = "Bonus",
			icon = CategoryIcon.Coin,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.sunshade.toArgb()
		)

		val award = Category(
			id = 996,
			name = "Award",
			icon = CategoryIcon.Trophy,
			defaultCategory = true,
			colorArgb = DailyCostColorPalette.blazeOrange.toArgb()
		)

		val values = arrayOf(
			salary,
			investment,
			bonus,
			award,
			other
		)
	}

}