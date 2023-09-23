package com.dcns.dailycost.data.datasource.local

import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.model.Category

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
		defaultCategory = true
	)

	object Expense {
		val food = Category(
			id = 1,
			name = "Food",
			icon = CategoryIcon.Coffee,
			defaultCategory = true
		)

		val shopping = Category(
			id = 2,
			name = "Shopping",
			icon = CategoryIcon.ShoppingCartSimple,
			defaultCategory = true
		)

		val transport = Category(
			id = 3,
			name = "Transport",
			icon = CategoryIcon.Bus,
			defaultCategory = true
		)

		val electronic = Category(
			id = 4,
			name = "Electronic",
			icon = CategoryIcon.Flash,
			defaultCategory = true
		)

		val entertainment = Category(
			id = 5,
			name = "Entertainment",
			icon = CategoryIcon.Ticket,
			defaultCategory = true
		)

		val gadget = Category(
			id = 6,
			name = "Gadget",
			icon = CategoryIcon.DesktopTower,
			defaultCategory = true
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
			defaultCategory = true
		)

		val investment = Category(
			id = 998,
			name = "Investment",
			icon = CategoryIcon.ChartLineUp,
			defaultCategory = true
		)

		val bonus = Category(
			id = 997,
			name = "Bonus",
			icon = CategoryIcon.Coin,
			defaultCategory = true
		)

		val award = Category(
			id = 996,
			name = "Award",
			icon = CategoryIcon.Trophy,
			defaultCategory = true
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